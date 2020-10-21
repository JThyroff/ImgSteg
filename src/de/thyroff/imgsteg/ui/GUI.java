package de.thyroff.imgsteg.ui;

import javax.swing.*;
import java.io.File;

import static de.thyroff.imgsteg.legacy.HiderLegacy.hide;
import static de.thyroff.imgsteg.legacy.RevealerLegacy.reveal;

public class GUI {
    JFileChooser chooser = new JFileChooser();

    private void guiHide() {
        chooser.showOpenDialog(null);
        File selectedFile = chooser.getSelectedFile();
        System.out.println("Selected File: " + selectedFile.getAbsolutePath());

        String msg = JOptionPane.showInputDialog("Type Message you want to hide: ");

        hide(selectedFile, msg);
    }

    private void guiReveal() {
        chooser.showDialog(null, "image");
        File selectedFile = chooser.getSelectedFile();

        JFileChooser keyChooser = new JFileChooser();
        keyChooser.showDialog(null, "key");
        File key = keyChooser.getSelectedFile();

        JOptionPane.showMessageDialog(null, "The revealed text is: " + reveal(selectedFile, key));
    }

    public GUI() {
        ////////////////////////////////////////////////////////////////////////////
        ///// hide or reveal ? /////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////

        int mode = JOptionPane.showOptionDialog(null, "Hide or reveal?", "Select Mode", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE, null,
                new String[]{"Hide", "Reveal"}, "Hide");

        System.out.println("Selected mode: " + mode);

        ////////////////////////////////////////////////////////////////////////////
        ///// distinction //////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////

        if (mode == 0) {//hide
            guiHide();
        } else { //reveal
            guiReveal();
        }
    }
}
