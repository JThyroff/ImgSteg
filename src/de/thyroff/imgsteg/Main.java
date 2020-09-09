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
        System.out.println("This program hides a given ASCII String in a given Image and returns another Image as key for revealing.");
        System.out.println("If program is used in commandline all arguments are mandatory.");
        System.out.println("Argument 1 (optional) : Mode -> h(ide) or r(eveal)");
        System.out.println("Argument 2 (optional) : String to hide in the Image");
        System.out.println("Argument 3 (optional) : String to key or String to hide (dependent from chosen mode)");
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
            for (short y = 0; y < image.getHeight(); y++) {
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
                    m = new MyPosition(x, y, Channel.RED, (short) 0);
                } else if (green == c) {
                    m = new MyPosition(x, y, Channel.GREEN, (short) 0);
                } else if (blue == c) {
                    m = new MyPosition(x, y, Channel.BLUE, (short) 0);
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

                Channel channel = Channel.BLUE;
                short localOffset = (short) (c - blue);
                if (alphaDiff <= redDiff && alphaDiff <= greenDiff && alphaDiff <= blueDiff) {
                    channel = Channel.ALPHA;
                    localOffset = (short) (c - alpha);
                } else if (redDiff <= alphaDiff && redDiff <= greenDiff && redDiff <= blueDiff) {
                    channel = Channel.RED;
                    localOffset = (short) (c - red);
                } else if (greenDiff <= alphaDiff && greenDiff <= redDiff && greenDiff <= blueDiff) {
                    channel = Channel.GREEN;
                    localOffset = (short) (c - green);
                }

                //this location is not suitable -> compare with best point
                if (bestPoint == null || Math.abs(localOffset) < Math.abs(bestPoint.offset)) { //overwrite best point
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
            writeImage(file.getParent(), list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeImage(String path, LinkedList<MyPosition> list) {
        BufferedImage bi = new BufferedImage(list.size(), 2, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < list.size(); x++) {
            MyPosition myPosition = list.get(x);
            bi.setRGB(x, 0, myPosition.x + (myPosition.y << 16));
            bi.setRGB(x, 1, myPosition.channel.toInt() + (myPosition.offset << 16));
        }
        try {
            ImageIO.write(bi, "PNG", new File(path + "/key.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String reveal(File selectedFile, File key) {
        try {
            BufferedImage image = ImageIO.read(selectedFile);
            BufferedImage keyImg = ImageIO.read(key);

            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < keyImg.getWidth(); x++) {
                int posYX = keyImg.getRGB(x, 0);
                int offsetChannel = keyImg.getRGB(x, 1);

                short x_coord = (short) (posYX);
                short y_coord = (short) (posYX >> 16);
                short offset = (short) (offsetChannel >> 16);
                sb.append(getChar(image, new MyPosition(x_coord, y_coord, Channel.toChannel((short) offsetChannel), offset)));
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static char getChar(BufferedImage image, MyPosition pos) {
        int rgb = image.getRGB(pos.getX(), pos.getY());
        int alpha = (rgb >> 24) & 0xff;
        int red = (rgb >> 16) & 0xff;
        int green = (rgb >> 8) & 0xff;
        int blue = rgb & 0xff;

        switch (pos.channel) {

            case ALPHA -> {
                return (char) (alpha + pos.offset);
            }
            case RED -> {
                return (char) (red + pos.offset);
            }
            case GREEN -> {
                return (char) (green + pos.offset);
            }
            case BLUE -> {
                return (char) (blue + pos.offset);
            }
            default -> {
                return ' ';
            }
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            if (args.length == 3) { // execute in cmd mode
                String mode = args[0];
                String file = args[1];
                String msg = args[2];
                if (mode.equalsIgnoreCase("h") || mode.equalsIgnoreCase("hide")) {
                    hide(new File(file), msg);
                } else if (mode.equalsIgnoreCase("r") || mode.equalsIgnoreCase("reveal")) {
                    System.out.println(reveal(new File(file), new File(msg)));
                } else {
                    printHelp();
                }
            } else {// show some help
                printHelp();
            }
        } else { // show dialogs for user input
            int mode = JOptionPane.showOptionDialog(null, "hide or reveal?", "Select Mode", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE, null,
                    new String[]{"hide", "reveal"}, "hide");

            System.out.println("Selected mode: " + mode);

            JFileChooser chooser = new JFileChooser();
            if (mode == 0) {//hide
                chooser.showOpenDialog(null);
                File selectedFile = chooser.getSelectedFile();
                System.out.println("Selected File: " + selectedFile.getAbsolutePath());

                String msg = JOptionPane.showInputDialog("Type Message you want to hide: ");

                hide(selectedFile, msg);
            } else { //reveal
                chooser.showDialog(null, "image");
                File selectedFile = chooser.getSelectedFile();

                JFileChooser keyChooser = new JFileChooser();
                keyChooser.showDialog(null, "key");
                File key = keyChooser.getSelectedFile();

                JOptionPane.showMessageDialog(null, "The revealed text is: " + reveal(selectedFile, key));
            }
        }

    }
}
