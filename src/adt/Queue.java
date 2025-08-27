package adt;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author calve
 */
public class Queue<E> implements QueueInterface<E> {
    private static final int SIZE = 100;
    private Object[] items;
    private int front;
    private int back;
    private int count;

    public Queue() {
        items = new Object[SIZE];
        front = 0;
        back = 0;
        count = 0;
    }

    @Override
    public void enqueue(E data) {
        if (isFull()) {
            System.out.println("Queue is full");
            return;
        }
        items[back] = data;
        back = (back + 1) % SIZE;
        count++;
    }

    @Override
    public E dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty!");
            return null;
        }
        E removed = (E) items[front];
        front = (front + 1) % SIZE;
        count--;
        return removed;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            System.out.println("Queue is empty!");
            return null;
        }
        return (E) items[front];
    }

    @Override
    public void display() {
        if (isEmpty()) {
            System.out.println("Queue is empty!");
            return;
        }
        System.out.println("Queue contents:");
        for (int i = 0; i < count; i++) {
            int index = (front + i) % SIZE;
            System.out.println(items[index]);
        }
    }
    
    public E get(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + count);
        }
        int actualIndex = (front + index) % SIZE;
        return (E) items[actualIndex];
    }

    @Override
    public E remove(E data) {
        if (isEmpty()) {
            return null;
        }

        int i = front;
        for (int j = 0; j < count; j++) {
            if (items[i].equals(data)) {
                // Shift elements left
                for (int k = j; k < count - 1; k++) {
                    int from = (front + k + 1) % SIZE;
                    int to = (front + k) % SIZE;
                    items[to] = items[from];
                }

                // Update back and count
                back = (back - 1 + SIZE) % SIZE;
                items[back] = null;     
                count--;
                return data;
            }
            i = (i + 1) % SIZE;
        }

        return null;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    public boolean isFull() {
        return count == SIZE;
    }
}
