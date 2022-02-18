public class Food extends EObject
{
    private boolean alive;
    private Area area;

    public Food(Area a, int x, int y)
    {
        super(x, y);
        area = a;
        alive = true;
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
