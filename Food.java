import java.util.Random;

public class Food
{
    private int posX, posY;
    private boolean alive;
    private Area area;

    public Food(Area a, int x, int y)
    {
        area = a;
        posX = x;
        posY = y;
        //FoodListManager.add(this);
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
