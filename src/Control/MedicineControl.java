package Control;
import Entity.Medicine;
import adt.LinkedHashMap;
import adt.List;

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
    
    public boolean updateMedicineName(String medID, String newName) {
        Medicine med = medMap.get(medID);
        if (med != null) {
            med.setName(newName);
            return true;
        }
        return false;
    }

    public boolean updateMedicineDesc(String medID, String newDesc) {
        Medicine med = medMap.get(medID);
        if (med != null) {
            med.setDesc(newDesc);
            return true;
        }
        return false;
    }
    
    public boolean updateMedicinePrice(String medID, double price) {
        Medicine med = medMap.get(medID);
        if (med != null) {
            med.setPrice(price);
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
        System.out.printf("%-15s %-25s %-40s %-15s %-10s%n",
                "MedID", "Name", "Description", "Stock", "Price (RM)");
        System.out.println("==============================================================================================================================");

        Object[] meds = medMap.getValues();
        for (Object obj : meds) {
            Medicine med = (Medicine) obj;
            System.out.printf("%-15s %-25s %-40s %-15d RM%-9.2f%n",
                    med.getMedID(),
                    med.getName(),
                    med.getDesc(),
                    med.getStock(),
                    med.getPrice());
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
    
    public List<String> suggestedMeds(String symptoms) {
        String[][] medicines = {
            {"Panadol", "fever", "headache", "pain"},
            {"Amoxicillin", "infection", "bacteria", "throat infection"},
            {"Vitamin C", "immunity", "cold", "flu"},
            {"Loratadine", "allergy", "itchy", "runny nose", "sneeze"},
            {"Omeprazole", "acid", "stomach", "reflux", "heartburn"},
            {"Paracetamol", "pain", "fever"},
            {"Ibuprofen", "inflammation", "swelling", "pain"},
            {"Salbutamol", "asthma", "shortness of breath", "wheezing"},
            {"Aspirin", "blood clot", "chest pain", "heart", "stroke"},
            {"Metformin", "diabetes", "sugar", "glucose"}
        };

        List<String> suggested = new List<>();
        String lowerSymptoms = symptoms.toLowerCase();

        for (String[] med : medicines) {
            String medName = med[0];
            for (int i = 1; i < med.length; i++) {
                if (lowerSymptoms.contains(med[i].toLowerCase())) {
                    if (!suggested.contains(medName)) {
                        suggested.add(medName);
                    }
                }
            }
        }

        return suggested;
    }
}

