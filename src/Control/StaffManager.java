package Control;
import Entity.Staff;
import adt.LinkedHashMap;
import adt.List;

/**
 * 
 * 
 * @author tanjixian
 */
public class StaffManager {
    protected List<Staff> staffList;
    protected LinkedHashMap<String, Staff> staffLookup;
       
    // Constructor
    public StaffManager(){
        staffList = new List<>();
        staffLookup = new LinkedHashMap();
    }
    
  // Functions
    // Add new staff into List and Hashmap, boolean to ensure success or fail
    public boolean addNewStaff(Staff newStaff){
        staffList.add(newStaff);
        staffLookup.put(newStaff.getID(), newStaff);
        return true;
    }    
         
    // View all Staff
    public void viewAllStaff(){
        for(int i = 0; i < staffList.size(); i++){
            staffList.get(i).toString();
        }
    }
    
    // Get all Staff (List)
    public List<Staff> getStaffList(){
        return staffList;
    }
    
    // Find Staff 
    public Staff findStaff(String id){
        return staffLookup.get(id);
    }
    
    // Remove Staff
    public void removeStaff(String id){
        Staff s = staffLookup.remove(id);
        for(int i = 0; i < staffList.size(); i++){
            if(staffList.get(i) == s){
                staffList.remove(i);
            }
        }
    }
}
