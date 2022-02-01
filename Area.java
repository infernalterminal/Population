import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Area
{
    private int posX, posY;
    private int aWidth, aHeight;
    private String aName;
    private Color aColor;
    private Object [][] aList;
    private ArrayList<Food> foodList;
    private ArrayList<Animal> animalList;
    private AreaManager aManager = null;
    private Area[] aNeighbors = null;


    public Area(int x, int y, int width, int height)
    {
        posX = x;
        posY = y;
        aWidth = width;
        aHeight = height;
        aList = new Object[8][8];
        foodList = new ArrayList<Food>(10);
        animalList = new ArrayList<Animal>(10);
    }

    public Area()
    {
        posX = 0;
        posY = 0;
        aWidth = 64;
        aHeight = 64;
        aList = new Object[8][8];
        foodList = new ArrayList<Food>(10);
        animalList = new ArrayList<Animal>(10);
    }

    public void setAreaManager(AreaManager areaManager)
    {
        aManager = areaManager;
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

    public Object getObject(int x, int y)
    {
        if(x >= aWidth | x < 0 | y >= aHeight | y < 0)
            throw new IndexOutOfBoundsException();
        return aList[y][x];
    }

    public void moveObject(Object object, int x, int y, int newX, int newY) {
        if (x >= aWidth | x < 0 | y >= aHeight | y < 0)
            throw new IndexOutOfBoundsException();
        if (newX >= aWidth | newX < 0 | newY >= aHeight | newY < 0)
            throw new IndexOutOfBoundsException();

        if (getObject(x, y) == object)
        {
            aList[y][x] = null;
            //System.out.println(x + " - " + y + " - right object");
        }
        aList[newY][newX] = object;
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
        if(countFreeSpace() > 0)
        {
            int[] space = findFreeSpace();
            Food f = new Food(this, space[0], space[1]);
            aList[space[1]][space[0]] = f;
            foodList.add(f);
        }
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
        if(countFreeSpace() > 0)
        {
            int[] space = findFreeSpace();
            Animal a = new Animal(this, space[0], space[1]);
            aList[space[1]][space[0]] = a;
            animalList.add(a);
            //a.findFood();
        }
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

    private int[] findFreeSpace()
    {
        /*if(countFreeSpace() < 1)
        {
            return null;
        }

         */
        int x,y;
        Random random = new Random();

        do
        {
            x = random.nextInt(8);
            y = random.nextInt(8);
        }
        while(aList[y][x] != null);

        return new int[] {x,y};
    }

    public void findNeighbors()
    {
        try
        {
            aNeighbors = aManager.getNeighbors(posX, posY);
        }
        catch (IndexOutOfBoundsException exc)
        {
            System.out.println(exc);
        }
        //for(Area a : aNeighbors)
        //    System.out.println(a.getName());
    }
}