
package Boundary;

import java.util.Scanner;
import Entity.Staff.Position;
import Control.StaffManager;
import Control.DoctorManager;
import Entity.Staff;
import exception.InvalidInputException;
import exception.TryCatchThrowFromFile;
import exception.ValidationUtility;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


/**
 *
 * @author tanjixian
 */
public class StaffManagementUI {
        
    // Variables
    Scanner scanner = new Scanner(System.in);
    StaffManager staffManager;
    DoctorManager docManager;
    String choice = "1";
    
    // Constructor
    public StaffManagementUI(StaffManager staffManager, DoctorManager docManager){
        this.staffManager = staffManager;
        this.docManager = docManager;
    }
    
   //FUNCTIONS   
    // Confirmation Prompt
    public boolean confirmation(){
        String input;
        
        while(true){
            System.out.print("(Y/N): ");
            input = scanner.nextLine();
            try{
                TryCatchThrowFromFile.validateYesOrNo(input.charAt(0));
                break;
            } catch (InvalidInputException e){
                ValidationUtility.printErrorWithSolution(e);
            }
        }
        
        return (input.charAt(0) == 'Y');
    }
    
    // Edit Name
    public boolean editName(String id){
        String newName;
        
        System.out.println("\nCurrent Staff Name: " + staffManager.findStaff(id).getName());
        newName = name();
        
        System.out.println("\nAre you sure?");
        if(confirmation()){
            staffManager.findStaff(id).setName(newName);
            System.out.println("\nName Changed Successfully");
            return true;
        } else {
            System.out.println("\nFail to edit name, Press enter to return...");
            scanner.nextLine();
            return false;
        }
    }
    
    // Edit Age
    public boolean editAge(String id){
        int newAge;
        
        System.out.println("\nCurrent Staff Age: " + staffManager.findStaff(id).getAge());
        newAge = age();
        
        System.out.println("\nAre you sure?");
        if(confirmation()){
            staffManager.findStaff(id).setAge(newAge);
            System.out.println("\nAge Changed Successfully");
            return true;
        } else {
            System.out.println("\nFail to edit age, Press enter to return...");
            scanner.nextLine();
            return false;
        }
    }
    
    // Edit Gender
    public boolean editGender(String id){
        String newGender;
        
        System.out.println("\nCurrent Staff Age: " + staffManager.findStaff(id).getGender());
        newGender = gender();
        
        System.out.println("\nAre you sure?");
        if(confirmation()){
            staffManager.findStaff(id).setGender(newGender);
            System.out.println("\nGender Changed Successfully");
            return true;
        } else {
            System.out.println("\nFail to edit gender, Press enter to return...");
            scanner.nextLine();
            return false;
        }
    }
    
    // Edit Position
    public boolean editPosition(String id){
        Position newPosition;
        
        System.out.println("\nCurrent Staff Position: " + staffManager.findStaff(id).getPosition());
        newPosition = position();
        
        System.out.println("\nAre you sure?");
        if(confirmation()){
            staffManager.findStaff(id).setPosition(newPosition);
            System.out.println("\nPosition Changed Successfully");
            return true;
        } else {
            System.out.println("\nFail to edit position, Press enter to return...");
            scanner.nextLine();
            return false;
        }    
    }
    
    // Edit Password
    public boolean editPassword(String id){
        String newPassword;
        
        System.out.println("\nCurrent Staff Age: " + staffManager.findStaff(id).getPassword());
        newPassword = password();
        
        System.out.println("\nAre you sure?");
        if(confirmation()){
            staffManager.findStaff(id).setPassword(newPassword);
            System.out.println("\nPassword Changed Successfully");
            return true;
        } else {
            System.out.println("\nFail to edit password, Press enter to return...");
            scanner.nextLine();
            return false;
        }
    }
    
