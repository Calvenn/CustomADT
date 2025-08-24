package Control;
import Entity.Medicine;
import adt.LinkedHashMap;

public class MedicineControl {
    private LinkedHashMap<String,Medicine> medMap;

    public MedicineControl() {
        medMap = new LinkedHashMap<>();
    }
    public MedicineControl(LinkedHashMap<String,Medicine> medMap){
        this.medMap = medMap;
    }

    public void addMedicine(Medicine med) {//
        medMap.put(med.getMedID(),med);
    }
    
    public boolean updateStock(String medID, int newStock) {//
        Medicine med = medMap.get(medID);
        if (med != null) {
            med.setStock(newStock);
            return true;
        }
        return false;
    }

    public boolean removeMedicine(String medID) {//
        return medMap.remove(medID) != null;
    }
    
    public void displayAllMedicines() {
        if (medMap.isEmpty()) {
            System.out.println("\nNo medicines available in stock.");
            return;
        }

        System.out.println("\n=== Medicine List ===");
        System.out.printf("%-15s %-25s %-40s %-15s%n", "MedID", "Name", "Description", "Stock");
        System.out.println("==================================================================================================================");

        Object[] meds = medMap.getValues();
        for (Object obj : meds) {
            Medicine med = (Medicine) obj;
            System.out.printf("%-15s %-25s %-40s %-15d%n",
                    med.getMedID(),
                    med.getName(),
                    med.getDesc(),
                    med.getStock());
        }
    }

    
    public Medicine findMedicineById(String medID) {//
        return medMap.get(medID);
    }
    
    public Medicine findMedicineByName(String medName) {
        Object[] values = medMap.getValues(); 
        for (Object obj : values) {
            Medicine med = (Medicine) obj; 
            if (med.getName().equalsIgnoreCase(medName)) {
                return med;
            }
        }
        return null; 
    }
    
    public boolean consists(String medID){
        return medMap.containsKey(medID);
    }
    public int getSize() {//
        return medMap.size();
    }
    
    public void displayLowStock(int threshold) {
        System.out.println("\n=== Low Stock Alert (below " + threshold + ") ===");
        boolean found = false;
        for (Medicine med : getAllMedicines()) {
            if (med.getStock() < threshold) {
                System.out.println(med.getMedID() + " | " + med.getName() + " | Stock: " + med.getStock());
                found = true;
            }
        }
        if (!found) {
            System.out.println("All medicines have sufficient stock.");
        }else System.out.println("Please reorder these medicines soon!\n");
    }
    
    public Iterable<Medicine> getAllMedicines() {
        Object[] meds = medMap.getValues();
        java.util.List<Medicine> medList = new java.util.ArrayList<>();
        for (Object obj : meds) {
            medList.add((Medicine) obj);
        }
        return medList;
    }
}

