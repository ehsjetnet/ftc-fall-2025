package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A collection of fixed maximum size to which elements may always be added.
 * If the maximum size is reached, adding an element overwrites the oldest in the collection.
 */
public final class CircularBuffer<E> implements Collection<E> {
    /**
     * The maximum size the buffer will expand to before overwriting old elements.
     */
    private final int maxSize;

    /**
     * The buffer that stores elements.
     */
    private final ArrayList<E> buffer;

    /**
     * The next position to write to in the buffer.
     */
    private int idx;

    /**
     * Constructs a CircularBuffer.
     *
     * @param maxSize - the maximum size the buffer will expand to before overwriting old elements.
     */
    public CircularBuffer(int maxSize) {
        this.maxSize = maxSize;
        buffer = new ArrayList<>(maxSize);
        idx = 0;
    }

    @Override
    public boolean add(E e) {
        if (buffer.size() < maxSize) {
            buffer.add(e);
        } else {
            buffer.set(idx, e);
            idx = (idx + 1) % maxSize;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        c.forEach(this::add);
        return true;
    }

    @Override
    public void clear() {
        buffer.clear();
        idx = 0;
    }

    @Override
    public boolean contains(Object o) {
        return buffer.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return buffer.containsAll(c);
    }

    @Override
    public boolean isEmpty() {
        return buffer.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        // Mask Iterator.remove
        Iterator<E> iter = buffer.iterator();
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public E next() {
                return iter.next();
            }
        };
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return buffer.size();
    }

    @Override
    public Object[] toArray() {
        return buffer.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return buffer.toArray(a);
    }
}
