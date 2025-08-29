/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package adt;

/**
 * References
 * 1.https://www.programiz.com/dsa/heap-data-structure
 * 2.https://www.enjoyalgorithms.com/blog/introduction-to-heap-data-structure
 * @param <E> a type that implements Comparable
 * @author CalvenPhnuahKahHong
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
    
    //@param index of the element
    //@return the element of that index
    E get(int index);
    
    //@param data to be find
    //@return whether data is found
    boolean contains(E data);
    
    //Remove the contents of heap
    void clear();
    
}
