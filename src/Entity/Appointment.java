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
    private static int idNo = 0; 
    private String apptID;
    private String patientName; // patient id
    private String phoneNum;
    private Doctor doctor;
    private int severity;
    private String doctorName; //chg to doc id after integration
    private LocalDateTime time;
    
    public Appointment(String patientName, String phoneNum, Doctor doctor, int severity, LocalDateTime time){
        apptID = "A" + String.format("%04d", generateId()); 
        this.patientName = patientName;
        this.phoneNum = phoneNum;
        this.doctor = doctor;
        this.severity = severity;
        this.time = time;
    }
    
    private static int generateId() {
        idNo += 1; 
        return idNo; 
    }
    
    
    public String getPatientName(){
        return patientName;
    }
    
    public String getPhoneNum(){
        return phoneNum;
    }
    
    public Doctor getDoctor(){
        return doctor;
    }
    
    public int getSeverity(){
        return severity;
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
    
    public void setSeverity(int severity){
        this.severity = severity;
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
        return "Appointment Details:\n"
             + " Date & Time : " + time.format(formatter) + "\n"
             + " Patient     : " + patientName + " (" + phoneNum + ")\n"
             + " Doctor      : " + doctor.getDoctorName() + "\n"
             + " Severity    : " + severity;
    }
}
