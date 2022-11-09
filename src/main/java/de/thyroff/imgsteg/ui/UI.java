package de.thyroff.imgsteg.ui;

import java.io.File;
import java.nio.file.Path;

public interface UI {

    void hide(File file, File keyFile, Path msgPath);

    void reveal();
}
