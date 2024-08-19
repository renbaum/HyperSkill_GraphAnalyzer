package visualizer;

import java.util.ArrayDeque;

public class FixedSizeQueue<E> extends ArrayDeque<E> {
    private int maxSize;

    public FixedSizeQueue(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(E e) {
        if (size() == maxSize) {
            // Remove element at the head of the queue when the maximum size is reached
            removeFirst();
        }
        return super.add(e);
    }

}
