package Control;
import Entity.Doctor;
import adt.Heap;
import java.time.LocalDate;

// 1. Use getMinWorkDoctor(), which is doctor with least patient
// 2. Then use updateDoctor(Doctor minWorkDoctor), to update the doctor back into heap

/**
 * 
 * 
 * @author tanjixian
 */
public class DoctorManager {
    private Heap<Doctor> doctorHeap;
    
    public DoctorManager(Heap<Doctor> doctorHeap){
        this.doctorHeap = doctorHeap;
    }
    
  // Functions
    // Create a new doctor and insert into heap, boolean to ensure success or fail
    public boolean addNewDoctor(String doctorID, String doctorName, int doctorAge, String doctorPhoneNo, String doctorGender, String position, LocalDate dateJoined){
        Doctor newDoctor = new Doctor(doctorID, doctorName, doctorAge, doctorPhoneNo, doctorGender, position, dateJoined);
        doctorHeap.insert(newDoctor);
        return true;
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
         
    // View all Doctor, return their id, name and worload
    public void viewAllDoctor(){
        try{
            for(int i = 0; i < doctorHeap.size(); i++){
                System.out.print("ID: " + doctorHeap.get(i).getDoctorID() + "\nName: " + doctorHeap.get(i).getDoctorName() + "\nWorkload: " + doctorHeap.get(i).getPatientCount() + "\n");
            }
        } catch (Exception e){
            System.err.println("Error during findDoctor: " + e.getMessage());
        }
    }
    
    // Update doctor workload/patientCount, used after consultation
    public void updateDoctor(Doctor minWorkDoctor){
        int currCount = minWorkDoctor.getPatientCount();
        minWorkDoctor.setPatientCount(++currCount);
        doctorHeap.insert(minWorkDoctor);
    }
    
    // Get size of heap for doctorHeap
    public int sizeOfDoctHeap(){
        return doctorHeap.size();
    }
    
    // Find Doctor (from doctorHeap), receive doctor id then return the index(position) in the heap
    public int findDoctor(Doctor toFind){
        try{
            if (toFind == null) return -1; // Null check 
            for(int i = 0; i < sizeOfDoctHeap(); i++ ){
                if(doctorHeap.get(i).equals(toFind)) return i;  // If same doctorID then return the index
            } return -1; // No such doctor
        } catch (Exception e){
            System.err.println("Error during findDoctor: " + e.getMessage());
            return -1;  // Error
        }
    }
    
    public Doctor findDoctor(String toFindID){
        try{
            if (toFindID == null) return null; // Null check 
            for(int i = 0; i < sizeOfDoctHeap(); i++ ){
                if(doctorHeap.get(i).getDoctorID().equals(toFindID)) {
                    return doctorHeap.get(i);
                }  // If same doctorID then return the index
            } return null; // No such doctor
        } catch (Exception e){
            System.err.println("Error during findDoctor: " + e.getMessage());
            return null;  // Error
        }
    }
    
    // Remove Doctor (from doctorHeap)
    public void removeDoctor(Doctor toRemove){
        doctorHeap.remove(toRemove);
    } 
}
