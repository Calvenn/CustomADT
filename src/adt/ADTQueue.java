package adt;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author calve
 */
public class ADTQueue<E> implements QueueInterface<E>{
    private static final int SIZE = 100;
    private Object[] items;
    private int front, back;
    
    public ADTQueue(){
        items = new Object[SIZE];
        front = 0;
        back = 0;
    }
    
    @Override
    public void enqueue(E data){
        if(isFull()){
            System.out.println("Queue is full");
            return;
        }
        items[back++] = data;
    }
    
    @Override
    public E dequeue(){
        if (isEmpty()) {
            System.out.println("Queue is empty!");
            return null;
        }
        return (E) items[front++];
    }
    
    @Override
    public E peek(){
        if (isEmpty()) {
            System.out.println("Queue is empty!");
            return null;
        }
        return (E) items[front];
    }
    
    @Override
    public void display(){
        for(int i = front; i < back; i++){
            System.out.println(items[i]);
        }
    }
    
    @Override
    public int size(){
        return back - front;
    }
    
    @Override
    public boolean isEmpty(){
        return front == back;
    }
    
    /*Helper function*/
    public boolean isFull(){
        return back == SIZE;
    }
}
