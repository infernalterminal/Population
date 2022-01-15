import java.util.ArrayList;

public class Seeker
{
    private String ob;
    private int wdt, hgh;
    private int steps;
    int posX, posY;
    private Object[][] objects;

    private Object isObj(int x, int y)
    {
        if(objects[y][x] != null)
        {
            if(objects[y][x].getClass().getName().equals(ob))
            {
                System.out.println(x + " - " + y + " - " + objects[y][x].getClass().getSimpleName());
                return objects[y][x];
            }
        }
        return null;
    }

    Seeker(String  className, Object[][] o, int width, int height, int x, int y)
    {
        hgh = height;
        wdt = width;
        steps = 1;
        ob = className;
        objects = o;
        posX = x;
        posY = y;
    }

    private void search(int x, int y, boolean vertical, int range)
    {
        int x1 = x;
        int y1 = y;
        for(int i = 0; i < range ; i++)
        {
            if(vertical)
                y1--;
            else
                x1--;

            if(y1 < 0 | x1 < 0)
                break;
            steps ++;
            isObj(x1, y1);
            //System.out.println(x1 + " - " + y1 + " - ");
        }

        x1 = x;
        y1 = y;
        for(int j = 0; j < range; j++)
        {
            if(vertical)
                y1++;
            else
                x1++;

            if(y1 >= hgh | x1 >= wdt )
                break;
            steps++;
            isObj(x1, y1);
            //System.out.println(x1 + " - " + y1 + " + ");
        }
    }

    public void seek()
    {
        boolean vertical = false;
        boolean reverse = false;
        int h = posY;
        int w = posX;
        int k = 1;
        int range = 1;
        int counter = 1;

        do
        {
            for (int i = 0; i < 4; i++)
            {
                h = posY;
                w = posX;
                if(vertical)
                    h = posY + (range * k);
                else
                    w = posX + (range * k);

                if((w > -1 & h > -1) & (w < wdt & h < hgh))
                {
                    steps++;
                    //System.out.println(w + " - " + h + " m");
                    isObj(w, h);
                    search(w,h,!vertical, range);
                }

                counter++;

                vertical = !vertical;
                if (counter % 2 == 0)
                {
                    counter = 0;
                    reverse = !reverse;
                }
                k = reverse ? -1 : 1;
            }
            range ++;

        } while (steps < wdt * hgh);
    }
}