    // Edit Department
    public boolean editDepartment(String id){
        String newDepartment;
        
        System.out.println("\nCurrent Staff Age: " + docManager.findDoctor(id).getDepartment());
        newDepartment = department();
        
        System.out.println("\nAre you sure?");
        if(confirmation()){
            docManager.findDoctor(id).setDepartment(newDepartment);
            System.out.println("\nDepartment Changed Successfully");
            return true;
        } else {
            System.out.println("\nFail to edit department, Press enter to return...");
            scanner.nextLine();
            return false;
        }
    }
    
   // GET INPUTS
    // ID
    public String id(){
        String input;
        
        // Get ID
        while(true){
            System.out.print("Enter Staff ID: ");
            input = scanner.nextLine();
            try {
                TryCatchThrowFromFile.validateNotNull(input);
                staffManager.existStaff(input);
                break;
            } catch (InvalidInputException e){
                ValidationUtility.printErrorWithSolution(e);
            }
        }
        
        return input;
    }
    
    // Name
    public String name(){
        String input;
        
        // Get Name
        while(true){
            System.out.print("New Staff Name: ");
            input = scanner.nextLine();
            try {
                TryCatchThrowFromFile.validateNotNull(input);
                break;
            } catch (InvalidInputException e){
                ValidationUtility.printErrorWithSolution(e);
            }
        }
        
        return input;
    }
      
