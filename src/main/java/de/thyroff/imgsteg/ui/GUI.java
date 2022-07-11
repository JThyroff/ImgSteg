package de.thyroff.imgsteg.ui;

import com.formdev.flatlaf.FlatLightLaf;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUI implements UI {

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));

        jPanel.add(jButton);
        jPanel.add(jLabel);
        jPanel.add(picLabel);

        return jPanel;
    }

    public JTabbedPane getOptionSwitchPanel() {
        JTabbedPane jTabbedPane = new JTabbedPane();

        JPanel hide_panel = new JPanel();
        JPanel reveal_panel = new JPanel();

        hide_panel.add(new JLabel("HIDE!!!!!"));
        hide_panel.setLayout(new BoxLayout(hide_panel, BoxLayout.Y_AXIS));
        hide_panel.add(getFileDisplay("Image", "/home/jo/Workspace/ImgSteg/test/images/copy/copy_testImg1.jpg"));
        hide_panel.add(getFileDisplay("Key", "/home/jo/Workspace/ImgSteg/test/images/copy/copy_blankKey84.png"));
        hide_panel.add(getFileDisplay("FileToHide", "/home/jo/Workspace/ImgSteg/test/images/copy/copy_blankKey84White.png"));


        reveal_panel.add(new JLabel("REVEAL!!!!"));

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
