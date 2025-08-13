package Entity;
import java.time.LocalDateTime;

/**
 *
 * @author tanjixian
 */

public class Doctor extends Staff implements Comparable<Doctor>{
    // Variables
    private String department;   // Treatment - Consultation
    private int patientCount;    // Availability, How many work handled by this doctor
    private static int doctorCount = 0;     // To count the total amount of doctors 
    
    // Constructor
    public Doctor(){ doctorCount++; }
    public Doctor(String id, String name, int age, String phoneNo, String gender, String position, LocalDateTime dateJoined, String department){
        super(id, name, age, phoneNo, gender, position, dateJoined);
        doctorCount++;
        this.patientCount = 0;
        this.department = department;
    }
    
    // Setters
    public void setPatientCount (int workload) { this.patientCount = workload; }n
    
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
    public int compareTo(Staff other){
        return Integer.compare(this.patientCount, other.patientCount);
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
