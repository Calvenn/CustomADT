package Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Visit implements Comparable<Visit> {
    private String visitId;
    private Patient patient;
    private String symptoms;
    public Severity severityLevel;
    private Doctor doctor;
    private LocalDateTime registrationTime;

    public Visit(String visitId, Patient patient, String symptoms, Severity severityLevel, Doctor doctor, LocalDateTime registrationTime) {
        this.visitId = visitId;
        this.patient = patient;
        this.symptoms = symptoms;
        this.severityLevel = severityLevel;
        this.doctor = doctor;
        this.registrationTime = registrationTime; // use the passed value
    }

    // For new visits (auto-set to now)
    public Visit(String visitId, Patient patient, String symptoms, Severity severityLevel, Doctor doctor) {
        this(visitId, patient, symptoms, severityLevel, doctor, LocalDateTime.now());
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

    public boolean isToday() {
        return registrationTime.toLocalDate().equals(LocalDate.now());
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("| %-10s | %-14s | %-14s | %-17s | %-30s |\n", visitId, severityLevel, doctor.getName(), registrationTime.format(formatter), symptoms));
        sb.append("-".repeat(101));

        return sb.toString();
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