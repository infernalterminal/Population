public class Food extends EObject
{
    public Food(Area a, int x, int y)
    {
        super(a, x, y);
    }

    @Override
    public String toString() {
        return "Food";
    }

    synchronized void eat()
    {
        alive = false;
        area.removeObject(getId());
    }
}