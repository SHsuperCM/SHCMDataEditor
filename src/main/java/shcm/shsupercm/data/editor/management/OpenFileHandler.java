package shcm.shsupercm.data.editor.management;

import shcm.shsupercm.data.framework.DataBlock;
import shcm.shsupercm.data.utils.CompressionUtils;

import java.io.File;

public class OpenFileHandler {
    public static final CompressionUtils COMPRESSION = CompressionUtils.GZIP;

    private File file;

    public DataBlock data;

    public boolean changed = false;

    public OpenFileHandler(File file) {
        this.file = file;
        this.reload();
    }

    public OpenFileHandler(DataBlock data) {
        this.file = null;
        this.data = data;
    }

    public File getFile() {
        return file;
    }

    public boolean reload() {
        if(file == null || !file.exists()) return false;
        try {
            this.data = (DataBlock) COMPRESSION.deserializeFile(this.file);
            this.changed = false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean save() {
        if(file == null || this.data == null) return false;
        if(file.exists()) file.delete();

        try {
            COMPRESSION.serializeFile(this.data, this.file);
            changed = false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void saveAs(File file) {
        this.file = file;
        save();
    }

    public String getText() {
        return file == null ? "New File" : file.getAbsolutePath();
    }
}
