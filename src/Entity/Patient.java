package Entity;

/**
 *
 * @author NgPuiYin
 */
public class Patient{
    private String patientIC;
    private String studentID;
    private String patientName;
    private String patientPhoneNo;
    private int patientAge;
    private char patientGender;
    private String patientAddress;

    public Patient(String patientIC, String studentID, String patientName, String patientPhoneNo, int patientAge, char patientGender, String patientAddress) {
        this.patientIC = patientIC;
        this.studentID = studentID;
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
    
    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
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
        String line = "-".repeat(67);

        return line + "\n" +
            String.format("| %-63s |\n", "PATIENT INFO") +
            line + "\n" +
            String.format("| IC         | %-50s |\n", patientIC) +
            String.format("| StudentID  | %-50s |\n", studentID) +
            String.format("| Name       | %-50s |\n", patientName) +
            String.format("| Phone      | %-50s |\n", patientPhoneNo) +
            String.format("| Age        | %-50d |\n", patientAge) +
            String.format("| Gender     | %-50c |\n", patientGender) +
            String.format("| Address    | %-50s |\n", patientAddress) +
            line;
    }

}