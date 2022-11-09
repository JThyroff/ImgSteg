package de.thyroff.imgsteg.ui;

import com.formdev.flatlaf.FlatLightLaf;
import de.thyroff.imgsteg.Hider;
import de.thyroff.imgsteg.utils.ImageHelper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;

public class GUI implements UI {

    private final JFileChooser chooser = new JFileChooser();

    public static void main(String[] args) {
        FlatLightLaf.setup();
        new GUI();
    }

    private JTabbedPane getOptionSwitchPanel() {
        JTabbedPane jTabbedPane = new JTabbedPane();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////// hide panel /////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////

        HidePanel hide_panel = new HidePanel(this);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////// reveal panel //////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////

        RevealPanel reveal_panel = new RevealPanel();

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
        //jFrame.setResizable(false);
    }

    @Override
    public void hide(File file, File keyFile, Path msgPath) {
        Hider.hide(file, keyFile, msgPath);
    }

    @Override
    public void reveal() {

    }

    private class HidePanel extends JPanel {
        private String imagePath = "/home/jo/Workspace/ImgSteg/test/images/copy/copy_testImg1.jpg";
        private String keyPath = "/home/jo/Workspace/ImgSteg/test/images/copy/copy_blankKey84.png";
        private String fileToHidePath = "/home/jo/Workspace/ImgSteg/test/images/copy/copy_blankKey84White.png";


        public HidePanel(GUI gui) {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.add(new JLabel("HIDE"));

            JPanel hidePanelPick = new JPanel();
            GridBagLayout mgr = new GridBagLayout();
            hidePanelPick.setLayout(mgr);
            GridBagConstraints c = new GridBagConstraints();
            mgr.setConstraints(hidePanelPick, c);

            /*
            hidePanelPick.add(getFileDisplay("Image", "/home/jo/Workspace/ImgSteg/test/images/copy/copy_testImg1.jpg"));
            hidePanelPick.add(getFileDisplay("Key", "/home/jo/Workspace/ImgSteg/test/images/copy/copy_blankKey84.png"));
            hidePanelPick.add(getFileDisplay("FileToHide", "/home/jo/Workspace/ImgSteg/test/images/copy/copy_blankKey84White.png"));*/

            try {
                Field imagePathField = HidePanel.class.getDeclaredField("imagePath");
                imagePathField.setAccessible(true);
                Field keyPathField = HidePanel.class.getDeclaredField("keyPath");
                keyPathField.setAccessible(true);
                Field fileToHidePathField = HidePanel.class.getDeclaredField("fileToHidePath");
                fileToHidePathField.setAccessible(true);

                addFileDisplay(c, 0, hidePanelPick, "Image", this.imagePath, imagePathField);
                addFileDisplay(c, 1, hidePanelPick, "Key", this.keyPath, keyPathField);
                addFileDisplay(c, 2, hidePanelPick, "FileToHide", this.fileToHidePath, fileToHidePathField);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
            this.add(hidePanelPick);
            this.add(new JSeparator());
            JButton hide = new JButton("Hide");
            hide.addActionListener(e -> {
                gui.hide(new File(this.imagePath), new File(this.keyPath), Path.of(this.fileToHidePath));
            });
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            jPanel.add(hide);
            this.add(jPanel);
        }

        /**
         * Adds a new row to the hide panel pick area
         *
         * @param c                 GridBagConstraints
         * @param row               where to add the new row. Modifies the GridBagConstraints accordingly
         * @param hidePanelPick     hide panel pick area where the new row is added in the end
         * @param fileName          the filename of the image that should be picked
         * @param path              the file path of the image that should be picked
         * @param pathVariableField the path variable field to store the updated path in
         */
        private void addFileDisplay(GridBagConstraints c, int row, JPanel hidePanelPick, String fileName, String path, Field pathVariableField) {
            JButton jButton = new JButton(fileName);
            String[] split = path.split("/");

            JLabel jLabel = new JLabel(split[split.length - 1]);

            JLabel picLabel = new JLabel(ImageHelper.getResizedImageIconFromPath(path));
            picLabel.setHorizontalAlignment(SwingConstants.CENTER);

            jButton.addActionListener(e -> {
                chooser.showOpenDialog(null);
                File selectedFile = chooser.getSelectedFile();
                String selectedFileAbsolutePath = selectedFile.getAbsolutePath();
                try {
                    pathVariableField.set(this, selectedFile.getAbsolutePath());
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }

                System.getLogger("App").log(System.Logger.Level.INFO, fileName + "Selected File absolute Path : " + selectedFileAbsolutePath);
                jLabel.setText(selectedFile.getName());
                picLabel.setIcon(ImageHelper.getResizedImageIconFromPath(selectedFileAbsolutePath));

                hidePanelUpdate();
            });

            c.gridy = row;
            c.gridx = 0;
            hidePanelPick.add(jButton, c);
            c.gridx = 1;
            hidePanelPick.add(new JSeparator(SwingConstants.VERTICAL), c);
            c.gridx = 2;
            hidePanelPick.add(jLabel, c);
            c.gridx = 3;
            hidePanelPick.add(new JSeparator(SwingConstants.VERTICAL), c);
            c.gridx = 4;
            hidePanelPick.add(picLabel, c);

            hidePanelPick.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        }

        private void hidePanelUpdate() {
            System.getLogger("App").log(System.Logger.Level.INFO, "imagePath : " + imagePath);
            System.getLogger("App").log(System.Logger.Level.INFO, "keyPath : " + keyPath);
            System.getLogger("App").log(System.Logger.Level.INFO, "fileToHidePath : " + fileToHidePath);
        }
    }

    private class RevealPanel extends JPanel {
        public RevealPanel() {
            this.add(new JLabel("REVEAL"));
        }
    }
}
