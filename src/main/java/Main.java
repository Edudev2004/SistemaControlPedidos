/**
 *
 * @author 2004rend
 */
import sistemadecontrol.view.LoginFrame;
import sistemadecontrol.controller.LoginController;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the System look and feel */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Iniciamos con el login
                LoginFrame login = new LoginFrame();
                new LoginController(login);
                login.setVisible(true);
            }
        });
    }
}
