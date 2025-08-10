package Control;
import Entity.Medicine;
import adt.Heap;

public class MedicineControl {
    private Heap<Medicine> medHeap;

    public MedicineControl(Heap<Medicine> lowStockMed) {
        this.medHeap = lowStockMed;
    }

    public void addMedicine(Medicine med) {//
        medHeap.insert(med);
    }
    
    public boolean updateStock(String medID, int newStock) {//
        for (int i = 0; i < medHeap.size(); i++) {
            Medicine med = medHeap.get(i);
            if (med.getMedID().equals(medID)) {
                Medicine newMed = new Medicine(med);
                newMed.setStock(newStock);
                medHeap.update(med,newMed);
                return true;
            }
        }
        return false;
    }

    public boolean removeMedicine(String medID) {//
        for (int i = 0; i < medHeap.size(); i++) {
            Medicine med = medHeap.get(i);
            if (med.getMedID().equals(medID)) {
                return medHeap.remove(med);
            }
        }
        return false;
    }
    
    
    public Medicine peekLowestStock() {//
        return medHeap.peekRoot();
    }
    public void displayAllStock() {//
        medHeap.display();
    }
    public Medicine extractLowestStock() {//
        return medHeap.extractRoot();
    }
    
    public Medicine findMedicine(String medToFind) {//search by name or id
        for (int i = 0; i < medHeap.size(); i++) {
            Medicine med = medHeap.get(i);
            if (med.getMedID ().equals(medToFind) ||
            med.getName().equalsIgnoreCase(medToFind)) {
                return med;
            }
        }
      return null;
    }

    public int getSize() {//
        return medHeap.size();
    }
}

