package adt;

/**
 * 
 * @author Cheang Wei Ting
 */

public interface ListInterface<E> {
    boolean add(E data);
    
    boolean add(int position, E data);
    
    E remove(int position);
    
    E get(int position);
    
    boolean replace(int position, E data);
    
    boolean contains(E data);
    
    int size();
    
    boolean isEmpty();
}

