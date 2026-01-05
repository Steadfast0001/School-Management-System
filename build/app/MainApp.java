package app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ui.LoginFrame;

public class MainApp {

    public static void main(String[] args) {

        // seed DB with admin user if needed
        DBSeeder.seedAdmin();

        // Run Swing on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Use system look and feel
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Launch Login UI
            new LoginFrame();
        });
    }
}
