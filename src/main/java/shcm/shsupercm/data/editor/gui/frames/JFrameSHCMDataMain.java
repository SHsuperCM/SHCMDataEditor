package shcm.shsupercm.data.editor.gui.frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JFrameSHCMDataMain extends JFrame {
    public JFrameSHCMDataMain() throws HeadlessException {
        this.setTitle("SHCMData Editor");
        this.setSize(750,660);
        this.addWindowListener(new WindowEvents());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }


    private class WindowEvents extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            if (JOptionPane.showConfirmDialog(JFrameSHCMDataMain.this,
                    "Are you sure you want to close this window?", "Close Window?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                System.exit(0);
            }
        }
    }
}
