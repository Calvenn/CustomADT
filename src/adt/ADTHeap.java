/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adt;
import java.util.Arrays;
/**
 *
 * @author calve
 * @param <E> any comparable type
 */
public class ADTHeap<E extends Comparable<E>> implements HeapInterface<E> {
    private static final int DEFAULT_SIZE = 20;
    private E[] heap;
    private int size;
    
    public ADTHeap(){
        this.heap = (E[]) new Comparable[DEFAULT_SIZE];
        this.size = 0;
    }
    
    @Override
    public void insert(E data){
        ensureSize();
        heap[size] = data;
        heapUp(size);
        size++;
    }
    
    @Override
    public E extractRoot(){
        if(isEmpty()) return null;
        E root = heap[0];
        heap[0] = heap[size - 1];
        heap[size -1] = null;
        size--;
        heapDown(0);
        return root;
    }
    
    @Override
    public E peekRoot() {
        return isEmpty() ? null : heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void display() {
        for (int i = 0; i < size; i++) {
            System.out.println(heap[i]);
        }
    }
    
    @Override
    public boolean update(E oldData, E newData) {
        for (int i = 0; i < size; i++) {
            if (heap[i].equals(oldData)) {
                heap[i] = newData;
                heapUp(i);
                heapDown(i);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean remove(E data){
        int index = -1;
        for(int i=0; i< size; i++){
            if(heap.equals(data)){
                index = i;
                break;
            }
        }
        
        if(index == -1) return false;
        
        heap[index] = heap[size-1]; //assign removed index position = last index
        heap[size-1] = null; //assign the last element to null
        size--;
        
        heapDown(index);
        heapUp(index);
        return true;
    }
    
    /****************helper function**************/
    private void ensureSize(){
        if(size == heap.length){
            heap = Arrays.copyOf(heap, heap.length * 2);
        }
    }
    
    // to insert data to heap
    private void heapUp(int index) {
        int child = index;
        while (child > 0) {
            int parent = (child - 1) / 2; //apply formula of floor(i/2)
            if (heap[child].compareTo(heap[parent]) > 0) {
                swap(child, parent);
                child = parent;
            } else {
                break;
            }
        }
    }

    // to rearrange data 
    private void heapDown(int index) {
        int parent = index;
        while (true) {
            int left  = 2 * parent + 1; // to locate the number of position
            int right = 2 * parent + 2;
            int largest = parent;

            if (left < size && heap[left].compareTo(heap[largest]) > 0) {
                largest = left;
            }
            if (right < size && heap[right].compareTo(heap[largest]) > 0) {
                largest = right;
            }
            if (largest != parent) {
                swap(parent, largest);
                parent = largest;
            } else {
                break;
            }
        }
    }

    // Swap two elements 
    private void swap(int i, int j) {
        E tmp    = heap[i];
        heap[i]  = heap[j];
        heap[j]  = tmp;
    }
}
