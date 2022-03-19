import java.math.BigInteger;
import java.util.ArrayList;

public class Animal extends EObject implements Runnable {

    private boolean isHungry;
    private boolean isFound;
    private boolean suspendingFlag;

    private float floatX, floatY;
    private float speed;

    private int foodLimit, foodCount;

    private String name;

    private AnimalLiveStages stage;
    private Food food;
    private Animal partner;
    private Thread thread;

    public Animal(Area a, int x, int y) {
        super(a, x, y);

        isHungry = true;
        isFound = false;
        suspendingFlag = false;

        floatX = 0;
        floatY = 0;

        speed = 0.05F;

        foodLimit = 1;
        foodCount = 0;

        name = "Animal";

        stage = AnimalLiveStages.GROWTH;
        food = null;
        partner = null;
        thread = new Thread(this, name);
        thread.setPriority(1);
        thread.start();
    }

    public void run()
    {

        while(foodCount < foodLimit)
        {
            findFood();

            if(moveToEObject(food))
            {
                foodCount += 1;
                food.eat();
            }
            food = null;
            isFound = false;
        }
        isHungry = false;
        area.fed(id);

        boolean firstPartner = true;
        if(findPartner())
        {
            firstPartner = false;
            partner.setPartner(this);
            setPartner(partner);
            partner.myResume();
        }

        else
        {
            firstPartner = true;
            suspendingFlag = true;
            waiting();
        }

        moveToEObject(partner);
        if(firstPartner)
            makeChild();

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

    public void setStage(AnimalLiveStages stg)
    {
        stage = stg;
    }

    public AnimalLiveStages getStage()
    {
        return stage;
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

    private boolean findPartner()
    {
        Animal target = null;
        Area[] areas = area.getAllNeighbors();
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
                        horDistToAnimal = distTo(getAbsPosX(), animal.getAbsPosX());
                        verDistToAnimal = distTo(getAbsPosY(), animal.getAbsPosY());

                        horDistToPartner = distTo(getAbsPosX(), target.getAbsPosX());
                        verDistToPartner = distTo(getAbsPosY(), target.getAbsPosY());

                        if(horDistToAnimal + verDistToAnimal < horDistToPartner + verDistToPartner)
                        {
                            if(animal.getId() != id & !animal.haveAPartner())
                                target = animal;
                        }
                    }

                    else
                    {
                        if(animal.getId() != id & !animal.haveAPartner())
                        {
                            target = animal;
                            isFoundPartner = true;
                        }
                    }

                }
            }
        }
        partner = target;
        if(partner != null)
            return true;
        else
            return false;
    }

    public boolean haveAPartner()
    {
        if(partner != null)
            return true;

        return false;
    }

    public void setPartner(Animal animal)
    {
        partner = animal;
        area.removeFromFedList(id);
    }

    private void waiting()
    {
        try
        {
            while(suspendingFlag)
            {
                synchronized (this)
                {
                    wait();
                }
            }
        }
        catch (InterruptedException exc)
        {
            System.out.println("Thread " + name + ": " + exc);
        }
    }

    synchronized void mySuspend()
    {
        suspendingFlag = true;
        waiting();
    }

    synchronized void myResume()
    {
        suspendingFlag = false;
        notify();
        return;
    }

    private boolean moveToEObject(EObject eObj)
    {
        if(confirmTarget(eObj))
        {
            int k = (eObj instanceof Animal) ? 2 : 1;
            int horDistToFood, verDistToFood;
            int horDirection, verDirection;
            int gcd;
            BigInteger a, b;

            horDistToFood = distTo(getAbsPosX(), eObj.getAbsPosX()) / k;
            verDistToFood = distTo(getAbsPosY(), eObj.getAbsPosY()) / k;

            horDirection = (getAbsPosX() > eObj.getAbsPosX()) ? -1 : 1;
            verDirection = (getAbsPosY() > eObj.getAbsPosY()) ? -1 : 1;

            a = new BigInteger(String.valueOf(horDistToFood));
            b = new BigInteger(String.valueOf(verDistToFood));

            gcd = a.gcd(b).intValue();


            for(int i = 0; i < gcd; i++)
            {
                for(int j = 0; j < horDistToFood / gcd; j++)
                {
                    horDistToFood = distTo(getAbsPosX(), eObj.getAbsPosX());
                    if(horDistToFood > 0 & confirmTarget(eObj))
                        move(false, horDirection);
                    else
                        break;
                }

                for(int l = 0; l < verDistToFood / gcd; l++)
                {
                    verDistToFood = distTo(getAbsPosY(), eObj.getAbsPosY());
                    if(verDistToFood > 0 & confirmTarget(eObj))
                        move(true, verDirection);
                    else
                        break;
                }
            }

            if( horDistToFood <= 1 & verDistToFood <= 1 & confirmTarget(eObj) )
                return true;
            else
                return false;
        }
        else
            return false;
    }

    public void makeChild()
    {
        for(int i = -1; i < 2; i++)
        {
            for(int j = -1; j < 2; j++)
            {
                if ( (posX + j >= 0) & (posX + j < area.getWidth()) & (posY + i < area.getHeight()) & (posY + i >= 0) )
                {
                    if (area.getObject(posX + j, posY + i) == null)
                    {
                        area.createAnimal(posX + j, posY + i);
                        return;
                    }

                }
            }
        }
    }
}