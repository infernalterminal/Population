import java.util.ArrayList;
import java.util.Random;

public class Animal
{
    private boolean isHungry = true;
    private int posX, posY;
    Area area;

    public  Animal(Area a, int x, int y)
    {
        area = a;
        posX = x;
        posY = y;
    }

    public void findNearestFood()
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

    public void findFood()
    {
        Object objs[][] = area.getList();
        Seeker fSeeker = new Seeker("Food", objs, 8, 8, posX, posY);
        fSeeker.seek();

        //Seeker aSeeker = new Seeker("Animal", objs, 8, 8, posX, posY);

    }
}
