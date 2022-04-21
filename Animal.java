import java.awt.*;
import java.math.BigInteger;
import java.util.ArrayList;

public class Animal extends EObject implements Runnable {

    private boolean isFound;
    private boolean isParent;
    private boolean firstPartner;
    private boolean suspendingFlag;

    private float floatX, floatY;
    private float speed;

    private int foodLimit, foodCount;
    private int circles;

    private String name;

    private Color color;
    private AnimalLiveStages stage;
    private Food food;
    private Animal partner;

    public Animal(Area a, int x, int y) {
        super(a, x, y);

        isFound = false;
        isParent = false;
        firstPartner = true;
        suspendingFlag = false;

        floatX = 0;
        floatY = 0;

        speed = 0.04F;
        circles = 0;
        color = new Color(216,91,85);

        foodLimit = 1;
        foodCount = 1;

        name = "Animal" + getId();

        stage = AnimalLiveStages.GROWTH;
        food = null;
        partner = null;

        ThreadPoolManager.addTask(this);
    }

    public void run()
    {
        try
        {
            while(foodCount < foodLimit & !Thread.currentThread().isInterrupted())
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
            if(stage == AnimalLiveStages.REPRODUCTION & !isParent)
            {
                area.addToMatureAnimalList(id);

                if(partner == null)
                {
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
                }

                moveToEObject(partner);
                if(!isParent & firstPartner & !Thread.currentThread().isInterrupted())
                    makeChild();

            }
            else
                return;

        }
        catch (InterruptedException exc)
        {
            //System.out.println("Thread " + name + " is terminated");
            return;
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
        circles = 0;
        foodCount = 0;
        switch (stage)
        {
            case GROWTH:
                speed = 0.04F;
                color = new Color(216,91,85);
                foodLimit = 1;
                break;
            case REPRODUCTION:
                color = Color.red;
                speed = 0.05F;
                foodLimit = 2;
                break;
            case DYING:
                color = new Color(153,0,51);
                speed = 0.04F;
                break;
        }
    }

    public AnimalLiveStages getStage()
    {
        return stage;
    }

    public Color getColor() { return color; }

    public void hasChild() { isParent = true; }

    private boolean confirmTarget(EObject e)
    {
        if (e != null)
            return e.isAlive();

        return false;
    }


    private void move(boolean vertical, int direction) throws InterruptedException
    {
        for(int i = 0; i < 1 / speed; i++)
        {

            if(!Thread.currentThread().isInterrupted())
            {
                if(vertical)
                    floatY += speed * direction;
                else
                    floatX += speed * direction;

                Thread.sleep(33);
                if(floatY >= 1.0 | floatY <= -1 | floatX <= -1 | floatX >= 1)
                    break;
            }
            else
            {
                throw new InterruptedException();
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

    private boolean findPartner() throws InterruptedException
    {
        Animal target = null;
        Area[] areas = area.getAllNeighbors();

        for(Area a : areas)
        {
            if(!Thread.currentThread().isInterrupted())
            {
                boolean isFoundPartner;
                int horDistToPartner, verDistToPartner;
                int horDistToAnimal, verDistToAnimal;
                Animal animal;

                Animal [] animalList = a.getMatureAnimals();

                isFoundPartner = false;

                if(animalList.length > 0)
                {
                    for(int i = 0; i < animalList.length; i++)
                    {
                        if(!Thread.currentThread().isInterrupted())
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
                        else
                        {
                            throw new InterruptedException();
                        }
                    }
                }
            }
            else
            {
                throw new InterruptedException();
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
        area.removeFromMatureAnimalList(id);
    }

    private void waiting() throws InterruptedException
    {
        while(suspendingFlag)
        {

            if(!Thread.currentThread().isInterrupted())
            {
                synchronized (this)
                {
                    wait();
                }
            }
            else
            {
                throw new InterruptedException();
            }
        }
    }

    synchronized void mySuspend() throws InterruptedException
    {
        suspendingFlag = true;
        waiting();
    }

    synchronized void myResume()
    {
        suspendingFlag = false;
        notify();
    }

    private boolean moveToEObject(EObject eObj) throws InterruptedException
    {
        if(confirmTarget(eObj))
        {
            int h, v;
            int k = (eObj instanceof Animal) ? 2 : 1;
            int horDistToTarget, verDistToTarget;
            int horDirection, verDirection;
            int gcd;
            BigInteger a, b;

            horDistToTarget = (int) Math.ceil(distTo(getAbsPosX(), eObj.getAbsPosX()) / k);
            verDistToTarget = (int) Math.ceil(distTo(getAbsPosY(), eObj.getAbsPosY()) / k);

            h = horDistToTarget;
            v = verDistToTarget;

            horDirection = (getAbsPosX() > eObj.getAbsPosX()) ? -1 : 1;
            verDirection = (getAbsPosY() > eObj.getAbsPosY()) ? -1 : 1;

            a = new BigInteger(String.valueOf(horDistToTarget));
            b = new BigInteger(String.valueOf(verDistToTarget));

            gcd = a.gcd(b).intValue();

            for(int i = 0; i < gcd; i++)
            {
                for(int j = 0; j < h / gcd; j++)
                {
                    if(!Thread.currentThread().isInterrupted())
                    {
                        if(confirmTarget(eObj))
                            move(false, horDirection);
                    }
                    else
                    {
                        throw new InterruptedException();
                    }
                }

                for(int l = 0; l < v / gcd; l++)
                {
                    if(!Thread.currentThread().isInterrupted())
                    {
                        if(confirmTarget(eObj))
                            move(true, verDirection);
                    }
                    else
                    {
                        throw new InterruptedException();
                    }
                }
            }

            horDistToTarget = distTo(getAbsPosX(), eObj.getAbsPosX());
            verDistToTarget = distTo(getAbsPosY(), eObj.getAbsPosY());
            if( horDistToTarget <= 1 & verDistToTarget <= 1 & confirmTarget(eObj) )
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
            if ((posY + i < area.getHeight()) & (posY + i >= 0) )
            {
                if (area.getObject(posX, posY + i) == null) {
                    area.createAnimal(posX, posY + i);
                    partner.hasChild();
                    hasChild();
                    return;
                }
            }
        }
        for(int j = -1; j < 2; j++)
        {
            if( (posX + j >= 0) & (posX + j < area.getWidth()))
            {
                if (area.getObject(posX + j, posY) == null) {
                    area.createAnimal(posX + j, posY);
                    partner.hasChild();
                    hasChild();
                    return;
                }
            }
        }

        for(int i = -1; i < 2; i++)
        {
            for(int j = -1; j < 2; j++)
            {
                if ( (posX + j >= 0) & (posX + j < area.getWidth()) & (posY + i < area.getHeight()) & (posY + i >= 0) )
                {
                    if (area.getObject(posX + j, posY + i) == null) {
                        area.createAnimal(posX + j, posY + i);
                        partner.hasChild();
                        hasChild();
                        return;
                    }
                }
            }
        }
    }

    public void update()
    {
        if(foodCount < foodLimit)
        {
            area.removeObject(id);
            area.increaseDeadAnimals();
            ThreadPoolManager.removeTask(this);
        }
        else
        {
            if(circles >= stage.getCircles())
            {
                switch (stage)
                {
                    case GROWTH:
                        stage = AnimalLiveStages.REPRODUCTION;
                        circles = 0;
                        color = Color.red;
                        speed = 0.05F;
                        foodLimit = 2;
                        break;
                    case REPRODUCTION:
                        stage = AnimalLiveStages.DYING;
                        circles = 0;
                        color = new Color(153,0,51);
                        speed = 0.04F;
                        break;
                    case DYING:
                        circles += 1;
                        speed -= 0.005F;
                        break;
                }
            }
            else
                circles += 1;
        }
        foodCount = 0;
        food = null;
        partner = null;
    }
}