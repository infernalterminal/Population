import java.util.ArrayList;
import java.util.Random;

public class Animal
{
    private boolean isHungry = true;
    private int posX, posY;
    Area area;

    public  Animal(int x, int y, Area a)
    {
        area = a;
        posX = x;
        posY = y;
        AnimalsListManager.add(this);
    }

    public  Animal(Area a)
    {

        int x, y;
        Random random = new Random();
        do
        {
            x = random.nextInt(8);
            y = random.nextInt(8);
        }
        while(a.getObjFromList(x,y) != null);
        area = a;
        posX = x;
        posY = y;
        AnimalsListManager.add(this);
    }

    private Food[] getFood()
    {
        ArrayList<Food> food = new ArrayList<Food>();
        Object[][] list = area.getList();
        //System.out.println(list);
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++)
            {
                if(list[i][j] != null)
                {
                    if(list[i][j].toString().equals("Food"))
                        food.add((Food) list[i][j]);
                }
            }
        }
        return food.toArray(new Food[food.size()]);
    }

    public void findNearestFood()
    {
        Food[] food = (Food[])getFood();

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
}
