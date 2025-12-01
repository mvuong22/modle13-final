import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Scanner;

/**
 * WhaleBaseGUI provides a graphical interface for the WhaleBase Explorer.
 * <p>
 * Users can add, remove, update, display, and analyze whales via a GUI,
 * complete with a themed ocean-inspired color palette.
 * </p>
 */
public class WhaleBaseGUI extends JFrame {

    private AppController controller; // Controller manages backend operations
    private DefaultTableModel tableModel = new DefaultTableModel(); // Table model for displaying whale data
    private JTable table = new JTable(tableModel); // JTable linked to tableModel

    // OCEAN theme color palette for UI vibes
    private final Color COLOR_MAIN_BG = new Color(0x08, 0x5d, 0x84);
    private final Color COLOR_MENU_BG = new Color(0x14, 0x35, 0x48);
    private final Color COLOR_TABLE_BG = new Color(0x19, 0x30, 0x3d);
    private final Color COLOR_TABLE_HEADER = new Color(0x07, 0x43, 0x63);
    private final Color COLOR_TEXT = Color.WHITE;

    private final Font TYPEWRITER = new Font("Monospaced", Font.PLAIN, 15); // Font for a classic explorer feel

    private JPanel mainPanel;      // Panel for main content (table or startup GIF)
    private JPanel startupPanel;   // Panel for GIF display
    private JScrollPane tableScrollPane; // Scrollable table view

    /** Default constructor shows a GIF on startup */
    public WhaleBaseGUI() {
        this("C:\\Users\\josec\\Downloads\\anomura-bg_undersea.gif");
    }

    /**
     * Constructor that initializes the GUI and prompts user for DB path
     * @param gifPath Path to startup GIF
     */
    public WhaleBaseGUI(String gifPath) {
        // Prompt user for database path
        String dbPath = JOptionPane.showInputDialog(
                null,
                "Enter path to your SQLite database:",
                "Database Path",
                JOptionPane.QUESTION_MESSAGE
        );

        if (dbPath == null || dbPath.isBlank()) { // Require database path
            JOptionPane.showMessageDialog(null, "Database path is required. Exiting.");
            System.exit(0);
        }

        // Initialize controller with Database object
        Database db = new Database(dbPath.trim());
        controller = new AppController(db);

        // --- GUI setup ---
        setTitle("WhaleBase Explorer");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_MAIN_BG);

