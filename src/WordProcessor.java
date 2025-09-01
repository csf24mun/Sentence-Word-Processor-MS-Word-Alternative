import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.util.Scanner;
import java.util.Vector;

public class WordProcessor extends JFrame implements ActionListener
{
    //declare variables

    JTextPane textPane;
    JScrollPane scrollPane;

    JLabel fontLabel;
    JButton fontColourButton;
    JComboBox<String> fontBox;
    JComboBox<String> sizeBox;

    String [] FONT_SIZES  = {"Size", "12", "14", "16", "18", "20", "22", "24", "26", "28", "30"};

    JMenuBar menuBar;

    JMenu fileMenu;
    JMenuItem openItem;
    JMenuItem saveItem;
    JMenuItem saveAsItem;
    JMenuItem exitItem;

    JMenu imageMenu;
    JMenuItem importImage;

    JMenuItem printItem;

    ImageIcon appIcon;

    String fileName;

    StyleContext sc = new StyleContext();
    DefaultStyledDocument dse = new DefaultStyledDocument(sc);


    //Get available font families for word processor
    private Vector<String> getEditorFonts()
    {

        String [] availableFonts =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Vector<String> returnList = new Vector<>();

        for (String font : availableFonts)
        {

            returnList.add(font);

        }

        return returnList;
    }



    WordProcessor()
    {
        fileName = Main.fileName; //Get file name from the file menu if opened

        //Set layout of JFrame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Sentence: " + fileName);
        this.setSize(750, 900);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);

        //Set the app icon
        appIcon = new ImageIcon("sentenceIcon.png");
        this.setIconImage(appIcon.getImage());

        //Set layout of JTextPane
        textPane = new JTextPane();
        textPane.setFont(new Font("Arial", Font.PLAIN, 20));
        textPane.setMargin( new Insets(10,20,10,20));
        textPane.setDocument(dse);

