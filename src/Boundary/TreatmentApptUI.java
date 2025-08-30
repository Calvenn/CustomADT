package Boundary;
import Control.TreatmentApptManager;
import Control.DoctorManager;
import Control.PaymentManager;
import adt.List;
import adt.Heap;
import Entity.TreatmentAppointment;
import Entity.Appointment;
import Entity.Doctor;
import Entity.Consultation;
import Entity.Payment;
import Entity.Treatment;
import exception.InvalidInputException;
import exception.TryCatchThrowFromFile;
import exception.ValidationUtility;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner; 

/**
 *
 * @author MeganYeohTzeXuan
 */
public class TreatmentApptUI {
    private final TreatmentApptManager treatmentApptManager;
    private final DoctorManager doctorManager; 
    private final TreatmentUI treatmentUI; 
    private final Scanner scanner; 
    private final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
    
    public TreatmentApptUI(TreatmentApptManager treatmentApptManager, DoctorManager doctorManager, TreatmentUI treatmentUI) {
        this.treatmentApptManager = treatmentApptManager; 
        this.doctorManager = doctorManager; 
        this.scanner = new Scanner(System.in); 
        this.treatmentUI = treatmentUI;
    }
    
    private void printTitle(String text, int length) {
        int center = (length + text.length()) / 2; 
        System.out.println();
        System.out.println("=".repeat(length)); 
        System.out.printf("%" + center + "s\n", text); 
        System.out.println("=".repeat(length)); 
    }
    
