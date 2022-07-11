package de.thyroff.imgsteg.legacy.ui;

import java.io.File;

import static de.thyroff.imgsteg.legacy.HiderLegacy.hide;
import static de.thyroff.imgsteg.legacy.RevealerLegacy.reveal;

public class CLILegacy {
    public CLILegacy(String[] args) {
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


    public void showHelp() {
        System.out.println("-------------------");
        System.out.println("This program hides a given ASCII String in a given Image and returns another Image as key for revealing.");
        System.out.println("If program is used in commandline all arguments are mandatory.");
        System.out.println("Argument 1 : Mode -> h(ide) or r(eveal)");
        System.out.println("Argument 2 : String to hide in the Image");
        System.out.println("Argument 3 : String to key or String to hide (dependent from chosen mode)");
        System.out.println("-------------------");
    }
}
