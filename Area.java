import java.awt.*;

public class Area
{
    private int posX, posY;
    private int aWidth, aHeight;
    private String aName;
    private Color aColor;
    Frame frame;
    Object [][] aList;

    public Area(Frame f, int x, int y, int width, int height)
    {
        frame = f;
        posX = x;
        posY = y;
        aWidth = width;
        aHeight = height;
        aList = new Object[8][8];
    }

    public Area(Frame f)
    {
        frame = f;
        posX = 0;
        posY = 0;
        aWidth = 64;
        aHeight = 64;
        aList = new Object[8][8];
    }

    public void setXY(int x, int y)
    {
        posX = x;
        posY = y;
    }

    public void setWidthAndHeight(int w, int h)
    {
        aWidth = w;
        aHeight = h;
    }

    public void setName(String name)
    {
        aName = name;
    }

    public void setColor(Color c)
    {
        aColor = c;
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }

    public int getWidth() {
        return aWidth;
    }

    public int getHeight() {
        return aHeight;
    }

    public Object findObj(int x, int y)
    {
        int wStep = aWidth / 8;
        int hStep = aHeight / 8;
        int h = y / hStep - 1;
        int w = x / wStep - 1;
        if (aList[h][w] != null)
            return aList[h][w];
        return null;
    }

    public Object getObjFromList(int x, int y)
    {
        if(aList[y][x] == null)
            return null;
        return aList[y][x];
    }

    private void createFood()
    {
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(aList[i][j] == null)
                {
                    aList[i][j] = new Food(j,i,this);
                }
            }
        }
    }

    public void start()
    {
        for(int i = 0; i < 2; i++)
        {
            Food f = new Food(this);
            aList[f.getPosY()][f.getPosX()] = f;
        }
    }
}
