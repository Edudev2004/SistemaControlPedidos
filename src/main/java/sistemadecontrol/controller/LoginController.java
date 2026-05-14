/**
 *
 * @author 2004rend
 */
package sistemadecontrol.controller;

import sistemadecontrol.dao.RepartidorDAO;
import sistemadecontrol.view.LoginFrame;
import sistemadecontrol.view.PanelPrincipal;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// Controlador que maneja la logica del login
public class LoginController {
    private LoginFrame vista;
    private RepartidorDAO repartidorDAO;

    public LoginController(LoginFrame vista) {
        this.vista = vista;
        this.repartidorDAO = new RepartidorDAO();

        // Conectamos el boton "Ingresar" con la accion de validar
        this.vista.btnIngresar.addActionListener(e -> validarCredenciales());

        // Tambien permitimos presionar ENTER en el campo de clave
        this.vista.txtClave.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    validarCredenciales();
                }
            }
        });
    }

    // Metodo que valida las credenciales contra la BD
    private void validarCredenciales() {
        // Obtener datos de la vista
        String usuario = vista.txtUsuario.getText().trim();
        String clave = new String(vista.txtClave.getPassword());

        // Validacion de campos basicos
        if (usuario.isEmpty() || clave.isEmpty()) {
            vista.lblMensaje.setText("Complete ambos campos");
            vista.lblMensaje.setForeground(new Color(200, 50, 50));
            return;
        }

        // Validar contra la base de datos
        boolean esValido = repartidorDAO.validarUsuario(usuario, clave);

        if (esValido) {
            // Abrir el panel principal tras el login exitoso
            vista.dispose();
            PanelPrincipal panel = new PanelPrincipal();
            new PedidoController(panel);
            panel.setVisible(true);
        } else {
            // Mostrar mensaje de error si falla la autenticacion
            vista.lblMensaje.setText("Usuario o clave incorrectos");
            vista.lblMensaje.setForeground(new Color(200, 50, 50));
            vista.txtClave.setText("");
        }
    }
}
