import java.math.BigInteger;
import java.util.ArrayList;

public class Animal extends EObject implements Runnable {

    private boolean isHungry = true;
    private boolean isFound = false;
    private boolean suspendingFlag;
    private float floatX, floatY;
    private float speed;
    private String name;
    private Food food;
    private Animal partner;
    private Thread thread;

    public Animal(Area a, int x, int y) {
        super(a, x, y);
        floatX = 0;
        floatY = 0;
        suspendingFlag = false;
        food = null;
        partner = null;
        name = "Animal";
        thread = new Thread(this, name);
        thread.start();
        speed = 0.05F;
    }

    public void run()
    {
        while(isAlive())
        {
            findFood();
            moveToEObject(food);

            if(confirmTarget(food))
            {
                isHungry = false;
                food.eat();
                area.fed(id);
            }
            food = null;
            isFound = false;
        }
    }

    private void findFood()
    {
        searchFoodInAreas(new Area[]{area});
        Area[] areas = area.getNeighbors();
        searchFoodInAreas(areas);
        if(!isFound)
        {
            ArrayList<Area> anotherAreas = new ArrayList<>();
            Area[] allAreas = area.getAllNeighbors();
            for(int i = 0; i < allAreas.length; i++)
            {
                for(int j = 0; j < areas.length; j++)
                {
                    if(!( allAreas[i].getName().equals(areas[j].getName()) |
                            allAreas[i].getName().equals(area.getName())))
                        anotherAreas.add(allAreas[i]);
                }
            }
            searchFoodInAreas(anotherAreas.toArray(new Area[anotherAreas.size()]));
        }
    }

    private void searchFoodInAreas(Area[] areas) {
        int horDistToFood, verDistToFood;
        int horDistToArea, verDistToArea;
        int projectionX, projectionY;
        int areaX = area.getX();
        int areaY = area.getY();
        int[] anotherTarget;

        horDistToFood = 0;
        verDistToFood = 0;

        for (Area a : areas) {
            horDistToArea = 0;
            verDistToArea = 0;
            projectionX = posX;
            projectionY = posY;
            int aX = a.getX();
            int aY = a.getY();

            if(isFound)
            {
                horDistToFood = distTo(getAbsPosX(), food.getAbsPosX());
                verDistToFood = distTo(getAbsPosY(), food.getAbsPosY());
            }

            if (aX > areaX) {
                horDistToArea = (aX - areaX - 1) * 8 + (8 - posX);
                projectionX = 0;
            } else if (aX < areaX) {
                horDistToArea = (areaX - aX - 1) * 8 + (posX + 1);
                projectionX = 7;
            }

            if (aY > areaY) {
                verDistToArea = (aY - areaY - 1) * 8 + (8 - posY);
                projectionY = 0;
            } else if (aY < areaY) {
                verDistToArea = (areaY - aY - 1) * 8 + (posY + 1);
                projectionY = 7;
            }

            int distToFood = horDistToFood + verDistToFood;
            int distToArea = horDistToArea + verDistToArea;

            if (a.getFoodList().length > 0 & ( distToArea < distToFood | !isFound ) )
            {
                int wdt = (isFound) ? Math.min(distToFood - distToArea + projectionX, 8) : 8;
                int hgh = (isFound) ? Math.min(distToFood - distToArea + projectionY, 8) : 8;

                anotherTarget = useSeeker(a, wdt, hgh, projectionX, projectionY);

                if(anotherTarget[0] != -1)
                {
                    int horDistToAnotherFood = distTo(anotherTarget[0], projectionX);
                    int verDistToAnotherFood = distTo(anotherTarget[1], projectionY);
                    int distToAnotherFood = horDistToAnotherFood + verDistToAnotherFood + horDistToArea + verDistToArea;

                    if( (isFound & (distToAnotherFood <= distToFood)) | !isFound )
                        chooseTarget(anotherTarget, a);
                }
            }
        }
    }

    private int distTo(int a, int b) {
        return Math.max(a, b) - Math.min(a, b);
    }

    private int[] useSeeker(Area a, int wdt, int hgh, int x, int y) {
        Seeker fSeeker = new Seeker("Food", a.getList(), 8, 8, x, y, hgh * wdt);
        return fSeeker.seek();
    }

    private void chooseTarget(int[] t, Area a) {
        EObject eObject = a.getObject(t[0], t[1]);
        if(eObject instanceof Food)
        {
            food = (Food) eObject;
            isFound = true;
        }
    }

