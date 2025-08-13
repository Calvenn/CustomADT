
package Entity;

import java.time.LocalDateTime;

/**
 *
 * @author tanjixian
 */

public abstract class Staff {
    // Variable
    private final String id;          // S0001
    private String name;        
    private int age;            
    private String phoneNo;     // 011-222 4568
    private String gender;      // Male - Female
    private String position;    // Doctor - Nurse - Admin
    private LocalDateTime dateJoined;   // 23 January 2023
    private static int staffCount = 0;
    
    // Constructor
    public Staff(){  }
    public Staff(String id, String name, int age, String phoneNo, String gender, String position, LocalDateTime dateJoined){
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
    public void setPosition(String position) { this.position = position; }
    
    // Getters
    public String getID(){ return id; }
    public String getName(){ return name; }
    public int getAge() { return age; }
    public String getPhoneNo() { return phoneNo; }
    public String getGender() { return gender; }
    public String getPosition() { return position; }
    public LocalDateTime getDateJoined() { return dateJoined; }
    
    // toString
    @Override
    public String toString(){
        return "HI";
    }
    
    // equals
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Staff)) return false;
        Staff other = (Staff) o;
        return id.equals(other.id);
    }
    
    // compareTo (ABSTRACT because comparing in child class is different)
    public abstract int compareTo(Staff other);
}