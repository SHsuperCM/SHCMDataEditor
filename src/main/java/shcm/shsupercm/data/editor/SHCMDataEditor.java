package shcm.shsupercm.data.editor;

import shcm.shsupercm.data.editor.gui.frames.JFrameSHCMDataMain;

import javax.swing.*;

public class SHCMDataEditor {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrameSHCMDataMain mainFrame = new JFrameSHCMDataMain();
        mainFrame.setVisible(true);
    }
}
