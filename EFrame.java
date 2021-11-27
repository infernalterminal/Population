import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EFrame extends Frame implements Runnable
{
    boolean suspendingFlag;
    private Dimension d;
    private Thread thrd;
    private Area[][] areas = new Area[3][5];

    Image buffer = null;

    public EFrame()
    {
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                suspendingFlag = true;
                System.out.println(0);
                System.exit(0);
            }
        });
        thrd = new Thread(this, "EFrame");
        suspendingFlag = false;
        int x = 128;
        int y = 128;
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 5; j++)
            {
                Area area = new Area(this, (x * j) , (y * i) , x, y);
                area.setName("Area" + i + "-" + j);
                areas[i][j] = area;
                area.start();
            }
        }
        repaint();
        thrd.start();
    }

    @Override
    public void run()
    {
        try
        {
            while(!suspendingFlag)
            {
                repaint();
                System.out.println("update EFrame");
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
            System.out.println("Поток EFrame приостановлен");
        }
    }

    synchronized void mySuspend()
    {
        suspendingFlag = true;
    }

    synchronized void myResume()
    {
        suspendingFlag = false;
        notify();
    }

    @Override
    public void paint(Graphics g)
    {
        Graphics screengc = null;

        screengc = g;
        g = buffer.getGraphics();

        /*d = getSize();
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 5; j++)
            {
                Area a = areas[i][j];
                g.setColor(Color.BLUE);
                g.drawRect(a.getX(),a.getY() + getInsets().top,128,128);
            }
        }

         */
        Food [] foodList = FoodListManager.getCopyOfList();
        for(Food f: foodList)
        {
            g.setColor(new Color(12, 125, 12));
            int x = f.getPosX() * (f.getArea().getWidth()/8) + f.getArea().getX();
            int y = f.getPosY() * (f.getArea().getHeight()/8) + f.getArea().getY();
            g.fillOval(x, y + getInsets().top, 16,16);
        }

        screengc.drawImage(buffer, 0, 0, null);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }
}