        //Set layout of JScrollPane
        scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(this.getWidth() - 50, this.getHeight() - 100)); //700x700 initially
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED); //as needed can be used

        //Font label
        fontLabel = new JLabel("Font: ");
        fontLabel.setFont(new Font("Century Gothic", Font.PLAIN, 20));

        //Set layout of font colour JButton
        fontColourButton = new JButton("Colour");
        fontColourButton.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        fontColourButton.setBackground(Color.getHSBColor(0.12f, 0.3f, 0.9f));
        fontColourButton.addActionListener(this);

        //Set layout of font family selector box using getEditorFonts method
        Vector<String> editorFonts = getEditorFonts();
        editorFonts.add(0, "Family");
        fontBox = new JComboBox<String>(editorFonts);
        fontBox.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        fontBox.setBackground(Color.getHSBColor(0.12f, 0.3f, 0.9f));
        //Reference dedicated item listener
        fontBox.addItemListener(new FontFamilyItemListener());

        //Set layout of font size selector box using the FONT_SIZES method
        sizeBox = new JComboBox<String>(FONT_SIZES);
        sizeBox.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        sizeBox.setBackground(Color.getHSBColor(0.12f, 0.3f, 0.9f));
        sizeBox.setEditable(false);
        //Reference dedicated item listener
        sizeBox.addItemListener(new FontSizeItemListener());



        // menuBar

        //Instantiate menuBar
        menuBar = new JMenuBar();
        //Instantiate fileMenu and contents
        fileMenu = new JMenu("File");
        fileMenu.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        openItem = new JMenuItem("Open");
        openItem.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        saveItem = new JMenuItem("Save");
        saveItem.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        saveAsItem = new JMenuItem("Save as");
        saveAsItem.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        printItem = new JMenuItem("Print");
        printItem.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        exitItem = new JMenuItem("Exit");
        exitItem.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        //Instantiate imageMenu and contents
        imageMenu = new JMenu("Image");
        imageMenu.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        importImage = new JMenuItem("Import image");
        importImage.setFont(new Font("Century Gothic", Font.PLAIN, 20));

        //Add action listeners to each
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        saveAsItem.addActionListener(this);
        printItem.addActionListener(this);
        exitItem.addActionListener(this);
        importImage.addActionListener(this);

        //Add menuItems to fileMenu and imageMenu
        fileMenu.add(openItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(printItem);
        fileMenu.add(exitItem);
        imageMenu.add(importImage);

        //Add menus to menuBar
        menuBar.add(fileMenu);
        menuBar.add(imageMenu);

        //Set layout of menuBar
        menuBar.setBackground(Color.getHSBColor(0.1f, 0.89f, 0.95f));

        //menuBar END/



        //Add components to the frame
        this.setJMenuBar(menuBar);
        this.add(fontLabel);
        this.add(sizeBox);
        this.add(fontColourButton);
        this.add(fontBox);
        this.add(scrollPane);
        this.setVisible(true);


        //If file has been opened from AppFiles
        if (Main.openFile == 1)
        {
            //Create path to find file
            File file;
            file = new File("./data/files/" + Main.fileName);
            Scanner fileIn = null;

            try
            {
                fileIn = new Scanner(file);
                //If an openable file
                if (file.isFile())
                {
                    //Declare and instantiate file and object input streams
                    FileInputStream fi;
                    fi = new FileInputStream(file);
                    ObjectInputStream oi;
                    oi = new ObjectInputStream(fi);
                    //Read the object
                    Object obj = oi.readObject();
                    //Reference file object as a DefaultStyledDocument and input to the textPane
                    dse = ((DefaultStyledDocument)obj);
                    textPane.setDocument(dse);
                }
            }
            catch (IOException ex)
            {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } finally
            {
                fileIn.close();
            }

            Main.openFile = 0;
        }
        else //if file has just been created
        {
            File file;
            file = new File("./data/files/" + fileName);

            try
            {
                //Declare and instantiate file and object input streams
                FileOutputStream fo;
                fo = new FileOutputStream(file);
                ObjectOutputStream oo;
                oo = new ObjectOutputStream(fo);
                //Write the object in DefaultStyledDocument
                oo.writeObject(dse);
                oo.close();

            } catch (IOException ioex)
            {
                throw new RuntimeException(ioex);
            }
        }

    }



    //Will allow user to change font family of selected text
    private class FontFamilyItemListener implements ItemListener
    {

        public void itemStateChanged(ItemEvent e)
        {

            if ((e.getStateChange() != ItemEvent.SELECTED) ||
                    (fontBox.getSelectedIndex() == 0))
            {

                return;
            }

            //Get selected option chosen from the fontBox
            String fontFamily = (String) e.getItem();
            //Set selected text to the selected font
            fontBox.setAction(new StyledEditorKit.FontFamilyAction(fontFamily, fontFamily));
            fontBox.setSelectedIndex(0); // initialize to (default) select
            textPane.requestFocusInWindow();
        }
    }



    //Will allow user to change font size of selected text
    private class FontSizeItemListener implements ItemListener
    {

        public void itemStateChanged(ItemEvent e)
        {

            if ((e.getStateChange() != ItemEvent.SELECTED) ||
                    (sizeBox.getSelectedIndex() == 0))
            {

                return;
            }

            String fontSizeStr = (String) e.getItem();
            int newFontSize = 0;

            try
            {
                newFontSize = Integer.parseInt(fontSizeStr);
            }
            catch (NumberFormatException ex)
            {

                return;
            }

            sizeBox.setAction(new StyledEditorKit.FontSizeAction(fontSizeStr, newFontSize));
            sizeBox.setSelectedIndex(0); // initialize to (default) select
            textPane.requestFocusInWindow();
        }
    }



    public void actionPerformed(ActionEvent e)
    {
        //Will change the foreground colour of selected text
        if (e.getSource() == fontColourButton)
        {
            //Display wind of colour options
            Color newColor = JColorChooser.showDialog(this, "Choose a color",
                    Color.BLACK);
            if (newColor == null)
            {

                textPane.requestFocusInWindow();
                return;
            }

            SimpleAttributeSet attr = new SimpleAttributeSet();
            //Set foreground colour to selected colour
            StyleConstants.setForeground(attr, newColor);
            textPane.setCharacterAttributes(attr, false);
            textPane.requestFocusInWindow();
        }



        //Will open a selected object within the local file
        if (e.getSource() == openItem)
        {
            try
            {
                //Declare and instantiate a JFileChooser to navigate to a file
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("."));

                int response = fileChooser.showOpenDialog(null);

                //If file is selected
                if (response == JFileChooser.APPROVE_OPTION)
                {
                    //Get path of file
                    File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    Scanner fileIn = null;

                    try
                    {
                        fileIn = new Scanner(file);
                        //If an openable file
                        if (file.isFile())
                        {
                            //Declare and instantiate file and object input streams
                            FileInputStream fi;
                            fi = new FileInputStream(file);
                            ObjectInputStream oi;
                            oi = new ObjectInputStream(fi);
                            //Read the object
                            Object obj = oi.readObject();
                            //Reference file object as a DefaultStyledDocument and input to the textPane
                            dse = ((DefaultStyledDocument)obj);
                            textPane.setDocument(dse);
                        }
                    }
                    catch (FileNotFoundException ex)
                    {
                        throw new RuntimeException(ex);
                    }
                    finally
                    {
                        fileIn.close();
                    }
                }


            } catch (IOException | ClassNotFoundException ioex)
            {
                throw new RuntimeException(ioex);
            }
        }



        if (e.getSource() == saveAsItem)
        {
            //Get path of file
            File file;
            file = new File("./data/files/" + fileName);

            try
            {
                //Declare and instantiate file and object input streams
                FileOutputStream fo;
                fo = new FileOutputStream(file);
                ObjectOutputStream oo;
                oo = new ObjectOutputStream(fo);
                //Write the object in DefaultStyledDocument
                oo.writeObject(dse);
                oo.close();

            } catch (IOException ioex)
            {
                throw new RuntimeException(ioex);
            }
        }


        //To print a page by translating textPane to an image
        if (e.getSource() == printItem)
        {
            Dimension d = textPane.getSize();
            //Create new image
            BufferedImage printImg = new BufferedImage(
                    1240, 1754, BufferedImage.TYPE_INT_RGB); //d.width, d.height
            Graphics2D g2d = printImg.createGraphics();
            //Use textPane as image
            textPane.print(g2d);
            g2d.dispose();
            try
            {
                //Initiate print method
                ImageIO.write(printImg, "jpg", new File("./prints/printImg.jpg"));
                printDoc(printImg);

            } catch (IOException ex) {

                throw new RuntimeException(ex);
            }

        }



        //Allows user to select and import image files to display an image in textPane
        if (e.getSource() == importImage)
        {
            //Declare and instantiate a JFileChooser to navigate to a file
            JFileChooser fileChooser = new JFileChooser();
            File workingDir = new File(".");
            fileChooser.setCurrentDirectory(workingDir);
            //Create and apply a FileNameExtensionFilter to allow only selected image files to be imported
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "png", "jpg", "bmp");
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showOpenDialog(getParent());

            //If file is selected
            if (result == JFileChooser.APPROVE_OPTION)
            {
                try
                {
                    File file = fileChooser.getSelectedFile();
                    //Create BufferedImage from the read image file
                    BufferedImage img = ImageIO.read(file);
                    //Get BufferedImage width and height
                    double imgWidth = img.getWidth();
                    double imgHeight = img.getHeight();

                    double constant = 0.8;

                    //Created to ensure the image does not extend width boundary of scrollPane
                    while (imgWidth > 700)
                    {
                        imgWidth = imgWidth * constant;
                        imgHeight = imgHeight * constant;
                    }

                    //Create and scale image to be ready for the image icon
                    Image dimg = img.getScaledInstance((int) imgWidth, (int) imgHeight,
                            Image.SCALE_SMOOTH);
                    //Declare and instantiate labelIcon
                    ImageIcon labelIcon = new ImageIcon(dimg);
                    //Insert labelIcon into textPane
                    textPane.insertIcon(labelIcon);
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                    JOptionPane.showMessageDialog(null, "ERROR");
                }
            }
        }



        //Close the program
        if (e.getSource() == exitItem)
        {
            new AppMenu();
            dispose();
            //System.exit(0);
        }
    }

    //Will display a print window using screenshot of textPane
    public void printDoc(BufferedImage img)
    {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(new Printable() {
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex != 0) {
                    return NO_SUCH_PAGE;
                }
                graphics.drawImage(img, 0, 0, 1240 ,1754, null); //img.getWidth(), img.getHeight()
                return PAGE_EXISTS;
            }
        });
        try {
            if (printJob.printDialog())
            {
                printJob.print();
            }

        } catch (PrinterException e1) {
            e1.printStackTrace();
        }
    }

}

