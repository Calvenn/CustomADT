package adt;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

/**
 *
 * @author calve
 */
public interface QueueInterface<E> {
    void enqueue(E data);
    E dequeue();
    E peek();
    void display();
    boolean isEmpty();
    int size();
}
