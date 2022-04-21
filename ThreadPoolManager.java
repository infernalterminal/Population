import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class ThreadPoolManager
{
    private static ExecutorService pool = Executors.newCachedThreadPool();
    private static List<Runnable> tasks = new ArrayList<>();

    static public void addTask(Runnable task)
    {
        tasks.add(task);
        if(!pool.isShutdown())
            pool.execute(task);
    }

    static public void removeTask(Runnable task)
    {
        tasks.remove(task);
    }

    static public void stop()
    {
        pool.shutdownNow();
        //System.out.println("Потоки остановлены");
    }

    public static void resume()
    {
        int size = tasks.size();
        pool = Executors.newCachedThreadPool();

        for(int i = 0; i < size; i++)
        {
            pool.execute(tasks.get(i));
        }
        //System.out.println("Потоки возобновлены");
    }
}
