package vista;

import controlador.Ctrl_Usuario;
import controlador.Sesion;
import util.ThemeManager;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

public class Login extends JFrame {

    private boolean mostrar = false;
    private char echoOriginal;

    public Login() {
        ThemeManager.aplicarTemaGuardado();
        initComponents();
        setTitle("Login");
        setResizable(false);
        setLocationRelativeTo(null);
        echoOriginal = txt_password.getEchoChar();
        configurarColores();

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/Sentryo Logo.jpeg"));
            this.setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.println("❌ No se pudo cargar el icono: " + e.getMessage());
        }
    }

    private void configurarColores() {
        Color bgColor = UIManager.getColor("Panel.background");
        Color fgColor = UIManager.getColor("Panel.foreground");

        background.setBackground(bgColor);
        jLabel7.setForeground(fgColor);
        jLabel2.setForeground(fgColor);
        lblUser.setForeground(fgColor);
        lblPassword.setForeground(fgColor);
        jLabel1.setForeground(fgColor);
        chkRecordarme.setForeground(fgColor);
        btnPassShow.setBackground(bgColor);

        txt_usuario.setBackground(bgColor);
        txt_usuario.setForeground(fgColor);
        txt_password.setBackground(bgColor);
        txt_password.setForeground(fgColor);
    }

    private void realizarLogin() {
        String usuario = txt_usuario.getText().trim();
        String password = new String(txt_password.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese sus credenciales");
            return;
        }

        Ctrl_Usuario controlUsuario = new Ctrl_Usuario();
        Usuario usuarioLogueado = controlUsuario.loginUser(crearUsuario(usuario, password));

        if (usuarioLogueado != null) {
            iniciarSesion(usuarioLogueado);
        } else {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
        }
    }

    private Usuario crearUsuario(String usuario, String password) {
        Usuario u = new Usuario();
        u.setNickname(usuario);
        u.setPassword(password);
        return u;
    }

    private void iniciarSesion(Usuario usuario) {
        JOptionPane.showMessageDialog(null, "Login Correcto");

        Sesion.iniciar(
                usuario.getID(),
                usuario.getNombre(),
                usuario.getNickname(),
                usuario.getCorreo(),
                usuario.getApellido(),
                usuario.getPassword()
        );

        Preferences prefs = Preferences.userRoot();
        if (chkRecordarme.isSelected()) {
            prefs.putBoolean("sesionActiva", true);
            prefs.putInt("ID_Usuario", usuario.getID());
        } else {
            prefs.putBoolean("sesionActiva", false);
            prefs.remove("ID_Usuario");
        }

        new mainFrame().setVisible(true);
        dispose();
    }

    private void togglePasswordVisibility() {
        if (mostrar) {
            txt_password.setEchoChar(echoOriginal);
            btnPassShowText.setIcon(new ImageIcon(getClass().getResource("/img/show pass icon.png")));
        } else {
            txt_password.setEchoChar((char) 0);
            btnPassShowText.setIcon(new ImageIcon(getClass().getResource("/img/hide pass icon.png")));
        }
        mostrar = !mostrar;
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JPanel();
        txt_usuario = new javax.swing.JTextField();
        lblUser = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_password = new javax.swing.JPasswordField();
        btnRegister = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        btnLogin = new javax.swing.JPanel();
        btnLoginText = new javax.swing.JLabel();
        chkRecordarme = new javax.swing.JCheckBox();
        btnPassShow = new javax.swing.JPanel();
        btnPassShowText = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        setSize(new java.awt.Dimension(100, 50));

        background.setBackground(new java.awt.Color(255, 255, 255));
        background.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_usuario.setForeground(new java.awt.Color(204, 204, 204));
        txt_usuario.setText("Ingresa su nombre de usuario");
        txt_usuario.setToolTipText("");
        txt_usuario.setBorder(null);
        txt_usuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txt_usuarioMousePressed(evt);
            }
        });
        txt_usuario.addActionListener(this::txt_usuarioActionPerformed);
        background.add(txt_usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 300, 30));

        lblUser.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblUser.setText("Usuario:");
        background.add(lblUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 225, -1, -1));

        lblPassword.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblPassword.setText("Contraseña:");
        background.add(lblPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 285, -1, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel7.setText("Bienvenido");
        background.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 60, -1, -1));

        txt_password.setForeground(new java.awt.Color(204, 204, 204));
        txt_password.setText("********");
        txt_password.setToolTipText("");
        txt_password.setBorder(null);
        txt_password.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txt_passwordMousePressed(evt);
            }
        });
        txt_password.addActionListener(this::txt_passwordActionPerformed);
        background.add(txt_password, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 300, 300, 30));

        btnRegister.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnRegister.setForeground(new java.awt.Color(0, 102, 204));
        btnRegister.setText("Registrate Ahora");
        btnRegister.setBorder(null);
        btnRegister.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegisterMouseClicked(evt);
            }
        });
        btnRegister.addActionListener(this::btnRegisterActionPerformed);
        background.add(btnRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 450, -1, -1));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel1.setText("¿No tienes una cuenta?");
        background.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 450, -1, -1));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setText("Ingresa tus credenciales para continuar");
        background.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 180, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/login icon block.png"))); // NOI18N
        background.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 105, -1, 64));
        background.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 270, 300, -1));
        background.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 330, 300, -1));

        btnLogin.setBackground(new java.awt.Color(105, 115, 218));

        btnLoginText.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnLoginText.setForeground(new java.awt.Color(255, 255, 255));
        btnLoginText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnLoginText.setText("Iniciar Sesion");
        btnLoginText.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLoginText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLoginTextMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLoginTextMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLoginTextMouseExited(evt);
            }
        });

        javax.swing.GroupLayout btnLoginLayout = new javax.swing.GroupLayout(btnLogin);
        btnLogin.setLayout(btnLoginLayout);
        btnLoginLayout.setHorizontalGroup(
            btnLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnLoginText, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
        );
        btnLoginLayout.setVerticalGroup(
            btnLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnLoginText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        background.add(btnLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 390, 255, 45));

        chkRecordarme.setText("Recordarme");
        chkRecordarme.addActionListener(this::chkRecordarmeActionPerformed);
        background.add(chkRecordarme, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 345, -1, -1));

        btnPassShow.setBackground(new java.awt.Color(255, 255, 255));

        btnPassShowText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnPassShowText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/show pass icon.png"))); // NOI18N
        btnPassShowText.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPassShowText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPassShowTextMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPassShowTextMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPassShowTextMouseExited(evt);
            }
        });

        javax.swing.GroupLayout btnPassShowLayout = new javax.swing.GroupLayout(btnPassShow);
        btnPassShow.setLayout(btnPassShowLayout);
        btnPassShowLayout.setHorizontalGroup(
            btnPassShowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
            .addGroup(btnPassShowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnPassShowLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(btnPassShowText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        btnPassShowLayout.setVerticalGroup(
            btnPassShowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
            .addGroup(btnPassShowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(btnPassShowLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(btnPassShowText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        background.add(btnPassShow, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 300, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(background, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_usuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_usuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_usuarioActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed

        Register register = new Register();
        register.setVisible(true);

        this.dispose(); // Cierra Login
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnLoginTextMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginTextMouseEntered
        btnLogin.setBackground(new Color(122, 133, 252));
    }//GEN-LAST:event_btnLoginTextMouseEntered

    private void btnLoginTextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginTextMouseExited
        btnLogin.setBackground(new Color(105, 115, 218));// TODO add your handling code here:
    }//GEN-LAST:event_btnLoginTextMouseExited

    private void btnLoginTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginTextMouseClicked
        this.realizarLogin();
    }//GEN-LAST:event_btnLoginTextMouseClicked

    private void txt_usuarioMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_usuarioMousePressed
        if (txt_usuario.getText().equals("Ingresa su nombre de usuario")) {
            txt_usuario.setText("");
            txt_usuario.setForeground(Color.black);
        };
        if (String.valueOf(txt_password.getPassword()).isEmpty()) {
            txt_password.setText("********");
            txt_password.setForeground(Color.gray);
        }
    }//GEN-LAST:event_txt_usuarioMousePressed

    private void txt_passwordMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_passwordMousePressed
        if (String.valueOf(txt_password.getPassword()).equals("********")) {
            txt_password.setText("");
            txt_password.setForeground(Color.black);
        }
        if (txt_usuario.getText().isEmpty()) {
            txt_usuario.setText("Ingresa su nombre de usuario");
            txt_usuario.setForeground(Color.gray);
        }
    }//GEN-LAST:event_txt_passwordMousePressed

    private void btnRegisterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegisterMouseClicked

    }//GEN-LAST:event_btnRegisterMouseClicked

    private void chkRecordarmeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRecordarmeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkRecordarmeActionPerformed

    private void btnPassShowTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPassShowTextMouseClicked

        if (mostrar) {

            txt_password.setEchoChar(echoOriginal);

            btnPassShowText.setIcon(
                    new ImageIcon(getClass().getResource("/img/show pass icon.png")));

        } else {

            txt_password.setEchoChar((char) 0);

            btnPassShowText.setIcon(
                    new ImageIcon(getClass().getResource("/img/hide pass icon.png")));
        }

        mostrar = !mostrar;
    }//GEN-LAST:event_btnPassShowTextMouseClicked

    private void btnPassShowTextMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPassShowTextMouseEntered
        btnPassShow.setBackground(new Color(243, 244, 246));
    }//GEN-LAST:event_btnPassShowTextMouseEntered

    private void btnPassShowTextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPassShowTextMouseExited
        btnPassShow.setBackground(Color.white);
    }//GEN-LAST:event_btnPassShowTextMouseExited

    private void txt_passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_passwordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_passwordActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {

            Preferences prefs
                    = Preferences.userRoot();

            boolean activa
                    = prefs.getBoolean(
                            "sesionActiva",
                            false);

            if (activa) {

                int id
                        = prefs.getInt(
                                "ID_Usuario",
                                0);

                Ctrl_Usuario ctrl
                        = new Ctrl_Usuario();

                Usuario usuario
                        = ctrl.buscarPorId(id);

                if (usuario != null) {

                    Sesion.iniciar(
                            usuario.getID(),
                            usuario.getNombre(),
                            usuario.getNickname(),
                            usuario.getCorreo(),
                            usuario.getApellido(),
                            usuario.getPassword()
                    );

                    mainFrame main
                            = new mainFrame();

                    main.setVisible(true);

                } else {

                    new Login().setVisible(true);
                }

            } else {

                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel background;
    private javax.swing.JPanel btnLogin;
    private javax.swing.JLabel btnLoginText;
    private javax.swing.JPanel btnPassShow;
    private javax.swing.JLabel btnPassShowText;
    private javax.swing.JButton btnRegister;
    private javax.swing.JCheckBox chkRecordarme;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPasswordField txt_password;
    private javax.swing.JTextField txt_usuario;
    // End of variables declaration//GEN-END:variables
}
