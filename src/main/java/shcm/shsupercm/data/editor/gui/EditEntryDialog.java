package shcm.shsupercm.data.editor.gui;

import shcm.shsupercm.data.editor.management.DataEntry;

import javax.swing.*;
import java.awt.*;

public class EditEntryDialog extends JDialog {
    public final DataEntry dataEntry;

    public EditEntryDialog(Frame owner, DataEntry entry) {
        super(owner);
        this.dataEntry = entry;

        this.setResizable(false);
        this.setSize(256,128);
    }
}
