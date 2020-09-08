package de.thyroff.imgsteg;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void printHelp() {
        System.out.println("-------------------");
        System.out.println("This program hides a given ASCII String in a given Image and returns another Image as key for un-hiding.");
        System.out.println("If program is used in commandline both arguments are mandatory.");
        System.out.println("Argument 1 (optional) : Path to Image");
        System.out.println("Argument 2 (optional) : String to hide in the Image");
        System.out.println("-------------------");
    }

    /**
     * @param image the image
     * @param c     the char to hide
     * @return the best location where the char can be hidden
     */
    public static MyClass searchBestPos(BufferedImage image, char c) {
        MyClass bestPoint = null;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); x++) {
                int rgb = image.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xff;
                int red = (rgb >> 16) & 0xff;
                int green = (rgb >> 8) & 0xff;
                int blue = rgb & 0xff;

                ////////////////////////////////////////////////////////////////////////////////
                ////////////////////////////// test whether location is suitable ///////////////
                ////////////////////////////////////////////////////////////////////////////////

                MyClass m = null;
                if (alpha == c) {
                    m = new MyClass(x, y, Channel.ALPHA, 0);
                } else if (red == c) {
                    m = new MyClass(x, y, Channel.R, 0);
                } else if (green == c) {
                    m = new MyClass(x, y, Channel.G, 0);
                } else if (blue == c) {
                    m = new MyClass(x, y, Channel.B, 0);
                }

                if (m != null) {
                    // suitable location found
                    return m;
                }

                ////////////////////////////////////////////////////////////////////////////////
                ////////////////////////////// location is not suitable ////////////////////////
                ////////////////////////////////////////////////////////////////////////////////
                int alphaDiff = Math.abs(c - alpha);
                int redDiff = Math.abs(c - red);
                int greenDiff = Math.abs(c - green);
                int blueDiff = Math.abs(c - blue);

                Channel channel = Channel.B;
                int localOffset = c - blue;
                if (alphaDiff <= redDiff && alphaDiff <= greenDiff && alphaDiff <= blueDiff) {
                    channel = Channel.ALPHA;
                    localOffset = c - alpha;
                } else if (redDiff <= alphaDiff && redDiff <= greenDiff && redDiff <= blueDiff) {
                    channel = Channel.R;
                    localOffset = c - red;
                } else if (greenDiff <= alphaDiff && greenDiff <= redDiff && greenDiff <= blueDiff) {
                    channel = Channel.G;
                    localOffset = c - green;
                }

                //this location is not suitable -> compare with best point
                if (bestPoint == null || localOffset < bestPoint.offset) { //overwrite best point
                    bestPoint = new MyClass(x, y, channel, localOffset);
                }
            }
        }

        return bestPoint;
    }


    public static void hide(File file, String msg) {
        try {
            BufferedImage image = ImageIO.read(file);
            for (char c : msg.toCharArray()) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
