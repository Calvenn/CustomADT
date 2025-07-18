package Entity;

public class Patient implements Comparable<Patient>{
    private String patientIC;
    private String patientName;
    private String patientPhoneNo;
    private int patientAge;
    private char patientGender;
    private String patientAddress;
    private boolean isAppt;

    public Patient(String patientIC, String patientName, String patientPhoneNo, int patientAge, char patientGender, String patientAddress) {
        this.patientIC = patientIC;
        this.patientName = patientName;
        this.patientPhoneNo = patientPhoneNo;
        this.patientAge = patientAge;
        this.patientGender = patientGender;
        this.patientAddress = patientAddress;
        this.isAppt = false;
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

    public boolean isAppt() {
        return isAppt;
    }

    public void setAppt(boolean isAppt) {
        this.isAppt = isAppt;
    }

    @Override
    public int compareTo(Patient other) {
        return this.patientName.compareTo(other.patientName);
    }

    @Override
    public String toString() {
        return 
            "Name           : " + patientName + "\n" +
            "Phone Number   : " + patientPhoneNo + "\n" +
            "Age           : " + patientAge + "\n" +
            "Gender        : " + patientGender + "\n" +
            "Address       : " + patientAddress;
    }
}