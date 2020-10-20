package de.thyroff.imgsteg;

import de.thyroff.imgsteg.ui.CUI;
import de.thyroff.imgsteg.ui.GUI;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0) {
            new CUI(args);
        } else { // show dialogs for user input
            new GUI();
        }
    }
}
