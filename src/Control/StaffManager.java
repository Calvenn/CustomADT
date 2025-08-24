package Control;
import Entity.Staff;
import adt.LinkedHashMap;
import java.time.LocalDateTime;

/**
 * 
 * 
 * @author tanjixian
 */
public class StaffManager {
    protected LinkedHashMap<String, Staff> staffLookup;
       
    // Constructor
    public StaffManager(LinkedHashMap<String, Staff> staffLookup){
        this.staffLookup = staffLookup;
    }
    
  // Functions
    // Add new staff into List and Hashmap, boolean to ensure success or fail
    public boolean addNewStaff(Staff newStaff){
        staffLookup.put(newStaff.getID(), newStaff);
        return true;
    }    
    
    public boolean addNewStaff(String name, int age, String phoneNo, String gender, Staff.Position position, LocalDateTime dateJoined, String password){
        Staff s = new Staff(name, age, phoneNo, gender, position, dateJoined, password);
        staffLookup.put(s.getID(), s);
        return true;
    }
         
    // Get all Staff (LinkedHashMap)
    public Staff[] viewAllStaff() {
        Object[] obj = staffLookup.getValues();
        Staff[] staff = new Staff[obj.length];
        for (int i = 0; i < obj.length; i++) {
            staff[i] = (Staff) obj[i];  // safe if all are Staff
        }
        return staff;
    }
    
    public void printStaff(Staff s){
        System.out.println(s.toString());
    }
    
    public void printStaff(Staff[] staff){
        for(Staff s: staff){
            System.out.println(s.toString());
        }
    }
    
    // Find Staff 
    public Staff findStaff(String id){
        return staffLookup.get(id);
    }
    
    // Remove Staff
    public void removeStaff(String id){
        staffLookup.remove(id);
    }
    
    // Exist
    public boolean existStaff(String id){
        return staffLookup.containsKey(id);
    }
}