package Control;
import Entity.Medicine;
import adt.ADTHeap;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MedicineControl {
    private ADTHeap<Medicine> lowStockHeap;

    public MedicineControl(ADTHeap<Medicine> lowStockHeap) {
        this.lowStockHeap = lowStockHeap;
    }

    public void addMedicine(Medicine med) {
        lowStockHeap.insert(med);
    }

    public Medicine peekLowestStock() {
        return lowStockHeap.peekRoot();
    }


    // Display all medicines in heap (unsorted)
    public void displayAllStock() {
        lowStockHeap.display();
    }
    
    public boolean updateStock(String batchID, int newStock) {// this one only same batch of med in same expiry date
        for (int i = 0; i < lowStockHeap.size(); i++) {
            Medicine med = lowStockHeap.get(i);
            if (med.getBatchID().equals(batchID)) {
                
                lowStockHeap.remove(med);
                Medicine updatedMed = new Medicine(med);
                updatedMed.setStock(newStock);  
                lowStockHeap.insert(updatedMed);
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

    public boolean removeExpiredMedicine(String batchID) {
        for (int i = 0; i < lowStockHeap.size(); i++) {
            Medicine med = lowStockHeap.get(i);
            if (med.getBatchID().equals(batchID)) {
                return lowStockHeap.remove(med);
            }
        }
        return false;
    }
    
    private boolean isExpired(Medicine med) {
        LocalDate today = LocalDate.now();
        LocalDate exp = LocalDate.parse(med.getExpiryDate(), 
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return exp.isBefore(today);
    }
    
    // Extract the most urgent (lowest stock) medicine
    //??
    public Medicine extractLowestStock() {
        return lowStockHeap.extractRoot();
    }
    
    //Add/search record medicine
}

