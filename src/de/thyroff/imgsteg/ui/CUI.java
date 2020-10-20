package de.thyroff.imgsteg.ui;

import java.io.File;

import static de.thyroff.imgsteg.Hider.hide;
import static de.thyroff.imgsteg.Revealer.reveal;

public class CUI extends UI {
    public CUI(String[] args) {
        if (args.length == 3) { // execute in cmd mode
            String mode = args[0];
            String file = args[1];
            String msg = args[2];

            if (mode.equalsIgnoreCase("h") || mode.equalsIgnoreCase("hide")) {
                hide(new File(file), msg);
            } else if (mode.equalsIgnoreCase("r") || mode.equalsIgnoreCase("reveal")) {
                System.out.println(reveal(new File(file), new File(msg)));
            } else {
                showHelp();
            }
        } else {// show some help
            showHelp();
        }
    }

    @Override
    public void showHelp() {
        System.out.println("-------------------");
        System.out.println("This program hides a given ASCII String in a given Image and returns another Image as key for revealing.");
        System.out.println("If program is used in commandline all arguments are mandatory.");
        System.out.println("Argument 1 (optional) : Mode -> h(ide) or r(eveal)");
        System.out.println("Argument 2 (optional) : String to hide in the Image");
        System.out.println("Argument 3 (optional) : String to key or String to hide (dependent from chosen mode)");
        System.out.println("-------------------");
    }
}
