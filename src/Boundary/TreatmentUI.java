package Boundary;
import Entity.Treatment; 
import Control.TreatmentManager;
import exception.InvalidInputException;
import adt.Heap; 
import adt.List; 
import java.util.Scanner; 
import java.time.Duration; 
import exception.TryCatchThrowFromFile;
import exception.ValidationUtility;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; 
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MeganYeohTzeXuan
 */

public class TreatmentUI {
    private final TreatmentManager treatmentManager; 
    private final Scanner scanner;
    private final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
    private final int CHART_WIDTH = 35;
    
    public TreatmentUI(TreatmentManager treatmentManager) {
        this.treatmentManager = treatmentManager; 
        scanner = new Scanner(System.in);
    }
    
    private void printTitle(String text, int length) {
        int center = (length + text.length()) / 2; 
        System.out.println();
        System.out.println("=".repeat(length)); 
        System.out.printf("%" + center + "s\n", text); 
        System.out.println("=".repeat(length)); 
    }
    
    public void treatmentMenu() {
        while(true) {
            String input; 
            printTitle("Treatment Menu", 35);
            System.out.println("1. Show All Treatments"); 
            System.out.println("2. Search A Treatment"); 
            System.out.println("3. Add New Treatment"); 
            System.out.println("4. Update A Treatment");
            System.out.println("5. Remove A Treatment");
            System.out.println("6. Generate Reports");
            System.out.println("7. Back"); 
            
            while (true) {
                System.out.print("Choose > "); 
                input = scanner.nextLine(); 
                try {
                    TryCatchThrowFromFile.validateIntegerRange(input, 1, 7);
                    break; 
                } catch(InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            
            switch(Integer.parseInt(input)) {
                case 1 -> showTreatmentsUI();
                case 2 -> searchTreatmentUI(); 
                case 3 -> addTreatmentUI();
                case 4 -> modifyTreatmentUI();
                case 5 -> deleteTreatmentUI();
                case 6 -> generateReportUI(); 
                case 7 -> {
                    System.out.println("Returning to main menu..."); 
                    return; 
                }
            }
        }
    }
    
    public void showTreatmentsUI() {
        printTitle("Treatments List", 35);
        Treatment[] treatments = treatmentManager.displayAllTreatments(); 
        for(Treatment trt : treatments) {
            System.out.println(trt);
        }
        System.out.println("Enter to return..."); 
        scanner.nextLine(); 
    }
    
    public void searchTreatmentUI() {  
        String input; 
        while (true) {
            printTitle("Search Treatments", 35); 
            Treatment treatment = searchForTreatment(); 
            if(treatment == null) {
                return; 
            }
            System.out.println("\nFound Treatment!\n"); 
            System.out.println(treatment); 
            
            //ask if want to search another
            while (true) {
                System.out.print("Search another treatment? (y/n): "); 
                input = scanner.nextLine(); 
                try {
                    TryCatchThrowFromFile.validateNotNull(input);
                    TryCatchThrowFromFile.validateYesOrNo(input.charAt(0));
                    break;
                } catch (InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            if(input.charAt(0) == 'n') {
                return; 
            }
        }
    }
    
    private boolean checkCancel(String input) {
        return input.trim().equalsIgnoreCase("x");
    }
    
    private String inputName() {
        String name; 
        while (true) {
            System.out.println("Enter x to cancel.");
            System.out.print("Treatment Name: "); 
            name = scanner.nextLine().trim();

            try {
                TryCatchThrowFromFile.validateNotNull(name);
                if(checkCancel(name)) return null;
                if(treatmentManager.treatmentExist(name)) {
                    throw new InvalidInputException("Treatment " + name + " already exist."); 
                }
                break; 
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }
        return name; 
    }
    
    private String inputDescription() {
        System.out.println("Enter x to cancel.");
        System.out.print("Description: ");
        String description = scanner.nextLine(); 
        if(checkCancel(description)) return null; 
        return description; 
    }
    
    private Duration inputDuration() {
        String duration; 
        while(true) {
            System.out.println("Enter x to cancel.");
            System.out.print("Duration (minutes): ");
            duration = scanner.nextLine();

            try {
                TryCatchThrowFromFile.validatePositiveInteger(duration);
                if(checkCancel(duration)) return null;
                break; 
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e); 
            }
        }
        return Duration.ofMinutes(Integer.parseInt(duration)); 
    }
    
    private double inputPrice() {
        String price; 
        while(true) {
            System.out.println("Enter x to cancel.");
            System.out.print("Price (RM): ");
            price = scanner.nextLine();

            try {
                if(checkCancel(price)) return 0;
                TryCatchThrowFromFile.validatePrice(price);
                break; 
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e); 
            }
        }
        return Double.parseDouble(price); 
    }
    
    public void addTreatmentUI() {
        printTitle("Add Treatment", 35);
        
        while(true) {
            String name = inputName(); 
            if(name == null) break; 
            System.out.println();
            
            String description = inputDescription(); 
            if(description == null) break; 
            System.out.println();
            
            Duration duration = inputDuration(); 
            if(duration == null) break; 
            System.out.println();
            
            double price = inputPrice();
            if(price == 0) break; 
            System.out.println();

            treatmentManager.newTreatment(name, description, duration, price); 
            System.out.println("Add treatment success!");
            System.out.println("Enter to continue...");
            scanner.nextLine();
            return; 
        }
        System.out.println("\nAdd treatment cancelled.");
        System.out.println("Enter to continue...");
        scanner.nextLine();
    }
    
    public Treatment searchForTreatment() {
        String input;  
        Treatment treatment;
        while(true) {
            while (true) {
                System.out.print("Enter Treatment Name to search (x to Back): "); 
                input = scanner.nextLine();
                try {
                    TryCatchThrowFromFile.validateNotNull(input);
                    break;
                } catch (InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            if(input.matches("x") || input.matches("X")) {
                return null;
            } else {
                treatment = treatmentManager.findTreatmentName(input);
            }
            if(treatment == null) {
                System.out.println("Treatment is not found."); 
                while (true) {
                    System.out.print("Try again? (y/n): "); 
                    input = scanner.nextLine(); 
                    try {
                        TryCatchThrowFromFile.validateNotNull(input);
                        TryCatchThrowFromFile.validateYesOrNo(input.charAt(0));
                        break;
                    } catch (InvalidInputException e) {
                        ValidationUtility.printErrorWithSolution(e);
                    }
                }
                if(input.charAt(0) == 'n') {
                    return null; 
                }
            } else {
                return treatment;
            }
        }
        
    }
    
    public void modifyTreatmentUI() {
        while(true) {
            printTitle("Modify Treatment", 35); 
            Treatment treatment = searchForTreatment(); 
            if(treatment == null) {
                return; 
            }
            
            System.out.println("=".repeat(20));
            System.out.println("Treatment found!");
            
            System.out.println(treatment); 
            
            String input; 
            
            System.out.println("Select field to modify: "); 
            System.out.println("1. Description"); 
            System.out.println("2. Duration");
            System.out.println("3. Price");
            System.out.println("4. Back"); 
            
            while (true) {
                System.out.print("Choose > "); 
                input = scanner.nextLine(); 
                try {
                    TryCatchThrowFromFile.validateIntegerRange(input, 1, 4);
                    break; 
                } catch(InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            System.out.println();
            try {
                switch(Integer.parseInt(input)) {
                    case 1 -> {
                        String newDescription = inputDescription();
                        String oldDescription = treatment.getDescription();
                        if(confirmChange(oldDescription, newDescription)) {
                            if(!treatmentManager.changeDescription(treatment, newDescription)) {
                                throw new InvalidInputException("No changes made due to same data.");
                            }
                        } else {
                            System.out.println("Cancelled modify description.");
                        }
                        break; 
                    }
                    case 2 -> {
                        Duration newDuration  = inputDuration();
                        Duration oldDuration = treatment.getDuration();
                        if(confirmChange(oldDuration.toMinutes() + " minutes", newDuration.toMinutes() + " minutes")) {
                            if(!treatmentManager.changeDuration(treatment, newDuration)) {
                                throw new InvalidInputException("No changes made due to same data.");
                            }
                        } else {
                            System.out.println("Cancelled modify duration.");
                        }
                        break; 
                    }
                    case 3 -> {
                        double newPrice  = inputPrice();
                        double oldPrice = treatment.getPrice();
                        if(confirmChange(String.format("RM %.02f", oldPrice), String.format("RM %.02f", newPrice))) {
                            if(!treatmentManager.changePrice(treatment, newPrice)) {
                                throw new InvalidInputException("No changes made due to same data.");
                            }
                        } else {
                            System.out.println("Cancelled modify price.");
                        }
                        break; 
                    }
                    case 4 -> {
                        break; 
                    }
                }
            } catch(InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e); 
            } 
            
            printTitle("After Changes", 35);
            System.out.println(treatment); 
            
            char another; 
            while (true) {
                System.out.print("Modify another treatment? (y/n): "); 
                another = scanner.next().charAt(0);
                scanner.nextLine();
                try {
                    TryCatchThrowFromFile.validateNotNull(input);
                    TryCatchThrowFromFile.validateYesOrNo(another);
                    break; 
                } catch(InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            
            if(Character.toLowerCase(another) == 'n') {
                System.out.println("Returning to treatment menu...\n"); 
                return; 
            }
        }
        
    }
    
    private boolean confirmChange(String before, String after) {
        printTitle("Confirm Changes",35);
        System.out.println("Before: " + before); 
        System.out.println("After: " + after); 
        char input; 
        while(true) {
            System.out.print("Confirm modification? (y/n): "); 
            input = scanner.next().charAt(0);
            try {
                TryCatchThrowFromFile.validateNotNull(input);
                TryCatchThrowFromFile.validateYesOrNo(input);
                break; 
            } catch(InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }
        return Character.toLowerCase(input) == 'y';
    }
    
    public void deleteTreatmentUI() {
        char input; 
        while(true) {
            printTitle("Delete Treatment", 35);
            Treatment treatment = searchForTreatment(); 
            if(treatment == null) {
                return; 
            }
            
            System.out.println(treatment);
            while(true) {
                System.out.print("Confirm delete? (y/n): "); 
                input = scanner.nextLine().charAt(0); 
                try {
                    TryCatchThrowFromFile.validateNotNull(input);
                    TryCatchThrowFromFile.validateYesOrNo(input);
                    break; 
                } catch(InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            System.out.println(); 
            
            if(Character.toLowerCase(input) == 'y') {
                if(!treatmentManager.deleteTreatment(treatment)) {
                    System.out.println("An error occured where the delete failed. Please try again.");
                } else {
                    System.out.println("Delete success!"); 
                }
            }
            
            while(true) {
                System.out.print("Delete another record? (y/n): "); 
                input = scanner.nextLine().charAt(0); 
                try {
                    TryCatchThrowFromFile.validateNotNull(input);
                    TryCatchThrowFromFile.validateYesOrNo(input);
                    break; 
                } catch(InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            System.out.println(); 

            if(Character.toLowerCase(input) == 'n') {
                System.out.println("Returning to treatment menu...\n"); 
                return; 
            }
        }
        
    }
    
    public void generateReportUI() {
        String input; 
        while(true) {
            printTitle("Treatment Reports", 35); 
            System.out.println("Select report to generate: "); 
            System.out.println("1. Treatment Frequency Report"); 
            System.out.println("2. Time Allocation Report"); 
            System.out.println("3. Treatment Revenue Analysis Report"); 
            System.out.println("4. Back"); 
            
            while (true) {
                System.out.print("Choose > "); 
                input = scanner.nextLine(); 
                try {
                    TryCatchThrowFromFile.validateIntegerRange(input, 1, 4);
                    break; 
                } catch(InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e);
                }
            }
            
            switch(Integer.parseInt(input)) {
                case 1 -> treatmentFrequencyReportUI();
                case 2 -> timeAllocationReportUI(); 
                case 3 -> revenueAnalysisReportUI(); 
                case 4 -> {
                    System.out.println("Returning to treatment menu..."); 
                    return; 
                }
            }
            
            printTitle("End of Report", 100); 
            while(true) {
                System.out.print("Generate another report? (y/n): ");
                input = scanner.nextLine(); 
                try {
                    TryCatchThrowFromFile.validateYesOrNo(input.charAt(0));
                    break; 
                } catch (InvalidInputException e) {
                    ValidationUtility.printErrorWithSolution(e); 
                }
            }
            if(input.charAt(0) == 'n') return; 
        }
    }
    
    private void treatmentFrequencyReportUI() {
        generateReportHeader("Treatment Frequency Report");
        Heap<Treatment> treatmentHeap = treatmentManager.getFrequencyReport(); 
        
        if(treatmentHeap == null) {
            System.out.println("No data is found."); 
            return; 
        }
        
        int heapSize = treatmentHeap.size(); 

        System.out.println("_".repeat(100)); 
        System.out.printf("%-13s | %-30s | %-10s \n", "Treatment ID", "Treatment Name", "Frequency");
        System.out.println("-".repeat(100)); 
        
        for(int i = 0; i < heapSize; i++) {
            Treatment treatment = treatmentHeap.extractRoot();
            System.out.printf("%-13s | %-30s | %-10d \n", treatment.getTreatmentId(), treatment.getName(), treatment.getFrequency());
        }
        System.out.println("-".repeat(100)); 
        
        System.out.println("Total Treatments: " + treatmentManager.totalTreatments() + "\n"); 
        
        System.out.println("=".repeat(100)); 

        treatmentHeap = treatmentManager.getFrequencyReport();
        int maximum = treatmentHeap.peekRoot().getFrequency();
        
        System.out.printf("%-30s ", "Treatment");
        printBar(CHART_WIDTH, false);
        System.out.printf("  %10s\n", "Frequency");
        
        for(int i = 0; i < heapSize; i++) {
            Treatment trt = treatmentHeap.extractRoot();
            int frequency = trt.getFrequency();
            
            //get bar length by finding the percentage it should take up on chart width (so wont overflow if many data) 
            //so first find the ratio of frequency to the biggest frequency of the data 
            //then need see this ratio will take up how many of this chart 
            int barLength = (int) Math.round((double) frequency / maximum * CHART_WIDTH);

            System.out.printf("%-30s ", trt.getName());
            printBar(barLength, true); 
            printBar((CHART_WIDTH - barLength), false);
            System.out.printf("%5d\n", frequency);
        }

    }
    
    private void timeAllocationReportUI() {
        generateReportHeader("Time Allocation Report");
        List<Treatment> treatmentList = treatmentManager.getTimeAllocationReport(); 
        
        if(treatmentList == null) {
            System.out.println("No data is found."); 
            return; 
        }
        
        int listSize = treatmentList.size(); 
        
        System.out.println("_".repeat(100)); 
        System.out.printf("%-13s | %-30s | %-10s | %-10s | %-15s \n", "Treatment ID", "Treatment Name", "Frequency", "Duration", "Time Allocated");
        System.out.println("-".repeat(100)); 
        
        Duration totalTime = Duration.ZERO; 
        
        for(int i = 1; i <= listSize; i++) {
            Treatment treatment = treatmentList.get(i);
            System.out.printf("%-13s | %-30s | %-10d | %-10s | %-15s \n", treatment.getTreatmentId(), treatment.getName(), treatment.getFrequency(), treatment.getDuration().toMinutes() + " minutes", treatment.getTimeAllocation().toMinutes() + " minutes");
            totalTime = totalTime.plus(treatment.getTimeAllocation());
        }
        
        System.out.println("-".repeat(100)); 
        System.out.println("Total Treatments: " + treatmentManager.totalTreatments()); 
        System.out.println("Total Time Allocated on Treatment: " + totalTime.toMinutes() + " minutes\n");
        
        System.out.println("=".repeat(100)); 

        int maximum = (int) treatmentList.get(1).getTimeAllocation().toMinutes();
        
        System.out.printf("%-30s ", "Treatment");
        printBar(CHART_WIDTH, false);
        System.out.printf("  %10s\n", "Duration (min)");
        for(int i = 1; i <= listSize; i++) {
            Treatment trt = treatmentList.get(i);
            int duration = (int) trt.getTimeAllocation().toMinutes();
            
            //get bar length by finding the percentage it should take up on chart width (so wont overflow if many data) 
            //so first find the ratio of frequency to the biggest frequency of the data 
            //then need see this ratio will take up how many of this chart 
            int barLength = (int) Math.round((double) duration / maximum * CHART_WIDTH);

            System.out.printf("%-30s ", trt.getName());
            printBar(barLength, true); 
            printBar((CHART_WIDTH - barLength), false);
            System.out.printf("%5d\n", duration);
        }

    }
    
    private void revenueAnalysisReportUI() {
        generateReportHeader("Treatment Revenue Analysis Report");
        List<Treatment> treatmentList = treatmentManager.getTreatmentRevenueReport();
        
        if(treatmentList == null) {
            System.out.println("No data is found."); 
            return; 
        }
        
        int listSize = treatmentList.size(); 
        
        System.out.println("_".repeat(100)); 
        System.out.printf("%-13s | %-30s | %-10s | %-10s | %-15s \n", "Treatment ID", "Treatment Name", "Frequency", "Price", "Total Revenue");
        System.out.println("-".repeat(100)); 
        
        double totalEarned = 0; 
        
        for(int i = 1; i <= listSize; i++) {
            Treatment treatment = treatmentList.get(1);
            System.out.printf("%-13s | %-30s | %-10d | %-10s | %-15s \n", treatment.getTreatmentId(), treatment.getName(), treatment.getFrequency(), String.format("RM %.02f", treatment.getPrice()), String.format("RM %.02f", treatment.getEarned()));
            totalEarned += treatment.getEarned();
        }
        
        System.out.println("-".repeat(100)); 
        System.out.println("Total Treatments: " + treatmentManager.totalTreatments()); 
        System.out.println("Total Revenue from Treatment: " + String.format("RM %.02f", totalEarned) + "\n");
        
        System.out.println("=".repeat(100)); 

        double maximum = (double) treatmentList.get(1).getEarned();
        
        System.out.printf("%-30s ", "Treatment");
        printBar(CHART_WIDTH, false);
        System.out.printf("  %10s\n", "Revenue (RM)");
        for(int i = 1; i <= listSize; i++) {
            Treatment trt = treatmentList.get(i);
            double revenue = trt.getEarned();
            
            //get bar length by finding the percentage it should take up on chart width (so wont overflow if many data) 
            //so first find the ratio of frequency to the biggest frequency of the data 
            //then need see this ratio will take up how many of this chart 
            int barLength = (int) Math.round(revenue / maximum * CHART_WIDTH);

            System.out.printf("%-30s ", trt.getName());
            printBar(barLength, true); 
            printBar((CHART_WIDTH - barLength), false);
            System.out.printf("%10.02f\n", revenue);
        }
    }
    
    private void generateReportHeader(String header) {
        System.out.println("-".repeat(100)); 
        printTitle(header, 100); 
        System.out.println("Generated at: " + LocalDateTime.now().format(DATE_FORMAT)); 
    }
    
    private void printBar(int repeat, boolean black) {
        try {
            byte[] bar = black ? "▬".getBytes("UTF-8") : "　".getBytes("UTF-8");
            for (int i = 0; i < repeat; i++) {
                System.out.write(bar);
            }
        } catch (UnsupportedEncodingException ex) {
            System.out.print("=".repeat(repeat));
        } catch (IOException ex) {
            System.out.print("=".repeat(repeat));
        }
    }
}