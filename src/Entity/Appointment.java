/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author calve
 */
public class Appointment implements Comparable<Appointment>{
    private String patientName;
    private String phoneNum;
    private String doctorID;
    private String doctorName; //chg to doc id after integration
    private LocalDateTime time;
    
    public Appointment(String patientName, String phoneNum, String doctorName, LocalDateTime time){
        this.patientName = patientName;
        this.phoneNum = phoneNum;
        this.doctorID = "D001"; //FOR TESTING PURPOSE
        this.doctorName = doctorName;
        this.time = time;
    }
    
    public String getPatientName(){
        return patientName;
    }
    
    public String getPhoneNum(){
        return phoneNum;
    }
    
    public String getDoctorName(){
        return doctorName;
    }
    
    public LocalDateTime getTime(){
        return time;
    }
    
    public void setPatientName(String patientName){
        this.patientName = patientName;
    }
    
    public void setDoctorName(String doctorName){
        this.doctorName = doctorName;
    }
    
    public void setTime(LocalDateTime time){
        this.time = time;
    }
    
    //@Override
    public int compareTo(Appointment other){
        return this.time.compareTo(other.time);
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "[" + time.format(formatter) + "] "
             + patientName + " with " + doctorName;
    }

}
