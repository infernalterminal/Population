public abstract class EObject
{
    protected boolean alive;
    protected int posX;
    protected int posY;
    protected int id;
    protected Area area;

    EObject(Area a, int x, int y)
    {
        area = a;
        posX = x;
        posY = y;
        alive = true;
        id = Counter.getId();
    }

    public int getPosX()
    {
        return posX;
    }

    public int getPosY()
    {
        return posY;
    }

    public int getId()
    {
        return id;
    }

    synchronized public boolean isAlive()
    {
        return alive;
    }

    public Area getArea() { return area; }

    public int getAbsPosX()
    {
        return area.getX() * 8 + posX;
    }

    public int getAbsPosY()
    {
        return area.getY() * 8 + posY;
    }
}

class Counter
{
    private static int number = -1;

    synchronized static int getId()
    {
        number++;
        return number;
    }
}