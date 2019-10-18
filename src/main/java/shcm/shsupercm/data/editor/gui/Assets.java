package shcm.shsupercm.data.editor.gui;

import shcm.shsupercm.data.framework.DataBlock;
import shcm.shsupercm.data.framework.DataKeyedBlock;

import javax.swing.*;

public class Assets {
    public static final ImageIcon LOGO = new ImageIcon(Assets.class.getResource("/logo.png"));

    public enum DataIcon {
        DATAKEYEDBLOCK(new ImageIcon(Assets.class.getResource("/icons/data_types/datakeyedblock.png"))),
        DATABLOCK(new ImageIcon(Assets.class.getResource("/icons/data_types/datablock.png"))),
        ARRAY(new ImageIcon(Assets.class.getResource("/icons/data_types/array.png"))),
        BOOLEAN(new ImageIcon(Assets.class.getResource("/icons/data_types/boolean.png"))),
        STRING(new ImageIcon(Assets.class.getResource("/icons/data_types/string.png"))),
        CHARACTER(new ImageIcon(Assets.class.getResource("/icons/data_types/character.png"))),
        BYTE(new ImageIcon(Assets.class.getResource("/icons/data_types/byte.png"))),
        SHORT(new ImageIcon(Assets.class.getResource("/icons/data_types/short.png"))),
        INTEGER(new ImageIcon(Assets.class.getResource("/icons/data_types/integer.png"))),
        FLOAT(new ImageIcon(Assets.class.getResource("/icons/data_types/float.png"))),
        DOUBLE(new ImageIcon(Assets.class.getResource("/icons/data_types/double.png"))),
        LONG(new ImageIcon(Assets.class.getResource("/icons/data_types/long.png")));

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