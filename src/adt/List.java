package adt;
public class List<E> implements ListInterface<E>{
    private E[] array;
    private int numOfEntries;
    private static final int DEFAULT_CAPACITY = 100;
    
    public List() {
    array = (E[]) new Object[DEFAULT_CAPACITY];
    numOfEntries = 0;
    }
    
    @Override
    public boolean add(E data){
        array[numOfEntries] = data;
        numOfEntries++;
        return true;
    }
    
    @Override
    public boolean add(int position, E data){
        boolean success = true;
        if (position >= 1 && position <= numOfEntries + 1){
            makeRoom(position);
            array[position - 1] = data;
            numOfEntries++;
        }else
            success = false;
        
        return success;
        
    }
    
    @Override
    public E remove(int position){
        E data = null;
        if(position >= 1 && position <= numOfEntries){
            data = array[position - 1];
            
            if(position < numOfEntries){
                removeGap(position);
            }
            numOfEntries--;
        }
        return data;
    }
    
    @Override
    public E get(int position){
        E data = null;
        
        if(position >= 1 && position <= numOfEntries){
            data = array[position - 1];
        }
        
        return data;
    }
    
    @Override
    public boolean replace(int position, E data){
        boolean success = true;
        if(position >= 1 && position <= numOfEntries){
            array[position - 1] = data;
        }else
            success = false;
        
        return success;
    }
    
    @Override
    public int size(){
        return numOfEntries;
    }
    
    @Override
    public boolean isEmpty(){
        return numOfEntries == 0;
    }
    
    //helper function
    private void makeRoom(int position){
        int newIndex = position - 1;
        int lastIndex = numOfEntries - 1;
        
        for(int i = lastIndex; i >= newIndex; i++){
            array[i] = array[i +1];
        }
    }
    
    private void removeGap(int position){
        int removed = position - 1;
        int last = numOfEntries - 1;
        
        for (int i = removed; i < last; i++){
            array[i] = array[i + 1];
        }
    }
}

