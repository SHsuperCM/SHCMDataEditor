package shcm.shsupercm.data.editor.gui;

import shcm.shsupercm.data.SHCMData;
import shcm.shsupercm.data.editor.SHCMDataEditor;
import shcm.shsupercm.data.editor.management.DataEntry;
import shcm.shsupercm.data.editor.management.DummyDataEntry;
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
import java.util.HashSet;

public class JFrameSHCMDataEditor extends JFrame {
    public static final Color CHANGED_COLOR = new Color(0xE0FFD9);

    public OpenFileHandler openFileHandler;
    public JTree valueTree;

    public HashSet<String> openedPaths = new HashSet<>();

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
                JMenuItem itemNew;
                this.add(itemNew = new JMenuItem(new AbstractAction("New") {
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
                                    if(optionSave == 0) {
                                        if(!openFileHandler.save()) {
                                            JFileChooser saveDialog = new JFileChooser();

                                            saveDialog.setDialogType(JFileChooser.SAVE_DIALOG);
                                            saveDialog.setSelectedFile(new File("file.shcmd"));
                                            saveDialog.setFileFilter(new FileNameExtensionFilter("SHCMData File", "shcmd"));
                                            saveDialog.setMultiSelectionEnabled(false);

                                            if(saveDialog.showSaveDialog(JFrameSHCMDataEditor.this) == JFileChooser.APPROVE_OPTION) {
                                                if(saveDialog.getSelectedFile().getName().contains("."))
                                                    saveDialog.setSelectedFile(new File(saveDialog.getSelectedFile().getAbsolutePath() + ".shcmd"));
                                                if(saveDialog.getSelectedFile().exists()) {
                                                    if (JOptionPane.showConfirmDialog(null, "Do you want to replace the existing file?", "SHCMData Editor - Idiot Proof Protocol", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
                                                        this.actionPerformed(e);
                                                        return;
                                                    }
                                                }
                                                openFileHandler.saveAs(saveDialog.getSelectedFile());
                                            }
                                        }
                                    }
                                }
                                openFileHandler = new OpenFileHandler(new DataBlock());
                            }
                        } else {
                            openFileHandler = new OpenFileHandler(new DataBlock());
                        }
                        refresh();
                    }
                }) {{setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));}});
                JMenuItem itemOpen;
                this.add(itemOpen = new JMenuItem(new AbstractAction("Open") {
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
                                        if(optionSave == 0) {
                                            if(!openFileHandler.save()) {
                                                JFileChooser saveDialog = new JFileChooser();

                                                saveDialog.setDialogType(JFileChooser.SAVE_DIALOG);
                                                saveDialog.setSelectedFile(new File("file.shcmd"));
                                                saveDialog.setFileFilter(new FileNameExtensionFilter("SHCMData File", "shcmd"));
                                                saveDialog.setMultiSelectionEnabled(false);

                                                if(saveDialog.showSaveDialog(JFrameSHCMDataEditor.this) == JFileChooser.APPROVE_OPTION) {
                                                    if(saveDialog.getSelectedFile().getName().contains("."))
                                                        saveDialog.setSelectedFile(new File(saveDialog.getSelectedFile().getAbsolutePath() + ".shcmd"));
                                                    if(saveDialog.getSelectedFile().exists()) {
                                                        if (JOptionPane.showConfirmDialog(null, "Do you want to replace the existing file?", "SHCMData Editor - Idiot Proof Protocol", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
                                                            this.actionPerformed(e);
                                                            return;
                                                        }
                                                    }
                                                    openFileHandler.saveAs(saveDialog.getSelectedFile());
                                                }
                                            }
                                        }
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
                JMenuItem itemReload;
                this.add(itemReload = new JMenuItem(new AbstractAction("Reload") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(openFileHandler != null) {
                            openFileHandler.reload();
                            refresh();
                        }
                    }
                }) {{setAccelerator(KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));}});
                JMenuItem itemSave;
                this.add(itemSave = new JMenuItem(new AbstractAction("Save") {
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
                JMenuItem itemSaveAs;
                this.add(itemSaveAs = new JMenuItem(new AbstractAction("Save As") {
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
                        itemReload.setEnabled(openFileHandler != null && openFileHandler.getFile() != null);
                        itemSave.setEnabled(openFileHandler != null);
                        itemSaveAs.setEnabled(openFileHandler != null);
                    }

                    @Override
                    public void menuDeselected(MenuEvent e) {}
                    @Override
                    public void menuCanceled(MenuEvent e) {}
                });
            }});
            menu.add(new JMenu("Edit") {{
                this.add(new JMenuItem(new AbstractAction("Delete") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(openFileHandler != null && valueTree != null) {
                            TreePath[] selectionPaths = valueTree.getSelectionPaths();
                            if(selectionPaths != null) {
                                for (TreePath selectedTreePath : selectionPaths) {
                                    DataEntry dataEntry = (DataEntry) selectedTreePath.getLastPathComponent();
                                    if(dataEntry != null)
                                        dataEntry.delete();
                                }


                                openFileHandler.changed = true;
                                refresh();
                            }
                        }
                    }
                }) {{setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));}});
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
            valueTree = new JTree();

            valueTree.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = valueTree.getRowForLocation(e.getX(),e.getY());
                    if(row == -1)
                        valueTree.clearSelection();
                    else if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                        valueTree.setSelectionRow(row);
                        new EditEntryDialog(JFrameSHCMDataEditor.this, (DataEntry) valueTree.getPathForRow(row).getLastPathComponent()).setVisible(true);
                    } else if(SwingUtilities.isRightMouseButton(e)) {
                        DataEntry dataEntry = (DataEntry) valueTree.getPathForRow(row).getLastPathComponent();
                        valueTree.setSelectionRow(row);
                        new JPopupMenu() {{
                            if(dataEntry.canHaveChildren)
                                add(new JMenuItem(new AbstractAction("Add") {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        new EditEntryDialog(JFrameSHCMDataEditor.this, new DummyDataEntry(dataEntry)) {

                                        }.setVisible(true);
                                    }
                                }));
                            if(dataEntry.parent != null) {
                                add(new JMenuItem(new AbstractAction("Edit") {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        new EditEntryDialog(JFrameSHCMDataEditor.this, dataEntry).setVisible(true);
                                    }
                                }));
                                add(new JMenuItem(new AbstractAction("Delete") {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        dataEntry.delete();
                                        openFileHandler.changed = true;
                                        refresh();
                                    }
                                }));
                            }
                        }}.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            });

            JScrollPane scrollPane = new JScrollPane(valueTree);
            scrollPane.setViewportView(valueTree);

            valueTree.addTreeExpansionListener(new TreeExpansionListener() {
                @Override
                public void treeExpanded(TreeExpansionEvent event) {
                    openedPaths.add(((DataEntry)event.getPath().getLastPathComponent()).fullPath);
                }

                @Override
                public void treeCollapsed(TreeExpansionEvent event) {
                    openedPaths.remove(((DataEntry)event.getPath().getLastPathComponent()).fullPath);
                }
            });

            valueTree.setCellRenderer(new DataCellRenderer());
            valueTree.setToggleClickCount(0);

            add(scrollPane);
            scrollPane.requestFocus();
        }

        refresh();
    }

    public void refresh() {
        if(openFileHandler == null) {
            this.setTitle("SHCMData Editor");
            this.valueTree.setEnabled(false);
            this.openedPaths.clear();
            this.valueTree.setModel(null);
        } else {
            this.setTitle("SHCMData Editor - " + openFileHandler.getText() + (openFileHandler.changed ? " *" : ""));
            this.valueTree.setEnabled(true);
            this.valueTree.setBackground(openFileHandler.changed ? CHANGED_COLOR : Color.WHITE);

            this.valueTree.setModel(new DefaultTreeModel(DataEntry.read(null, openFileHandler.getText(), openFileHandler.data, this), true));

            this.openedPaths.add(((DataEntry) this.valueTree.getModel().getRoot()).fullPath);

            this.expandPreviouslyExpanded((DataEntry) this.valueTree.getModel().getRoot());
        }
    }

    private void expandPreviouslyExpanded(DataEntry dataEntry) {
        if(!this.openedPaths.contains(dataEntry.fullPath))
            return;

        this.valueTree.expandPath(new TreePath(dataEntry.getPath()));
        for (int i = 0; i < dataEntry.getChildCount(); i++)
            expandPreviouslyExpanded((DataEntry) dataEntry.getChildAt(i));
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
