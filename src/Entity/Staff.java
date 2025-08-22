
package Entity;

import java.time.LocalDateTime;

/**
 *
 * @author tanjixian
 */

public abstract class Staff {
    // Enums
    public enum Position{DOCTOR, NURSE, ADMIN}
    
    // Variable
    protected String id;          // S0001
    protected String name;        
    protected int age;            
    protected String phoneNo;     // 011-222 4568
    protected String gender;      // Male - Female
    protected Position position;    // Doctor - Nurse - Admin
    protected LocalDateTime dateJoined;   // 23 January 2023
    protected static int staffCount = 0;
    
    // Constructor
    public Staff(){}
    public Staff(String id, String name, int age, String phoneNo, String gender, Position position, LocalDateTime dateJoined){
        this.id = id;
        this.name = name;
        this.age = age;
        this.phoneNo = phoneNo;
        this.gender = gender;
        this.position = position;
        this.dateJoined = dateJoined;
        staffCount++;
    }
    
    
    // Setters
    public void setName(String name){ this.name = name; }
    public void setAge(int age){ this.age = age; }
    public void setPhoneNo(String phoneNo){ this.phoneNo = phoneNo; }
    public void setGender(String gender) { this.gender = gender; }
    public void setPosition(Position position) { this.position = position; }
    
    // Getters
    public String getID(){ return id; }
    public String getName(){ return name; }
    public int getAge() { return age; }
    public String getPhoneNo() { return phoneNo; }
    public String getGender() { return gender; }
    public Position getPosition() { return position; }
    public LocalDateTime getDateJoined() { return dateJoined; }
    
    // toString
    @Override
    public String toString(){
        return "HI";
    }
    
    // equals
    //equals, Compare objects contents 
    @Override
    public boolean equals(Object o){
        if(o == this) return true;
        if(!(o instanceof Staff)) return false;

        Staff that = (Staff) o;
        return this.age == that.age      // Compare their Age
             && (this.name != null ? this.name.equals(that.name) : that.name == null) // Names are equal or both are null
             && (this.id != null ? this.id.equals(that.id) : that.id == null); // IDs are equal or both are null
    }
    
    @Override
    public int hashCode(){
        return id.hashCode();
    }
    
}