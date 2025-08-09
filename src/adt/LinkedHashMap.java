package adt;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author calve
 */
public class LinkedHashMap<K,V> implements LinkedHashMapInterface<K,V>{
    private static final int DEFAULT_CAPACITY = 100;
    private Node<K, V>[] buckets;
    private int size;
    
    private Node<K, V> head;  // first inserted node
    private Node<K, V> tail;  // most recently inserted node
    
    class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev, next; 
        Node<K, V> prevInOrder;   
        Node<K, V> nextInOrder;


        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public LinkedHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
        size = 0;
    }
    
    private int getIndex(K key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> current = buckets[index];

        // check if key exists in chain
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value; // if not exist then store the value in bucket with key
                return;
            }
            current = current.next; // otherwise go to the next bucket
        }

        // when still not found then insert new
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = buckets[index]; // link to the current node
        buckets[index] = newNode; // insert to front
        size++;

        // update head and tail position
        if (head == null) { 
            head = tail = newNode; // when it is first value then set head & tail = new node
        } else { // update the buckets when not the first value
            tail.nextInOrder = newNode; // next order = new added node (which is a tail)
            newNode.prevInOrder = tail; // previous tail = the prev node of new node
            tail = newNode; // to update the tail object with the new node
        }
    }

    @Override
    public V get(K key) {
        int index = getIndex(key);
        Node<K, V> current = buckets[index];

        while (current != null) {
            if (current.key.equals(key)) return current.value;
            current = current.next;
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public V remove(K key) {
        int index = getIndex(key);
        Node<K, V> current = buckets[index];
        Node<K, V> prev = null; // keep track previous node

        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    buckets[index] = current.next; // remove head node
                } else {
                    prev.next = current.next; // by pass the current node
                }
                // also update insertion order list
                if (current.prevInOrder != null)
                    current.prevInOrder.nextInOrder = current.nextInOrder; // update next in order
                if (current.nextInOrder != null)
                    current.nextInOrder.prevInOrder = current.prevInOrder; // update previous in order
                if (current == head) head = current.nextInOrder; // update if removed node is head
                if (current == tail) tail = current.prevInOrder; // update if removed node is tail

                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }

    @Override
    public void clear() {
        size = 0;
        head = tail = null;
        buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void display() {
        Node<K, V> current = head;
        while (current != null) {
            System.out.println(current.key + " -> " + current.value);
            current = current.nextInOrder;
        }
    }
    
    @Override
    // Return an array of keys in insertion order
    public Object[] getKeys() {
        Object[] keys = new Object[size];
        Node<K, V> current = head;
        int i = 0;
        while (current != null) {
            keys[i++] = current.key;
            current = current.nextInOrder;
        }
        return keys;
    }

    @Override
    // Return an array of values in insertion order
    public Object[] getValues() {
        Object[] values = new Object[size];
        Node<K, V> current = head;
        int i = 0;
        while (current != null) {
            values[i++] = current.value;
            current = current.nextInOrder;
        }
        return values;
    }
}
