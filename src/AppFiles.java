import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AppFiles extends JFrame {
    int i;

    JMenuBar titleBar;
    JLabel title;

    JPanel topBar;

    JButton exit;

    JScrollPane scrollPane;

    Container cont = new Container();

    ImageIcon appIcon;

    ArrayList<JPanel> pnlArray = new ArrayList<>();
    ArrayList<JButton> openArray = new ArrayList<>();
    ArrayList<JButton> deleteArray = new ArrayList<>();

    AppFiles() throws IOException
    {
        appIcon = new ImageIcon("sentenceIcon.png");
        this.setIconImage(appIcon.getImage());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Sentence");
        this.setSize(750, 900);
        this.setLayout(null);
        this.setLocationRelativeTo(null);

        topBar = new JPanel();
        topBar.setLayout(null);
        topBar.setBounds(0, 0, this.getWidth(), 80);
        topBar.setBackground(Color.getHSBColor(0.1f, 0.89f, 0.95f));

        titleBar = new JMenuBar();
        //titleBar.setLayout(new GridBagLayout());
        titleBar.setLayout(null);
        titleBar.setBackground(Color.getHSBColor(0.1f, 0.89f, 0.95f));

        exit = new JButton("< Back");
        exit.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        exit.setBounds(5, 23, 110, 30);
        title = new JLabel("Files");
        title.setFont(new Font("Bahnschrift", Font.PLAIN, 65));
        title.setBounds(300, 10, 200, 80);

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AppMenu();
                dispose();
            }
        });

        cont.setLayout(null);


        scrollPane = new JScrollPane(cont);

        scrollPane.setBounds(25, 90, this.getWidth() - 50, 770);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); //as needed can be used

        //File file = new File("./data/fileList.txt");
        //String line32 = Files.readAllLines(Paths.get("./data/fileList.txt")).get(32);

        BufferedReader br = new BufferedReader(new FileReader("./data/fileList.txt"));
        String str;
        ArrayList<String> list = new ArrayList<String>();

        //Gathers each fileName from txt file
        while ((str = br.readLine()) != null) {
            list.add(str);
        }
        String[] stringArr = list.toArray(new String[0]);

        //Displays each file in a dedicated panel
        for (i = 0; i < stringArr.length; i++) {
            JLabel lbl = new JLabel(stringArr[i]);
            lbl.setFont(new Font("Century Gothic", Font.PLAIN, 30));
            JButton opnBtn = new JButton("Open");
            opnBtn.setFont(new Font("Century Gothic", Font.PLAIN, 30));
            JButton delBtn = new JButton("Delete");
            delBtn.setFont(new Font("Century Gothic", Font.PLAIN, 30));
            JPanel pnl = new JPanel();

            pnl.setLayout(new GridLayout(1, 3, 10, 10));
            pnl.add(lbl);
            openArray.add(opnBtn);
            deleteArray.add(delBtn);

            //What? Always file 3 as i is set at 3 when the loop has ended
            //Need to get file associated with the button
            //Attach an int to each panel, this int will be used for string array
            //int f = i;

            int f = i;

            openArray.get(i).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Main.openFile = 1;
                    Main.fileName = stringArr[f];
                    new WordProcessor();
                    dispose();
                }
            });

            deleteArray.get(i).addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    int result = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to delete " + stringArr[f],
                            "Delete file",
                            JOptionPane.YES_NO_OPTION);

                    if (result == 0)
                    {
                        File file = new File("./data/files/" + stringArr[f]);

                        // Delete the File
                        if (file.delete()) {
                            System.out.println("File deleted successfully");
                        }
                        else {
                            System.out.println("Failed to delete the file");
                        }

                        File inputFile = new File("./data/fileList.txt");
                        File tempFile = new File("./data/fileListTemp.txt");
                        //Main.openFile = 1;
                        //Main.fileName = stringArr[f];

                        //Delete mention of file in fileList
                        try
                        {
                            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
                            String currentLine;
                            int currentLineNumber = 0;
                            while ((currentLine = reader.readLine()) != null)
                            {
                                if (currentLineNumber != f)
                                {
                                    writer.write(currentLine);
                                    writer.newLine();
                                    System.out.println(currentLine);
                                }
                                currentLineNumber++;
                            }
                            writer.flush();
                            reader.close();
                            writer.close();


                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }


                        File newInputFile = new File("./data/fileListTemp.txt");
                        File outputFile = new File("./data/fileList.txt");
                        //Main.openFile = 1;
                        //Main.fileName = stringArr[f];


                        try
                        {
                            BufferedReader reader = new BufferedReader(new FileReader(newInputFile));
                            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
                            String currentLine;
                            while ((currentLine = reader.readLine()) != null)
                            {
                                writer.write(currentLine);
                                writer.newLine();
                                System.out.println(currentLine);
                            }
                            writer.flush();
                            reader.close();
                            writer.close();


                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                        try {
                            new AppFiles();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        dispose();
                    }


                }
            });


            pnl.add(openArray.get(i));
            pnl.add(deleteArray.get(i));

            pnl.setBackground(Color.LIGHT_GRAY);
            //pnl.setPreferredSize(new Dimension(90, 50));
            int y = i * 120;
            pnl.setBounds(0, y, 680, 100);
            pnlArray.add(pnl);
            cont.add(pnlArray.get(i));
        }

        cont.setPreferredSize(new Dimension(500, i * 120));

        topBar.add(exit);
        topBar.add(title);

        //titleBar.add(exit);
        //titleBar.add(title);
        //this.setJMenuBar(titleBar);
        this.add(topBar);
        this.add(scrollPane);
        this.setVisible(true);
    }
}





/*
                              //Search for stringArr[f] in text fileList.txt and delete
                          BufferedReader brDel = new BufferedReader(new FileReader(inputFile));
                        String str;

                        //Gathers each fileName from txt file
                        while((str = brDel.readLine()) != null)
                        {
                            if (str == stringArr[f])
                            {

                            }
                        }
                        //Then delete actual file
                        //Open a new window to refresh changes
                        new AppFiles();
                        dispose();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }*/