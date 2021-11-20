import java.util.Random;

public class Food
{
    private int posX, posY;
    private boolean alive;
    private Area a;

    public Food(int x, int y, Area area)
    {
        a = area;
        posX = x;
        posY = y;
        FoodListManager.add(this);
    }

    public Food(Area area)
    {
        int x,y;
        Random random = new Random();

        do
        {
            x = random.nextInt(8);
            y = random.nextInt(8);
        }
        while(area.getObjFromList(x,y) != null);
        a = area;
        posX = x;
        posY = y;
        FoodListManager.add(this);
    }

    public int getPosX()
    {
        return posX;
    }

    public int getPosY()
    {
        return posY;
    }

    public Area getArea() { return a; }

    public boolean isAlive()
    {
        return alive;
    }
}
