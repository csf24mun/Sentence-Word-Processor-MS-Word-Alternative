import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class AppMenu extends JFrame implements ActionListener
{
    //Declare variables

    JMenuBar titleBar;
    JLabel title;
    JButton nFile;
    JButton files;
    JOptionPane createNew;

    //Put gridLayout with buttons in container then center container itself
    Container cont = new Container();

    ImageIcon appIcon;

    AppMenu()
    {
        //Set app icon
        appIcon = new ImageIcon("sentenceIcon.png");
        this.setIconImage(appIcon.getImage());

        //Set layout of JFrame and components

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Sentence");
        this.setSize(750, 900);
        this.setLayout(new GridBagLayout());
        //GridBagConstraints gbc = new GridBagConstraints();
        cont.setLayout(new GridLayout(6, 1, 20, 30));

        this.setLocationRelativeTo(null);

        createNew = new JOptionPane();

        titleBar = new JMenuBar();
        titleBar.setLayout(new GridBagLayout());
        titleBar.setBackground(Color.getHSBColor(0.1f, 0.89f, 0.95f));
        //titleBar.setSize(this.getWidth(), 40);

        title = new JLabel("Sentence");
        nFile = new JButton("Create New");
        files = new JButton("Files");

        nFile.setPreferredSize(new Dimension(300, 80));
        nFile.setFont(new Font("Century Gothic", Font.PLAIN, 30));

        files.setPreferredSize(new Dimension(300, 80));
        files.setFont(new Font("Century Gothic", Font.PLAIN, 30));

        nFile.addActionListener(this);
        files.addActionListener(this);

        title.setFont(new Font("Bahnschrift", Font.PLAIN, 65));
        //title.setHorizontalAlignment(JLabel.CENTER);

        titleBar.add(title);
        this.setJMenuBar(titleBar);
        cont.add(nFile);
        cont.add(files);
        this.add(cont);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        //New file button
        if (e.getSource() == nFile)
        {
            //Will first display a window asking for a file name
            Main.fileName = (String)JOptionPane.showInputDialog(
                    this,
                    "What is the name of your new file:",
                    "New File",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null, null
            );

            int sameName = 0;
            try //Check the file if name has already been used
            {
                File inputFile = new File("./data/fileList.txt");

                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                String currentLine;
                //Read each line in fileList
                while ((currentLine = reader.readLine()) != null)
                {
                    //If entered name is the same of a file name stored in the list
                    if (Main.fileName.equals(currentLine))
                    {
                        sameName = 1;
                    }
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


            //If file name is not blank
            if (Main.fileName != null && Main.fileName.length() > 0 && sameName == 0)
            {
                new WordProcessor();
                dispose();

                File file = new File("./data/fileList.txt");

                //append name to fileList
                try {
                    FileWriter myWriter = new FileWriter(file, true);
                    myWriter.write(Main.fileName + "\n");
                    myWriter.close();
                    System.out.println("Successfully wrote to the file.");
                } catch (IOException ioe) {
                    System.out.println("An error occurred.");
                    ioe.printStackTrace();
                }
            }
            else if (Main.fileName == null || !(Main.fileName.length() > 0)) //if name has no string
            {
                JOptionPane.showMessageDialog(null, "Not a valid name, try again",
                        "Invalid Name", JOptionPane.ERROR_MESSAGE, null);
            }
            else if (sameName == 1) //If name has already been taken
            {
                JOptionPane.showMessageDialog(null, "File name has already been taken, try again",
                        "Name Taken", JOptionPane.ERROR_MESSAGE, null);
            }
        }

        //Will open the files frame displaying all files
        if (e.getSource() == files)
        {
            try {
                new AppFiles();
                dispose();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