    public Area getArea() {
        return area;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public float getFloatX() {
        return floatX;
    }

    public float getFloatY() {
        return floatY;
    }

    public void setName(String n) {
        name = n;
    }

    public String getName() { return name; }

    public void setPosX(int x)
    {
        posX = x;
    }

    public void setPosY(int y)
    {
        posY = y;
    }

    public void setArea(Area a)
    {
        area = a;
    }

    private boolean confirmTarget(EObject e)
    {
        if (e != null)
            return e.isAlive();

        return false;
    }


    private void move(boolean vertical, int direction)
    {
        for(int i = 0; i < 1 / speed; i++)
        {
            if(vertical)
                floatY += speed * direction;
            else
                floatX += speed * direction;
            try
            {
                Thread.sleep(33);
            }
            catch (InterruptedException exc)
            {
                System.out.println(exc);
            }
        }

        if(floatX > 1.0 | floatX < -1.0)
        {
            posX += (int) floatX;
            if(posX > 7 | posX < 0 | posY > 7 | posY < 0)
                area.emigrate(this);

            area.moveObject(id);
            floatX = 0.0F;
        }

        else if(floatY > 1.0 | floatY < -1.0)
        {
            posY += (int) floatY;
            if(posX > 7 | posX < 0 | posY > 7 | posY < 0)
                area.emigrate(this);

            area.moveObject(id);
            floatY = 0.0F;
        }
    }

    private void findPartner()
    {
        Animal target = null;
        Area[] areas = area.getNeighbors();
        for(Area a : areas)
        {
            boolean isFoundPartner;
            int horDistToPartner, verDistToPartner;
            int horDistToAnimal, verDistToAnimal;
            Animal animal;

            Animal [] animalList = a.getFedAnimalList();

            isFoundPartner = false;


            if(animalList.length > 0)
            {
                for(int i = 0; i < animalList.length; i++)
                {
                    animal = animalList[i];

                    if(isFoundPartner)
                    {
                        horDistToAnimal = distTo(area.getX() * 8 + posX, animal.getArea().getX() * 8 + animal.getPosX());
                        verDistToAnimal = distTo(area.getY() * 8 + posY, animal.getArea().getY() * 8 + animal.getPosY());

                        horDistToPartner = distTo(area.getX() * 8 + posX, target.getArea().getX() * 8 + target.getPosX());
                        verDistToPartner = distTo(area.getY() * 8 + posY, target.getArea().getY() * 8 + target.getPosY());

                        if(horDistToAnimal + verDistToAnimal < horDistToPartner + verDistToPartner)
                        {
                            if(animal.getId() != id )
                                target = animal;
                        }
                    }

                    else
                    {
                        if(animal.getId() != id )
                        {
                            target = animal;
                            isFoundPartner = true;
                        }
                    }

                }
            }
        }
        partner = target;
    }

    synchronized void mySuspend()
    {
        suspendingFlag = true;
    }

    synchronized void myResume()
    {
        suspendingFlag = false;
        notify();
    }

    private void moveToEObject(EObject eObj)
    {
        if(confirmTarget(eObj))
        {
            int horDistToFood, verDistToFood;
            int horDirection, verDirection;
            int h, v;
            int gcd;
            BigInteger a, b;

            h = distTo(getAbsPosX(), eObj.getAbsPosX());
            v = distTo(getAbsPosY(), eObj.getAbsPosY());

            do
            {
                horDistToFood = distTo(getAbsPosX(), eObj.getAbsPosX());
                verDistToFood = distTo(getAbsPosY(), eObj.getAbsPosY());

                horDirection = (getAbsPosX() > eObj.getAbsPosX()) ? -1 : 1;
                verDirection = (getAbsPosY() > eObj.getAbsPosY()) ? -1 : 1;

                a = new BigInteger(String.valueOf(horDistToFood));
                b = new BigInteger(String.valueOf(verDistToFood));

                gcd = a.gcd(b).intValue();

                for(int j = 0; j < h / gcd; j++)
                {
                    horDistToFood = distTo(getAbsPosX(), eObj.getAbsPosX());
                    if(horDistToFood > 0 & confirmTarget(eObj))
                        move(false, horDirection);
                    else
                        break;
                }

                for(int l = 0; l < v / gcd; l++)
                {
                    verDistToFood = distTo(getAbsPosY(), eObj.getAbsPosY());
                    if(verDistToFood > 0 & confirmTarget(eObj))
                        move(true, verDirection);
                    else
                        break;
                }
                horDistToFood = distTo(getAbsPosX(), eObj.getAbsPosX());
                verDistToFood = distTo(getAbsPosY(), eObj.getAbsPosY());
            }
            while( (horDistToFood > 1 | verDistToFood > 1) & confirmTarget(eObj));
        }
    }
}