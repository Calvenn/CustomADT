/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;
import adt.List;
import java.time.LocalDateTime;

/**
 *
 * @author calve
 */
public class Consultation extends Appointment{
    private List<MedRecord> medRecords = new List<>();
    private List<TreatmentAppointment> trtAppts = new List<>();
    //for report purpose
    public static int numOfFollowUp = 0;
    public static int numOfPharmacy = 0;
    public static int numOfTreatment = 0;
    
    //patient id as foreign key
    private static int idNo = 0; 
    private final String consultationID; //C0001
    private int severity;
    private String disease;
    private String notes;
    private LocalDateTime consultTime;
    private LocalDateTime createdAt;
     
    private TreatmentAppointment trtAppt;
    private MedRecord medRec;
    
    //Constructor for fully new consult rec
    public Consultation(int severity, Patient patient, String disease, String notes, Doctor doc, LocalDateTime consultTime, LocalDateTime apptDateTime, LocalDateTime createdAt){
        super(patient, doc, apptDateTime);
        this.consultationID = "C" + String.format("%04d", generateId()); 
        this.severity = severity;
        this.disease = disease;
        this.notes = notes;
        this.consultTime = consultTime;
        this.createdAt = createdAt;
    }
    
    //Constructor for already created consult rec (just for appointment use)
    public Consultation(String consultationID, int severity, Patient patient, String disease, String notes, Doctor doc, LocalDateTime consultTime, LocalDateTime apptDateTime, LocalDateTime createdAt) {
        super(patient, doc, apptDateTime);
        this.consultationID = consultationID;
        this.severity = severity;
        this.disease = disease;
        this.notes = notes;
        this.consultTime = consultTime;
        this.createdAt = createdAt;
    }
    
    private static int generateId() {
        idNo += 1; 
        return idNo; 
    }
    
    public String getID(){
        return consultationID;
    }
    
    public int getSeverity(){
        return severity;
    }
    
    public String getDisease(){
        return disease;
    }
    
    public String getNotes(){
        return notes;
    }
    
    public LocalDateTime getConsultTime(){
        return consultTime;
    }
    
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    
    public TreatmentAppointment getTrtAppt(){
        return trtAppt;
    }
    
    public MedRecord getMedRec(){
        return medRec;
    }
    
    public void setNotes(){
        this.notes = notes;
    }
    
    //Helper method to see the all record
    public void addMedRecord(MedRecord m) {
        if (m != null) medRecords.add(m);
    }

    public void addTreatmentAppointment(TreatmentAppointment t) {
        if (t != null) trtAppts.add(t);
    }

    public List<MedRecord> getMedRecords() {
        return medRecords;
    }

    public List<TreatmentAppointment> getTreatmentAppointments() {
        return trtAppts;
    }
    
    @Override
    public String getAppointmentType() {
        return "Consultation";
    }

    @Override
    public String toString() {
        return String.format(
            "%-8s | %-18s | %-15s | %-8d | %-30s | %-60s | %-20s | %-20s | %-20s | %-20s\n" + "-".repeat(242),
            consultationID,getPatient().getPatientName(),getPatient().getPatientIC(),severity,disease,notes == null ? "" : notes,getDoctor().getName(),
            getConsultTime(),(getDateTime() == null ? "" : getDateTime()),getCreatedAt()
        );
    } 
    
    public static String getHeader(){
        return String.format(
            "\n" + "-".repeat(242) + "\n%-8s  %-18s  %-17s  %-8s  %-30s  %-63s  %-20s  %-20s  %-20s  %-22s\n" + "-".repeat(242),
            "ConsultID","Patient Name","Patient IC","Severity","Diagnosis","Notes","Doctor","Consult Time","Appt Time","Created At"            
        );
    }
    
    public String generateFullReport() {
        String report = "";

        report += "==================== Consultation Report ====================\n\n";
        report += "Consultation ID : " + consultationID + "\n";
        report += "Patient Name    : " + getPatient().getPatientName() + "\n";
        report += "Patient IC      : " + getPatient().getPatientIC() + "\n";
        report += "Severity Level  : " + severity + "\n";
        report += "Diagnosis       : " + disease + "\n";
        report += "Notes           : " + (notes == null ? "" : notes) + "\n";
        report += "Doctor          : " + getDoctor().getName() + "\n";
        report += "Consult Time    : " + consultTime + "\n";
        report += "Appointment Time: " + (getDateTime() == null ? "" : getDateTime()) + "\n";
        report += "Created At      : " + createdAt + "\n";

        // --- Medical Records ---
        report += "\n\n--- Medical Records ---\n";
        report += String.format("%-8s %-12s %-40s\n", "MedID", "Date", "Description");

        if (medRecords.isEmpty()) {
            report += "No medical records found.\n";
        } else {
            for (int i = 1; i <= medRecords.size(); i++) {
                MedRecord m = medRecords.get(i);
                report += String.format("%-8s %-12s %-40s\n",
                        m.getRecordID(),
                        m.getTimestamp(),
                        m.getMed().getName());
            }
        }

        // --- Treatment Records ---
        report += "\n--- Treatment Records ---\n";
        report += String.format("%-8s %-12s %-40s\n", "TrtID", "Date", "Treatment");

        if (trtAppts.isEmpty()) {
            report += "No treatment records found.\n";
        } else {
            for (int i = 1; i <= trtAppts.size(); i++) {
                TreatmentAppointment t = trtAppts.get(i);
                report += String.format("%-8s %-12s %-40s\n",
                        t.getAppointmentId(),
                        t.getDateTime(),
                        t.getTreatment().getName());
            }
        }

        report += "\n============================================================\n";

        return report;
    }
}