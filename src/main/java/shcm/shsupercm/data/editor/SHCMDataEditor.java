package shcm.shsupercm.data.editor;

import shcm.shsupercm.data.editor.gui.frames.JFrameSHCMDataEditor;

import javax.swing.*;

public class SHCMDataEditor {

    public static final String version = "UNUSABLE_ALPHA";

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        //todo handle file opening

        new JFrameSHCMDataEditor().setVisible(true);
    }
}
