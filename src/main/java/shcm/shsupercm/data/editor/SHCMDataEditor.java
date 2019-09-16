package shcm.shsupercm.data.editor;

import shcm.shsupercm.data.editor.gui.frames.JFrameSHCMDataEditor;

import javax.swing.*;

public class SHCMDataEditor {

    public static final int version = 0;

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        //todo

        JFrameSHCMDataEditor mainFrame = new JFrameSHCMDataEditor();
        mainFrame.setVisible(true);
    }
}
