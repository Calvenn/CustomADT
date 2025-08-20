package exception;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class ValidationUtility {
    
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(\\+?6?01[0-9]-*[0-9]{7,8})$");
    private static final Pattern IC_PATTERN = Pattern.compile("^\\d{6}-\\d{2}-\\d{4}$");
    
    // Check if value is null or empty
    public static boolean isNullOrEmpty(Object value) {
        return value == null || (value instanceof String && ((String) value).trim().isEmpty());
    }
    
    // Check if string is a valid integer
    public static boolean isValidInteger(String value) {
        if (isNullOrEmpty(value)) return false;
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Check if string is a valid integer within range
    public static boolean isValidIntegerRange(String value, int min, int max) {
        if (!isValidInteger(value)) return false;
        int intValue = Integer.parseInt(value);
        return intValue >= min && intValue <= max;
    }
    
    // Check if string is valid date time in format "dd-MM-yyyy HH:mm"
    public static boolean isValidDateTime(String dateTimeStr) {
        if (isNullOrEmpty(dateTimeStr)) return false;

        String pattern = "dd-MM-yyyy HH:mm";

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime.parse(dateTimeStr.trim(), formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    // Check if phone number is valid
    public static boolean isValidPhone(String phone) {
        if (isNullOrEmpty(phone)) return false;
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }
    
    // Check if IC number is valid
    public static boolean isValidIC(String ic) {
        if (isNullOrEmpty(ic)) return false;
        return IC_PATTERN.matcher(ic.trim()).matches();
    }
    
    // Check if gender is valid
    public static boolean isValidGender(char gender) {
        char upperGender = Character.toUpperCase(gender);
        return upperGender == 'M' || upperGender == 'F';
    }

    public static boolean isValidYesOrNo(char value) {
        char upperValue = Character.toUpperCase(value);
        return upperValue == 'Y' || upperValue == 'N';
    }
    
    // Simplified: Automatically detect error type and show relevant solution
    public static void showSolutionFromFile(String errorMessage) {
        String errorType = detectErrorType(errorMessage);
        showSpecificSolution(errorType);
    }
    
    // Detect error type based on error message only
    private static String detectErrorType(String errorMessage) {
        if (errorMessage == null) {
            return "GENERAL ERROR";
        }

        String msg = errorMessage.toLowerCase();

        if (msg.contains("field cannot be null or empty")) {
            return "NULL VALUE ERROR";
        }
        if (msg.contains("value must be between the range of")) {
            return "RANGE ERROR";
        }
        if (msg.contains("value must be a positive integer") ||
            msg.contains("invalid integer format")) {
            return "INTEGER ERROR";
        }
        if (msg.contains("invalid date format")) {
            return "DATE TIME ERROR";
        }
        if (msg.contains("invalid phone number format")) {
            return "PHONE ERROR";
        }
        if (msg.contains("invalid ic number format")) {
            return "IC ERROR";
        }

        return "GENERAL ERROR";
    }

    
    // Show specific solution section from file
    private static void showSpecificSolution(String errorType) {
        try (BufferedReader br = new BufferedReader(new FileReader("src\\solution.txt"))){

            String line;
            boolean foundSection = false;
            boolean inTargetSection = false;
            
            while ((line = br.readLine()) != null) {
                // Check if we found the target section
                if (line.contains(errorType)) {
                    foundSection = true;
                    inTargetSection = true;
                    System.out.println(line);
                    continue;
                }
                
                // If we're in the target section, print until next section or end
                if (inTargetSection) {
                    // Stop if we hit the next section (line of equal signs)
                    if (line.startsWith("================================================================================") && foundSection) {
                        break;
                    }
                    System.out.println(line);
                }
            }
            System.out.println("=".repeat(50));
            
        } catch (IOException e) {
            System.out.println("Could not read solution file: " + e.getMessage());
        }
    }

    // Inside ValidationUtility
    public static void printErrorWithSolution(Exception e) {
        System.out.println("\n===================================================");
        System.out.println("\nError: " + e.getMessage());
        showSolutionFromFile(e.getMessage());
        System.out.println("Please try again.\n");
    }

}