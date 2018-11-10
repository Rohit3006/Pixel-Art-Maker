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
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class PixelArt extends JFrame {
    private static final long serialVersionUID = 1L;
    static int size = 20;
    static int height = 50, width = 50, screenHeight = height * size, screenWidth = width * size;
    static JButton[][] buttons;
    static boolean pressed = false;
    static JMenuBar menuBar;
    static JFrame frame;
    static JComboBox<Color> dropDown;
    static Color[] colors;
    static Color currentColor = new Color(28, 25, 22);

    public PixelArt() {
        menuBar = new JMenuBar();
        frame = new JFrame();
        frame.setSize(screenWidth, screenHeight + menuBar.getHeight());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(height, width, 0, 0));
        panel.setSize(screenWidth, screenHeight);

        buttons = new JButton[height][width];

        JTextField textField = new JTextField("#");

        JButton print = new JButton("Print");
        print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                print();
            }
        });
        Vector<Color> comboBoxItems = new Vector<Color>();
        comboBoxItems.add(currentColor);
        final DefaultComboBoxModel<Color> model = new DefaultComboBoxModel<>(comboBoxItems);

        JButton add = new JButton("Add");

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                model.addElement(Color.decode(textField.getText()));
                textField.setText("#");
            }
        });

        JButton save = new JButton("Save");
        save.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                save();
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
        menuBar.add(print);
        menuBar.add(dropDown);
        menuBar.add(textField);
        menuBar.add(add);
        menuBar.add(save);

        frame.setJMenuBar(menuBar);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

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
            panel.add(button, i);
        }
        frame.add(panel);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public static void main(String[] args) {
        new PixelArt();
    }

    public static void save(){
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        for (int i = 0; i < buttons.length; i++){
            for (int j = 0; j < buttons[i].length; j++){
                JButton button = buttons[i][j];
                graphics.setColor(button.getBackground());
                graphics.drawRect(j, i, 1, 1);
            }
        }

        File file = new File("myImg.jpeg");
        try {
            ImageIO.write(image, "jpeg", file);
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
}