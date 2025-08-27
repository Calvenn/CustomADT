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
public class Heap<E extends Comparable<E>> implements HeapInterface<E> {
    private static final int DEFAULT_SIZE = 20;
    private E[] heap;
    private int size;
    private boolean isMaxHeap = true; //Default is max heap
    //to use min heap init like this -> ADTHeap<Integer> maxHeap = new ADTHeap<>(false);
    
    public Heap(boolean isMaxHeap){
        this.heap = (E[]) new Comparable[DEFAULT_SIZE];
        this.size = 0;
        this.isMaxHeap = isMaxHeap;
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
        E root = heap[0]; //assign the first element as a TMP
        heap[0] = heap[size - 1]; //the last element is assign to first
        heap[size -1] = null; //last element become null
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
            if(heap[i].equals(data)){
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
    
    @Override
    public boolean contains(E data){
        for(int i=0; i<size; i++){
            if(heap[i].equals(data)){
                return true;
            }
        }
        return false;
    }
    
    /****************helper function**************/
    private void ensureSize(){
        if(size == heap.length){
            heap = Arrays.copyOf(heap, heap.length * 2);
        }
    }
    
    // to insert data to heap and rearrange
    private void heapUp(int index) {
        int child = index;
        while (child > 0) {
            int parent = (child - 1) / 2; //apply formula of floor(i/2)
            if (shouldSwap(heap[child], heap[parent])) {
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
            int selected = parent;

            if (left < size && shouldSwap(heap[left], heap[selected])) {
                selected = left;
            }
            if (right < size && shouldSwap(heap[right], heap[selected])) {
                selected = right;
            }
            if (selected != parent) {
                swap(parent, selected);
                parent = selected;
            } else {
                break;
            }
        }
    }
    
    private boolean shouldSwap(E a, E b) {
        if (a == null || b == null)return false;
        return isMaxHeap ? a.compareTo(b) > 0 : a.compareTo(b) < 0;
    }

    // Swap two elements 
    private void swap(int i, int j) {
        E tmp    = heap[i];
        heap[i]  = heap[j];
        heap[j]  = tmp;
    }
    
    public E get(int index) {
        if (index >= 0 && index < size) {
            return heap[index];
        }
        return null;
    }
}
