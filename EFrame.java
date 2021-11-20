import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EFrame extends Frame
{
    private Dimension d;
    private Area[][] areas = new Area[3][5];

    public EFrame()
    {
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.out.println(0);
                System.exit(0);
            }
        });
    }

    public void start()
    {
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
    }

    @Override
    public void paint(Graphics g)
    {
        Insets insets = getInsets();
        /*d = getSize();
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 5; j++)
            {
                Area a = areas[i][j];
                g.setColor(Color.BLUE);
                g.drawRect(a.getX(),a.getY() + insets.top,128,128);
            }
        }

         */
        Food [] foodList = FoodListManager.getCopyOfList();
        for(Food f: foodList)
        {
            g.setColor(new Color(139,255,139));
            int x = f.getPosX() * (f.getArea().getWidth()/8) + f.getArea().getX();
            int y = f.getPosY() * (f.getArea().getHeight()/8) + f.getArea().getY();
            g.fillOval(x, y + insets.top, 16,16);
        }
    }
}
