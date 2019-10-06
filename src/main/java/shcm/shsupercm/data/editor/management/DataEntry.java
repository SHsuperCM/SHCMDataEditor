package shcm.shsupercm.data.editor.management;

import shcm.shsupercm.data.editor.gui.Assets;
import shcm.shsupercm.data.framework.DataBlock;
import shcm.shsupercm.data.framework.DataKeyedBlock;
import shcm.shsupercm.data.utils.DataStringConversion;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class DataEntry {
    public final DataEntry parent;
    public final Object key;
    public final Object value;

    public final String keyString;
    public final String valueString;
    public final DataIcon[] dataIcons;
    public final boolean canHaveChildren;

    DataEntry(DataEntry parent, Object key, Object value) {
        this.parent = parent;
        this.key = key;
        this.value = value;

        this.canHaveChildren = !(
                this.value instanceof String ||
                this.value instanceof Boolean ||
                this.value instanceof Byte ||
                this.value instanceof Short ||
                this.value instanceof Character ||
                this.value instanceof Integer ||
                this.value instanceof Float ||
                this.value instanceof Long ||
                this.value instanceof Double);

        this.keyString = key == null ? "" : DataStringConversion.toString(key);
        this.valueString = DataStringConversion.toString(value);

        ArrayList<DataIcon> dataIcons = new ArrayList<>();

        dataIcons.add(DataIcon.getFor(value.getClass()));

        if(value.getClass().isArray()) {
            dataIcons.add(DataIcon.getFor(value.getClass().getComponentType()));
        } else if(value instanceof DataKeyedBlock && ((DataKeyedBlock)value).keyType != null)
            dataIcons.add(DataIcon.getFor(((DataKeyedBlock)value).keyType));

        this.dataIcons = dataIcons.toArray(new DataIcon[0]);
    }

    public static DefaultMutableTreeNode read(DataEntry parent, Object key, Object value) {
        if(value == null)
            return null;

        DataEntry nodeEntry = new DataEntry(parent, key, value);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeEntry) {
            @Override
            public boolean getAllowsChildren() {
                if(!(userObject instanceof DataEntry))
                    return false;
                return ((DataEntry) userObject).canHaveChildren;
            }
        };

        if(value.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(value); i++)
                node.add(read(nodeEntry, i, Array.get(value, i)));
        } else if(value instanceof DataKeyedBlock) {
            for (Object subKey : ((DataKeyedBlock) value).getKeys())
                //noinspection unchecked
                node.add(read(nodeEntry, subKey, ((DataKeyedBlock) value).get(subKey)));
        }

        return node;
    }

    public enum DataIcon {
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

        public static DataIcon getFor(Class<?> value) {
            if(value == String.class)
                return STRING;
            if(value == Boolean.class)
                return BOOLEAN;
            if(value == Byte.class)
                return BYTE;
            if(value == Short.class)
                return SHORT;
            if(value == Character.class)
                return CHARACTER;
            if(value == Integer.class)
                return INTEGER;
            if(value == Float.class)
                return FLOAT;
            if(value == Long.class)
                return LONG;
            if(value == Double.class)
                return DOUBLE;

            if(value.isArray())
                return ARRAY;
            if(value == DataBlock.class)
                return DATABLOCK;
            if(value == DataKeyedBlock.class)
                return DATAKEYEDBLOCK;

            return null;
        }

        public ImageIcon icon;

        DataIcon(ImageIcon icon) {
            this.icon = icon;
        }
    }

    @Override
    public String toString() {
        return keyString + ": " + valueString;
    }
}
