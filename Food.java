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
        alive = true;
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
