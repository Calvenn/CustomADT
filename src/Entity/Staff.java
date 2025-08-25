
package Entity;

import java.time.LocalDateTime;

/**
 *
 * @author tanjixian
 */

public class Staff {
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
    protected String password;
    protected static int adminCount = 0;
    protected static int doctorCount = 0;
    protected static int nurseCount = 0;
    
    // Constructor
    public Staff(){}
    public Staff(String name, int age, String phoneNo, String gender, Position position, LocalDateTime dateJoined, String password){
        increaseCount(position);
        this.id = generateID(position);
        this.name = name;
        this.age = age;
        this.phoneNo = phoneNo;
        this.gender = gender;
        this.position = position;
        this.dateJoined = dateJoined;
        this.password = password;
    }
    public Staff(String id, String name, int age, String phoneNo, String gender, Position position, LocalDateTime dateJoined, String password){
        increaseCount(position);
        this.id = id;
        this.name = name;
        this.age = age;
        this.phoneNo = phoneNo;
        this.gender = gender;
        this.position = position;
        this.dateJoined = dateJoined;
        this.password = password;
    }
    
    // Helper
    private String generateID(Position position){
        return switch(position){
            case ADMIN -> String.format("%c%04d", 'A', adminCount);
            case DOCTOR -> String.format("%c%04d", 'A', doctorCount);
            case NURSE -> String.format("%c%04d", 'A', nurseCount);
        };
    }
    private void increaseCount(Position position){
        switch(position){
            case ADMIN -> adminCount++;
            case DOCTOR -> doctorCount++;
            case NURSE -> nurseCount++;

        }
    }
    
    // Setters
    public void setName(String name){ this.name = name; }
    public void setAge(int age){ this.age = age; }
    public void setPhoneNo(String phoneNo){ this.phoneNo = phoneNo; }
    public void setGender(String gender) { this.gender = gender; }
    public void setPosition(Position position) { this.position = position; }
    public void setPassword(String password) { this.password = password; }
    
    // Getters
    public String getID(){ return id; }
    public String getName(){ return name; }
    public int getAge() { return age; }
    public String getPhoneNo() { return phoneNo; }
    public String getGender() { return gender; }
    public Position getPosition() { return position; }
    public LocalDateTime getDateJoined() { return dateJoined; }
    public String getPassword() { return password; }
    public static int getAdminCount() { return adminCount; }
    public static int getDoctorCount() { return doctorCount; }
    public static int getNurseCount() { return nurseCount; }
    public static int getTotalStaffCount() { return adminCount + doctorCount + nurseCount; }
    // toString
    @Override
    public String toString(){
        return "\nID   : " + this.id +
               "\nName : " + this.name +
               "\nAge  : " + this.age + 
               "\nGender: " + this.gender +
               "\nPhoneNo : " + this.phoneNo + 
               "\nPosition: " + this.position +
               "\nDate Joined: " + this.dateJoined +
               "\nPassword: " + this.password;
               
        
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