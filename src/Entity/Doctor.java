package Entity;
import java.util.Date;
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
    private Date dateJoined;
    private int workload;    // Availability, How many work handled by this doctor
    private static int doctorCount = 0;     // To count the total amount of doctors 
    
    // Constructor
    public Doctor(){ doctorCount++; }
    public Doctor(String doctorName, int doctorAge, String doctorPhoneNo, String doctorGender, String position, Date dateJoined){
        doctorCount++;
        this.doctorID = String.format("D%03d", doctorCount);
        this.doctorName = doctorName;
        this.doctorAge = doctorAge;
        this.doctorPhoneNo = doctorPhoneNo;
        this.doctorGender = doctorGender;
        this.position = position;
        this.dateJoined = dateJoined;
        this.workload = 0;
    }
    
    // Setters
    public void setDoctorName (String doctorName){ this.doctorName = doctorName; }
    public void setDoctorAge (int doctorAge){ this.doctorAge = doctorAge; }
    public void setDoctorPhoneNo (String doctorPhoneNo){ this.doctorPhoneNo = doctorPhoneNo; }
    public void setDoctorGender (String doctorGender){ this.doctorGender = doctorGender; }
    public void setPosition (String position) { this.position = position; }
    public void setWorkload (int workload) { this.workload = workload; }
    
    // Getters
    public String getDoctorID () { return doctorID; }
    public String getDoctorName () { return doctorName; }
    public int getDoctorAge () { return doctorAge; }
    public String getDoctorPhoneNo () { return doctorPhoneNo; }
    public String getDoctorGender () { return doctorGender; }
    public String getPosition () { return position; }
    public Date getDateJoined () { return dateJoined; }
    public int getWorkload() { return workload; }
    
    //compareTo
    @Override
    public int compareTo(Doctor other){
        return this.doctorID.compareTo(other.doctorID);
    }
    
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
               "Workload   : " + workload + "\n";
    }
    
    
}
