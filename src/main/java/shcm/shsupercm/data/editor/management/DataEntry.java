package shcm.shsupercm.data.editor.management;

import shcm.shsupercm.data.editor.gui.Assets;
import shcm.shsupercm.data.framework.DataKeyedBlock;
import shcm.shsupercm.data.utils.DataStringConversion;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.lang.reflect.Array;

public class DataEntry {
    public final DataEntry parent;
    public final Object key;
    public final Object value;

    public final String keyString;
    public final String valueString;
    public final DataEntryType valueType;

    DataEntry(DataEntry parent, Object key, Object value) {
        this.parent = parent;
        this.key = key;
        this.value = value;

        this.keyString = key.toString();
        if(value instanceof Integer)
    }

    public static DefaultMutableTreeNode read(DataEntry parent, Object key, Object value) {
        if(value == null)
            return null;

        DataEntry nodeEntry = new DataEntry(parent, key, value);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeEntry);

        if(value.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(value); i++)
                node.add(read(nodeEntry, i, Array.get(value, i)));
        } else if(value instanceof DataKeyedBlock) {
            for (Object subKey : ((DataKeyedBlock) value).getKeys())
                node.add(read(nodeEntry, subKey, ((DataKeyedBlock) value).get(subKey)));
        }

        return node;
    }

    public enum DataEntryType {
        DATAKEYEDBLOCK(Assets.ICON_DATAKEYEDBLOCK),
        DATABLOCK(Assets.ICON_DATABLOCK),
        ARRAY(Assets.ICON_ARRAY),
        BOOLEAN(Assets.ICON_BOOLEAN),
        STRING(Assets.ICON_STRING),
        CHARACTER(Assets.ICON_CHARACTER),
        BYTE(Assets.ICON_BYTE),
        SHORT(Assets.ICON_SHORT),
        INTEGER(Assets.ICON_INTEGER),
        FLOAT(Assets.ICON_FLOAT),
        DOUBLE(Assets.ICON_DOUBLE),
        LONG(Assets.ICON_LONG);

        public ImageIcon icon;

        DataEntryType(ImageIcon icon) {
            this.icon = icon;
        }
    }
}
