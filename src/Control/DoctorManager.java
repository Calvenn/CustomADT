package Control;
import Entity.Doctor;
import adt.Heap;
import adt.LinkedHashMap;
import adt.List;

// 1. Use getMinWorkDoctor(), which is doctor with least patient
// 2. (A)IF THE DOCTOR IS APPOINTED NEW TASK then use updateDoctorInc(Doctor minWorkDoctor), to update the doctor back into heap
//    (B)IF THE DOCTOR FINISH A TASK then use updateDoctorDec(Doctor minWorkDoctor), to update the doctor back into heap
/**
 * 
 * 
 * @author tanjixian
 */
public class DoctorManager extends StaffManager {
    private Heap<Doctor> doctorHeap;
    private List<Doctor> staffList;
    private LinkedHashMap<String, Doctor> staffLookup;
    
    // Constructor
    public DoctorManager(){
        super();    // initialise List and HashMap
        doctorHeap = new Heap<>(false);
    }
    
  // Functions
    public boolean addNewDoctor(Doctor newDoctor){
        doctorHeap.insert(newDoctor);
        staffList.add(newDoctor);
        staffLookup.put(newDoctor.getID(), newDoctor);
        return true;
    }
    
    // View All Doctors
    public void viewAllDoctor(){
        for(int i = 0; i < staffList.size(); i++){
            staffList.get(i).toString();
        }
    }
    
    // Get List of Doctors
    public List<Doctor> getDoctorList(){
        return this.staffList;
    }
    
    // Find Doctors
    public Doctor findDoctor(String id){
        return staffLookup.get(id);
    }
    
    // Extract doctor from min-heap root (least workload), used in "bookAppointment"
    public Doctor getMinWorkDoctor(){
        Doctor minWorkDoctor = doctorHeap.extractRoot();
        return minWorkDoctor;
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
    
    // Remove Doctor (from doctorHeap, staffList, staffLookup)
    public void removeDoctor(String id){
        Doctor d = staffLookup.remove(id);
        for(int i = 0; i < staffList.size(); i++){
            if(staffList.get(i).equals(d)){
                staffList.remove(i);
            }
        } 
        doctorHeap.remove(d);
    }
}
