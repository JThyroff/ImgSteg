package de.thyroff.imgsteg;

import de.thyroff.imgsteg.ui.CLI;
import de.thyroff.imgsteg.ui.GUI;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0) {
            new CLI(args);
        } else { // show dialogs for user input
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            new GUI();
        }
    }
}
