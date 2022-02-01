public class Animal implements Runnable
{
    private boolean isHungry = true;
    private int posX, posY;
    private float floatX, floatY;
    private int[] target;
    private String name;
    private Area area;
    private Thread thread;

    public  Animal(Area a, int x, int y)
    {
        area = a;
        posX = x;
        posY = y;
        floatX = 0;
        floatY = 0;
        name = "Animal";
        thread = new Thread(this, name);
        thread.start();
    }

    public void run()
    {
        findFood();
        /*try
        {
            Thread.sleep(3000);
            for(int i = 0; i < 11; i++)
            {
                floatY += 0.1;
                floatX += 0.1;
                System.out.println(floatX + " " + floatY);
                Thread.sleep(500);
            }
            changePosition();
        }
        catch(InterruptedException exc)
        {
            System.out.println(name + " прерван");
        }

        /*if(target[0] != -1)
        {
            moveToTarget();
        }

         */
    }

    private void findNearestFood()
    {
        Food[] food = area.getFoodList();

        int nearestX = food[0].getPosX();
        int nearestY = food[0].getPosY();
        int foodX, foodY, distX, distY;

        int nearestDistX = nearestX > posX ? (nearestX - posX) : (posX - nearestX);
        int nearestDistY = nearestY > posY ? (nearestY - posY) : (posY - nearestY);
        for(int i = 1; i < food.length; i++)
        {
            foodX = food[i].getPosX();
            foodY = food[i].getPosY();
            distX = foodX > posX ? (foodX - posX) : (posX - foodX);
            distY = foodY > posY ? (foodY - posY) : (posY - foodY);

            if((distX < nearestDistX) & (distY < nearestDistY))
            {
                nearestDistX = distX;
                nearestDistY = distY;
            }
        }
        System.out.println(area.getName() + ": " + nearestDistX + " " + nearestDistY);
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

    public void setName(String n)
    {
        name = n;
    }

    private void findFood()
    {
        Object objs[][] = area.getList();
        Seeker fSeeker = new Seeker("Food", objs, 8, 8, posX, posY);
        target = fSeeker.seek();
        //System.out.println(posX + " - " + posY + " - Animal");
        //if(target[0] != -1)
         //   System.out.println(target[0] + " - " + target[1] + " - Food");
    }

    private void changePosition()
    {
        try
        {
            int newPosX = (int)(posX + floatX);
            int newPosY = (int)(posY + floatY);
            System.out.println("posX: " + newPosX);
            System.out.println("posY: " + newPosY);
            area.moveObject(this, posX, posY, newPosX , newPosY);
            floatX = 0;
            floatY = 0;
            posX = newPosX;
            posY = newPosY;
            System.out.println("position has changed successful");
            System.out.println(posX + " - " + posY + " - Animal");
        }
        catch(IndexOutOfBoundsException exc)
        {
            System.out.println(name + " cannot move");
        }
    }

    private boolean moveToTarget()
    {
        Food food;
        int i = 0;
        if(target[0] != -1)
        {
            try
            {
                Object obj = area.getObject(target[0], target[1]);
                if(obj instanceof Food)
                {
                    food = (Food) obj;
                    System.out.println(target[0] + " - " + target[1] + " is Food");
                }

                else
                    return false;

                if(food.isAlive())
                {
                    while(food.isAlive())
                    {
                        System.out.print(".");
                        thread.sleep(100);
                        if( i >= 100)
                            break;
                        i++;
                    }
                }
            }
            catch(IndexOutOfBoundsException exc)
            {
                System.out.println(exc);
                return false;
            } catch (InterruptedException e) {
                return false;
            }

        }
        return true;
    }
}
