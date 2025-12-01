import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * WhaleRepository handles storage and retrieval of Whale objects
 * from a SQLite database.
 *
 * Provides methods to add, remove, update, and query whales.
 */
public class WhaleRepository {

    private final Database db; // Reference to Database helper

    /**
     * Constructor ensures the database is initialized (tables created)
     * @param db Database instance
     */
    public WhaleRepository(Database db) {
        this.db = db;
        db.init(); // Ensure the 'whales' table exists
    }

    /** Converts a pipe-separated string from DB into a List of strings */
    private List<String> stringToList(String str) {
        return Arrays.asList(str.split("\\|"));
    }

    /** Converts a List of strings into a pipe-separated string for DB storage */
    private String listToString(List<String> list) {
        return String.join("|", list);
    }

    /**
     * Adds a whale to the database
     * @param whale Whale object to store
     * @return true if insertion succeeds, false if fails (e.g., duplicate ID)
     */
    public boolean addWhale(Whale whale) {
        String sql = "INSERT INTO whales(id, scientific_name, common_names, length, weight, status, habitats) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = db.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, whale.getSpeciesId());
            pstmt.setString(2, whale.getScientificName());
            pstmt.setString(3, listToString(whale.getCommonNames()));
            pstmt.setDouble(4, whale.getAvgLengthMeters());
            pstmt.setDouble(5, whale.getAvgWeightTons());
            pstmt.setString(6, whale.getConservationStatus().name());
            pstmt.setString(7, listToString(whale.getHabitatRegions()));

            pstmt.executeUpdate();
            return true; // Successfully added

        } catch (SQLException e) {
            System.out.println("Failed to add whale: " + e.getMessage());
            return false; // Likely duplicate ID or DB issue
        }
    }

    /**
     * Removes a whale by ID
     * @param id Whale species ID
     * @return true if removal succeeded, false if not found or error
     */
    public boolean removeWhale(String id) {
        String sql = "DELETE FROM whales WHERE id = ?";
        try (Connection conn = db.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            int rows = pstmt.executeUpdate();
            return rows > 0; // true if at least 1 row deleted

        } catch (SQLException e) {
            System.out.println("Failed to remove whale: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates a whale's info by ID
     * @param id Whale ID to update
     * @param whale Whale object with new data
     * @return true if update succeeded, false if no matching record or error
     */
    public boolean updateWhale(String id, Whale whale) {
        String sql = "UPDATE whales SET scientific_name=?, common_names=?, length=?, weight=?, status=?, habitats=? WHERE id=?";
        try (Connection conn = db.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, whale.getScientificName());
            pstmt.setString(2, listToString(whale.getCommonNames()));
            pstmt.setDouble(3, whale.getAvgLengthMeters());
            pstmt.setDouble(4, whale.getAvgWeightTons());
            pstmt.setString(5, whale.getConservationStatus().name());
            pstmt.setString(6, listToString(whale.getHabitatRegions()));
            pstmt.setString(7, id);

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Failed to update whale: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all whales in the database
     * @return List of Whale objects
     */
    public List<Whale> getAllWhales() {
        List<Whale> whales = new ArrayList<>();
        String sql = "SELECT * FROM whales";

        try (Connection conn = db.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("id");
                String sciName = rs.getString("scientific_name");
                List<String> commonNames = stringToList(rs.getString("common_names"));
                double length = rs.getDouble("length");
                double weight = rs.getDouble("weight");
                ConservationStatus status = ConservationStatus.valueOf(rs.getString("status"));
                List<String> habitats = stringToList(rs.getString("habitats"));

                whales.add(new Whale(id, sciName, commonNames, length, weight, status, habitats));
            }

        } catch (SQLException e) {
            System.out.println("Failed to fetch whales: " + e.getMessage());
        }

        return whales;
    }

    /**
     * Retrieves a whale by its ID
     * @param id Whale species ID
     * @return Whale object if found, null otherwise
     */
    public Whale getWhaleById(String id) {
        String sql = "SELECT * FROM whales WHERE id=?";
        try (Connection conn = db.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String sciName = rs.getString("scientific_name");
                List<String> commonNames = stringToList(rs.getString("common_names"));
                double length = rs.getDouble("length");
                double weight = rs.getDouble("weight");
                ConservationStatus status = ConservationStatus.valueOf(rs.getString("status"));
                List<String> habitats = stringToList(rs.getString("habitats"));

                return new Whale(id, sciName, commonNames, length, weight, status, habitats);
            }

        } catch (SQLException e) {
            System.out.println("Failed to fetch whale: " + e.getMessage());
        }

        return null; // Not found
    }
}
