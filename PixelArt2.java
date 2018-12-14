import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;

public class PixelArt2{
    static JMenuBar menuBar;
    static JFrame frame;
    static JComboBox<Color> dropDown;
    static Color[][] colors;
    static Color currentColor = Color.black;
    static Vector<Color> comboBoxItems = new Vector<Color>();
    static int width, height, pixelsX, pixelsY;
    static int pixelWidth, pixelHeight;
    static JPanel panel;
    public PixelArt2(){
        createMenuBar();
        width = 800;
        height = 800;
        pixelsX = 800;
        pixelsY = 800;
        pixelWidth = width / pixelsX;
        pixelHeight = height / pixelsY;
        colors = new Color[pixelsY][pixelsX];
        frame = new JFrame();
        panel = new JPanel();
        panel.setSize(width, height);
        frame.setJMenuBar(menuBar);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.addMouseListener(new MouseListener(){
            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int x = (int) (e.getX() / pixelWidth) * pixelWidth;
                int y = (int) (e.getY() / pixelHeight) * pixelHeight;
                if (SwingUtilities.isLeftMouseButton(e)) {
                    paintPixel(x, y, currentColor);
                    colors[(int) (e.getY() / pixelHeight)][(int) (e.getX() / pixelWidth)] = currentColor;
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    paintPixel(x, y, Color.white);
                    colors[(int) (e.getY() / pixelHeight)][(int) (e.getX() / pixelWidth)] = Color.white;
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
        panel.addMouseMotionListener(new MouseMotionListener(){

            @Override
            public void mouseMoved(MouseEvent e) {

            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int x = (int) (e.getX() / pixelWidth) * pixelWidth;
                int y = (int) (e.getY() / pixelHeight) * pixelHeight;
                if (SwingUtilities.isLeftMouseButton(e)) {
                    paintPixel(x, y, currentColor);
                    colors[(int) (e.getY() / pixelHeight)][(int) (e.getX() / pixelWidth)] = currentColor;
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    paintPixel(x, y, Color.white);
                    colors[(int) (e.getY() / pixelHeight)][(int) (e.getX() / pixelWidth)] = Color.white;
                }
            }
        });

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2 + 50);

        frame.add(panel);
        frame.setSize(width, height + 50);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void main(String[] args){
        new PixelArt2();
    }

    public static void paintPixel(int x, int y, Color c){
        Graphics g = panel.getGraphics();
        g.setColor(c);
        g.fillRect(x, y, pixelWidth, pixelHeight);
    }

    public static void createMenuBar() {
        menuBar = new JMenuBar();
        comboBoxItems.add(currentColor);
        final DefaultComboBoxModel<Color> model = new DefaultComboBoxModel<>(comboBoxItems);
        JTextField textField = new JTextField("#");
        textField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    model.addElement(Color.decode(textField.getText()));
                    textField.setText("#");
                    dropDown.setSelectedIndex(dropDown.getItemCount() - 1);
                    currentColor = dropDown.getItemAt(dropDown.getItemCount() - 1);
                }
            }
        });

        JMenuItem print = new JMenuItem("Print");
        print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                print();
            }
        });

        dropDown = new JComboBox<>(model);
        dropDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<Color> cb = (JComboBox<Color>) e.getSource();
                currentColor = (Color) cb.getSelectedItem();
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
        JMenuItem url = new JMenuItem("URL");
        url.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane optionPane = new JOptionPane();
                String url = optionPane.showInputDialog("Enter image address:");
                try {
                    importColors(new URL(url));
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
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
        importColors.add(url);
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
    }

    public static void save() {
        BufferedImage image = new BufferedImage(pixelsX, pixelsY, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < pixelsX; x++) {
            for (int y = 0; y < pixelsY; y++) {
                image.setRGB(x, y, colors[y][x].getRGB());
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
        for (int i = 0; i < pixelsX * pixelsY; i++) {
            Color c = colors[i / pixelsY][i % pixelsX];
            int r = c.getRed();
            int b = c.getBlue();
            int g = c.getGreen();
            String rs = ("0" + Integer.toHexString(r));
            String bs = ("0" + Integer.toHexString(b));
            String gs = ("0" + Integer.toHexString(g));
            System.out.print(rs.substring(rs.length() - 2) + gs.substring(gs.length() - 2)
                    + bs.substring(bs.length() - 2) + " ");
        }
        System.out.println();
    }

    public static void importColors(URL url) throws IOException {
        File file;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
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
                file = j.getSelectedFile();
                image = ImageIO.read(file);
            } else{
                return;
            }
        } else {
            image = ImageIO.read(url);
        }

        double multiplierX = (double) image.getWidth() / pixelsX;
        double multiplierY = (double)image.getHeight() / pixelsY;
        int pixelWidth = width / pixelsX;
        int pixelHeight = height / pixelsY;
        for (int y = 0; y < pixelsY; y++) {
            for (int x = 0; x < pixelsX; x++) {
                Color c = new Color(image.getRGB((int)(x * multiplierX), (int)(y * multiplierY)));
                colors[y][x] = c;
                paintPixel(pixelWidth * x, pixelHeight * y, c);
            }
        }
    }

    public static void clear() {
        int pixelWidth = width / pixelsX;
        int pixelHeight = height / pixelsY;
        for (int x = 0; x < pixelsX; x++){
            for (int y = 0; y < pixelsY; y++){
                paintPixel(x * pixelWidth, y * pixelHeight, Color.white);
            }
        }
    }
}