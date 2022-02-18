public class Seeker
{
    private String ob;
    private int wdt, hgh;
    private int steps;
    private int posX, posY;
    private Object[][] objects;
    private int nearestX, nearestY;
    private boolean isFound = false;

    private void isObj(int x, int y)
    {
        if(objects[y][x] != null)
        {
            if(objects[y][x].getClass().getName().equals(ob))
                compare(x,y);
        }
    }

    Seeker(String  className, Object[][] o, int width, int height, int x, int y)
    {
        hgh = height;
        wdt = width;
        steps = 0;
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
        }
    }

    private void compare(int x, int y)
    {
        if(isFound)
        {
            int distWdt = nearestX > posX ? (nearestX - posX) : (posX - nearestX);
            int distHgh = nearestY > posY ? (nearestY - posY) : (posY - nearestY);
            int newDistWdt = x > posX ? (x - posX) : (posX - x);
            int newDistHgh = y > posY ? (y - posY) : (posY - y);

            if(Math.sqrt(distHgh*distHgh + distWdt * distWdt) > Math.sqrt(newDistWdt*newDistWdt + newDistHgh*newDistHgh))
            {
                nearestX = x;
                nearestY = y;
            }
        }
        else
        {
            nearestX = x;
            nearestY = y;
            isFound = true;
        }
    }

    public int[] seek()
    {
        boolean vertical = false;
        boolean reverse = false;
        boolean check = false;
        int h;
        int w;
        int k = 1;
        int range = 1;
        int counter = 1;

        isObj(posX,posY);
        steps++;
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
                    isObj(w, h);
                    search(w,h,!vertical, range);
                    if(isFound)
                        check = true;
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

            if(isFound & check)
                return new int[] {nearestX, nearestY};

        } while (steps < wdt * hgh);
        return new int[] {-1};
    }
}