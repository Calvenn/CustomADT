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
    
    private Node<K, V> head;  // head of insertion-order list
    private Node<K, V> tail;  // tail of insertion-order list
    
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
                current.value = value; // update
                return;
            }
            current = current.next;
        }

        // insert new node
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        size++;

        // maintain insertion order list
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.nextInOrder = newNode;
            newNode.prevInOrder = tail;
            tail = newNode;
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
        Node<K, V> prev = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    buckets[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                // also update insertion order list
                if (current.prevInOrder != null)
                    current.prevInOrder.nextInOrder = current.nextInOrder;
                if (current.nextInOrder != null)
                    current.nextInOrder.prevInOrder = current.prevInOrder;
                if (current == head) head = current.nextInOrder;
                if (current == tail) tail = current.prevInOrder;

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
}
