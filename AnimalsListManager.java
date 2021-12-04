import java.util.ArrayList;

public class AnimalsListManager
{
    private static ArrayList<Animal> animalsList = new ArrayList<>(20);

    static void add(Animal a)
    {
        animalsList.add(a);
    }

    static void delete(Animal a)
    {
        if( animalsList.indexOf(a) != -1 )
            animalsList.remove(a);
    }

    static Animal[] getCopyOfList()
    {
        return animalsList.toArray(new Animal[animalsList.size()]);
    }
}
