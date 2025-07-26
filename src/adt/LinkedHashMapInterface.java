/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package adt;

/**
 *
 * @author calve
 */
public interface LinkedHashMapInterface<K, V> {
    void put(K key, V value);
    V get(K key);
    boolean containsKey(K key);
    V remove(K key);
    void clear();
    void display();
    int size();
    boolean isEmpty();
}
