package de.thyroff.imgsteg;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Main {

    private static void printHelp() {
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
    private static MyPosition searchBestPos(BufferedImage image, char c) {
        MyPosition bestPoint = null;
        for (short x = 0; x < image.getWidth(); x++) {
            for (short y = 0; y < image.getHeight(); x++) {
                int rgb = image.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xff;
                int red = (rgb >> 16) & 0xff;
                int green = (rgb >> 8) & 0xff;
                int blue = rgb & 0xff;

                ////////////////////////////////////////////////////////////////////////////////
                ////////////////////////////// test whether location is suitable ///////////////
                ////////////////////////////////////////////////////////////////////////////////

                MyPosition m = null;
                if (alpha == c) {
                    m = new MyPosition(x, y, Channel.ALPHA, (short) 0);
                } else if (red == c) {
                    m = new MyPosition(x, y, Channel.R, (short) 0);
                } else if (green == c) {
                    m = new MyPosition(x, y, Channel.G, (short) 0);
                } else if (blue == c) {
                    m = new MyPosition(x, y, Channel.B, (short) 0);
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
                short localOffset = (short) (c - blue);
                if (alphaDiff <= redDiff && alphaDiff <= greenDiff && alphaDiff <= blueDiff) {
                    channel = Channel.ALPHA;
                    localOffset = (short) (c - alpha);
                } else if (redDiff <= alphaDiff && redDiff <= greenDiff && redDiff <= blueDiff) {
                    channel = Channel.R;
                    localOffset = (short) (c - red);
                } else if (greenDiff <= alphaDiff && greenDiff <= redDiff && greenDiff <= blueDiff) {
                    channel = Channel.G;
                    localOffset = (short) (c - green);
                }

                //this location is not suitable -> compare with best point
                if (bestPoint == null || localOffset < bestPoint.offset) { //overwrite best point
                    bestPoint = new MyPosition(x, y, channel, localOffset);
                }
            }
        }

        return bestPoint;
    }


    private static void hide(File file, String msg) {
        try {
            BufferedImage image = ImageIO.read(file);
            LinkedList<MyPosition> list = new LinkedList<>();
            for (char c : msg.toCharArray()) {
                list.add(searchBestPos(image, c));
            }
            writeImage(file.getAbsolutePath(), list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeImage(String absolutePath, LinkedList<MyPosition> list) {
        BufferedImage bi = new BufferedImage(list.size(), 1, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < list.size(); x++) {
            MyPosition myPosition = list.get(x);
            int rgb = 0;
            bi.setRGB(x, 1, rgb);
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
