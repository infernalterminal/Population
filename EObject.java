public abstract class EObject
{
    protected int posX;
    protected int posY;
    protected int id;

    EObject(int x, int y)
    {
        posX = x;
        posY = y;
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