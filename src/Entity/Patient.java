package Entity;

import java.time.format.DateTimeFormatter;

public class Patient{
    private String patientIC;
    private String patientName;
    private String patientPhoneNo;
    private int patientAge;
    private char patientGender;
    private String patientAddress;

    public Patient(String patientIC, String patientName, String patientPhoneNo, int patientAge, char patientGender, String patientAddress) {
        this.patientIC = patientIC;
        this.patientName = patientName;
        this.patientPhoneNo = patientPhoneNo;
        this.patientAge = patientAge;
        this.patientGender = patientGender;
        this.patientAddress = patientAddress;
    }
    
    public String getPatientIC() {
        return patientIC;
    }

    public void setPatientIC(String patientIC) {
        this.patientIC = patientIC;
    }
    
    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientPhoneNo() {
        return patientPhoneNo;
    }

    public void setPatientPhoneNo(String patientPhoneNo) {
        this.patientPhoneNo = patientPhoneNo;
    }

    public int getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(int patientAge) {
        this.patientAge = patientAge;
    }

    public char getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(char patientGender) {
        this.patientGender = patientGender;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("| %-15s | %-20s | %-15s | %-5d | %-8s | %-40s |\n", patientIC, patientName, patientPhoneNo, patientAge, patientGender, patientAddress));
        sb.append("-".repeat(111)).append("\n");
        
        return sb.toString();
    }
}