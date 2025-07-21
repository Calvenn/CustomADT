package Entity;
import java.time.LocalDate;

/**
 *
 * @author tanjixian
 */

public class Doctor implements Comparable<Doctor>{
    // Variables
    private String doctorID;
    private String doctorName;
    private int doctorAge;
    private String doctorPhoneNo;
    private String doctorGender;
    private String position; // Treatment  & Consultation 
    private LocalDate dateJoined;
    private Integer patientCount;    // Availability, How many work handled by this doctor
    private static int doctorCount = 0;     // To count the total amount of doctors 
    
    // Constructor
    public Doctor(){ doctorCount++; }
    public Doctor(String doctorID, String doctorName, int doctorAge, String doctorPhoneNo, String doctorGender, String position, LocalDate dateJoined){
        doctorCount++;
        this.doctorID = String.format("D%03d", doctorCount);
        this.doctorName = doctorName;
        this.doctorAge = doctorAge;
        this.doctorPhoneNo = doctorPhoneNo;
        this.doctorGender = doctorGender;
        this.position = position;
        this.dateJoined = dateJoined;
        this.patientCount = 0;
    }
    
    // Setters
    public void setDoctorName (String doctorName){ this.doctorName = doctorName; }
    public void setDoctorAge (int doctorAge){ this.doctorAge = doctorAge; }
    public void setDoctorPhoneNo (String doctorPhoneNo){ this.doctorPhoneNo = doctorPhoneNo; }
    public void setDoctorGender (String doctorGender){ this.doctorGender = doctorGender; }
    public void setPosition (String position) { this.position = position; }
    public void setPatientCount (int workload) { this.patientCount = workload; }
    
    // Getters
    public String getDoctorID () { return doctorID; }
    public String getDoctorName () { return doctorName; }
    public int getDoctorAge () { return doctorAge; }
    public String getDoctorPhoneNo () { return doctorPhoneNo; }
    public String getDoctorGender () { return doctorGender; }
    public String getPosition () { return position; }
    public LocalDate getDateJoined () { return dateJoined; }
    public int getPatientCount() { return patientCount; }
    
    //compareTo, Compare patientCount, used for Heap
    @Override
    public int compareTo(Doctor other){
        return this.patientCount.compareTo(other.patientCount);
    }
    
    //equals, Compare objects contents 
    @Override
    public boolean equals(Object obj){
       if (this == obj) return true;
       if (obj == null || !(obj instanceof Doctor)) return false;
       Doctor other = (Doctor) obj;
       return this.doctorID.equals(other.doctorID); 
    }
    
    // !NO HASHCODE, BUT NO USE HASHMAP
    //@Override
    //public int hashCode(){}
    
    // toString
    @Override
    public String toString(){
        return "Doctor ID  : " + doctorID + "\n" +
               "Doctor Name: " + doctorName + "\n" +
               "Age        : " + doctorAge + "\n" +
               "Gender     : " + doctorGender + "\n" +
               "Phone Number: " + doctorPhoneNo + "\n" +
               "Position   : " + position + "\n" +
               "Date Joined: " + dateJoined + "\n" +
               "Patient Count: " + patientCount + "\n";
    }
    
    
}
