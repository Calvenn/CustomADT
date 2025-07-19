package Control;
import Entity.Doctor;
import adt.ADTHeap;
import java.util.Date;

/**
 *
 * @author tanjixian
 */
public class DoctorManager {
    
  // Variables
    ADTHeap<Doctor> doctorHeap = new ADTHeap<>(false);

    
  // Functions
    // Create a new doctor and insert into heap, boolean to ensure success or fail
    public boolean addNewDoctor(String doctorName, int doctorAge, String doctorPhoneNo, String doctorGender, String position, Date dateJoined){
        Doctor newDoctor = new Doctor(doctorName, doctorAge, doctorPhoneNo, doctorGender, position, dateJoined);
        doctorHeap.insert(newDoctor);
        return true;
    }
    
    // 
}
