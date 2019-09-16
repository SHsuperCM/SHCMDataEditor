package shcm.shsupercm.data.editor.gui.frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JFrameSHCMDataEditor extends JFrame {


    public JFrameSHCMDataEditor() throws HeadlessException {
        this.setTitle("SHCMData Editor");
        this.setSize(750,660);
        this.setMaximumSize(new Dimension(750, 660));
        this.setMinimumSize(new Dimension(750, 660));
        this.addWindowListener(new WindowEvents());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        {
            JMenuBar menu = new JMenuBar();
            this.setJMenuBar(menu);
            menu.add(new JMenu("File") {{
                this.add(new JMenuItem(new AbstractAction("New") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(this.getValue("Name"));
                    }
                }) {{setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));}});
                this.add(new JMenuItem(new AbstractAction("Open") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(this.getValue("Name"));
                    }
                }) {{setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));}});
                this.add(new JMenuItem(new AbstractAction("Save") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(this.getValue("Name"));
                    }
                }) {{setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));}});
                this.add(new JMenuItem(new AbstractAction("Save As") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(this.getValue("Name"));
                    }
                }) {{setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.SHIFT_MASK));}});
            }});
        }


    }

    private class WindowEvents extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            //todo dont do this if saved
            //int option = JOptionPane.showOptionDialog(JFrameSHCMDataEditor.this, "You can't just close the window!\nWhat do I do?", "SHCMData Editor - Idiot Proof Protocol", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{"Save", "Don't Save", "Back"}, "Back");
            //if (option == 0 || option == 1) {
                //todo
                System.exit(0);
            //}
        }
    }
}
