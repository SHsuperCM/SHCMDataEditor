package shcm.shsupercm.data.editor.management;

import java.io.File;

public class OpenFileHandler {
    public final File file;

    public OpenFileHandler(File file) {
        this.file = file;
    }

    public OpenFileHandler() {
        this(null);
    }
}
