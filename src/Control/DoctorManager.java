package Control;
import Entity.Doctor;
import Control.StaffManager;
import Entity.Staff;
import adt.Heap;
import adt.LinkedHashMap;
import adt.List;
import java.time.LocalDateTime;

// 1. Use getMinWorkDoctor(), which is doctor with least patient
// 2. (A)IF THE DOCTOR IS APPOINTED NEW TASK then use updateDoctorInc(Doctor minWorkDoctor), to update the doctor back into heap
//    (B)IF THE DOCTOR FINISH A TASK then use updateDoctorDec(Doctor minWorkDoctor), to update the doctor back into heap
/**
 * 
 * 
 * @author tanjixian
 */
public class DoctorManager {
    private LinkedHashMap<String, Doctor> doctorLookup;
    private Heap<Doctor> doctorHeap;
    
    // Constructor
    public DoctorManager(LinkedHashMap<String, Doctor> doctorLookup, Heap<Doctor> doctorHeap){
        this.doctorLookup = doctorLookup;
        this.doctorHeap = doctorHeap;
    }
    
  // Functions
    public boolean addNewDoctor(Doctor newDoctor){
        doctorLookup.put(newDoctor.getID(), newDoctor);
        doctorHeap.insert(newDoctor);
        return true;
    }
    
    public boolean addNewDoctor(String name, int age, String phoneNo, String gender, Staff.Position position, String department, LocalDateTime dateJoined, String password){
        Doctor s = new Doctor(name, age, phoneNo, gender, position, department, dateJoined, password);
        doctorLookup.put(s.getID(), s);
        doctorHeap.insert(s);
        return true;
    }
    
    // Peek All Doctors
    public String[] peekAllDoctorID() {
        String[] allDocIDs = new String[doctorHeap.size()];
        for (int i = 0; i < doctorHeap.size(); i++) {
            allDocIDs[i] = doctorHeap.get(i).getID();
        }
        return allDocIDs;
    }
    
    public Doctor[] viewAllDoctor() {
        Object[] obj = doctorLookup.getValues();
        Doctor[] doctor = new Doctor[obj.length];
        for (int i = 0; i < obj.length; i++) {
            doctor[i] = (Doctor) obj[i];  // safe if all are Staff
        }
        return doctor;
    }
    
    public void printDoctor(Doctor d){
         System.out.println(d.toString());
    }
    
    public void printDoctor(Doctor[] doctor){
        for(Doctor d: doctor){
            System.out.println(d.toString());
        }
    }
            
    // Find Doctor 
    public Doctor findDoctor(String id){
        return doctorLookup.get(id);
    }
    
    // Extract doctor from min-heap root (least workload), used in "bookAppointment"
    public Doctor getMinWorkDoctorByDept(String dept) {
        Doctor minDoctor = null;

        for (int i = 0; i < doctorHeap.size(); i++) {
            Doctor d = doctorHeap.get(i);
            if (d.getDepartment().equalsIgnoreCase(dept)) {
                if (minDoctor == null || d.getPatientCount() < minDoctor.getPatientCount()) {
                    minDoctor = d;
                }
            }
        }
        return minDoctor;
    }
    
    // Peek lowest patientCount doctor
    public Doctor peekRootDoctor(){
        return doctorHeap.peekRoot();
    } 
    
    // Update doctor workload/patientCount, used after !APPOINTED NEW TASK!
    public void updateDoctorInc(Doctor minWorkDoctor){
        int currCount = minWorkDoctor.getPatientCount();
        minWorkDoctor.setPatientCount(++currCount);
        doctorHeap.insert(minWorkDoctor);
    }

    // Update doctor workload/patientCount, used after !DOCTOR FINISH ITS TASK!
    public void updateDoctorDec(Doctor minWorkDoctor){
        int currCount = minWorkDoctor.getPatientCount();
        if (currCount > 0) minWorkDoctor.setPatientCount(--currCount);
        doctorHeap.insert(minWorkDoctor);
    }
    
    // Get size of heap for doctorHeap
    public int sizeOfDoctHeap(){
        return doctorHeap.size();
    }
    
    // Remove Doctor (from doctorHeap, doctorLookup)
    public void removeDoctor(String id){
        Doctor d = doctorLookup.remove(id);
        doctorHeap.remove(d);
    }
    
    // isDoctor
    public boolean isDoctor(Staff staff){
        return (staff instanceof Doctor);
    }
    
    public List<Doctor> getDoctorsByDept(String dept) {
        if(doctorLookup.isEmpty()) {
            return null;
        }
        List<Doctor> doctors = new List<>(); 
        for(Doctor doctor : viewAllDoctor()) {
            if(doctor.getDepartment().equalsIgnoreCase(dept)) {
                doctors.add(doctor);
            }
        }
        return doctors;
    }
}