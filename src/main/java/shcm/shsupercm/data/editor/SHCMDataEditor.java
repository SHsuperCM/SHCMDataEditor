package shcm.shsupercm.data.editor;

import shcm.shsupercm.data.editor.gui.JFrameSHCMDataEditor;
import shcm.shsupercm.data.editor.management.OpenFileHandler;
import shcm.shsupercm.data.framework.DataBlock;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SHCMDataEditor {
    public static final String version = "0.2";

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        ToolTipManager.sharedInstance().setInitialDelay(200);
        UIManager.put("ToolTip.background", Color.WHITE);

        OpenFileHandler openFileHandler = null;

        if (args.length > 0) {
            if (args[0].equals("New"))
                openFileHandler = new OpenFileHandler(new DataBlock());
            else {
                File file = new File(args[0]);
                if (file.exists())
                    openFileHandler = new OpenFileHandler(file);
            }
        }

        new JFrameSHCMDataEditor(openFileHandler).setVisible(true);
    }
}
