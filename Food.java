import java.util.Random;

public class Food
{
    private int posX, posY;
    private boolean alive;
    private Area area;

    public Food(int x, int y, Area a)
    {
        area = a;
        posX = x;
        posY = y;
        FoodListManager.add(this);
    }

    public Food(Area a)
    {
        int x,y;
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

    public Area getArea() { return area; }

    public boolean isAlive()
    {
        return alive;
    }

    @Override
    public String toString() {
        return "Food";
    }
}
