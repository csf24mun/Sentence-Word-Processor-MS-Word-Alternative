//By Noah Mullen
//Ulster University

import java.awt.*;

public class Main
{
    public static int openFile = 0;
    public static String fileName;
    public static int monW;
    public static int monH;

    public static void main(String[] args)
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        monW = screenSize.width;
        monH = screenSize.height;
        //new WordProcessor();
        new AppMenu();
    }
}