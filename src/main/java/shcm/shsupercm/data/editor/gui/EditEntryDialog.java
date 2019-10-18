package shcm.shsupercm.data.editor.gui;

import shcm.shsupercm.data.editor.management.DataEntry;
import shcm.shsupercm.data.utils.DataStringConversion;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EditEntryDialog extends JDialog {
    public static final Color CHANGED_COLOR = new Color(0xC8FFBB);
    public static final Color MALFORMED_COLOR = new Color(0xFFB3B3);

    public final DataEntry dataEntry;
    public JTextField keyTextField;
    public JTextField valueTextField;

    public Object newKey = null;
    public Object newValue = null;

    public EditEntryDialog(JFrameSHCMDataEditor owner, DataEntry entry) {
        super(owner);
        this.dataEntry = entry;
        if (entry == null)
            return;

        this.setTitle("Edit entry");
        this.setModal(true);
        this.setResizable(false);
        this.setSize(512, 280);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        if (dataEntry.dataIconType != null) {
            JPanel icons = new JPanel();
            icons.setAlignmentX(LEFT_ALIGNMENT);
            icons.setLayout(new BoxLayout(icons, BoxLayout.X_AXIS));

            icons.add(new JLabel(dataEntry.dataIconType.icon));

            if (dataEntry.dataIconSubType != null)
                icons.add(new JLabel(dataEntry.dataIconSubType.icon));

            add(icons);
            add(Box.createVerticalStrut(11));
        }
        {
            JTextField fullPath;
            JScrollPane jsp = new JScrollPane(fullPath = new JTextField(dataEntry.fullPath));
            fullPath.setAlignmentX(LEFT_ALIGNMENT);
            jsp.setAlignmentX(LEFT_ALIGNMENT);
            jsp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 39));
            jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            fullPath.setEditable(false);
            add(jsp);
        }
        add(Box.createVerticalStrut(28));
        //todo handle entry validity(int key in array children, typed keys in datakeyedblocks, array types, etc..)
        if (dataEntry.parent != null) {
            this.add(new JLabel(" Key: ", SwingConstants.LEFT) {{
                setAlignmentX(LEFT_ALIGNMENT);
            }});
            add(Box.createVerticalStrut(3));

            keyTextField = new JTextField(dataEntry.keyString);
            keyTextField.setAlignmentX(LEFT_ALIGNMENT);
            keyTextField.setMaximumSize(new Dimension(512, 23));
            keyTextField.setHorizontalAlignment(SwingConstants.LEFT);
            keyTextField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    keyTextField.setBackground(keyTextField.getText().equals(dataEntry.keyString) ? Color.WHITE : CHANGED_COLOR);
                    try {
                        newKey = DataStringConversion.fromString(keyTextField.getText());
                    } catch (Exception ex) {
                        keyTextField.setBackground(MALFORMED_COLOR);
                        newKey = null;
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    keyTextField.setBackground(keyTextField.getText().equals(dataEntry.keyString) ? Color.WHITE : CHANGED_COLOR);
                    try {
                        newKey = DataStringConversion.fromString(keyTextField.getText());
                    } catch (Exception ex) {
                        keyTextField.setBackground(MALFORMED_COLOR);
                        newKey = null;
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    keyTextField.setBackground(keyTextField.getText().equals(dataEntry.keyString) ? Color.WHITE : CHANGED_COLOR);
                    try {
                        newKey = DataStringConversion.fromString(keyTextField.getText());
                    } catch (Exception ex) {
                        keyTextField.setBackground(MALFORMED_COLOR);
                        newKey = null;
                    }
                }
            });

            this.add(keyTextField);
            add(Box.createVerticalStrut(15));
        }
        {
            this.add(new JLabel(" Value: ", SwingConstants.LEFT) {{
                setAlignmentX(LEFT_ALIGNMENT);
            }});
            add(Box.createVerticalStrut(3));

            valueTextField = new JTextField(dataEntry.valueString);
            valueTextField.setAlignmentX(LEFT_ALIGNMENT);
            valueTextField.setMaximumSize(new Dimension(512, 23));
            valueTextField.setHorizontalAlignment(SwingConstants.LEFT);
            valueTextField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    valueTextField.setBackground(valueTextField.getText().equals(dataEntry.valueString) ? Color.WHITE : CHANGED_COLOR);
                    try {
                        newValue = DataStringConversion.fromString(valueTextField.getText());
                    } catch (Exception ex) {
                        valueTextField.setBackground(MALFORMED_COLOR);
                        newValue = null;
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    valueTextField.setBackground(valueTextField.getText().equals(dataEntry.valueString) ? Color.WHITE : CHANGED_COLOR);
                    try {
                        newValue = DataStringConversion.fromString(valueTextField.getText());
                    } catch (Exception ex) {
                        valueTextField.setBackground(MALFORMED_COLOR);
                        newValue = null;
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    valueTextField.setBackground(valueTextField.getText().equals(dataEntry.valueString) ? Color.WHITE : CHANGED_COLOR);
                    try {
                        newValue = DataStringConversion.fromString(valueTextField.getText());
                    } catch (Exception ex) {
                        valueTextField.setBackground(MALFORMED_COLOR);
                        newValue = null;
                    }
                }
            });

            this.add(valueTextField);
        }
        {
            add(Box.createVerticalStrut(23));
            JPanel buttonPanel = new JPanel();
            buttonPanel.setAlignmentX(LEFT_ALIGNMENT);

            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));

            buttonPanel.add(Box.createHorizontalGlue());
            buttonPanel.add(new JButton(new AbstractAction("Cancel") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    EditEntryDialog.this.setVisible(false);
                    EditEntryDialog.this.dispose();
                }
            }) {{
                this.setMaximumSize(new Dimension(70, 30));
            }});
            buttonPanel.add(Box.createHorizontalGlue());
            buttonPanel.add(new JButton(new AbstractAction("Apply") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (newValue != null)
                        dataEntry.setValue(newValue);
                    if (newKey != null)
                        dataEntry.setNewKey(newKey);

                    owner.openFileHandler.changed = true;
                    owner.refresh();
                    EditEntryDialog.this.setVisible(false);
                    EditEntryDialog.this.dispose();
                }
            }) {{
                this.setMaximumSize(new Dimension(70, 30));
            }});
            buttonPanel.add(Box.createHorizontalGlue());
            add(buttonPanel);
        }

        getRootPane().registerKeyboardAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditEntryDialog.this.setVisible(false);
                EditEntryDialog.this.dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (newValue != null)
                    dataEntry.setValue(newValue);
                if (newKey != null)
                    dataEntry.setNewKey(newKey);

                owner.openFileHandler.changed = true;
                owner.refresh();
                EditEntryDialog.this.setVisible(false);
                EditEntryDialog.this.dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
}
