public class Seeker <T>
{
    private T ob;

    Seeker(T o)
    {
        ob = o;
    }

    public Seeker() {}

    private Object isT(Object objects[][], int x, int y)
    {
        if(objects[y][x] != null)
        {
            if(objects[y][x].getClass().getName().equals(this.getClass().getName()))
            {
                System.out.println(x + " - " + y + " - Food");
                return objects[y][x];
            }
        }

        System.out.println(x + " - " + y + " - null");
        return null;
    }

    public Object searchInList(Object objects[][])
    {
        for(Object o: objects)
        {
            if(o != null)
            {
                System.out.println(o.getClass());
                if(o.getClass() == this.getClass())
                {
                    System.out.println(o.getClass());
                    return o;
                }
            }
        }
        return null;
    }

}