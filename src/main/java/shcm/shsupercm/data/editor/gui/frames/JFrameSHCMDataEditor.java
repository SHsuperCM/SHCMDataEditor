package shcm.shsupercm.data.editor.gui.frames;

import shcm.shsupercm.data.SHCMData;
import shcm.shsupercm.data.editor.SHCMDataEditor;
import shcm.shsupercm.data.editor.gui.Assets;
import shcm.shsupercm.data.editor.gui.graphics.DataCellRenderer;
import shcm.shsupercm.data.editor.management.DataEntry;
import shcm.shsupercm.data.editor.management.OpenFileHandler;
import shcm.shsupercm.data.framework.DataBlock;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class JFrameSHCMDataEditor extends JFrame {
    private OpenFileHandler openFileHandler;
    private JTree valueTree;
    private JToolBar toolBar;

    public JFrameSHCMDataEditor(OpenFileHandler openFileHandlerIN) throws HeadlessException {
        this.openFileHandler = openFileHandlerIN;
        this.setIconImage(Assets.LOGO.getImage());
        this.setMinimumSize(new Dimension(750, 660));
        this.setSize(this.getMinimumSize());
        this.setLayout(new BorderLayout());
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
                            int option = JOptionPane.showOptionDialog(JFrameSHCMDataEditor.this, "Where do I open this new file?", "SHCMData Editor - Idiot Proof Protocol", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{"New window", "This window", "Cancel"}, "New window");
                            if (option == 0) {
                                try {
                                    new ProcessBuilder("java", "-jar", new File(SHCMDataEditor.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath(), "New").start();
                                } catch (IOException | URISyntaxException ex) {
                                    ex.printStackTrace();
                                }
                            } else if (option == 1) {
                                if(openFileHandler.changed) {
                                    int optionSave = JOptionPane.showOptionDialog(JFrameSHCMDataEditor.this, "You can't just close the window!\nWhat do I do?", "SHCMData Editor - Idiot Proof Protocol", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{"Save", "Don't Save", "Back"}, "Save");
                                    if(optionSave == 2)
                                        return;
                                    if(optionSave == 0)
                                        openFileHandler.save();
                                }
                                openFileHandler = new OpenFileHandler(new DataBlock());
                            }
                        } else {
                            openFileHandler = new OpenFileHandler(new DataBlock());
                        }
                        refresh();
                    }
                }) {{setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));}});
                this.add(new JMenuItem(new AbstractAction("Open") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser openDialog = new JFileChooser();

                        openDialog.setDialogType(JFileChooser.OPEN_DIALOG);
                        openDialog.setSelectedFile(new File("file.shcmd"));
                        openDialog.setFileFilter(new FileNameExtensionFilter("SHCMData File", "shcmd"));
                        openDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        openDialog.setMultiSelectionEnabled(false);

                        if(openDialog.showOpenDialog(JFrameSHCMDataEditor.this) == JFileChooser.APPROVE_OPTION && openDialog.getSelectedFile().exists()) {
                            if(openFileHandler != null) {
                                int option = JOptionPane.showOptionDialog(JFrameSHCMDataEditor.this, "Where do I open this file?", "SHCMData Editor - Idiot Proof Protocol", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{"New window", "This window", "Cancel"}, "New window");
                                if (option == 0) {
                                    try {
                                        new ProcessBuilder("java", "-jar", new File(SHCMDataEditor.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath(), openDialog.getSelectedFile().getAbsolutePath()).start();
                                    } catch (IOException | URISyntaxException ex) {
                                        ex.printStackTrace();
                                    }
                                } else if (option == 1) {
                                    if(openFileHandler.changed) {
                                        int optionSave = JOptionPane.showOptionDialog(JFrameSHCMDataEditor.this, "You can't just close this file!!\nWhat do I do?", "SHCMData Editor - Idiot Proof Protocol", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{"Save", "Don't Save", "Back"}, "Save");
                                        if(optionSave == 2)
                                            return;
                                        if(optionSave == 0)
                                            openFileHandler.save();
                                    }
                                    openFileHandler = new OpenFileHandler(openDialog.getSelectedFile());
                                }
                            } else {
                                openFileHandler = new OpenFileHandler(openDialog.getSelectedFile());
                            }
                        }

                        refresh();
                    }
                }) {{setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));}});
                this.add(new JMenuItem(new AbstractAction("Reload") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(openFileHandler != null) {
                            openFileHandler.reload();
                            refresh();
                        }
                    }
                }) {{setAccelerator(KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));}});
                this.add(new JMenuItem(new AbstractAction("Save") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(openFileHandler != null) {
                            if(openFileHandler.getFile() == null) {
                                menu.getMenu(0).getItem(4).getAction().actionPerformed(e);
                            } else {
                                openFileHandler.save();
                                refresh();
                            }
                        }
                    }
                }) {{setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));}});
                this.add(new JMenuItem(new AbstractAction("Save As") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(openFileHandler != null) {
                            JFileChooser saveDialog = new JFileChooser();

                            saveDialog.setDialogType(JFileChooser.SAVE_DIALOG);
                            saveDialog.setSelectedFile(new File("file.shcmd"));
                            saveDialog.setFileFilter(new FileNameExtensionFilter("SHCMData File", "shcmd"));

                            if(saveDialog.showSaveDialog(JFrameSHCMDataEditor.this) == JFileChooser.APPROVE_OPTION) {
                                if(saveDialog.getSelectedFile().exists()) {
                                    if (JOptionPane.showConfirmDialog(null, "Do you want to replace the existing file?", "SHCMData Editor - Idiot Proof Protocol", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
                                        this.actionPerformed(e);
                                        return;
                                    }
                                }
                                openFileHandler.saveAs(saveDialog.getSelectedFile());
                            }
                            refresh();
                        }
                    }
                }) {{setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.SHIFT_MASK));}});

                this.addMenuListener(new MenuListener() {
                    @Override
                    public void menuSelected(MenuEvent e) {
                        //refresh();

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
                this.add(new JMenuItem(new AbstractAction("SHCMData Format") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                            try {
                                Desktop.getDesktop().browse(new URI("https://github.com/SHsuperCM/SHCMData/wiki/Data-Format"));
                            } catch (IOException | URISyntaxException ignored) {}
                        }
                    }
                }));
            }});
        }

        {
            toolBar = new JToolBar();

            toolBar.setRequestFocusEnabled(false);

            {
                JButton action = toolBar.add(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });
                action.setIcon(Assets.ICON_DATABLOCK);
                action.setToolTipText("Add a new Data Block to the selected item.");
            }

            {
                JButton action = toolBar.add(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });
                action.setIcon(Assets.ICON_DATAKEYEDBLOCK);
                action.setToolTipText("Add a new Data Keyed Block to the selected item.");
            }

            {
                JButton action = toolBar.add(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });
                action.setIcon(Assets.ICON_ARRAY);
                action.setToolTipText("Add a new Array to the selected item.");
            }

            toolBar.addSeparator();

            {
                JButton action = toolBar.add(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });
                action.setIcon(Assets.ICON_BOOLEAN);
                action.setToolTipText("Add a new Boolean to the selected item.");
            }

            toolBar.addSeparator();

            {
                JButton action = toolBar.add(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });
                action.setIcon(Assets.ICON_STRING);
                action.setToolTipText("Add a new String to the selected item.");
            }

            {
                JButton action = toolBar.add(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });
                action.setIcon(Assets.ICON_CHARACTER);
                action.setToolTipText("Add a new Character to the selected item.");
            }

            toolBar.addSeparator();

            {
                JButton action = toolBar.add(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });
                action.setIcon(Assets.ICON_INTEGER);
                action.setToolTipText("Add a new Integer to the selected item.");
            }

            {
                JButton action = toolBar.add(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });
                action.setIcon(Assets.ICON_BYTE);
                action.setToolTipText("Add a new Byte to the selected item.");
            }

            {
                JButton action = toolBar.add(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });
                action.setIcon(Assets.ICON_FLOAT);
                action.setToolTipText("Add a new Float to the selected item.");
            }

            {
                JButton action = toolBar.add(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });
                action.setIcon(Assets.ICON_DOUBLE);
                action.setToolTipText("Add a new Double to the selected item.");
            }

            {
                JButton action = toolBar.add(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });
                action.setIcon(Assets.ICON_LONG);
                action.setToolTipText("Add a new Long to the selected item.");
            }

            {
                JButton action = toolBar.add(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });
                action.setIcon(Assets.ICON_SHORT);
                action.setToolTipText("Add a new Short to the selected item.");
            }

            add(toolBar, BorderLayout.NORTH);
        }

        {
            valueTree = new JTree();

            valueTree.addTreeSelectionListener(e -> {
                boolean canAddItems = valueTree.getSelectionCount() == 1;
                if(canAddItems) {
                    try {
                        Object value = ((DataEntry) ((DefaultMutableTreeNode) valueTree.getLastSelectedPathComponent()).getUserObject()).value;
                        canAddItems = !(
                                value instanceof String ||
                                value instanceof Boolean ||
                                value instanceof Byte ||
                                value instanceof Short ||
                                value instanceof Character ||
                                value instanceof Integer ||
                                value instanceof Float ||
                                value instanceof Long ||
                                value instanceof Double);
                    } catch (Exception ignored) {
                        canAddItems = false;
                    }
                }
                for (Component component : toolBar.getComponents()) component.setEnabled(canAddItems);
            });

            valueTree.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = valueTree.getRowForLocation(e.getX(),e.getY());
                    if(row == -1)
                        valueTree.clearSelection();
                }
            });

            JScrollPane scrollPane = new JScrollPane(valueTree);
            scrollPane.setViewportView(valueTree);

            valueTree.setCellRenderer(new DataCellRenderer());

            add(scrollPane);
            scrollPane.requestFocus();
        }

        refresh();
    }

    public void refresh() {
        if(openFileHandler == null) {
            this.setTitle("SHCMData Editor");
            for (Component component : this.toolBar.getComponents()) component.setEnabled(false);
            this.valueTree.setEnabled(false);
            this.valueTree.setModel(null);
        } else {
            this.setTitle("SHCMData Editor - " + openFileHandler.getText() + (openFileHandler.changed ? " *" : ""));
            for (Component component : this.toolBar.getComponents()) component.setEnabled(false);
            this.valueTree.setEnabled(true);
            this.valueTree.setModel(new DefaultTreeModel(DataEntry.read(null, openFileHandler.getText(), openFileHandler.data), true));
        }
    }

    private class WindowEvents extends WindowAdapter {
        @Override
        public void windowOpened(WindowEvent e) {
            requestFocus();
        }

        @Override
        public void windowClosing(WindowEvent e) {
            if(openFileHandler != null && openFileHandler.changed) {
                int optionSave = JOptionPane.showOptionDialog(JFrameSHCMDataEditor.this, "You can't just close the window!\nWhat do I do?", "SHCMData Editor - Idiot Proof Protocol", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{"Save", "Don't Save", "Back"}, "Save");
                if(optionSave == 2)
                    return;
                if(optionSave == 0)
                    openFileHandler.save();
            }
            System.exit(0);
        }
    }
}
