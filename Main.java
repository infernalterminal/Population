import java.awt.*;

public class Main
{
    public static void main(String[] args)
    {
        EFrame frame = new EFrame();
        frame.setSize(new Dimension(640, 421));
        //frame.setSize(new Dimension(1920,1070));
        frame.setTitle("Population");
        frame.setBackground(Color.BLACK);
        frame.setVisible(true);
        frame.buffer = frame.createImage(640,421);
    }
}