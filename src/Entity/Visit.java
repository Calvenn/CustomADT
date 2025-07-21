package Entity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Visit implements Comparable<Visit> {
    private String visitId;
    private Patient patient;
    private String symptoms;
    public Severity severityLevel;
    private Doctor doctor;
    private LocalDateTime registrationTime;

    public Visit(String visitId, Patient patient, String symptoms, Severity severityLevel, Doctor doctor) {
        this.visitId = visitId;
        this.patient = patient;
        this.symptoms = symptoms;
        this.severityLevel = severityLevel;
        this.doctor = doctor;
        this.registrationTime = LocalDateTime.now();
    }
    
    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    
    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public Severity getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(Severity severityLevel) {
        this.severityLevel = severityLevel;
    }
    
    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return
            "Visit ID        : " + visitId + "\n" +
            "Symptoms        : " + symptoms + "\n" +
            "Severity Level  : " + severityLevel + "\n" +
            "Doctor          : " + doctor.getDoctorName() + "\n" +
            "Register Time   : " + registrationTime.format(formatter) + "\n";
    }

    @Override
    public int compareTo(Visit other) {
        int severityCompare = this.severityLevel.getSeverity() - other.severityLevel.getSeverity();
        
        // If severity is the same, compare by registration time (earlier time first)
        if (severityCompare == 0) {
            return other.registrationTime.compareTo(this.registrationTime);
        }
        
        return severityCompare;
    }
}