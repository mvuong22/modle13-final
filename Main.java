/**
 * Entry point for the WhaleBase application.
 * <p>
 * This class initializes the GUI for WhaleBase and launches it on the
 * Event Dispatch Thread to ensure thread-safe Swing operations.
 * </p>
 */
public class Main {

    /**
     * Main method to start the WhaleBase application.
     * Initializes the WhaleBaseGUI and makes it visible.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() ->
                new WhaleBaseGUI().setVisible(true)
        );
    }
}
