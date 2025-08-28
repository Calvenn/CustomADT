package Control;
import Entity.Consultation;
import adt.List;
import Entity.MedRecord;
import Entity.Medicine;
import Entity.Patient;
import Entity.Doctor;

import java.time.LocalDateTime;

/**
 * 
 * @author Cheang Wei Ting
 */

public class MedRecordControl {
    private List<MedRecord> recList;

    public MedRecordControl(){
        this.recList = new List<>();
    }
    public MedRecordControl(List<MedRecord> recList){
        this.recList = recList;
    }
    
    public void addRecord(Patient patient, Doctor doctor, Medicine medicine, int quantityTaken, boolean toSave, Consultation consult) {
        MedRecord record = new MedRecord(
                patient,
                doctor,
                medicine,
                quantityTaken,
                LocalDateTime.now(),
                toSave, 
                consult
        );
        recList.add(record);
        medicine.incrementDispensed(quantityTaken);
    }
    
    public void viewAllRecords() {
        if (recList.isEmpty()) {
            System.out.println("No medicine records found.");
            return;
        }
        for (int i = 1; i <= recList.size(); i++) {
            MedRecord record = recList.get(i);
            displayRecord(record);
        }
    }
    
    public void searchByPatient(String patient) {
        boolean found = false;
        for (int i = 1; i <= recList.size(); i++) {
            MedRecord record = recList.get(i);
            if (record.getPatient().getPatientIC().equalsIgnoreCase(patient)||record.getPatient().getPatientName().equalsIgnoreCase(patient)) {
                displayRecord(record);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No records found for patient: " + patient);
        }
    }
    
    public void searchByDoctor(String doctor) {
        boolean found = false;
        for (int i = 1; i <= recList.size(); i++) {
            MedRecord record = recList.get(i);
            if (record.getDoctor().getID().equalsIgnoreCase(doctor)||record.getDoctor().getName().equalsIgnoreCase(doctor)) {
                displayRecord(record);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No records found for doctor: " + doctor);
        }
    }
    
    public void searchByMedicine(String med) {
        boolean found = false;
        for (int i = 1; i <= recList.size(); i++) {
            MedRecord record = recList.get(i);
            if (record.getMed().getMedID().equalsIgnoreCase(med)||record.getMed().getName().equalsIgnoreCase(med)) {
                displayRecord(record);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No records found for medicine : " + med);
        }
    }

    //search by date?
    private void displayRecord(MedRecord record) {
        System.out.println("=======================================");
        System.out.println("Record ID: " + record.getRecordID());
        System.out.println("Patient: " + record.getPatient().getPatientName() + " (" + record.getPatient().getPatientIC() + ")");
        System.out.println("Doctor: " + record.getDoctor().getName() + " (" + record.getDoctor().getID() + ")");
        System.out.println("Medicine: " + record.getMed().getName() + " (" + record.getMed().getMedID() + ")");
        System.out.println("Quantity Taken: " + record.getQuantityTaken());
        System.out.println("Issued On: " + record.getTimestamp());
        System.out.println("=======================================");
    }
}