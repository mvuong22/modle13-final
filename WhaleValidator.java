import java.util.List;

/**
 * WhaleValidator checks if user input is valid for Whale fields
 * to prevent crashes or invalid data in the database.
 */
public class WhaleValidator {

    // Validate species ID is exactly 6 digits
    public static boolean isValidSpeciesId(String id) {
        return id != null && id.matches("\\d{6}");
    }

    // Validate scientific name (non-empty, no numbers)
    public static boolean isValidScientificName(String name) {
        return name != null && !name.trim().isEmpty() && !name.matches(".*\\d.*");
    }

    // Validate list of names or regions (non-empty, no numbers in names)
    public static boolean isValidList(List<String> list) {
        if (list == null || list.isEmpty()) return false;
        for (String item : list) {
            if (item == null || item.trim().isEmpty() || item.matches(".*\\d.*")) {
                return false;
            }
        }
        return true;
    }

    // Validate positive numbers for length and weight
    public static boolean isValidPositiveDouble(double value) {
        return value > 0;
    }

    // Validate conservation status (matches enum)
    public static boolean isValidConservationStatus(String status) {
        try {
            ConservationStatus.valueOf(status.toUpperCase().replace(" ", "_"));
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
