package Entity;
import java.time.LocalDateTime;

/**
 *
 * @author tanjixian
 */

public class Doctor extends Staff implements Comparable<Doctor>{
    // Variables
    private Integer patientCount;    // Availability, How many work handled by this doctor 
    
    // Constructor
    public Doctor(){ super(); this.patientCount = 0; }
    public Doctor(String id, String name, int age, String phoneNo, String gender, Position position, LocalDateTime dateJoined){
        super(id, name, age, phoneNo, gender, position, dateJoined);
        this.patientCount = 0;
    }
    
    // Setters
    public void setPatientCount (int workload) { this.patientCount = workload; }
    
    // Getters
    public int getPatientCount() { return patientCount; }
    
    //compareTo, Compare patientCount, used for Heap
    @Override
    public int compareTo(Doctor other){
        return Integer.compare(this.patientCount, other.patientCount);
    }
    
    //equals, Compare objects contents 
    @Override
    public boolean equals(Object o){
        if(o == this) return true;
        if(!(o instanceof Doctor)) return false;

        Doctor that = (Doctor) o;
        return this.age == that.age      // Compare their Age
             && (this.name != null ? this.name.equals(that.name) : that.name == null) // Names are equal or both are null
             && (this.id != null ? this.id.equals(that.id) : that.id == null); // IDs are equal or both are null
    }
    
    @Override
    public int hashCode(){
        return id.hashCode();
    }
    
    // toString
    @Override
    public String toString(){
        return super.toString();
    }
    
    
}