    // Age
    public int age(){
        
        String input;
        
        // Get Age
        while(true){
                System.out.print("Staff Age: ");
                input = scanner.nextLine();
                try{
                    TryCatchThrowFromFile.validatePositiveInteger(input);
                    break;
                } catch (InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
        
        return Integer.parseInt(input);
    }   
    
    // PhoneNo
    public String phoneNo(){
        String input;
        
        // Get Phone Number
        while(true){
            System.out.print("Phone Number: ");
            input = scanner.nextLine();
            try{
                TryCatchThrowFromFile.validatePhone(input);
                break;
            } catch (InvalidInputException e){
                ValidationUtility.printErrorWithSolution(e);
            }
        }
        
        return input;
    }
    
    // Gender
    public String gender(){
        String input = null;
        
        // Get Gender
        while(true){
            System.out.println("Gender - 1. Male");
            System.out.println("        2. Female");
            System.out.print("Select:  ");

            try{
                TryCatchThrowFromFile.validateIntegerRange(choice, 1, 2);
                break;
            } catch (InvalidInputException e){
                ValidationUtility.printErrorWithSolution(e);
            }

            switch(choice){
                case "1" -> input = "Male";
                case "2" -> input = "Female";
            }

        }
        
        return input;
        
    }
    
    // DateJoined
    public LocalDateTime dateJoined(){
        String input;
        
        // Get Date Joined
        while (true){
            System.out.print("Date Joined (2025-08-23 24:00) : ");
            input = scanner.nextLine();
            try{
                TryCatchThrowFromFile.validateDateTime(input);
                break;
            } catch (InvalidInputException e){
                ValidationUtility.printErrorWithSolution(e);
            }
        }
            
        return LocalDateTime.parse(input);
    }

    // Password
    public String password(){
        String input;
       
        // Get Password For Login
        while (true){
            System.out.print("Password: ");
            input = scanner.nextLine();
            try{
                TryCatchThrowFromFile.validateNotNull(input);
                break;
            } catch (InvalidInputException e){
                ValidationUtility.printErrorWithSolution(e);
            }
        }
        
        return input;
        
    }
    
    // Position
    public Position position(){
        Position position = null;
        
        // Get Staff Position
        while (true){
            System.out.println("\nStaff Position: 1. Admin");
            System.out.println("                  2. Doctor");
            System.out.println("                  3. Nurse");
            System.out.println("Select: ");
            scanner.nextLine();
            try{
                TryCatchThrowFromFile.validateIntegerRange(choice, 1, 3);
                break;
            } catch (InvalidInputException e){
                ValidationUtility.printErrorWithSolution(e);
            }
        }

        // Get position of new staff
        switch(choice){
            case "1" -> position = Position.valueOf("ADMIN");
            case "2" -> position = Position.valueOf("DOCTOR");
            case "3" -> position = Position.valueOf("NURSE");
        }
        
        return position;
        
    }
    
    // Department
    public String department(){
        String input = null;
        
        // Get Department
        while(true){
            System.out.println("Department - 1. Treatment");
            System.out.println("            2. Consult");
            System.out.print("Select: ");


            try{
                TryCatchThrowFromFile.validateIntegerRange(choice, 1, 2);
                break;
            } catch (InvalidInputException e){
                ValidationUtility.printErrorWithSolution(e);
            }

            switch(choice){
                case "1" -> input = "Treatment";
                case "2" -> input = "Consult";
            }           
        }
        
        return input;
    }
    
   // MENU
    // Menu Title
    public void printTitle(String title){
        int center = (35 + title.length()) / 2;
        System.out.println("\n===================================");
        System.out.printf("%" + center + "s\n", title);
        System.out.println("===================================");
    }
   
    // Staff Menu
    public void staffMenu(){
        while (true){ // Repeat the step if the user input invalid choice
           
            try {
               TryCatchThrowFromFile.validateIntegerRange(choice, 1, 5);
           } catch (InvalidInputException e){
               ValidationUtility.printErrorWithSolution(e);
           }
           
            printTitle("Staff Management Module");
            System.out.println("1. Register a new Staff");
            System.out.println("2. Edit Information");
            System.out.println("3. Remove Staff");
            System.out.println("4. View All Staff");
            System.out.println("5. Reports");
            System.out.println("6. Exit");  
            System.out.println("===============================");
            
            System.out.println("Enter your choice: ");
            choice = scanner.nextLine();

            switch(choice){
                case "1" -> addStaffMenu();
                case "2" -> editStaffMenu();
                case "3" -> removeStaffMenu();
                case "4" -> viewAllStaffMenu();
                case "5" -> reports();
                case "6" -> { // exit
                    System.out.println("\nThank you for using Staff Management System");
                    return;
                }
                default -> System.out.printf("\nInvalid choice entered. Please choose again.");
            }
            
        } // End of While Loop
    }
     
        // Add New Staff Menu
    public void addStaffMenu(){
        // Variables
        Position position = null;  // ENUM FOR ADMIN, DOCTOR, NURSE
        String name;
        int age;
        String phoneNo;     // 011-222 4568
        String gender;      // Male - Female
        LocalDateTime dateJoined;   // 23 January 2023
        String password;
        String department; // Treatment / Cosult !doctor only
        
        while (true){
            printTitle("Staff Management: Add New Staff");
            
            name = name();
            age = age();
            phoneNo = phoneNo();
            gender = gender();
            dateJoined = dateJoined();
            password = password();
            
            if(position.name().equals("DOCTOR")){
                // Get Department !!FOR DOCTOR ONLY
                department = department();
                
                if(docManager.addNewDoctor(name, age, phoneNo, gender, position, department, dateJoined, password)){
                    System.out.println("\nStaff Successfully Added");
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                }
            }
                // Add Staff
                if(staffManager.addNewStaff(name, age, phoneNo, gender, position, dateJoined, password)){
                    System.out.println("\nStaff Successfully Added");
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                    break;
                }
        }   // End of Loop
    }
    
    // Edit Staff Menu
    public void editStaffMenu(){
        String id;
        boolean cont = false;
        boolean success = false;
        
        printTitle("Staff Management: Edit Staff");
        System.out.println("\nStaff ID to edit (E.g. D0001)");
        id = id();

        // Print the Staff Information
        System.out.print(staffManager.findStaff(id).toString());

        while (true){
            System.out.print("\n\nWhat do you want to edit?\n");
            System.out.println("1. Name");
            System.out.println("2. Age");
            System.out.println("3. Gender");
            System.out.println("4. Position");
            System.out.println("5. Password");
            if(id.charAt(0) == 'D') {
                System.out.println("6. Department");
            }
            System.out.println("0. Exit");
            System.out.println("===============================");
            
            System.out.println("Enter your choice: ");
            choice = scanner.nextLine();

            try{
                TryCatchThrowFromFile.validateIntegerRange(choice, 0, 6);
                break;
            } catch (InvalidInputException e){
                ValidationUtility.printErrorWithSolution(e);
            }

        }

        switch(choice){
            case "0" -> cont = false;
            case "1" -> success = editName(id);
            case "2" -> success = editAge(id);
            case "3" -> success = editGender(id);
            case "4" -> success = editPosition(id);
            case "5" -> success = editPassword(id);
            case "6" -> success = editDepartment(id);
        }
        
        if (success){
            staffManager.findStaff(id).toString();
        }
        
        System.out.println("Continue to Edit?");
        cont = confirmation();
        
        if(cont){
            staffMenu();
        }
        
    }
    
    // Remove Staff Menu
    public void removeStaffMenu(){
        String id;
        String confirmation;
        printTitle("Staff Management: Remove Staff");
        System.out.println("\nStaff ID to delete (E.g. D0001)");
        id = id();
        System.out.println("Are you sure you want to remove " + staffManager.findStaff(id).getName() + " ?");
        
        if(confirmation()){
            staffManager.removeStaff(id);
            System.out.println("\nStaff Removed Successfully.");
            if(docManager.isDoctor(staffManager.findStaff(id))){
                docManager.removeDoctor(id);
            }
        } else {
            System.out.println("\nRequest Rejected / Declined, Removal was unsuccessful.");
        }
        
        System.out.println("\nEnter to Continue...");
        scanner.nextLine();
        
    }
    
    // View All Staff Menu
    public void viewAllStaffMenu(){
        printTitle("Staff Management: View All Staff");
        staffManager.printStaff(staffManager.viewAllStaff());
        System.out.println("\nPress Enter to Continue...");
        scanner.nextLine();
    }
    
   // REPORTING
    // Reporting
    public void reports(){
        
        while (true){
            printTitle("Staff Management: Reporting");
    
            System.out.println("1. Tenure Report");
            System.out.println("2. Staff Join Trend Report");
            System.out.println("0. Exit");  
            System.out.println("===============================");
            
            System.out.println("Enter your choice: ");
            choice = scanner.nextLine();
            try {
               TryCatchThrowFromFile.validateIntegerRange(choice, 0, 2);
               break;
            } catch (InvalidInputException e){
               ValidationUtility.printErrorWithSolution(e);
            }
        }
        
        switch(choice){
            case "1" -> tenureReport();
            case "2" -> staffJoinReport();
        }
    }
    
    // Tenure Report
    public void tenureReport(){
        Staff[] staff = staffManager.viewAllStaff();
        String displayName;
        LocalDateTime displayDate;
        Staff[] highestStaff = new Staff[20];
        int highStaffCount = 0;
        Staff[] lowestStaff = new Staff[20];
        int lowStaffCount = 0;
        long tenureYear;
        long tenureMonth;
        long tenureDay;
        long highestDay = 0;
        long lowestDay = 99999;
        
        System.out.println("\n+=========================================================================================+");
        System.out.println("|                CLINIC MANAGEMENT SYSTEM: STAFF TENURE REPORT                            |");
        System.out.println("+-----------------------------------------------------------------------------------------+");
        System.out.println("| Generated at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "                                                       |");
        System.out.println("+*****************************************************************************************+");
        System.out.println("| ID    | NAME                       | POSITION | DATE JOINED   | YEARS | MONTHS |  DAYS  |");
        System.out.println("+-------+----------------------------+----------+---------------+-------+--------+--------+");        
        for(Staff s: staff){    
            displayName = s.getName().length() > 26? s.getName().substring(0, 23) + "...": s.getName();
            displayDate = s.getDateJoined();
            tenureYear = ChronoUnit.YEARS.between(s.getDateJoined(), LocalDateTime.now());
            tenureMonth = ChronoUnit.MONTHS.between(s.getDateJoined(), LocalDateTime.now());
            tenureDay = ChronoUnit.DAYS.between(s.getDateJoined(), LocalDateTime.now());
            
             if(tenureDay >= highestDay){
                 highestDay = tenureDay;
            }
             
            if(tenureDay <= lowestDay){
                 lowestDay = tenureDay;
            }
            
            System.out.printf("| %5s | %-26s | %-8s | %2s/%2s/%4s    |  %2d   |  %3d   |  %4d  |\n", s.getID(), displayName, s.getPosition(), displayDate.getDayOfMonth(), displayDate.getMonthValue(), displayDate.getYear(), tenureYear, tenureMonth, tenureDay);
        }
        System.out.println("+-------+----------------------------+----------+---------------+-------+--------+--------+ ");
        
        for(Staff s: staff){
            if(ChronoUnit.DAYS.between(s.getDateJoined(), LocalDateTime.now()) >= highestDay){
                highestStaff[highStaffCount] = s;
                highStaffCount++;          
            }
            
            if (ChronoUnit.DAYS.between(s.getDateJoined(), LocalDateTime.now()) <= lowestDay){
                 lowestStaff[lowStaffCount] = s;
                 lowStaffCount++;
            }
        }
        
        System.out.println("\nStaff with the most experience: ");
        for(int i = 0; i < highStaffCount; i++){
            System.out.print("(Days: " + highestDay + ") " + lowestStaff[i].getName() + "\n");
        }

        System.out.println("\nStaff with the least experience: ");
        for(int i = 0; i < lowStaffCount; i++){
            System.out.print("(Days: " + lowestDay + ") " + lowestStaff[i].getName() + "\n");
        }
        
        System.out.println("\nPress Enter to Continue.");
        scanner.nextLine();
    }
    
    // Join Trend Report that show how many stuff join each year
    public void staffJoinReport(){
        LocalDateTime[] dateList = staffManager.getAllDateJoin();
        
        // Variables
        int n = dateList.length;
        int[] uniqueYears = new int[n];   // to store unique years
        int[] counts = new int[n];  // to store count of each year
        int uniqueCount = 0;
        int max = 0;
        
        for (int i = 0; i < n; i++) {
            int year = dateList[i].getYear();
            int position = -1;

            // check if year already recorded
            for (int j = 0; j < uniqueCount; j++) {
                if (uniqueYears[j] == year) {
                    position = j;
                    break;
                }
            }

            if (position == -1) {
                // Found a new unique year
                uniqueYears[uniqueCount] = year;
                counts[uniqueCount] = 1;
                uniqueCount++;
            } else {
                counts[position]++;
            }
        }
        
        // Find the Max count
        for(int i = 0; i < uniqueCount; i++){
            max = counts[i] > max ? counts[i] : max;
        }
        
        // Print Bar Chart (Data)
        System.out.println("\nStaff Join Trend (by Year)");
        System.out.println("\nNumber of Staff");
        System.out.println("   ^");
        
        
        for (int i = max + 2; i > 0; i--) {
            System.out.printf("%-2d | ", i);
            for (int j = 0; j < uniqueCount; j++) {
                if (counts[j] >= i) {
                    System.out.print("  | |  ");  // filled block
                } else if (counts[j] + 1 == i){
                    System.out.print("   _   ");  // spaces
                } else {
                    System.out.print("       ");  // spaces
                }
            }
            System.out.println();
        }
        
        // Print Bar Chart (Label)
        System.out.print("   ---------------------------------> Year\n     ");
        for (int i = 0; i < uniqueCount; i++) {
            System.out.printf("%4d ", uniqueYears[i]);
        }
        System.out.println();
        
        System.out.println("\nAdmin : " + Staff.getAdminCount());
        System.out.println("Doctor: " + Staff.getDoctorCount());
        System.out.println("Nurse : " + Staff.getNurseCount());
        System.out.println("-----------------");
        System.out.println("Total Staff: " + Staff.getTotalStaffCount());
        
        System.out.println("\nPress Enter to Continue.");
        scanner.nextLine();
    }
        
}
