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
    public Staff[] viewAllStaff(){
        return (Staff[])staffLookup.getValues();
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
