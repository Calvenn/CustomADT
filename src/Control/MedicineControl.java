package Control;
import Entity.Medicine;
import adt.ADTHeap;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MedicineControl {
    private ADTHeap<Medicine> lowStockHeap;

    public MedicineControl() {
        lowStockHeap = new ADTHeap<>(false); // Min-Heap by default
    }

    public void addMedicine(Medicine med) {
        lowStockHeap.insert(med);
    }

    public Medicine peekLowestStock() {
        return lowStockHeap.peekRoot();
    }


    // Display all medicines in heap (unsorted)
    public void displayAllStock() {
        for (int i = 0; i < lowStockHeap.size(); i++) {
            System.out.println(lowStockHeap.get(i)); 
        }
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

    public Medicine findMedicineByID(String medID) {
        //use hashMap for this to store same med but diff batch??
        for (int i = 0; i < lowStockHeap.size(); i++) {
            Medicine med = lowStockHeap.get(i);
            if (med.getMedID ().equals(medID)) {
                return med;
            }
        }
        return null;
    }
    
    // Extract the most urgent (lowest stock) medicine
    //??
    public Medicine extractLowestStock() {
        return lowStockHeap.extractRoot();
    }
}

