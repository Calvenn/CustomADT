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
 * @author CalvenPhnuahKahHong
 */
public class ValidationHelper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Scanner scanner = new Scanner(System.in);
    
    public static int inputValidatedChoice(int min, int max, String prompt) {
        while (true) {
            System.out.print("Enter " + prompt + ": ");
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

                TryCatchThrowFromFile.validateIsWorkingHour(input);
                TryCatchThrowFromFile.validateIsPastDate(input);
                return dt;
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
    
    public static String inputValidatedStudentID(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String studentID = scanner.nextLine().trim();
            try {
                TryCatchThrowFromFile.validateStudentID(studentID);
                return studentID;
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
    
    public static String inputValidatedPhone(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                TryCatchThrowFromFile.validatePhone(input);
                return input;
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }
    }

    public static String inputValidatedString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                TryCatchThrowFromFile.validateNotNull(input);
                return input;
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }
    }

    public static int inputValidatedPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                TryCatchThrowFromFile.validatePositiveInteger(input);
                return Integer.parseInt(input);
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }
    }

    public static char inputValidatedGender(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();
            char gender = input.charAt(0);
            try {
                TryCatchThrowFromFile.validateGender(gender);
                return gender;
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }
    }

    // Allows user to input a value but keeps current value if empty
    public static String inputOptionalValidatedString(String prompt, String currentValue) {
        while (true) {
            System.out.print(prompt + " [" + currentValue + "]: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return currentValue;
            try {
                TryCatchThrowFromFile.validateNotNull(input);
                return input;
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }
    }

    public static String inputOptionalValidatedPhone(String prompt, String currentValue) {
        while (true) {
            System.out.print(prompt + " [" + currentValue + "]: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return currentValue;
            try {
                TryCatchThrowFromFile.validatePhone(input);
                return input;
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }
    }

    public static int inputOptionalValidatedPositiveInt(String prompt, int currentValue) {
        while (true) {
            System.out.print(prompt + " [" + currentValue + "]: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return currentValue;
            try {
                TryCatchThrowFromFile.validatePositiveInteger(input);
                return Integer.parseInt(input);
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }
    }

    public static char inputOptionalValidatedGender(String prompt, char currentValue) {
        while (true) {
            System.out.print(prompt + " [" + currentValue + "]: ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.isEmpty()) return currentValue;
            char gender = input.charAt(0);
            try {
                TryCatchThrowFromFile.validateGender(gender);
                return gender;
            } catch (InvalidInputException e) {
                ValidationUtility.printErrorWithSolution(e);
            }
        }
    }


}

