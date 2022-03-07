import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EFrame extends Frame implements Runnable
{
    private int sp;
    private int leftSpace, topSpace;
    boolean suspendingFlag;
    //private Dimension d;
    private Thread thrd;
    private Area[][] areas;
    private AreaManager aManager;

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
        sp = (getHeight()/3) < (getWidth()/5) ? (getHeight()/3) : (getWidth()/5);
        aManager = new AreaManager(5,3);
        areas = aManager.getAreas();
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
            System.out.println("Поток EFrame прерван");
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

        Graphics screengc;

        screengc = g;
        g = buffer.getGraphics();

        //d = getSize();
        g.setColor(Color.BLUE);
        g.drawRect(+ leftSpace,getInsets().top + topSpace, sp * 5, sp * 3);
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 5; j++)
            {
                Area area = areas[i][j];

                //g.setColor(Color.BLUE);
                //g.drawRect(area.getX() * sp + leftSpace,area.getY() * sp + getInsets().top + topSpace, sp, sp);
                Food[] foodList = area.getFoodList();
                if(foodList.length != 0)
                {
                    for(Food f: foodList)
                    {
                        g.setColor(new Color(154, 205, 50));
                        int x = f.getPosX() * sp / 8 + f.getArea().getX() * sp;
                        int y = f.getPosY() * sp / 8 + f.getArea().getY() * sp;
                        g.fillOval(x + leftSpace, y + getInsets().top + topSpace, sp/8,sp/8);
                    }
                }

                Animal[] animalList = area.getAnimalList();
                if(animalList.length != 0)
                {
                    for(Animal a: animalList)
                    {
                        g.setColor(Color.RED);
                        int x = (int)((a.getPosX() + a.getFloatX()) * sp / 8) + a.getArea().getX() * sp;
                        int y = (int)((a.getPosY() + a.getFloatY()) * sp / 8) + a.getArea().getY() * sp;
                        g.fillOval(x + leftSpace, y + getInsets().top + topSpace, sp/8, sp/8);
                    }
                }
            }
        }


        screengc.drawImage(buffer, 0, 0, null);
    }

    @Override
    public void update(Graphics g) {
        int nsp;
        nsp = ((getHeight()-getInsets().top)/3) < (getWidth()/5) ? ((getHeight()-getInsets().top)/3) : (getWidth()/5);
        leftSpace = (getWidth()  - nsp * 5) / 2;
        topSpace = (getHeight() - getInsets().top - nsp * 3) / 2;
        buffer = createImage(getWidth(),getHeight());
        sp = nsp;
        paint(g);
    }
}
