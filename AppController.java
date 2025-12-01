import java.util.List;

/**
 * Controller class for the WhaleBase application.
 * Handles interactions between the user interface and the repository layer.
 * Responsible for adding, updating, removing, and displaying whales.
 */
public class AppController {
    private final WhaleRepository repository;

    /**
     * Constructs the AppController with a given database connection.
     * Initializes the WhaleRepository for all data operations.
     *
     * @param db the database instance to use for storing whale data
     */
    public AppController(Database db) {
        repository = new WhaleRepository(db);
    }

    /**
     * Adds a new whale to the repository.
     *
     * @param id unique identifier for the whale
     * @param scientificName official scientific name
     * @param commonNames list of common names
     * @param length average length in meters
     * @param weight average weight in tons
     * @param status conservation status as a string (e.g., "Least Concern")
     * @param habitats list of habitats where the whale lives
     * @return true if the whale was added successfully, false otherwise
     */
    public boolean addWhale(String id, String scientificName, List<String> commonNames,
                            double length, double weight, String status, List<String> habitats) {
        try {
            ConservationStatus cs = ConservationStatus.valueOf(status.toUpperCase().replace(" ", "_"));
            Whale whale = new Whale(id, scientificName, commonNames, length, weight, cs, habitats);
            return repository.addWhale(whale);
        } catch (Exception e) {
            System.out.println("Failed to add whale: " + e.getMessage());
            return false;
        }
    }

    /**
     * Removes a whale from the repository by its ID.
     *
     * @param id unique identifier of the whale to remove
     * @return true if the whale was removed successfully, false otherwise
     */
    public boolean removeWhale(String id) {
        return repository.removeWhale(id);
    }

    /**
     * Updates an existing whale's information.
     *
     * @param id unique identifier of the whale to update
     * @param scientificName new scientific name
     * @param commonNames new list of common names
     * @param length new average length in meters
     * @param weight new average weight in tons
     * @param status new conservation status as a string
     * @param habitats new list of habitat regions
     * @return true if the whale was updated successfully, false otherwise
     */
    public boolean updateWhale(String id, String scientificName, List<String> commonNames,
                               double length, double weight, String status, List<String> habitats) {
        Whale existing = repository.getWhaleById(id);
        if (existing == null) return false;

        try {
            ConservationStatus cs = ConservationStatus.valueOf(status.toUpperCase().replace(" ", "_"));
            Whale updated = new Whale(id, scientificName, commonNames, length, weight, cs, habitats);
            return repository.updateWhale(id, updated);
        } catch (Exception e) {
            System.out.println("Failed to update whale: " + e.getMessage());
            return false;
        }
    }

    /**
     * Displays all whales in the repository.
     * Prints a message if no whales have been added yet.
     */
    public void displayAll() {
        List<Whale> whales = repository.getAllWhales();
        if (whales.isEmpty()) {
            System.out.println("\nNo whales yet! Add some first.");
            return;
        }
        for (Whale w : whales) {
            System.out.println(w);
        }
    }

    /**
     * Prints an overview of whales by their conservation status.
     * Groups whales based on status and lists their scientific names.
     */
    public void conservationOverview() {
        List<Whale> whales = repository.getAllWhales();
        if (whales.isEmpty()) {
            System.out.println("\nNo whales yet! Add some first.");
            return;
        }

        System.out.println("\n<====== Conservation Overview ======>\n");
        for (ConservationStatus cs : ConservationStatus.values()) {
            List<String> species = whales.stream()
                    .filter(w -> w.getConservationStatus() == cs)
                    .map(Whale::getScientificName)
                    .toList();
            System.out.println(cs + " (" + species.size() + "): " + String.join(", ", species));
        }
    }

    // Getters used by MenuUI

    /**
     * Retrieves all whales from the repository.
     *
     * @return list of all Whale objects
     */
    public List<Whale> getAllWhales() {
        return repository.getAllWhales();
    }

    /**
     * Retrieves a single whale by its ID.
     *
     * @param id unique identifier of the whale
     * @return Whale object if found, null otherwise
     */
    public Whale getWhaleById(String id) {
        return repository.getWhaleById(id);
    }
}
