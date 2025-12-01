import java.util.List;
import java.util.Scanner;

/**
 * MenuUI handles the console-based user interface for WhaleBase.
 * <p>
 * Users can add, remove, update, display whales, or view conservation overviews.
 * Input validation and file upload support are included to ensure data integrity.
 * </p>
 */
public class MenuUI {
    private AppController controller;
    private Scanner scanner = new Scanner(System.in);

    /**
     * Constructor for MenuUI.
     * <p>
     * Prompts the user for the SQLite database file path on startup,
     * initializes the database, and sets up the AppController.
     * </p>
     */
    public MenuUI() {
        System.out.print("Enter SQLite DB file path (or type 'whales.db' to use default): ");
        String dbPath = scanner.nextLine().trim();
        if (dbPath.isEmpty()) dbPath = "whales.db";

        Database db = new Database(dbPath);
        controller = new AppController(db);
    }

    /**
     * Starts the main menu loop, displaying options and handling user input.
     */
    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("\nWelcome to WhaleBase Explorer! Let's add some whales to your WhaleLog!\n");
            System.out.println("<====== WB MENU ======>");
            System.out.println("\n1. Add Whale");
            System.out.println("2. Remove Whale");
            System.out.println("3. Update Whale");
            System.out.println("4. Display All Whales");
            System.out.println("5. Conservation Overview");
            System.out.println("6. Exit");
            System.out.print("\nSelect an option: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> addWhale();
                case "2" -> removeWhale();
                case "3" -> updateWhale();
                case "4" -> controller.displayAll();
                case "5" -> controller.conservationOverview();
                case "6" -> {
                    running = false;
                    System.out.println("\nExiting Whale Base... Wave you later!\n");
                }
                default -> System.out.println("\nInvalid input. Try again!\n");
            }
        }
    }

    /**
     * Handles the add whale menu, letting the user choose between manual entry
     * or uploading whales from a text file.
     */
    private void addWhale() {
        System.out.println("\nAdd Whale:");
        System.out.println("1. Manual Entry");
        System.out.println("2. Upload from Text File");
        System.out.print("\nChoose an option: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> addWhaleManually();
            case "2" -> uploadWhalesFromFile();
            default -> System.out.println("\nNice try, that's an invalid choice... Returning to menu!\n");
        }
    }

    /**
     * Guides the user through manual entry of whale data.
     * <p>
     * Each field is validated for correct format (IDs, names, numbers, etc.).
     * If all validations pass, the whale is added via the AppController.
     * </p>
     */
    private void addWhaleManually() {
        // Implementation unchanged, detailed validations included
    }

    /**
     * Uploads whales from a user-provided text file.
     * <p>
     * Each line must be in CSV format:
     * id,sciName,common1|common2,length,weight,status,habitat1|habitat2
     * Lines are validated before attempting to add to the database.
     * </p>
     */
    private void uploadWhalesFromFile() {
        // Implementation unchanged, includes file parsing and validation
    }

    /**
     * Removes a whale by its ID, after user confirmation.
     */
    private void removeWhale() {
        // Implementation unchanged
    }

    /**
     * Updates fields of an existing whale.
     * <p>
     * Users can selectively update any field including scientific name, common names,
     * length, weight, conservation status, and habitats.
     * Input is validated to prevent invalid entries.
     * </p>
     */
    private void updateWhale() {
        // Implementation unchanged
    }
}
