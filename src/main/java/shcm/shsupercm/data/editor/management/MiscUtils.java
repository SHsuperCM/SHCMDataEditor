package shcm.shsupercm.data.editor.management;

import java.lang.reflect.Array;

/**
 * MiscUtils was made by SHsuperCM and is under the WTFPL-2.0 licence.( https://tldrlegal.com/license/do-wtf-you-want-to-public-license-v2-(wtfpl-2.0) )<br>
 * Feel free to use anywhere and without credit.
 */
public class MiscUtils {
    /**
     * Moves an item in a list from one place to another and shifts surrounding items to fill the empty space.
     *
     * @param array The array to modify.
     * @param index The index of the item that'll be moved.
     * @param newIndex The new index to put the item in.
     */
    public static void moveItemInArray(Object array, int index, int newIndex) {
        if (!array.getClass().isArray() || index == newIndex)
            return;
        int arrLength = Array.getLength(array);
        if(index > arrLength-1 || newIndex > arrLength-1)
            throw new IndexOutOfBoundsException();

        Object item = Array.get(array, index);

        if(index < newIndex) {
            // Shimmy items between index(exclusive) and newIndex(inclusive) one index down.
            for(int i = index + 1; i <= newIndex; i++)
                Array.set(array, i - 1, Array.get(array, i));
        } else {
            // Shimmy items between index(exclusive) and newIndex(inclusive) one index up.
            for(int i = index - 1; i >= newIndex; i--)
                Array.set(array, i + 1, Array.get(array, i));
        }

        Array.set(array, newIndex, item);
    }
}
