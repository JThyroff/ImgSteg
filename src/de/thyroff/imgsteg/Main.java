package de.thyroff.imgsteg;

import javax.swing.*;
import java.io.File;

public class Main {

    public static void printHelp() {
        System.out.println("-------------------");
        System.out.println("This program hides a given String in a given Image and returns another Image as key for un-hiding.");
        System.out.println("If program is used in commandline both arguments are mandatory.");
        System.out.println("Argument 1 (optional) : Path to Image");
        System.out.println("Argument 2 (optional) : String to hide in the Image");
        System.out.println("-------------------");
    }

    public static void hide(File file, String msg) {

    }


    public static void main(String[] args) {
        if (args.length > 0) {
            if (args.length == 1) { // show help
                printHelp();
            } else if (args.length == 2) { // execute in cmd mode
                String path = args[0];
                String msg = args[1];
                System.err.println("not implemented yet.");
            }
        } else { // show dialogs for user input
            JFileChooser chooser = new JFileChooser();
            int ans = chooser.showOpenDialog(null);
            File selectedFile = null;
            if (ans == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
            } else {
                JOptionPane.showMessageDialog(null, "Choose a png file.");
                System.exit(1);
            }

            String msg = JOptionPane.showInputDialog("Type Message you want to hide: ");

            hide(selectedFile, msg);
        }
    }
}
