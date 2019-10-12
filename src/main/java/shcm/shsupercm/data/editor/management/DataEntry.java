package shcm.shsupercm.data.editor.management;

import shcm.shsupercm.data.editor.gui.Assets;
import shcm.shsupercm.data.editor.gui.JFrameSHCMDataEditor;
import shcm.shsupercm.data.framework.DataBlock;
import shcm.shsupercm.data.framework.DataKeyedBlock;
import shcm.shsupercm.data.utils.DataStringConversion;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.lang.reflect.Array;

public class DataEntry extends DefaultMutableTreeNode {
    public String fullPath;
    public DataEntry parent;
    public Object key;
    public Object value;

    public String keyString;
    public String valueString;

    public final DataIcon dataIconType;
    public final DataIcon dataIconSubType;
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

        this.keyString = key == null ? "" : ((parent != null && parent.value != null && parent.value.getClass().isArray()) ? key.toString() : DataStringConversion.toString(key));
        this.valueString = DataStringConversion.toString(value);

        dataIconType = DataIcon.getFor(value.getClass());

        if(value.getClass().isArray())
            dataIconSubType = DataIcon.getFor(value.getClass().getComponentType());
        else if(value instanceof DataKeyedBlock && ((DataKeyedBlock)value).keyType != null)
            dataIconSubType = DataIcon.getFor(((DataKeyedBlock)value).keyType);
        else
            dataIconSubType = null;

        StringBuilder fullPath = new StringBuilder(keyString);
        DataEntry p = parent;

        while(p != null) {
            fullPath.insert(0, p.keyString + '.');
            p = p.parent;
        }

        this.fullPath = fullPath.toString();
    }

    @Override
    public boolean getAllowsChildren() {
        return this.canHaveChildren;
    }

    //Constructs but also reads recursively children nodes
    public static DataEntry read(DataEntry parent, Object key, Object value, JFrameSHCMDataEditor frame) {
        if(value == null)
            return null;

        DataEntry node = new DataEntry(parent, key, value);

        if(value.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(value); i++)
                node.add(read(node, i, Array.get(value, i), frame));
        } else if(value instanceof DataKeyedBlock) {
            for (Object subKey : ((DataKeyedBlock) value).getKeys())
                //noinspection unchecked
                node.add(read(node, subKey, ((DataKeyedBlock) value).get(subKey), frame));
        }

        return node;
    }


    public void delete() {
        if(parent == null) return;
        if(parent.value instanceof DataKeyedBlock) {//noinspection unchecked
            ((DataKeyedBlock) parent.value).remove(key);
        } else if(parent.value.getClass().isArray()) {
            int originalLength = Array.getLength(parent.value);
            Object newArray = Array.newInstance(parent.value.getClass().getComponentType(), originalLength - 1);

            for (int i = 0; i < originalLength; i++) {
                DataEntry childAtI = (DataEntry) parent.getChildAt(i);

                if(i < (Integer)key) {
                    Array.set(newArray, i, childAtI.value);
                } else if(i > (Integer)key) {
                    Array.set(newArray, i-1, childAtI.value);
                }
            }

            parent.setValue(newArray);
        }
    }

    private void setValue(Object newVal) {
        if(parent == null)
            return;
        if(newVal == null)
            delete();
        else {
            if (parent.value instanceof DataKeyedBlock) {
                //noinspection unchecked
                ((DataKeyedBlock) parent.value).set(key, newVal);
            } else if (parent.value.getClass().isArray()) {
                Array.set(parent, (Integer) key, newVal);
            }
        }
    }

    private void setNewKey(Object newKey) {

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
            if(value == boolean.class)
                return BOOLEAN;
            if(value == byte.class)
                return BYTE;
            if(value == short.class)
                return SHORT;
            if(value == char.class)
                return CHARACTER;
            if(value == int.class)
                return INTEGER;
            if(value == float.class)
                return FLOAT;
            if(value == long.class)
                return LONG;
            if(value == double.class)
                return DOUBLE;

            if(value.isArray())
                return ARRAY;
            if(value == DataBlock.class)
                return DATABLOCK;
            if(value == DataKeyedBlock.class)
                return DATAKEYEDBLOCK;

            return null;
        }

        public final ImageIcon icon;

        DataIcon(ImageIcon icon) {
            this.icon = icon;
        }
    }
}
