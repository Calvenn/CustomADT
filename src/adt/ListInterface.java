package adt;

public interface ListInterface<E> {
    boolean add(E data);
    
    boolean add(int position, E data);
    
    E remove(int position);
    
    E getEntry(int position);
    
    boolean replace(int position, E data);
    
    int size();
    
    boolean isEmpty();
}