        // Top title label
        JLabel title = new JLabel("WhaleBase Explorer", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 28));
        title.setForeground(COLOR_TEXT);
        add(title, BorderLayout.NORTH);

        // Left menu panel with buttons
        JPanel menu = new JPanel(new GridLayout(6,1,5,5));
        menu.setBackground(COLOR_MENU_BG);

        JButton addBtn = createMenuButton("Add Whale");
        JButton uploadBtn = createMenuButton("Upload File");
        JButton updateBtn = createMenuButton("Update Whale");
        JButton removeBtn = createMenuButton("Remove Whale");
        JButton displayBtn = createMenuButton("Display All");
        JButton overviewBtn = createMenuButton("Conservation Overview");

        menu.add(addBtn); menu.add(uploadBtn); menu.add(updateBtn);
        menu.add(removeBtn); menu.add(displayBtn); menu.add(overviewBtn);
        add(menu, BorderLayout.WEST);

        // Main panel for GIF or table
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_MAIN_BG);

        startupPanel = new JPanel(new BorderLayout());
        startupPanel.setBackground(COLOR_MAIN_BG);
        JLabel gifLabel = new JLabel(new ImageIcon(gifPath));
        gifLabel.setHorizontalAlignment(SwingConstants.CENTER);
        startupPanel.add(gifLabel, BorderLayout.CENTER);

        mainPanel.add(startupPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // Table setup for displaying whale data
        table.setFont(TYPEWRITER);
        table.setBackground(COLOR_TABLE_BG);
        table.setForeground(COLOR_TEXT);
        table.setRowHeight(24);
        tableModel.setColumnIdentifiers(new Object[]{
                "ID","Scientific Name","Common Names","Length","Weight","Status","Habitats"
        });

        tableScrollPane = new JScrollPane(table);
        tableScrollPane.getViewport().setBackground(COLOR_TABLE_BG);
        table.getTableHeader().setBackground(COLOR_TABLE_HEADER);
        table.getTableHeader().setForeground(COLOR_TEXT);
        table.getTableHeader().setFont(TYPEWRITER);

        // --- Button actions ---
        addBtn.addActionListener(e -> addWhaleDialog());
        uploadBtn.addActionListener(e -> uploadFile());
        updateBtn.addActionListener(e -> updateWhaleDialog());
        removeBtn.addActionListener(e -> removeWhaleDialog());
        displayBtn.addActionListener(e -> showTable());
        overviewBtn.addActionListener(e -> showOverview());
    }

    /** Helper to create consistent menu buttons */
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(TYPEWRITER);
        btn.setBackground(COLOR_MENU_BG);
        btn.setForeground(COLOR_TEXT);
        return btn;
    }

    /**
     * Parses comma/pipe-separated names and ensures no numbers present
     * @param input Raw input string
     * @param fieldName Name of field for error messages
     * @return List of cleaned names
     * @throws Exception if validation fails
     */
    private List<String> parseAndValidateNames(String input, String fieldName) throws Exception {
        String[] items = input.replace("|", ",").split(",");
        for (String item : items) {
            if (item.matches(".*\\d.*")) {
                throw new Exception(fieldName + " cannot have numbers! Fix this: " + item);
            }
        }
        return List.of(items);
    }

    /**
     * Safely parses positive doubles
     * @param v String value
     * @param fieldName Name of field for errors
     * @return parsed positive double
     * @throws Exception if invalid
     */
    private double parseDoubleSafe(String v, String fieldName) throws Exception {
        try {
            double val = Double.parseDouble(v);
            if(val <= 0) throw new Exception(fieldName + " must be positive, researcher!");
            return val;
        } catch(Exception e) {
            throw new Exception(fieldName + " must be a valid number, nice try!");
        }
    }

    /** Display the JTable with all whale entries */
    private void showTable() {
        refreshTable();
        mainPanel.removeAll();
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /** Dialog for manually adding a whale */
    private void addWhaleDialog() {
        JTextField id = new JTextField();
        JTextField sci = new JTextField();
        JTextField common = new JTextField();
        JTextField len = new JTextField();
        JTextField wt = new JTextField();
        JComboBox<String> status = new JComboBox<>(new String[]{
                "Least Concern", "Vulnerable", "Endangered", "Critically Endangered"
        });
        JTextField habs = new JTextField();

        Object[] message = {
                "ID (6 digits):", id,
                "Scientific Name:", sci,
                "Common Names:", common,
                "Length in meters:", len,
                "Weight in tons:", wt,
                "Conservation Status:", status,
                "Habitats:", habs
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Whale", JOptionPane.OK_CANCEL_OPTION);
        if(option != JOptionPane.OK_OPTION) return;

        try {
            // Validate empty fields
            if(id.getText().trim().isEmpty() || sci.getText().trim().isEmpty() || common.getText().trim().isEmpty()
                    || len.getText().trim().isEmpty() || wt.getText().trim().isEmpty() || habs.getText().trim().isEmpty())
                throw new Exception("Oops! Fill in all the fields, explorer.");

            if(!id.getText().matches("\\d{6}")) throw new Exception("ID must be exactly 6 digits!");
            if(sci.getText().matches(".*\\d.*")) throw new Exception("Scientific Name can’t have numbers!");

            List<String> commonList = parseAndValidateNames(common.getText(), "Common Names");
            List<String> habList = parseAndValidateNames(habs.getText(), "Habitats");

            double length = parseDoubleSafe(len.getText(), "Length");
            double weight = parseDoubleSafe(wt.getText(), "Weight");

            boolean added = controller.addWhale(
                    id.getText().trim(),
                    sci.getText().trim(),
                    commonList,
                    length,
                    weight,
                    status.getSelectedItem().toString(),
                    habList
            );

            if(added) {
                JOptionPane.showMessageDialog(this, "Yay! Whale added successfully.");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "That whale ID already exists! Try another one.", "Oops", JOptionPane.WARNING_MESSAGE);
            }

        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Upload whales from a file, validates duplicates and formatting */
    private void uploadFile() {
        JFileChooser chooser = new JFileChooser();
        int res = chooser.showOpenDialog(this);
        if(res != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        int addedCount = 0;
        int skippedCount = 0;

        try (Scanner sc = new Scanner(file)) {
            while(sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if(line.isEmpty()) continue;
                String[] parts = line.split(",");
                if(parts.length < 7) continue;

                try {
                    boolean added = controller.addWhale(
                            parts[0].trim(),
                            parts[1].trim(),
                            parseAndValidateNames(parts[2], "Common Names"),
                            parseDoubleSafe(parts[3], "Length"),
                            parseDoubleSafe(parts[4], "Weight"),
                            parts[5].trim(),
                            parseAndValidateNames(parts[6], "Habitats")
                    );
                    if(added) addedCount++;
                    else skippedCount++;
                } catch(Exception e) {
                    skippedCount++;
                }
            }

            refreshTable();
            JOptionPane.showMessageDialog(this,
                    "Upload done! Added: " + addedCount + ", Skipped (duplicates/errors): " + skippedCount,
                    "Upload Results",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Upload failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Dialog for updating an existing whale entry */
    private void updateWhaleDialog() {
        String idInput = JOptionPane.showInputDialog(this, "Enter Whale ID to update:");
        if(idInput == null) return;

        Whale w = controller.getWhaleById(idInput.trim());
        if(w == null) { JOptionPane.showMessageDialog(this,"Whale not found!"); return; }

        JTextField sci = new JTextField(w.getScientificName());
        JTextField common = new JTextField(String.join(",", w.getCommonNames()));
        JTextField len = new JTextField(""+w.getAvgLengthMeters());
        JTextField wt = new JTextField(""+w.getAvgWeightTons());
        JComboBox<String> status = new JComboBox<>(new String[]{
                "Least Concern","Vulnerable","Endangered","Critically Endangered"
        });
        status.setSelectedItem(w.getConservationStatus().toString());
        JTextField habs = new JTextField(String.join(",", w.getHabitatRegions()));

        Object[] message = {
                "Scientific Name:", sci,
                "Common Names:", common,
                "Length:", len,
                "Weight:", wt,
                "Status:", status,
                "Habitats:", habs
        };

        int option = JOptionPane.showConfirmDialog(this,message,"Update Whale",JOptionPane.OK_CANCEL_OPTION);
        if(option != JOptionPane.OK_OPTION) return;

        try {
            if(sci.getText().matches(".*\\d.*")) throw new Exception("Scientific Name cannot have numbers!");
            List<String> commonList = parseAndValidateNames(common.getText(), "Common Names");
            List<String> habList = parseAndValidateNames(habs.getText(), "Habitats");

            controller.updateWhale(
                    w.getSpeciesId(),
                    sci.getText().trim(),
                    commonList,
                    parseDoubleSafe(len.getText(), "Length"),
                    parseDoubleSafe(wt.getText(), "Weight"),
                    status.getSelectedItem().toString(),
                    habList
            );
            refreshTable();
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Dialog for removing a whale by ID */
    private void removeWhaleDialog() {
        String id = JOptionPane.showInputDialog(this,"Enter Whale ID to remove:");
        if(id == null) return;

        if(controller.removeWhale(id.trim()))
            refreshTable();
        else
            JOptionPane.showMessageDialog(this,"Hmm..couldn’t remove whale, check again.");
    }

    /** Show an overview of conservation status counts */
    private void showOverview() {
        StringBuilder sb = new StringBuilder();
        for(ConservationStatus cs : ConservationStatus.values()) {
            List<String> species = controller.getAllWhales().stream()
                    .filter(w -> w.getConservationStatus() == cs)
                    .map(Whale::getScientificName).toList();
            sb.append(cs).append(": ").append(species.size()).append("\n");
        }
        JOptionPane.showMessageDialog(this,sb.toString());
    }

    /** Refreshes JTable content with current whale data */
    private void refreshTable() {
        table.setBackground(COLOR_TABLE_BG);
        table.setForeground(COLOR_TEXT);
        tableScrollPane.getViewport().setBackground(COLOR_TABLE_BG);

        tableModel.setRowCount(0); // clear old rows
        for(Whale w : controller.getAllWhales()) {
            tableModel.addRow(new Object[]{
                    w.getSpeciesId(),
                    w.getScientificName(),
                    String.join(", ", w.getCommonNames()),
                    w.getAvgLengthMeters(),
                    w.getAvgWeightTons(),
                    w.getConservationStatus(),
                    String.join(", ", w.getHabitatRegions())
            });
        }
    }

    /** Launches the GUI */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new WhaleBaseGUI().setVisible(true)
        );
    }
}
