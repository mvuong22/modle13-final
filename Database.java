import java.sql.*;

/**
 * Handles the SQLite database connection and initialization for WhaleBase.
 * Ensures the whales table exists and provides a way to connect to the database dynamically.
 */
public class Database {
    private final String dbUrl;  // URL to the SQLite database, set via constructor

    /**
     * Constructs a Database object with a dynamic file path.
     * This allows the user to specify the location of the SQLite file.
     *
     * @param dbFilePath the path to the SQLite database file
     */
    public Database(String dbFilePath) {
        this.dbUrl = "jdbc:sqlite:" + dbFilePath;
    }

    /**
     * Creates and returns a Connection object to the SQLite database.
     *
     * @return a Connection object to interact with the database
     * @throws SQLException if a connection cannot be established
     */
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }

    /**
     * Initializes the database by creating the whales table if it doesn't already exist.
     * The table includes columns for ID, scientific name, common names, length, weight,
     * conservation status, and habitats.
     * Prints a confirmation message once the table is ready.
     */
    public void init() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            String createTable = """
                CREATE TABLE IF NOT EXISTS whales (
                    id TEXT PRIMARY KEY,
                    scientific_name TEXT NOT NULL,
                    common_names TEXT NOT NULL,
                    length REAL NOT NULL,
                    weight REAL NOT NULL,
                    status TEXT NOT NULL,
                    habitats TEXT NOT NULL
                );
            """;

            stmt.execute(createTable);
            System.out.println("Database initialized!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
