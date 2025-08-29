/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adt;
import java.util.Arrays;
/**
 *
 * @author CalvenPhnuahKahHong
 * @param <E> any comparable type
 */
public class Heap<E extends Comparable<E>> implements HeapInterface<E> {
    private static final int DEFAULT_SIZE = 20;
    private E[] heap;
    private int size;
    private boolean isMaxHeap = true; //Default is max heap
    //To create a min heap: ADTHeap<Integer> maxHeap = new ADTHeap<>(false);
    
    public Heap(boolean isMaxHeap){
        this.heap = (E[]) new Comparable[DEFAULT_SIZE];
        this.size = 0;
        this.isMaxHeap = isMaxHeap;
    }
    
    @Override
    public void insert(E data){
        ensureSize(); // make sure the array size if always enough
        heap[size] = data; 
        heapUp(size); // rearange the order of root area
        size++;
    }
    
    @Override
    public E extractRoot(){
        if(isEmpty()) return null;
        E root = heap[0]; //assign the first element as a TMP
        heap[0] = heap[size - 1]; //the last element is assign to first
        heap[size -1] = null; //last element become null
        size--; // remove the root 
        heapDown(0); // rearrange the order of lower layer
        return root; // return the removed root
    }
    
    @Override
    public E peekRoot() {
        return isEmpty() ? null : heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // true if heap[size] is 0
    }

    @Override
    public int size() {
        return size; // size of heap
    }

    @Override
    public void display() {
        for (int i = 0; i < size; i++) {
            System.out.println(heap[i]); //display element in heap
        }
    }
    
    @Override
    public boolean update(E oldData, E newData) {
        for (int i = 0; i < size; i++) {
            if (heap[i].equals(oldData)) {
                heap[i] = newData; // replace the oldData to newData
                heapUp(i); // update and rearrange the order of root area
                heapDown(i); // update and rearrange the order of element
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
        
        heapDown(index); // update and rearrange the order of root area
        heapUp(index); // update and rearrange the order of element
        return true;
    }
    
    @Override
    public E get(int index) {
        if (index >= 0 && index < size) {
            return heap[index];
        }
        return null;
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
    
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;   // remove 
        }
        size = 0;
    }

    @Override
    public E extractSpecific(E item) {
        int index = -1;

        for (int i = 0; i < size; i++) { //find index of the element
            if (get(i).equals(item)) {
                index = i;
                break;
            }
        }

        if (index == -1)  return null;

        E removed = get(index); //specified item to be extract

        int lastIndex = size - 1;
        swap(index, lastIndex); //swap with last element

        remove(get(lastIndex)); //remove item to be extract

        if (index < size) { //restore original heap
            heapUp(index);
            heapDown(index);
        }
        return removed;
    }
    
    /****************helper function**************/
    private void ensureSize(){
        if(size == heap.length){
            heap = Arrays.copyOf(heap, heap.length * 2); // double the size of array
        }
    }
    
    // to insert data to heap and rearrange (upper layer of heap - root area)
    private void heapUp(int index) {
        int child = index;
        while (child > 0) {
            int parent = (child - 1) / 2; //apply formula of floor(i/2)
            if (shouldSwap(heap[child], heap[parent])) { // check whether parents is greater than child
                swap(child, parent);
                child = parent;
            } else {
                break;
            }
        }
    }

    // to rearrange data (lower layer of heap)
    private void heapDown(int index) {
        int parent = index;
        while (true) {
            int left  = 2 * parent + 1; // to locate the number of position (left element)
            int right = 2 * parent + 2; // to locate the number of position (right element)
            int selected = parent;

            if (left < size && shouldSwap(heap[left], heap[selected])) { 
                selected = left; //swap location if left child element is greater/smaller than parents
            }
            if (right < size && shouldSwap(heap[right], heap[selected])) { 
                selected = right; //swap location if right child element is greater/smaller than parents
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
        //Max Heap: swap if child > parent
        //Min Heap: swap if child < parent
    }

    // Swap two elements 
    private void swap(int i, int j) {
        E tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }
}
