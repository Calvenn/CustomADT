package Control;
import Entity.Medicine;
import adt.ADTHeap;

public class MedicineControl {
    private ADTHeap<Medicine> lowStockHeap;

    public MedicineControl(ADTHeap<Medicine> lowStockHeap) {
        this.lowStockHeap = lowStockHeap;
    }

    // Add a new medicine
    public void addMedicine(Medicine med) {
        lowStockHeap.insert(med);
    }

    // Peek the lowest stock medicine
    public Medicine peekLowestStock() {
        return lowStockHeap.peekRoot();
    }

    // Extract the most urgent (lowest stock) medicine
    //??
    public Medicine extractLowestStock() {
        return lowStockHeap.extractRoot();
    }

    // Display all medicines in heap (unsorted)
    public void displayAllStock() {
        lowStockHeap.display();
    }
    
    public boolean updateStock(String medID, int newStock) {
        for (int i = 0; i < lowStockHeap.size(); i++) {
            Medicine med = lowStockHeap.get(i);
            if (med.getMedID().equals(medID)) {
                // Remove the original object using the data-based remove()
                lowStockHeap.remove(med);

                // Create a new object or update stock (if mutable)
                med.setStock(newStock);  // assuming Medicine class has setStock()

                // Re-insert updated version to maintain heap property
                lowStockHeap.insert(med);
                return true;
            }
        }
        return false; // not found
    }
    
    public Medicine findMedicine(String medToFind) {
        for (int i = 0; i < lowStockHeap.size(); i++) {
            Medicine med = lowStockHeap.get(i);
            if (med.getMedID ().equals(medToFind) || med.getName().equalsIgnoreCase(medToFind)) {
                return med;
            }
        }
        return null;
    }
    
    public boolean removeExpiredMedicine(String expiryDate) {
        for (int i = 0; i < lowStockHeap.size(); i++) {
            Medicine med = lowStockHeap.get(i);
            if (med.getExpiryDate().equals(expiryDate)) {
                return lowStockHeap.remove(med); // remove by data
            }
        }
        return false; // No match found
    }

}

