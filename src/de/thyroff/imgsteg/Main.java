package de.thyroff.imgsteg;

import de.thyroff.imgsteg.legacy.ui.CLILegacy;
import de.thyroff.imgsteg.legacy.ui.GUILegacy;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0) {
            new CLILegacy(args);
        } else { // show dialogs for user input
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
            new GUILegacy();
        }
    }
}
