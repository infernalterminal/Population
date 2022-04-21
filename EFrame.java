import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EFrame extends Frame implements KeyListener//, Runnable
{
    private boolean stopFlag;
    private int sp;
    private int leftSpace, topSpace;
    private EThread thrd;
    private Area[][] areas;
    private AreaManager aManager;

    //ETimer timer;

    Image buffer = null;

    public EFrame()
    {
        stopFlag = false;
        addKeyListener(this);

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {

                if(!thrd.isSuspended())
                    thrd.mySuspend();

                //System.out.println(0);
                System.exit(0);
            }
        });
        //thrd = new Thread(this,"EFrame");
        //sp = (getHeight()/3) < (getWidth()/5) ? (getHeight()/3) : (getWidth()/5);
        sp = Math.min((getHeight()/3) , (getWidth()/5));
        aManager = new AreaManager(5,3);
        areas = aManager.getAreas();
        thrd = new EThread(this);
        //timer = new ETimer(10000);
        repaint();
        thrd.start();
    }

    @Override
    public void paint(Graphics g)
    {
        int animalCount = 0;
        int bornAnimals = 0;
        int deadAnimals = 0;
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
                animalCount += area.getAnimalList().length;
                bornAnimals += area.getBornAnimals();
                deadAnimals += area.getDeadAnimals();
                //g.setColor(Color.BLUE);
                //g.drawRect(area.getX() * sp + leftSpace,area.getY() * sp + getInsets().top + topSpace, sp, sp);
                Food[] foodList = area.getFoodList();
                if(foodList.length != 0)
                {
                    for(Food f: foodList)
                    {
                        g.setColor(new Color(99, 181, 33));
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
                        g.setColor(a.getColor());
                        int x = (int)((a.getPosX() + a.getFloatX()) * sp / 8) + a.getArea().getX() * sp;
                        int y = (int)((a.getPosY() + a.getFloatY()) * sp / 8) + a.getArea().getY() * sp;
                        g.fillOval(x + leftSpace, y + getInsets().top + topSpace, sp/8, sp/8);
                    }
                }
            }
        }

        g.setColor(Color.white);

        g.drawString("Завершено раундов: " + thrd.getCircles(), 5, getInsets().top + 30);
        g.drawString("Осталось времени: " + (int) thrd.getTime() / 1000, 5, getInsets().top + 60);
        g.drawString("Всего животных: " + animalCount, 5 , getInsets().top + 90);
        g.drawString("Родилось: " + bornAnimals, 5 , getInsets().top + 120);
        g.drawString("Погибло: " + deadAnimals, 5 , getInsets().top + 150);
        screengc.drawImage(buffer, 0, 0, null);

        if(animalCount == 0 & !thrd.isSuspended())
        {
            thrd.mySuspend();
        }
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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {

    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            thrd.stopOrContinue();
        }
    }

    public void updateField()
    {
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 5; j++)
            {
                Area area = areas[i][j];

                Food[] foodList = area.getFoodList();
                if(foodList.length != 0)
                {
                    for(Food f: foodList)
                    {
                        f.update();
                    }
                }

                Animal[] animalList = area.getAnimalList();
                if(animalList.length != 0)
                {
                    for(Animal a: animalList)
                    {
                        a.update();
                    }
                }
                area.update();
            }
        }
    }
}
