package inf112.skeleton.app;

/**
 * Represents all items on the board, like walls, holes, power-ups
 * and robots
 */
public interface IItem extends Comparable<IItem> {
    default int compareTo(IItem other) {
        return Integer.compare(getSize(), other.getSize());
    }

    /**
     * 'Might' be used to choose layer in which the item is drawn
     * @return undefined
     */
    int getSize();

    /**
     * Identifies the item type
     * @return String used for identification
     */
    String getName();
}
