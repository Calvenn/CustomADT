package Control;
import adt.List;
import Entity.MedRecord;
import Entity.Medicine;
import Entity.Patient;
import Entity.Doctor;

import java.time.LocalDateTime;

public class MedRecordControl {
    private List<MedRecord> recList;

    public MedRecordControl(){
        this.recList = new List<>();
    }
    
    public void addRecord(Patient patient, Doctor doctor, Medicine medicine, int quantityTaken) {
        MedRecord record = new MedRecord(
                patient,
                doctor,
                medicine,
                quantityTaken,
                LocalDateTime.now()
        );
        recList.add(record);
    }
    
    public void viewAllRecords() {
        if (recList.isEmpty()) {
            System.out.println("No medicine records found.");
            return;
        }
        for (int i = 0; i < recList.size(); i++) {
            MedRecord record = recList.get(i);
            displayRecord(record);
        }
    }
    
    public void searchByPatient(String patientID) {
        boolean found = false;
        for (int i = 0; i < recList.size(); i++) {
            MedRecord record = recList.get(i);
            if (record.getPatient().getPatientIC().equalsIgnoreCase(patientID)) {
                displayRecord(record);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No records found for patient ID: " + patientID);
        }
    }
    
    public void searchByDoctor(String doctorID) {
        boolean found = false;
        for (int i = 0; i < recList.size(); i++) {
            MedRecord record = recList.get(i);
            if (record.getDoctor().getDoctorID().equalsIgnoreCase(doctorID)) {
                displayRecord(record);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No records found for doctor ID: " + doctorID);
        }
    }
    
    public void searchByMedicine(String medID) {
        boolean found = false;
        for (int i = 0; i < recList.size(); i++) {
            MedRecord record = recList.get(i);
            if (record.getMedID().getMedID().equalsIgnoreCase(medID)) {
                displayRecord(record);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No records found for medicine ID: " + medID);
        }
    }

    //search by date?
    private void displayRecord(MedRecord record) {
        System.out.println("--------------------------------");
        System.out.println("Record ID: " + record.getRecordID());
        System.out.println("Patient: " + record.getPatient().getPatientName() + " (" + record.getPatient().getPatientIC() + ")");
        System.out.println("Doctor: " + record.getDoctor().getDoctorName() + " (" + record.getDoctor().getDoctorID() + ")");
        System.out.println("Medicine: " + record.getMedID().getName() + " (" + record.getMedID().getMedID() + ")");
        System.out.println("Quantity Taken: " + record.getQuantityTaken());
        System.out.println("Issued On: " + record.getTimestamp());
    }
}
