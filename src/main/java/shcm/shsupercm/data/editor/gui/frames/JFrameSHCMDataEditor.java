package shcm.shsupercm.data.editor.gui.frames;

import shcm.shsupercm.data.SHCMData;
import shcm.shsupercm.data.editor.SHCMDataEditor;
import shcm.shsupercm.data.editor.management.OpenFileHandler;
import shcm.shsupercm.data.framework.DataBlock;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class JFrameSHCMDataEditor extends JFrame {
    private OpenFileHandler openFileHandler = null;

    public JFrameSHCMDataEditor() throws HeadlessException {
        this.setTitle("SHCMData Editor");
        this.setSize(750,660);
        this.setResizable(false);
        this.addWindowListener(new WindowEvents());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        {
            JMenuBar menu = new JMenuBar();
            this.setJMenuBar(menu);
            menu.add(new JMenu("File") {{
                this.add(new JMenuItem(new AbstractAction("New") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(openFileHandler != null) {
                            int option = JOptionPane.showOptionDialog(JFrameSHCMDataEditor.this, "What do I do?", "SHCMData Editor - Idiot Proof Protocol", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{"Open in a new window", "Don't Save", "Back"}, "Open in a new window");
                            if (option == 0) {
                                try {
                                    new ProcessBuilder("java", "-jar", new File(SHCMDataEditor.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath(), "New").start();
                                } catch (IOException | URISyntaxException ex) {
                                    ex.printStackTrace();
                                }
                            } else if (option == 1) {
                                openFileHandler = new OpenFileHandler(new DataBlock());
                            }
                        }
                    }
                }) {{setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));}});
                this.add(new JMenuItem(new AbstractAction("Open") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(this.getValue("Name"));
                    }
                }) {{setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));}});
                this.add(new JMenuItem(new AbstractAction("Reload") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(this.getValue("Name"));
                    }
                }) {{setAccelerator(KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));}});
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

                this.addMenuListener(new MenuListener() {
                    @Override
                    public void menuSelected(MenuEvent e) {
                        JMenu file = menu.getMenu(0);

                        file.getItem(2).setEnabled(openFileHandler != null && openFileHandler.getFile() != null);
                        file.getItem(3).setEnabled(openFileHandler != null);
                        file.getItem(4).setEnabled(openFileHandler != null);
                    }

                    @Override
                    public void menuDeselected(MenuEvent e) {}
                    @Override
                    public void menuCanceled(MenuEvent e) {}
                });
            }});
            menu.add(new JMenu("About") {{
                this.add(new JMenuItem(new AbstractAction("SHCMData library version: V" + SHCMData.versionName) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                            try {
                                Desktop.getDesktop().browse(new URI("https://github.com/SHsuperCM/SHCMData"));
                            } catch (IOException | URISyntaxException ignored) {}
                        }
                    }
                }));
                this.add(new JMenuItem(new AbstractAction("SHCMData Editor version: V" + SHCMDataEditor.version) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                            try {
                                Desktop.getDesktop().browse(new URI("https://github.com/SHsuperCM/SHCMDataEditor"));
                            } catch (IOException | URISyntaxException ignored) {}
                        }
                    }
                }));
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