    public void treatmentApptMenu(String userID) {
        //check if is doctor 
        if(userID.toLowerCase().startsWith("d")) {
            //check doctor department
            if (!doctorManager.findDoctor(userID.toUpperCase()).getDepartment().equalsIgnoreCase("treatment")) {
                //if consult doctor
                printTitle("Treatment Appointment Menu", 35);
                System.out.println("Consult doctors are not allowed to access treatment appointment system.");
                System.out.println("Enter to continue...");
                scanner.nextLine();
                return;
            } else {
                //if treatment doctor
                showNextUI(userID);
            }
        }
        while(true) {
            String input; 
            
            printTitle("Treatment Appointment Menu", 35);
            System.out.println("1. Show All Incoming Appointments"); 
            System.out.println("2. Show Treatment Appointment History");
            System.out.println("3. Complete A Treatment");
            System.out.println("4. Cancel A Treatment");
            System.out.println("5. Back"); 
            
            while (true) {
                System.out.print("Choose > "); 
                input = scanner.nextLine(); 
                try {
                    TryCatchThrowFromFile.validateIntegerRange(input, 1, 5);
                    break; 
                } catch(InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            
            switch(Integer.parseInt(input)) {
                case 1 -> showIncomingUI(userID);   //accessible to admin, doctor (only self) 
                case 2 -> showHistoryUI(userID);    //accessible to admin, doctor (only self) 
                case 3 -> completeAppointmentUI(userID);    //only accessible to treatment doctor
                case 4 -> cancelAppointmentUI(userID);    //only accessible to treatment doctor
                case 5 -> {
                    System.out.println("Returning to main menu..."); 
                    return; 
                }
            }
        }
    }
    
    public String getValidID(String userID) {
        String doctorID; 
        if(userID.toLowerCase().startsWith("a")) {
            //search function to know which doctor to show 
            Doctor doctor = inputDoctorID();
            if(doctor == null) {
                return ""; 
            } else {
                doctorID = doctor.getID();
            }
        } else {
            doctorID = userID.toUpperCase(); 
        }
        return doctorID; 
    }
    
    public void showIncomingUI(String userID) {
        printTitle("Incoming Treatment Appointments", 40);
        String doctorID = getValidID(userID); 
        if(doctorID.isEmpty()) return; 
        Heap<Appointment> incoming = treatmentApptManager.getIncomingAppt(doctorID);
        
        System.out.println();
        if(incoming == null) {
            System.out.println("No record found!"); 
        } else {
            incoming.display();
        }
        
        System.out.println("Enter to return..."); 
        scanner.nextLine(); 
    }
    
    private Doctor inputDoctorID() {
        String doctorID; 
        Doctor doctor; 
        while (true) {
            System.out.println("Enter x to cancel.");
            System.out.print("Doctor ID: "); 
            doctorID = scanner.nextLine().trim();

            try {
                TryCatchThrowFromFile.validateNotNull(doctorID);
                if(checkCancel(doctorID)) return null;
                doctor = doctorManager.findDoctor(doctorID.toUpperCase()); 
                if(doctor == null) {
                    throw new InvalidInputException("Doctor is not found. Try again.");
                }
                if (!doctor.getDepartment().equalsIgnoreCase("treatment")) {
                    throw new InvalidInputException("Doctor ID entered is not treatment doctor. Try again.");
                }
                break; 
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
            
        }
        return doctor;
    }
    
    public void showHistoryUI(String userID) {
        printTitle("History Appointments", 35);
        
        //get doctor id, if retun empty string, means cancel search
        String doctorID = getValidID(userID); 
        if(doctorID.isEmpty()) return; 
        List<TreatmentAppointment> history = treatmentApptManager.getHistoryList(doctorID);
        
        System.out.println();
        if(history == null) {
            System.out.println("No record found!"); 
        } else {
            for(int i = 1; i <= history.size(); i++) {
                System.out.println(history.get(i));
            }
        }
        
        System.out.println("Enter to return..."); 
        scanner.nextLine(); 
    }
    
    public void showNextUI(String userID) {
        System.out.println();
        printTitle("Next Treatment Appointment", 35);

        TreatmentAppointment appt = treatmentApptManager.nextAppt(userID.toUpperCase());
        if(appt == null) {
            System.out.println("No record found!"); 
            System.out.println("Enter to return..."); 
            scanner.nextLine(); 
            return; 
        }
        System.out.println(appt); 
        System.out.println("Enter to return..."); 
        scanner.nextLine(); 
    }
    
    private boolean checkCancel(String input) {
        return input.trim().equalsIgnoreCase("x");
    }
    
    private LocalDateTime inputApptTime() {
        String timeString; 
        LocalDateTime apptTime; 
        while (true) {
            System.out.println("Instructions:");
            System.out.println("  - Enter 'x' to cancel");
            System.out.println("  - Time must be in 5-minute intervals");
            System.out.println("----------------------------------------");
            System.out.print("Appointment Time (yyyy-MM-dd HH:mm): ");
            timeString = scanner.nextLine().trim();

            try {
                if(checkCancel(timeString)) return null;
                TryCatchThrowFromFile.validateDateTime(timeString);
                apptTime = LocalDateTime.parse(timeString, DATE_FORMAT);
                
                if(!treatmentApptManager.validDateTime(apptTime)) {
                    throw new InvalidInputException("Appointment Time is not within business hours (8:00-17:00)\nOr not a future date time."); 
                }
                if(apptTime.getMinute() % 5 != 0) {
                    throw new InvalidInputException("Appointment Time is not in 5 minutes intervals.");
                }
                break; 
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }
        return apptTime; 
    }
    
    private TreatmentAppointment inputTrtApptID(String userID) {
        String trtApptID; 
        TreatmentAppointment appt; 
        while (true) {
            System.out.println("Enter x to cancel.");
            System.out.print("Treatment Appointment ID: "); 
            trtApptID = scanner.nextLine().trim();

            try {
                //check not null
                TryCatchThrowFromFile.validateNotNull(trtApptID);
                //check if entered x
                if(checkCancel(trtApptID)) return null;
                
                //find trtAppt by ID 
                appt = treatmentApptManager.searchAppt(userID, trtApptID);
                if(appt == null) {
                    throw new InvalidInputException("Treatment Appointment is not found. Try again.");
                }
                break; 
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
            
        }
        return appt;
    }
    
    public boolean addNewAppointmentUI(Consultation consultRec) {
        List<String> availableDoctors;
        List<LocalDateTime> bookedTime = new List<>();
        while(true) {
            printTitle("Add New Treatment Appointment", 35); 

            //getting appt time input 
            LocalDateTime apptTime;
            while(true) {
                apptTime = inputApptTime();
                if(apptTime == null) {
                    System.out.println("\nAdd treatment appointment cancelled.");
                    System.out.println("Enter to continue...");
                    scanner.nextLine();
                    return false;  
                }
                System.out.println(); 
                
                boolean hasConflict = false;
                if(!bookedTime.isEmpty()) {
                    for(int i = 1; i <= bookedTime.size(); i+=2) {
                        if(treatmentApptManager.startEndTimeConflict(bookedTime.get(i), bookedTime.get(i+1), apptTime)) {
                            System.out.println("Time has conflict with previous appointment at " + bookedTime.get(i).format(DATE_FORMAT));
                            System.out.println("Please enter another time.\n"); 
                            hasConflict = true; 
                            break; 
                        }
                    }
                }
                if(hasConflict) continue; 

                availableDoctors = treatmentApptManager.checkDoctorAvailability(apptTime); 
                if(availableDoctors.isEmpty()) {
                    System.out.println("No doctors available at " + apptTime.format(DATE_FORMAT)); 
                    System.out.println("Please enter another time.");
                    continue;
                }
                break;
            }
            
            //getting doctor input by print avialable doctors by time
            int doctorAmount = availableDoctors.size();
            System.out.println("List of Available Doctors: ");
            for(int i = 1; i <= doctorAmount; i++) {
                System.out.println(i + ". " + availableDoctors.get(i).toUpperCase()); 
            }
            System.out.println(doctorAmount+1 + ". Cancel");
            String input; 
            while (true) {
                System.out.print("Choose > "); 
                input = scanner.nextLine(); 
                try {
                    TryCatchThrowFromFile.validateIntegerRange(input, 1, doctorAmount+1);
                    break; 
                } catch(InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            
            int numberInput = Integer.parseInt(input);
            if(numberInput == doctorAmount+1) break; 
            Doctor doctor = doctorManager.findDoctor(availableDoctors.get(numberInput));
            System.out.println();
            
            //consult 
            Consultation consult = consultRec; 
            
            //treatment
            Treatment treatment = treatmentUI.searchForTreatment();
            if(treatment == null) break; 
            
            printTitle("Confirm Appointment Details", 56); 
            System.out.printf("| %-20s: %-30s |\n", "Appointment Time", apptTime.format(DATE_FORMAT));
            System.out.printf("| %-20s: %-30s |\n", "Doctor", String.format("%s - Dr. %s", doctor.getID(), doctor.getName()));
            System.out.printf("| %-20s: %-30s |\n", "Consult", String.format("%s - %s", consult.getID(), consult.getPatient().getPatientName()));
            System.out.printf("| %-20s: %-30s |\n", "Treatment", treatment.getName());
            System.out.println("=".repeat(56));
            
            while(true) {
                System.out.print("Confirm Appointment? (y/n): ");
                input = scanner.nextLine().toLowerCase(); 
                try {
                    TryCatchThrowFromFile.validateNotNull(input);
                    TryCatchThrowFromFile.validateYesOrNo(input.charAt(0));
                    break; 
                } catch (InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e); 
                }
            }
            if(input.charAt(0) == 'n') break; 
            treatmentApptManager.newTreatmentToHeap(doctor, consult, treatment, apptTime);  
            System.out.println("Add treatment appointment success!");
            
            //for cases where a consult requires multiple treatments 
            while(true) {
                System.out.print("Add another appointment for the same consult? (y/n): ");
                input = scanner.nextLine().toLowerCase(); 
                try {
                    TryCatchThrowFromFile.validateNotNull(input);
                    TryCatchThrowFromFile.validateYesOrNo(input.charAt(0));
                    break; 
                } catch (InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e); 
                }
            }
            //if yes, will return to top to key in another treatment appointment
            if(input.charAt(0) == 'y') {
                bookedTime.add(apptTime);
                bookedTime.add(apptTime.plusMinutes(treatment.getDuration().toMinutes()));
                continue;
            } 

            System.out.println("\nAdd treatment appointment completed!");
            System.out.println("Enter to continue...");
            scanner.nextLine();
            return true; 
        }
        System.out.println("\nAdd treatment appointment cancelled.");
        System.out.println("Enter to continue...");
        scanner.nextLine();
        return false; 
    }
    
    public void completeAppointmentUI(String userID) {
        printTitle("Complete Treatment Appointment", 35);
        
        //check if user is admin or doctor 
        //if admin, deny access
        if(userID.toLowerCase().startsWith("a")) {
            System.out.println("Admin denied access to complete treatment appointment."); 
            System.out.println("Enter to return..."); 
            scanner.nextLine(); 
            return;
        }
        
        TreatmentAppointment next = treatmentApptManager.nextAppt(userID);
        
        if(next == null) {
            System.out.println("No appointment to complete."); 
            System.out.println("Enter to return..."); 
            scanner.nextLine(); 
            return;
        }
        
        System.out.println(next); 
        
        String input;
        while(true) {
            System.out.print("Complete this treatment? (y/n): "); 
            input = scanner.nextLine(); 
            try {
                TryCatchThrowFromFile.validateNotNull(input);
                TryCatchThrowFromFile.validateYesOrNo(input.charAt(0));
                if(input.length() != 1) {
                    throw new InvalidInputException("Please enter y or n only.");
                }
                break; 
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e); 
            }
        }
        
        if(input.charAt(0) == 'n') {
            System.out.println("\nComplete treatment appointment cancelled.");
            System.out.println("Enter to continue...");
            scanner.nextLine();
            return; 
        }
        if(treatmentApptManager.completeAppt(userID)) {
            System.out.println("\nTreatment Appointemnt completed!");
            System.out.println("You can now view it from treatment appointment history.");
            PaymentManager.paymentRec.add(new Payment(next.getPatient(), next.getConsultation(), next.getTreatment().getPrice(), false, next, null));
            System.out.println("Please ask the patient to pay at the counter.");
            System.out.println("Enter to continue...");
            scanner.nextLine();
            return; 
        }
        System.out.println("\nFailed to complete treatment.");
        System.out.println("Please check if the appointment time has reached.");
        System.out.println("Enter to continue...");
        scanner.nextLine();
        
    }
    
    //cancel appointment -- remove from appt list 
    public void cancelAppointmentUI(String userID) {
        printTitle("Cancel Treatment Appointment", 35); 
        
        //check if user is admin or doctor 
        //if admin, deny access
        if(userID.toLowerCase().startsWith("a")) {
            System.out.println("Admin denied access to cancel treatment appointment."); 
            System.out.println("Enter to return..."); 
            scanner.nextLine(); 
            return;
        }
        
        while(true) {
            TreatmentAppointment toCancel = inputTrtApptID(userID); 
            if(toCancel == null) break;
            
            System.out.println(toCancel);

            String input; 
            while(true) {
                System.out.print("Confirm cancel appointment? (y/n): ");
                input = scanner.nextLine().toLowerCase(); 
                try {
                    TryCatchThrowFromFile.validateNotNull(input);
                    TryCatchThrowFromFile.validateYesOrNo(input.charAt(0));
                    break; 
                } catch (InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e); 
                }
            }
            if(input.charAt(0) == 'n') break; 
            treatmentApptManager.cancelTreatmentAppt(toCancel);
            System.out.println("Cancel treatment appointment success!");
            System.out.println("Enter to continue...");
            scanner.nextLine();
            return; 
        }
        
        System.out.println("Cancelled cancelling treatment appointment.");
        System.out.println("Enter to continue...");
        scanner.nextLine();
    }
    
}