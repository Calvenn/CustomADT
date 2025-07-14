/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;
import java.time.LocalDateTime;
/**
 *
 * @author calve
 */
public class Appointment implements Comparable<Appointment>{
    private String patientName;
    private String doctorID;
    private String doctorName; //chg to doc id after integration
    private LocalDateTime time;
    
    public Appointment(String patientName, String doctorName, LocalDateTime time){
        this.patientName = patientName;
        this.doctorID = "D001"; //FOR TESTING PURPOSE
        this.doctorName = doctorName;
        this.time = time;
    }
    
    public String getPatientName(){
        return patientName;
    }
    
    public String getDoctorName(){
        return doctorName;
    }
    
    public LocalDateTime getTime(){
        return time;
    }
    
    //@Override
    public int compareTo(Appointment other){
        return this.time.compareTo(other.time);
    }
    
    @Override
    public String toString() {
        return "[" + time + "] " + patientName + " with Dr. " + doctorName;
    }
}
