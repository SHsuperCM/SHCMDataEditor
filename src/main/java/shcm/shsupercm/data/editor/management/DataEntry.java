package shcm.shsupercm.data.editor.management;

import shcm.shsupercm.data.editor.gui.Assets;
import shcm.shsupercm.data.editor.gui.JFrameSHCMDataEditor;
import shcm.shsupercm.data.framework.DataBlock;
import shcm.shsupercm.data.framework.DataKeyedBlock;
import shcm.shsupercm.data.utils.DataStringConversion;

import javax.swing.tree.DefaultMutableTreeNode;
import java.lang.reflect.Array;

public class DataEntry extends DefaultMutableTreeNode {
    public String fullPath;
    public DataEntry parent;
    public Object key;
    public Object value;

    public String keyString;
    public String valueString;

    public Assets.DataIcon dataIconType;
    public Assets.DataIcon dataIconSubType;
    public boolean canHaveChildren;

    protected DataEntry() {

    }

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

        dataIconType = Assets.DataIcon.getFor(value.getClass());

        if(value.getClass().isArray())
            dataIconSubType = Assets.DataIcon.getFor(value.getClass().getComponentType());
        else if(value instanceof DataKeyedBlock && ((DataKeyedBlock)value).keyType != null && ((DataKeyedBlock)value).keyType != String.class)
            dataIconSubType = Assets.DataIcon.getFor(((DataKeyedBlock)value).keyType);
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

    //Constructs but also reads children nodes recursively
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

    @SuppressWarnings("unchecked")
    public void setValue(Object newVal) {
        if(newVal == null || parent == null)
            delete();
        else {
            if (parent.value instanceof DataKeyedBlock) {
                ((DataKeyedBlock) parent.value).set(key, newVal);
            } else if (parent.value.getClass().isArray()) {
                Array.set(parent.value, (Integer) key, newVal);
            }
        }
        this.value = newVal;
    }

    @SuppressWarnings("unchecked")
    public void setNewKey(Object newKey) {
        if(parent == null || newKey == null)
            return;

        if (parent.value instanceof DataKeyedBlock) {
            ((DataKeyedBlock) parent.value).set(newKey, this.value);
            ((DataKeyedBlock) parent.value).remove(key);
        } else if (parent.value.getClass().isArray()) {
            MiscUtils.moveItemInArray(parent.value, (Integer)key, (Integer)newKey);
        }
        this.key = newKey;
    }

    public void addChild(Object newKey, Object newVal) {
        if(newVal == null)
            return;

        Object k = newKey;

        if (value instanceof DataKeyedBlock) {
            if(!((DataKeyedBlock)value).isCorrectKeyType(k))
                return;

            //noinspection unchecked
            ((DataKeyedBlock) value).set(k, newVal);
        } else if (value.getClass().isArray()) {
            if(!(k instanceof Integer))
                return;

            int originalLength = Array.getLength(value);
            int index = (int) k;
            if(index < 0)
                index = originalLength;

            Object newArray = Array.newInstance(value.getClass().getComponentType(), originalLength + 1);
            boolean inserted = false;
            for (int i = 0; i < originalLength + 1; i++) {
                if(!inserted) {
                    if(i == index) {
                        Array.set(newArray, i, newVal);
                        inserted = true;
                    } else
                        Array.set(newArray, i, Array.get(value, i));
                } else
                    Array.set(newArray, i, Array.get(value, i - 1));
            }

            setValue(newArray);
        }
    }
}
