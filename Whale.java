import java.util.List;

/**
 * Represents a Whale with its key biological and conservation data.
 * <p>
 * Each whale has a unique species ID, scientific name, a list of common names,
 * average length (in meters), average weight (in tons), conservation status,
 * and a list of habitat regions. This class provides getters and setters for
 * all fields and a method to display whale information.
 * </p>
 */
public class Whale {
    private String speciesId;               // Unique ID
    private String scientificName;          // Scientific name
    private List<String> commonNames;       // List of common names
    private double avgLengthMeters;         // Average length
    private double avgWeightTons;           // Average weight
    private ConservationStatus conservationStatus; // Conservation status
    private List<String> habitatRegions;    // List of habitat regions

    /**
     * Constructs a Whale object with all required attributes.
     *
     * @param speciesId           Unique 6-digit species ID
     * @param scientificName      Official scientific name of the whale
     * @param commonNames         List of common names
     * @param avgLengthMeters     Average length in meters
     * @param avgWeightTons       Average weight in tons
     * @param conservationStatus  Conservation status enum
     * @param habitatRegions      List of habitat regions
     */
    public Whale(String speciesId, String scientificName, List<String> commonNames,
                 double avgLengthMeters, double avgWeightTons,
                 ConservationStatus conservationStatus, List<String> habitatRegions) {
        this.speciesId = speciesId;
        this.scientificName = scientificName;
        this.commonNames = commonNames;
        this.avgLengthMeters = avgLengthMeters;
        this.avgWeightTons = avgWeightTons;
        this.conservationStatus = conservationStatus;
        this.habitatRegions = habitatRegions;
    }

    /** @return the unique species ID */
    public String getSpeciesId() { return speciesId; }

    /** @param speciesId sets the unique species ID */
    public void setSpeciesId(String speciesId) { this.speciesId = speciesId; }

    /** @return the scientific name of the whale */
    public String getScientificName() { return scientificName; }

    /** @param scientificName sets the whale's scientific name */
    public void setScientificName(String scientificName) { this.scientificName = scientificName; }

    /** @return a list of common names */
    public List<String> getCommonNames() { return commonNames; }

    /** @param commonNames sets the list of common names */
    public void setCommonNames(List<String> commonNames) { this.commonNames = commonNames; }

    /** @return average length in meters */
    public double getAvgLengthMeters() { return avgLengthMeters; }

    /** @param avgLengthMeters sets the whale's average length in meters */
    public void setAvgLengthMeters(double avgLengthMeters) { this.avgLengthMeters = avgLengthMeters; }

    /** @return average weight in tons */
    public double getAvgWeightTons() { return avgWeightTons; }

    /** @param avgWeightTons sets the whale's average weight in tons */
    public void setAvgWeightTons(double avgWeightTons) { this.avgWeightTons = avgWeightTons; }

    /** @return the conservation status enum */
    public ConservationStatus getConservationStatus() { return conservationStatus; }

    /** @param conservationStatus sets the conservation status of the whale */
    public void setConservationStatus(ConservationStatus conservationStatus) { this.conservationStatus = conservationStatus; }

    /** @return list of habitat regions */
    public List<String> getHabitatRegions() { return habitatRegions; }

    /** @param habitatRegions sets the list of habitat regions */
    public void setHabitatRegions(List<String> habitatRegions) { this.habitatRegions = habitatRegions; }

    /**
     * Prints whale information to the console.
     * <p>
     * Displays all the attributes in a formatted manner for easy reading.
     * </p>
     */
    public void display() {
        System.out.println("\nID: " + speciesId);
        System.out.println("Scientific Name: " + scientificName);
        System.out.println("Common Names: " + String.join(", ", commonNames));
        System.out.println("Average Length (m): " + avgLengthMeters);
        System.out.println("Average Weight (tons): " + avgWeightTons);
        System.out.println("Conservation Status: " + conservationStatus);
        System.out.println("Habitat Regions: " + String.join(", ", habitatRegions));
        System.out.println("-------------------------------");
    }
}
