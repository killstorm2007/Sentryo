
package vista;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import conexion.Conexion;
import controlador.MD5;
import controlador.Sesion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import util.ColorUtils;

public class PerfilPanel extends javax.swing.JPanel {

    private boolean mostrar = false;
    private char echoOriginal;    
    private javax.swing.JPanel pnlFormulario; // Guardar referencia
    
    public PerfilPanel() {
        initComponents();
        cargarDatosUsuario();
        echoOriginal = textFieldPass.getEchoChar();
        configurarColores();
        reconstruirFormulario();
    }
    private void reconstruirFormulario() {
        // Guardar referencia al panel de contenido
        pnlFormulario = panelContenido;
        
        // Limpiar el panelContenido
        panelContenido.removeAll();
        panelContenido.setLayout(new GridBagLayout());
        
        Color bgColor = ColorUtils.getBackground();
        Color fgColor = ColorUtils.getForeground();
        
        // Aplicar colores al panelContenido
        panelContenido.setBackground(bgColor);
        panelContenido.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);

        // 1. Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        lblUserName.setForeground(fgColor);
        panelContenido.add(lblUserName, gbc);
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;

        // 2. Nombre
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        jLabel1.setForeground(fgColor);
        panelContenido.add(jLabel1, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 180;
        textFieldNombre.setBackground(bgColor);
        textFieldNombre.setForeground(fgColor);
        textFieldNombre.setBorder(null);
        panelContenido.add(textFieldNombre, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jSeparator2.setForeground(ColorUtils.getBorderColor());
        panelContenido.add(jSeparator2, gbc);

        // 3. Apellidos
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        jLabel2.setForeground(fgColor);
        panelContenido.add(jLabel2, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textFieldApellido.setBackground(bgColor);
        textFieldApellido.setForeground(fgColor);
        textFieldApellido.setBorder(null);
        panelContenido.add(textFieldApellido, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        jSeparator5.setForeground(ColorUtils.getBorderColor());
        panelContenido.add(jSeparator5, gbc);

        // 4. Correo
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        jLabel4.setForeground(fgColor);
        panelContenido.add(jLabel4, gbc);
        
        gbc.gridx = 1;
        textFieldCorreo.setBackground(bgColor);
        textFieldCorreo.setForeground(fgColor);
        textFieldCorreo.setBorder(null);
        panelContenido.add(textFieldCorreo, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        jSeparator4.setForeground(ColorUtils.getBorderColor());
        panelContenido.add(jSeparator4, gbc);

        // 5. Nickname
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.NONE;
        jLabel5.setForeground(fgColor);
        panelContenido.add(jLabel5, gbc);
        
        gbc.gridx = 1;
        textFieldNick.setBackground(bgColor);
        textFieldNick.setForeground(fgColor);
        textFieldNick.setBorder(null);
        panelContenido.add(textFieldNick, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 8;
        jSeparator1.setForeground(ColorUtils.getBorderColor());
        panelContenido.add(jSeparator1, gbc);

        // 6. Password
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.NONE;
        jLabel3.setForeground(fgColor);
        panelContenido.add(jLabel3, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        textFieldPass.setBackground(bgColor);
        textFieldPass.setForeground(fgColor);
        textFieldPass.setBorder(null);
        panelContenido.add(textFieldPass, gbc);
        
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        btnPassShow.setBackground(bgColor);
        panelContenido.add(btnPassShow, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jSeparator3.setForeground(ColorUtils.getBorderColor());
        panelContenido.add(jSeparator3, gbc);

        // 7. Botón Actualizar
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 0, 0);
        editBtn3.setBackground(new Color(105, 115, 218));
        panelContenido.add(editBtn3, gbc);

        // Actualizar el panel
        panelContenido.revalidate();
        panelContenido.repaint();
    }
    
    private void configurarColores() {
        Color bgColor = ColorUtils.getBackground();
        Color fgColor = ColorUtils.getForeground();
        Color borderColor = ColorUtils.getBorderColor();
        
        // Fondo del panel principal
        this.setBackground(bgColor);
        
        // Si el panelContenido existe, aplicar colores
        if (panelContenido != null) {
            panelContenido.setBackground(bgColor);
        }
        
        // Labels - siempre se actualizan
        lblUserName.setForeground(fgColor);
        jLabel1.setForeground(fgColor);
        jLabel2.setForeground(fgColor);
        jLabel3.setForeground(fgColor);
        jLabel4.setForeground(fgColor);
        jLabel5.setForeground(fgColor);
        
        // TextFields
        textFieldNombre.setBackground(bgColor);
        textFieldNombre.setForeground(fgColor);
        textFieldApellido.setBackground(bgColor);
        textFieldApellido.setForeground(fgColor);
        textFieldCorreo.setBackground(bgColor);
        textFieldCorreo.setForeground(fgColor);
        textFieldNick.setBackground(bgColor);
        textFieldNick.setForeground(fgColor);
        textFieldPass.setBackground(bgColor);
        textFieldPass.setForeground(fgColor);
        
        // Separadores
        jSeparator1.setForeground(borderColor);
        jSeparator2.setForeground(borderColor);
        jSeparator3.setForeground(borderColor);
        jSeparator4.setForeground(borderColor);
        jSeparator5.setForeground(borderColor);
        
        // Botón mostrar contraseña
        btnPassShow.setBackground(bgColor);
        
        // Botón actualizar
        editBtn3.setBackground(new Color(105, 115, 218));
    }

    private void cargarDatosUsuario() {
        textFieldNombre.setText(Sesion.getNombre());
        textFieldCorreo.setText(Sesion.getCorreo());
        textFieldApellido.setText(Sesion.getApellido());
        textFieldNick.setText(Sesion.getNickname());
        textFieldPass.setText("");
    }

    private void actualizarPerfil() {
        String nombre = textFieldNombre.getText().trim();
        String apellido = textFieldApellido.getText().trim();
        String correo = textFieldCorreo.getText().trim();
        String nickname = textFieldNick.getText().trim();
        String nuevaPass = new String(textFieldPass.getPassword());

        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || nickname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.");
            return;
        }

        try (Connection cn = Conexion.conectar()) {
            if (cn == null) {
                JOptionPane.showMessageDialog(this, "No se pudo conectar con la base de datos.");
                return;
            }

            String passwordGuardar = nuevaPass.isEmpty() ? Sesion.getPass() : MD5.encriptar(nuevaPass);
            
            String sql = "UPDATE usuarios SET nombre=?, apellidos=?, correo=?, nickname=?, password=? WHERE ID_Usuario=?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setString(1, nombre);
                ps.setString(2, apellido);
                ps.setString(3, correo);
                ps.setString(4, nickname);
                ps.setString(5, passwordGuardar);
                ps.setInt(6, Sesion.getIdUsuario());

                if (ps.executeUpdate() > 0) {
                    Sesion.iniciar(Sesion.getIdUsuario(), nombre, nickname, correo, apellido, passwordGuardar);
                    JOptionPane.showMessageDialog(this, "Perfil actualizado correctamente.");
                    textFieldPass.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el perfil.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void togglePasswordVisibility() {
        if (mostrar) {
            textFieldPass.setEchoChar(echoOriginal);
            btnPassShowText.setIcon(new ImageIcon(getClass().getResource("/img/show pass icon.png")));
        } else {
            textFieldPass.setEchoChar((char) 0);
            btnPassShowText.setIcon(new ImageIcon(getClass().getResource("/img/hide pass icon.png")));
        }
        mostrar = !mostrar;
    }

    // ==================== MÉTODO PARA ACTUALIZAR TEMA ====================
    
    /**
     * Método público para actualizar los colores cuando cambia el tema
     */
    public void actualizarTema() {
        configurarColores();
        reconstruirFormulario();
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelContenido = new javax.swing.JPanel();
        lblUserName = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        editBtn3 = new javax.swing.JPanel();
        editBtnText3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        textFieldNick = new javax.swing.JTextField();
        textFieldNombre = new javax.swing.JTextField();
        textFieldApellido = new javax.swing.JTextField();
        textFieldCorreo = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        textFieldPass = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        btnPassShow = new javax.swing.JPanel();
        btnPassShowText = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 40, 30, 40));
        setLayout(new java.awt.GridBagLayout());

        panelContenido.setBackground(new java.awt.Color(255, 255, 255));
        panelContenido.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        lblUserName.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblUserName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUserName.setText("Editar Perfil");

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setText("Nombre:");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setText("Apellidos:");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setText("Password:");

        editBtn3.setBackground(new java.awt.Color(105, 115, 218));
        editBtn3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        editBtnText3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        editBtnText3.setForeground(new java.awt.Color(255, 255, 255));
        editBtnText3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        editBtnText3.setText("Actualizar");
        editBtnText3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        editBtnText3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editBtnText3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                editBtnText3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                editBtnText3MouseExited(evt);
            }
        });

        javax.swing.GroupLayout editBtn3Layout = new javax.swing.GroupLayout(editBtn3);
        editBtn3.setLayout(editBtn3Layout);
        editBtn3Layout.setHorizontalGroup(
            editBtn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(editBtnText3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
        );
        editBtn3Layout.setVerticalGroup(
            editBtn3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(editBtnText3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setText("Correo:");

        textFieldNick.setBorder(null);

        textFieldNombre.setBorder(null);
        textFieldNombre.addActionListener(this::textFieldNombreActionPerformed);

        textFieldApellido.setBorder(null);

        textFieldCorreo.setBorder(null);
        textFieldCorreo.addActionListener(this::textFieldCorreoActionPerformed);

        textFieldPass.setBorder(null);
        textFieldPass.addActionListener(this::textFieldPassActionPerformed);

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setText("Nickname:");

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
            .addComponent(btnPassShowText, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        btnPassShowLayout.setVerticalGroup(
            btnPassShowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnPassShowText, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelContenidoLayout = new javax.swing.GroupLayout(panelContenido);
        panelContenido.setLayout(panelContenidoLayout);
        panelContenidoLayout.setHorizontalGroup(
            panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContenidoLayout.createSequentialGroup()
                .addContainerGap(125, Short.MAX_VALUE)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
            .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelContenidoLayout.createSequentialGroup()
                    .addGap(0, 48, Short.MAX_VALUE)
                    .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelContenidoLayout.createSequentialGroup()
                            .addGap(30, 30, 30)
                            .addComponent(lblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelContenidoLayout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(22, 22, 22)
                            .addComponent(textFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelContenidoLayout.createSequentialGroup()
                            .addGap(75, 75, 75)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelContenidoLayout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(14, 14, 14)
                            .addComponent(textFieldApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelContenidoLayout.createSequentialGroup()
                            .addGap(75, 75, 75)
                            .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelContenidoLayout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addGap(27, 27, 27)
                            .addComponent(textFieldCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelContenidoLayout.createSequentialGroup()
                            .addGap(75, 75, 75)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelContenidoLayout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addGap(10, 10, 10)
                            .addComponent(textFieldNick, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelContenidoLayout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addGap(10, 10, 10)
                            .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(textFieldPass, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(12, 12, 12)
                            .addComponent(btnPassShow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelContenidoLayout.createSequentialGroup()
                            .addGap(45, 45, 45)
                            .addComponent(editBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(0, 48, Short.MAX_VALUE)))
        );
        panelContenidoLayout.setVerticalGroup(
            panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContenidoLayout.createSequentialGroup()
                .addContainerGap(272, Short.MAX_VALUE)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79))
            .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelContenidoLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(lblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(30, 30, 30)
                    .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1)
                        .addComponent(textFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(7, 7, 7)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6)
                    .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2)
                        .addComponent(textFieldApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(5, 5, 5)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8, 8, 8)
                    .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel4)
                        .addComponent(textFieldCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(6, 6, 6)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8, 8, 8)
                    .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel5)
                        .addComponent(textFieldNick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(7, 7, 7)
                    .addGroup(panelContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelContenidoLayout.createSequentialGroup()
                            .addGap(21, 21, 21)
                            .addComponent(jLabel3))
                        .addGroup(panelContenidoLayout.createSequentialGroup()
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6, 6, 6)
                            .addComponent(textFieldPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelContenidoLayout.createSequentialGroup()
                            .addGap(7, 7, 7)
                            .addComponent(btnPassShow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(63, 63, 63)
                    .addComponent(editBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        add(panelContenido, new java.awt.GridBagConstraints());
    }// </editor-fold>//GEN-END:initComponents

    private void editBtnText3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editBtnText3MouseClicked
        actualizarPerfil();
    }//GEN-LAST:event_editBtnText3MouseClicked

    private void editBtnText3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editBtnText3MouseEntered
        editBtn3.setBackground(new Color(122, 133, 252));
    }//GEN-LAST:event_editBtnText3MouseEntered

    private void editBtnText3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editBtnText3MouseExited
        editBtn3.setBackground(new Color(105, 115, 218));
    }//GEN-LAST:event_editBtnText3MouseExited

    private void textFieldNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textFieldNombreActionPerformed

    private void textFieldCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textFieldCorreoActionPerformed

    private void textFieldPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldPassActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textFieldPassActionPerformed

    private void btnPassShowTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPassShowTextMouseClicked
        togglePasswordVisibility();
    }//GEN-LAST:event_btnPassShowTextMouseClicked

    private void btnPassShowTextMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPassShowTextMouseEntered
        btnPassShow.setBackground(new Color(243, 244, 246));
    }//GEN-LAST:event_btnPassShowTextMouseEntered

    private void btnPassShowTextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPassShowTextMouseExited
        btnPassShow.setBackground(ColorUtils.getBackground());
    }//GEN-LAST:event_btnPassShowTextMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnPassShow;
    private javax.swing.JLabel btnPassShowText;
    private javax.swing.JPanel editBtn3;
    private javax.swing.JLabel editBtnText3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JPanel panelContenido;
    private javax.swing.JTextField textFieldApellido;
    private javax.swing.JTextField textFieldCorreo;
    private javax.swing.JTextField textFieldNick;
    private javax.swing.JTextField textFieldNombre;
    private javax.swing.JPasswordField textFieldPass;
    // End of variables declaration//GEN-END:variables
}
