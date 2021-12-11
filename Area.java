import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Area
{
    private int posX, posY;
    private int aWidth, aHeight;
    private String aName;
    private Color aColor;
    Frame frame;
    Object [][] aList;
    ArrayList<Food> foodList;
    ArrayList<Animal> animalList;

    public Area(Frame f, int x, int y, int width, int height)
    {
        frame = f;
        posX = x;
        posY = y;
        aWidth = width;
        aHeight = height;
        aList = new Object[8][8];
        foodList = new ArrayList<Food>(10);
        animalList = new ArrayList<Animal>(10);
    }

    public Area(Frame f)
    {
        frame = f;
        posX = 0;
        posY = 0;
        aWidth = 64;
        aHeight = 64;
        aList = new Object[8][8];
        foodList = new ArrayList<Food>(10);
        animalList = new ArrayList<Animal>(10);
    }

    public void setXAndY(int x, int y)
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

    public int getWidth() { return aWidth; }

    public int getHeight() { return aHeight; }

    public String getName() { return aName; }

    public Object[][] getList()
    {
        return aList;
    }

    public Food[] getFoodList()
    {
        return foodList.toArray(new Food[foodList.size()]);
    }

    public Animal[] getAnimalList()
    {
        return animalList.toArray(new Animal[animalList.size()]);
    }

    public int countFreeSpace()
    {
        int freeSpace = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++)
            {
                if(aList[i][j] == null)
                    freeSpace++;
            }
        }
        return freeSpace;
    }

    public void createFood()
    {
        if(countFreeSpace() < 1)
        {
            return;
        }
        int x,y;
        Random random = new Random();

        do
        {
            x = random.nextInt(8);
            y = random.nextInt(8);
        }
        while(aList[y][x] != null);
        Food f = new Food(this, x, y);
        aList[y][x] = f;
        foodList.add(f);
    }

    public void createFood(int n)
    {
        int freeSpace = countFreeSpace();

        int count = n < freeSpace ? n : freeSpace;
        while(count > 0)
        {
            createFood();
            count--;
        }
    }

    public void createPopulation()
    {
        if(countFreeSpace() < 1)
        {
            return;
        }
        int x,y;
        Random random = new Random();

        do
        {
            x = random.nextInt(8);
            y = random.nextInt(8);
        }
        while(aList[y][x] != null);
        Animal a = new Animal(this, x, y);
        animalList.add(a);
        aList[y][x] = a;
    }

    public void createPopulation(int n)
    {
        int freeSpace = countFreeSpace();

        int count = n < freeSpace ? n : freeSpace;
        while(count > 0)
        {
            createPopulation();
            count--;
        }
    }
}
