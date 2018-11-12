import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;

public class PixelArt extends JFrame {
    private static final long serialVersionUID = 1L;
    static int height = 50, width = 50, size = 1000 / height, screenHeight = height * size, screenWidth = width * size;
    static JButton[][] buttons;
    static boolean pressed = false;
    static JMenuBar menuBar;
    static JFrame frame;
    static JComboBox<Color> dropDown;
    static Color[] colors;
    static Color currentColor = new Color(28, 25, 22);

    public PixelArt() {
        createMenuBar();

        frame = new JFrame();
        frame.setJMenuBar(menuBar);
        frame.setSize(screenWidth, screenHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buttons = new JButton[height][width];

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

        frame.setLayout(new GridLayout(height, width));
        for (int i = 0; i < height * width; i++) {
            JButton button = new JButton();
            button.setOpaque(true);
            button.setBackground(Color.white);
            button.addMouseListener(new MouseListener() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    pressed = false;
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    pressed = true;
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        button.setBackground(currentColor);
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        button.setBackground(Color.white);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (pressed) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            button.setBackground(currentColor);
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            button.setBackground(Color.white);
                        }
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                }
            });
            button.setBorderPainted(false);
            buttons[i / width][i % width] = button;
            frame.add(button, i);
        }
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public static void main(String[] args) {
        new PixelArt();
    }

    public static void save() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                JButton button = buttons[i][j];
                graphics.setColor(button.getBackground());
                graphics.drawRect(j, i, 1, 1);
            }
        }

        File file = new File("myImg.png");
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void print() {
        for (int i = 0; i < height * width; i++) {
            JButton button = buttons[i / width][i % width];
            int r = button.getBackground().getRed();
            int b = button.getBackground().getBlue();
            int g = button.getBackground().getGreen();
            String rs = ("0" + Integer.toHexString(r));
            String bs = ("0" + Integer.toHexString(b));
            String gs = ("0" + Integer.toHexString(g));
            System.out.print(rs.substring(rs.length() - 2) + gs.substring(gs.length() - 2)
                    + bs.substring(bs.length() - 2) + " ");
        }
        System.out.println();
    }

    public static void importColors(URL url) throws IOException {
        if (url == null) {
            JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            j.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public String getDescription() {
                    return "Image Files";
                }

                @Override
                public boolean accept(File f) {
                    return f.getName().endsWith(".png") || f.getName().endsWith(".jpg");
                }
            });
            int r = j.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                File file = j.getSelectedFile();
                BufferedImage image = ImageIO.read(file);

                if (width / height != image.getWidth() / image.getHeight()) {
                    System.out.println("Incompatible ratios!");
                    return;
                }
                int multiplier = image.getWidth() > width ? image.getWidth() / width : 1;
                for (int y = 0; y < buttons.length; y++) {
                    for (int x = 0; x < buttons[y].length; x++) {
                        buttons[y][x].setBackground(new Color(image.getRGB(x * multiplier, y * multiplier)));
                    }
                }
            }
        }
    }

    public static void clear() {
        for (int i = 0; i < buttons.length; i++) {
            for (JButton button : buttons[i]) {
                button.setBackground(Color.white);
            }
        }
    }

    public static void createMenuBar() {
        menuBar = new JMenuBar();
        JTextField textField = new JTextField("#");

        JMenuItem print = new JMenuItem("Print");
        print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                print();
            }
        });
        Vector<Color> comboBoxItems = new Vector<Color>();
        comboBoxItems.add(currentColor);
        final DefaultComboBoxModel<Color> model = new DefaultComboBoxModel<>(comboBoxItems);

        dropDown = new JComboBox<>(model);
        dropDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<Color> cb = (JComboBox<Color>) e.getSource();
                currentColor = (Color) cb.getSelectedItem();
            }
        });

        JMenuItem add = new JMenuItem("Add");

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    model.addElement(Color.decode(textField.getText()));
                    textField.setText("#");
                    dropDown.setSelectedIndex(dropDown.getItemCount() - 1);
                    currentColor = dropDown.getItemAt(dropDown.getItemCount() - 1);
                } catch (Exception ex) {
                }
            }
        });

        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });

        JMenu importColors = new JMenu("Import");
        JMenuItem fileItem = new JMenuItem("File");
        //JTextField field = new JTextField();
        fileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    importColors(null);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        importColors.add(fileItem);
        //importColors.add(field);
        JMenuItem clear = new JMenuItem("Clear");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        JMenu menu = new JMenu("Actions");
        menu.add(print);
        menu.add(save);
        menu.add(importColors);
        menu.add(clear);

        menuBar.add(menu);
        menuBar.add(dropDown);
        menuBar.add(textField);
        menuBar.add(add);
    }
}