/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package adt;

/**
 * @param <E> a type that implements Comparable
 * @author calve
 */
public interface HeapInterface<E extends Comparable<E>>{
    
    //@param data the element to insert
    void insert(E data);
    
    //@return the extracted root element
    E extractRoot();
    
    //@return the root element without removing it
    E peekRoot();
    
    //@return true If the heap is empty
    boolean isEmpty();
    
    //@return the size of the heap
    int size();
    
    //Display the contents of heap
    void display();
    
    //@param oldData that want to replace with newData
    //@return whether data sucessfully update
    boolean update(E oldData, E newData);
    
    //@param data to remove
    //@return whether data sucessfully delete
    boolean remove(E data);
    
    //@param data to be find
    //@return whether data is found
    boolean contains(E data);
    
}
