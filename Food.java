public class Food extends EObject
{
    private boolean hasChild;

    public Food(Area a, int x, int y)
    {
        super(a, x, y);
        hasChild = false;
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

    void update()
    {
        if(!hasChild)
        {
            Object[][] areas = area.getList();
            for(int i = -1; i < 2; i++)
            {
                for(int j = -1; j < 2; j++)
                {
                    if ( (posX + j >= 0) & (posX + j < area.getWidth()) & (posY + i < area.getHeight()) & (posY + i >= 0) )
                    {
                        if (area.getObject(posX + j, posY + i) == null)
                        {
                            area.createFood(posX + j, posY + i);
                            hasChild = true;
                            return;
                        }

                    }
                }
            }
        }
    }
}