import java.util.ArrayList;

public class FoodListManager
{
    private static ArrayList<Food> foodList = new ArrayList<>(20);

    static void add(Food f)
    {
        foodList.add(f);
    }

    static void delete(Food f)
    {
        if( foodList.indexOf(f) != -1 )
            foodList.remove(f);
    }

    static Food[] getCopyOfList()
    {
        return foodList.toArray(new Food[foodList.size()]);
    }
}
