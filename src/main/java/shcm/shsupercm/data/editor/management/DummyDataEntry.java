package shcm.shsupercm.data.editor.management;

public class DummyDataEntry extends DataEntry {
    public DummyDataEntry(DataEntry parent) {
        this.parent = parent;
        this.key = null;
        this.value = null;


        this.canHaveChildren = false;

        this.keyString = "";
        this.valueString = "";

        this.dataIconType = null;
        this.dataIconSubType = null;

        this.fullPath = parent.fullPath + ".NEW";
    }

    @Override
    public void delete() {
    }

    @Override
    public void setValue(Object newVal) {
    }

    @Override
    public void setNewKey(Object newKey) {
    }

    @Override
    public void addChild(Object newKey, Object newVal) {
    }
}
