package exception;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Scanner;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author calve
 */
public class ValidationHelper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Scanner scanner = new Scanner(System.in);
    
    public static int inputValidatedChoice(int min, int max) {
        while (true) {
            System.out.print("Enter your choice: ");
            String input = scanner.nextLine().trim();
            try {
                TryCatchThrowFromFile.validateIntegerRange(input, min, max);
                return Integer.parseInt(input);
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }
    }

    public static LocalDateTime inputValidatedDateTime(String prompt) {
        while (true) {
            System.out.print(prompt + " (yyyy-MM-dd HH:mm): ");
            String input = scanner.nextLine().trim();
            
            try {
                TryCatchThrowFromFile.validateDateTime(input);
                LocalDateTime dt = LocalDateTime.parse(input, formatter);
                
                System.out.println("Parsed date/time: " + dt);

                if (dt.isBefore(LocalDateTime.now())) {
                    System.out.println("Cannot enter a past date/time. Try again.\n");
                    continue;
                }

                return dt; // valid & not in past

            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            } 
        }
    }
    
    public static String validateICOnce(String ic) {
        try {
            TryCatchThrowFromFile.validateIC(ic);
            return ic; // valid
        } catch (InvalidInputException e) {
            ValidationUtility.printErrorWithSolution(e);
            return null; // invalid, caller decides what to do
        }
    }

    public static String inputValidatedIC(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String ic = scanner.nextLine().trim();
            try {
                TryCatchThrowFromFile.validateIC(ic);
                return ic;
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }
    }
    
    public static char inputValidateYesOrNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please enter Y or N.");
                continue;
            }

            char confirm = Character.toUpperCase(input.charAt(0)); // normalize to uppercase

            try {
                TryCatchThrowFromFile.validateYesOrNo(confirm); // your custom validation
                return confirm;
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }
    }
}

