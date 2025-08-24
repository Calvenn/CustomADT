package Control;
import Entity.Staff;
import adt.LinkedHashMap;

/**
 * 
 * 
 * @author tanjixian
 */
public class StaffManager {
    protected LinkedHashMap<String, Staff> staffLookup;
       
    // Constructor
    public StaffManager(){
        staffLookup = new LinkedHashMap();
    }
    
  // Functions
    // Add new staff into List and Hashmap, boolean to ensure success or fail
    public void addNewStaff(Staff newStaff){
        staffLookup.put(newStaff.getID(), newStaff);
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
    
    // Find Staff 
    public Staff findStaff(String id){
        return staffLookup.get(id);
    }
    
    // Remove Staff
    public void removeStaff(String id){
        
    }
    
    // Exist
}