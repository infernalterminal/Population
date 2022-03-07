import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Area
{
    private int posX, posY;
    private int aWidth, aHeight;
    private String aName;
    private Color aColor;
    private EObject [][] aList;
    private ArrayList<Food> foodList;
    private ArrayList<Animal> animalList;
    private ArrayList<Animal> fedAnimal;
    private AreaManager aManager = null;
    private Area[] aNeighbors = null;

    public Area()
    {
        posX = 0;
        posY = 0;
        aWidth = 64;
        aHeight = 64;
        aList = new EObject[8][8];
        foodList = new ArrayList<Food>(10);
        animalList = new ArrayList<Animal>(10);
        fedAnimal = new ArrayList<>(2);
    }

    public Area(int x, int y, int width, int height)
    {
        posX = x;
        posY = y;
        aWidth = width;
        aHeight = height;
        aList = new EObject[8][8];
        foodList = new ArrayList<Food>(10);
        animalList = new ArrayList<Animal>(10);
        fedAnimal = new ArrayList<>(2);
    }

    synchronized int countFreeSpace()
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

        int count = Math.min(freeSpace, n);
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
        }
    }

    public void createPopulation(int n)
    {
        int freeSpace = countFreeSpace();

        int count = Math.min(freeSpace, n);
        while(count > 0)
        {
            createPopulation();
            count--;
        }
    }

    synchronized int[] findFreeSpace()
    {
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

    private void findNeighbors()
    {
        try
        {
            aNeighbors = aManager.getNeighbors(posX, posY);
        }
        catch (IndexOutOfBoundsException exc)
        {
            System.out.println(exc);
        }
    }

    synchronized public void moveObject(int id)
    {
        EObject eObject;
        int [] position = findEObjectById(id);
        if(position[0] != -1)
        {
            eObject = aList[position[0]][position[1]];
            aList[position[0]][position[1]] = null;
            aList[eObject.getPosY()][eObject.getPosX()] = eObject;
        }
    }

    public void removeObject(int id)
    {
        EObject object;
        EObject[] list;

        int [] position = findEObjectById(id);
        if(position[0] != -1)
            aList[position[0]][position[1]] = null;

        list = animalList.toArray(new EObject[animalList.size()]);
        for(int i = 0; i < list.length; i++)
        {
            if(list[i].getId() == id)
            {
                //object = list[i];
                animalList.remove(list[i]);
                return;
            }
        }

        list = foodList.toArray(new EObject[foodList.size()]);
        for(int i = 0; i < list.length; i++)
        {
            if(list[i].getId() == id)
            {
                //object = list[i];
                foodList.remove(list[i]);
                return;
            }
        }

    }

    public void setAreaManager(AreaManager areaManager)
    {
        aManager = areaManager;
        findNeighbors();
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

    synchronized Food[] getFoodList()
    {
        return foodList.toArray(new Food[foodList.size()]);
    }

    synchronized Animal[] getAnimalList()
    {
        return animalList.toArray(new Animal[animalList.size()]);
    }

    synchronized Animal[] getFedAnimalList() { return fedAnimal.toArray(new Animal[fedAnimal.size()]); }

    public Area[] getNeighbors() {
        return aNeighbors;
    }

    public Area[] getAllNeighbors()
    {
        int wdt = aManager.getWidth();
        int hgh = aManager.getHeight();
        Area[][] areas = aManager.getAreas();
        Area[] allAreas = new Area[wdt * hgh];
        for(int i = 0; i < hgh; i++)
        {
            for(int j = 0; j < wdt; j++)
                allAreas[ (i * wdt) + j ] = areas[i][j];
        }
        return allAreas;
    }

    public EObject getObject(int x, int y)
    {
        if(x >= aWidth | x < 0 | y >= aHeight | y < 0)
            throw new IndexOutOfBoundsException();
        return aList[y][x];
    }

    public void emigrate(Animal animal)
    {
        if(animalList.contains(animal))
        {
            int areaX = posX;
            int areaY = posY;
            Area a;

            if(animal.getPosX() > 7)
            {
                areaX = posX + 1;
            }
            else if(animal.getPosX() < 0)
            {
                areaX = posX - 1;
            }
            if (animal.getPosY() > 7)
            {
                areaY = posY + 1;
            }
            else if (animal.getPosY() < 0)
            {
                areaY = posY - 1;
            }

            a = aManager.getArea(areaX, areaY);
            if(a.countFreeSpace() > 0)
                a.migrate(animal);

            removeObject(animal.getId());
        }
    }

    public void migrate(Animal animal)
    {
        if(countFreeSpace() > 0)
        {
            int newX = animal.getPosX();
            int newY = animal.getPosY();

            if(newX > 7)
            {
                newX = 0;
            }
            else if(newX < 0)
            {
                newX = 7;
            }
            else if (newY > 7)
            {
                newY = 0;
            }
            else if (newY < 0)
            {
                newY = 7;
            }

            animal.setPosX(newX);
            animal.setPosY(newY);
            animal.setArea(this);

            animalList.add(animal);
            aList[newY][newX] = animal;
        }
    }

    public int[] findEObjectById(int id)
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(aList[i][j] != null)
                {
                    if(aList[i][j].getId() == id)
                    {
                        return new int[]{i,j};
                    }
                }
            }
        }

        return new int[]{-1};
    }

    public void fed(int id)
    {
        int [] position = findEObjectById(id);
        EObject eObject;

        if(position[0] != -1)
        {
            eObject = aList[position[0]][position[1]];
            if(eObject instanceof Animal)
                fedAnimal.add((Animal) eObject);
        }
    }
}