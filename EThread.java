public class EThread extends Thread
{
    private boolean suspendingFlag;
    private boolean stopFlag;
    private long time;
    private long delay;
    private int circles;
    private EFrame frame;
    private ETimer timer;

    EThread(EFrame f)
    {
        frame = f;
        suspendingFlag = false;
        stopFlag = false;
        time = 15000;
        timer = new ETimer(time);
        delay = 0;
        circles = 0;
    }

    @Override
    public void run()
    {
        try
        {
            while(!suspendingFlag)
            {
                if(timer.start())
                {
                    reboot();
                }
                frame.repaint();
                Thread.sleep(33);
                synchronized(this)
                {
                    while(suspendingFlag)
                    {
                        wait();
                    }
                }
            }
        }
        catch(InterruptedException e)
        {
            System.out.println("Поток EThread прерван");
        }
    }

    synchronized void mySuspend()
    {
        suspendingFlag = true;
        stopFlag = true;

        delay = timer.stop();
        ThreadPoolManager.stop();
    }

    synchronized void myResume()
    {
        suspendingFlag = false;
        stopFlag = false;
        if(delay != 0)
        {
            timer.setDelay(delay);
        }
        else
        {
            timer.setDelay(time);
            frame.updateField();
        }
        ThreadPoolManager.resume();
        notify();
    }

    public void stopOrContinue()
    {
        if(stopFlag)
            myResume();
        else
            mySuspend();
    }

    private void reboot()
    {
        circles += 1;
        ThreadPoolManager.stop();

        timer.setDelay(10000);
        frame.updateField();

        ThreadPoolManager.resume();
    }

    public long getTime()
    {
        return timer.getTime();
    }

    public int getCircles() { return circles; }

    public boolean isSuspended() {
        return suspendingFlag;
    }
}
