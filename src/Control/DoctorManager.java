package Control;
import Entity.Doctor;
import Control.StaffManager;
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
public class DoctorManager {
    private LinkedHashMap<String, Doctor> doctorLookup;
    private Heap<Doctor> doctorHeap;
    
    // Constructor
    public DoctorManager(){
        doctorLookup = new LinkedHashMap();
        doctorHeap = new Heap<>(false);
    }
    
  // Functions
    public boolean addNewDoctor(Doctor newDoctor){
        //super.addNewStaff(newDoctor);
        doctorLookup.put(newDoctor.getID(), newDoctor);
        doctorHeap.insert(newDoctor);
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
    
    // Find Doctor 
    public Doctor findDoctor(String id){
        return doctorLookup.get(id);
    }
    
    // Extract doctor from min-heap root (least workload), used in "visit queue"
    public Doctor getMinWorkDoctor() {
        List<Doctor> temp = new List<>();
        Doctor chosen = null;

        while (!doctorHeap.isEmpty()) {
            Doctor doc = doctorHeap.extractRoot();
            if (doc.getDepartment().equalsIgnoreCase("CONSULT")) {
                chosen = doc;
                break;
            } else {
                temp.add(doc);
            }
        }

        // Put back the other doctors
        for (int i = 1; i <= temp.size(); i++) {
            Doctor d = doctorHeap.get(i-1);
            doctorHeap.insert(d);
        }

        return chosen; 
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
        Doctor d = doctorLookup.remove(id);
        doctorHeap.remove(d);
    }
}