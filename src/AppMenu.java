import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AppMenu extends JFrame implements ActionListener
{
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
        appIcon = new ImageIcon("sentenceIcon.png");
        this.setIconImage(appIcon.getImage());

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
        if (e.getSource() == nFile)
        {
            Main.fileName = (String)JOptionPane.showInputDialog(
                    this,
                    "What is the name of your new file:",
                    "New File",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null, null
            );

//If you're here, the return value was null/empty.
            //nFile.setLabel("Come on, finish the sentence!");

            /*JOptionPane.showConfirmDialog(null, "What is the name of your new file?",
                    "New file", JOptionPane.OK_CANCEL_OPTION);
            System.out.println(ans);

             */

            if (Main.fileName != null && Main.fileName.length() > 0)
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
            else if (Main.fileName == null || !(Main.fileName.length() > 0))
            {
                JOptionPane.showMessageDialog(null, "Not a valid name, try again",
                        "Invalid Name", JOptionPane.ERROR_MESSAGE, null);
            }
        }

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
