package exception;

public class TryCatchThrowFromFile {

    // Validate not null or empty (String or Object)
    public static void validateNotNull(Object value) throws InvalidInputException {
        if (ValidationUtility.isNullOrEmpty(value)) {
            throw new InvalidInputException("Field cannot be null or empty.");
        }
    }

    // Validate positive integer from String
    public static void validatePositiveInteger(String value) throws InvalidInputException {
        validateNotNull(value);
        if (!ValidationUtility.isValidInteger(value)) {
            throw new InvalidInputException("Invalid integer format.");
        }

        int intValue = Integer.parseInt(value);
        if (intValue < 0) {
            throw new InvalidInputException("Value must be a positive integer.");
        }
    }

    // Validate positive integer within range from String
    public static void validateIntegerRange(String value, int min, int max) throws InvalidInputException {
        validatePositiveInteger(value);

        int intValue = Integer.parseInt(value);
        if (intValue < min || intValue > max) {
            throw new InvalidInputException("Value must be between the range of " + min + " and " + max + ".");
        }
    }

    // Validate date time
    public static void validateDateTime(String dateTimeStr) throws InvalidInputException {
        validateNotNull(dateTimeStr);
        if (!ValidationUtility.isValidDateTime(dateTimeStr)) {
            throw new InvalidInputException("Invalid date-time format.");
        }
    }

    // Validate phone
    public static void validatePhone(String phone) throws InvalidInputException {
        validateNotNull(phone);
        if (!ValidationUtility.isValidPhone(phone)) {
            throw new InvalidInputException("Invalid phone number format.");
        }
    }

    // Validate IC
    public static void validateIC(String ic) throws InvalidInputException {
        validateNotNull(ic);
        if (!ValidationUtility.isValidIC(ic)) {
            throw new InvalidInputException("Invalid IC number format.");
        }
    }

    // Validate gender
    public static void validateGender(char gender) throws InvalidInputException {
        validateNotNull(gender);
        if (!ValidationUtility.isValidGender(gender)) {
            throw new InvalidInputException("Invalid gender value. Must be 'M' or 'F'.");
        }
    }

    public static void validateYesOrNo(char value) throws InvalidInputException {
        validateNotNull(value);
        if (!ValidationUtility.isValidYesOrNo(value)) {
            throw new InvalidInputException("Invalid input. Please enter 'Y' or 'N'.");
        }
    }

    public static <T, K> T findObjectOrThrow(T[] array, java.util.function.Function<T, K> keyExtractor, K key, String objectType, String keyName) throws InvalidInputException {
        T obj = ValidationUtility.findObjectByKey(array, keyExtractor, key);
        if (obj == null) {
            throw new InvalidInputException(objectType + " with " + keyName + " '" + key + "' not found.");
        }
        return obj;
    }

}