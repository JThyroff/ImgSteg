package de.thyroff.imgsteg.ui;

import com.formdev.flatlaf.FlatLightLaf;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUI implements UI {

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        new GUI();
    }

    public JPanel getFileDisplay(String fileName, String path) {
        JPanel jPanel = new JPanel();
        JButton jButton = new JButton(fileName);
        JLabel jLabel = new JLabel(fileName);

        BufferedImage myPicture;
        try {
            myPicture = ImageIO.read(new File(path));
            myPicture = resize(myPicture, 100, 100);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));

        jPanel.add(jButton);
        jPanel.add(new JSeparator(SwingConstants.VERTICAL));
        jPanel.add(jLabel);
        jPanel.add(picLabel);

        jPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return jPanel;
    }

    public void addFileDisplay(JPanel hide_panel_pick, String fileName, String path) {
        JButton jButton = new JButton(fileName);
        JPanel buttonPanel = new JPanel();
        jButton.setAlignmentY(SwingConstants.CENTER);
        buttonPanel.add(jButton);
        JLabel jLabel = new JLabel(fileName);

        BufferedImage myPicture;
        try {
            myPicture = ImageIO.read(new File(path));
            myPicture = resize(myPicture, 100, 100);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        picLabel.setHorizontalAlignment(SwingConstants.CENTER);

        hide_panel_pick.add(buttonPanel);
        hide_panel_pick.add(new JSeparator(SwingConstants.VERTICAL));
        hide_panel_pick.add(jLabel);
        hide_panel_pick.add(picLabel);

        hide_panel_pick.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    public JTabbedPane getOptionSwitchPanel() {
        JTabbedPane jTabbedPane = new JTabbedPane();

        JPanel hide_panel = new JPanel();
        hide_panel.setLayout(new BoxLayout(hide_panel, BoxLayout.Y_AXIS));
        hide_panel.add(new JLabel("HIDE"));

        JPanel hide_panel_pick = new JPanel();
        hide_panel_pick.setLayout(new GridLayout(3, 4));

        /*
        hide_panel_pick.add(getFileDisplay("Image", "/home/jo/Workspace/ImgSteg/test/images/copy/copy_testImg1.jpg"));
        hide_panel_pick.add(getFileDisplay("Key", "/home/jo/Workspace/ImgSteg/test/images/copy/copy_blankKey84.png"));
        hide_panel_pick.add(getFileDisplay("FileToHide", "/home/jo/Workspace/ImgSteg/test/images/copy/copy_blankKey84White.png"));*/
        addFileDisplay(hide_panel_pick, "Image", "/home/jo/Workspace/ImgSteg/test/images/copy/copy_testImg1.jpg");
        addFileDisplay(hide_panel_pick, "Key", "/home/jo/Workspace/ImgSteg/test/images/copy/copy_blankKey84.png");
        addFileDisplay(hide_panel_pick, "FileToHide", "/home/jo/Workspace/ImgSteg/test/images/copy/copy_blankKey84White.png");
        hide_panel.add(hide_panel_pick);

        JPanel reveal_panel = new JPanel();
        reveal_panel.add(new JLabel("REVEAL"));

        jTabbedPane.addTab("Hide", null, hide_panel, "Switch to hide mode");
        jTabbedPane.addTab("Reveal", null, reveal_panel, "Switch to reveal mode");
        jTabbedPane.setBounds(50, 50, 200, 200);
        //jTabbedPane.setTabPlacement(JTabbedPane.TOP);
        return jTabbedPane;
    }

    public GUI() {

        JFrame jFrame = new JFrame();
        jFrame.add(getOptionSwitchPanel());
        //jFrame.getContentPane().add(new Label("test"));

        jFrame.setMinimumSize(new Dimension(300, 300));

        jFrame.setLocation(300, 300);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    @Override
    public void hide() {

    }

    @Override
    public void reveal() {

    }
}
