package shcm.shsupercm.data.editor.gui;

import shcm.shsupercm.data.editor.management.DataEntry;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class DataCellRenderer implements TreeCellRenderer {
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        DataEntry dataEntry = (DataEntry) value;
        String stringValue = " " + dataEntry.keyString + (expanded ? "" : (": " + dataEntry.valueString));

        JPanel item = new JPanel();
        item.setLayout(new BoxLayout(item, BoxLayout.X_AXIS));

        item.setBackground(sel ? new Color(0x5E6EFF) : new Color(0x0FFFFFF, true));

        if(dataEntry.dataIcon1 != null)
            item.add(new JLabel(dataEntry.dataIcon1.icon));
        if(dataEntry.dataIcon2 != null)
            item.add(new JLabel(dataEntry.dataIcon2.icon));
        item.add(new JLabel(stringValue));

        return item;
    }
}
