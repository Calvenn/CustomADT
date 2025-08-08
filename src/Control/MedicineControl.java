package Control;
import Entity.Medicine;
import Entity.MedRecord;
import adt.Heap;
import adt.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MedicineControl {
    private Heap<Medicine> medHeap;
    private List<MedRecord> medRecords;

    public MedicineControl(Heap <Medicine> medHeap) {
        this.medRecords = new List<>();
        this.medHeap = medHeap;
    }

    public void addMedicine(Medicine med) {
        medHeap.insert(med);
    }

    public Medicine peekLowestStock() {
        return medHeap.peekRoot();
    }


    // Display all medicines in heap (unsorted)
    public void displayAllStock() {
        medHeap.display();
    }
    
    public boolean updateStock(String batchID, int newStock) {// this one only same batch of med in same expiry date
        for (int i = 0; i < medHeap.size(); i++) {
            Medicine med = medHeap.get(i);
            if (med.getBatchID().equals(batchID)) {
                
                medHeap.remove(med);
                Medicine updatedMed = new Medicine(med);
                updatedMed.setStock(newStock);  
                medHeap.insert(updatedMed);
                return true;
            }
        }
        return false; // not found
    }
    
    public Medicine findMedicine(String medToFind) {
        for (int i = 0; i < medHeap.size(); i++) {
            Medicine med = medHeap.get(i);
            if (med.getMedID ().equals(medToFind) || med.getName().equalsIgnoreCase(medToFind)) {
                return med;
            }
        }
      return null;
    }

    public boolean removeExpiredMedicine(String batchID) {
        for (int i = 0; i < medHeap.size(); i++) {
            Medicine med = medHeap.get(i);
            if (med.getBatchID().equals(batchID)) {
                return medHeap.remove(med);
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
        return medHeap.extractRoot();
    }
    
    //Add/search record medicine
}

