import javax.swing.JFrame;

public class Frame
{

    public static void main(String[] args)
    {

        System.setProperty("sun.java2d.opengl", "true");

        JFrame f = new JFrame();
        Panel p = new Panel();

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.setSize(1920, 1080);
        f.setResizable(true);
        f.add(p);

    }

}