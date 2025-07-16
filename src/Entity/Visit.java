package Entity;

public class Visit implements Comparable<Visit> {
    private String visitId;
    private String patientPhoneNo;
    private String symptoms;
    private Severity severityLevel;
    private String registrationTime;

    public Visit(String visitId, String patientPhoneNo, String symptoms, Severity severityLevel) {
        this.visitId = visitId;
        this.patientPhoneNo = patientPhoneNo;
        this.symptoms = symptoms;
        this.severityLevel = severityLevel;
        this.registrationTime = java.time.LocalDateTime.now().toString();
    }
    
    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getPatientPhoneNo() {
        return patientPhoneNo;
    }

    public void setPatientPhoneNo(String patientPhoneNo) {
        this.patientPhoneNo = patientPhoneNo;
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

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    
    @Override
    public String toString() { 
        return "Visit{" +
                "visitId='" + visitId + '\'' +
                ", patientPhoneNo='" + patientPhoneNo + '\'' +
                ", symptoms='" + symptoms + '\'' +
                ", severityLevel=" + severityLevel +
                ", registrationTime=" + registrationTime +
                '}';
    }

    @Override
    public int compareTo(Visit other) {
        return this.severityLevel.getSeverity() - other.severityLevel.getSeverity();
    }
}