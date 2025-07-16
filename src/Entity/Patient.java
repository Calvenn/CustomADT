package Entity;

public class Patient implements Comparable<Patient>{
    private String patientIC;
    private String patientName;
    private String patientPhoneNo;
    private int patientAge;
    private String patientGender;
    private String patientAddress;

    public Patient(String patientIC, String patientName, String patientPhoneNo, int patientAge, String patientGender, String patientAddress) {
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

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }
    
    @Override
    public int compareTo(Patient other) {
        return this.patientName.compareTo(other.patientName);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientName='" + patientName + '\'' +
                ", patientPhoneNo='" + patientPhoneNo + '\'' +
                ", patientAge=" + patientAge +
                ", patientGender='" + patientGender + '\'' +
                ", patientAddress='" + patientAddress + '\'' +
                '}';
    }
}