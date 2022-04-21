import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ETimer
{
    private long start;
    private long finish;
    private long delay;
    private long time;
    private Timer timer;

    ETimer(long milliSeconds)
    {
        start = 0;
        delay = milliSeconds;
        time = milliSeconds;
        timer = new Timer(100, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                time -= 100;
                if(time <= 0)
                    timer.stop();
            }
        });
    }

    public void setDelay(long milliSeconds)
    {
        delay = milliSeconds;
        time = delay;
    }

    public boolean start()
    {
        if(start == 0)
        {
            timer.start();
            start = System.currentTimeMillis();
            finish = start + delay;
        }

        if(finish <= System.currentTimeMillis())
        {
            start = 0;
            return true;
        }
        else
            return false;
    }

    public long stop()
    {
        if(start != 0)
            return finish - System.currentTimeMillis();

        return 0;
    }

    public long getTime() { return time; }
}