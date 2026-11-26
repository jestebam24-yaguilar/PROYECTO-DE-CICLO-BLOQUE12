/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import clases.Cliente;
import clases.ClienteContado;
import clases.ClienteHipotecario;
import clases.ClienteMivivienda;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import utilitario.Sistema;
import utilitario.Validacion;

/**
 *
 * @author Juan Esteban
 */
public class frmMantenimientoClientes extends javax.swing.JFrame {
    private DefaultTableModel modelo;

    public frmMantenimientoClientes() {
        initComponents();
        modelo = new DefaultTableModel();
        modelo.addColumn("DNI");
        modelo.addColumn("Cliente");
        modelo.addColumn("Estado civil");
        modelo.addColumn("Conyuge");
        modelo.addColumn("Telefono");
        modelo.addColumn("Correo");
        modelo.addColumn("Ingreso familiar");
        modelo.addColumn("Modalidad");
        tbCliente.setModel(modelo);
        
        listarClientes();
        habilitarCampos();
        setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
    }
    
    void listarClientes(){
        modelo.setNumRows(0);
        for (int i = 0; i < Sistema.gestionCliente.obtenerTotalClientes(); i++) {
            Cliente cliente = Sistema.gestionCliente.obtenerClientes()[i];
            Object[] fila = {
                cliente.getDni(),
                cliente.getNombreCompleto(),
                cliente.getEstadoCivil(),
                cliente.getNombreConyuge(),
                cliente.getTelefono(),
                cliente.getCorreo(),
                Validacion.moneda(cliente.getIngresoMensual()),
                cliente.getModalidad()
            };
            modelo.addRow(fila);
        }
    }
    
    boolean tieneConyuge(){
        String estado = cboEstadoCivil.getSelectedItem().toString();
        return estado.equals("Casado") || estado.equals("Conviviente");
    }
    
    boolean esCredito(){
        return !cboModalidad.getSelectedItem().toString().equals("Contado");
    }
    
    //Los datos del conyuge y del banco solo se piden cuando corresponden
    void habilitarCampos(){
        txtDniConyuge.setEnabled(tieneConyuge());
        txtNombreConyuge.setEnabled(tieneConyuge());
        txtBanco.setEnabled(esCredito());
    }
    
    void limpiar(){
        txtDni.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtDniConyuge.setText("");
        txtNombreConyuge.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtIngreso.setText("");
        txtBanco.setText("");
        cboEstadoCivil.setSelectedIndex(0);
        cboModalidad.setSelectedIndex(0);
        txtDni.setEditable(true);
        txtDni.requestFocus();
    }
    
    String validarFormulario(){
        if(!Validacion.esDni(txtDni.getText())){
            return "El DNI debe tener 8 digitos";
        }
        if(Validacion.esVacio(txtNombres.getText()) || Validacion.esVacio(txtApellidos.getText())){
            return "Ingrese nombres y apellidos";
        }
        if(tieneConyuge()){
            if(!Validacion.esDni(txtDniConyuge.getText())){
                return "El DNI del conyuge debe tener 8 digitos";
            }
            if(Validacion.esVacio(txtNombreConyuge.getText())){
                return "Ingrese el nombre del conyuge";
            }
        }
        if(!txtTelefono.getText().trim().matches("[0-9]{9}")){
            return "El telefono debe tener 9 digitos";
        }
        if(!txtCorreo.getText().trim().contains("@") || !txtCorreo.getText().trim().contains(".")){
            return "Ingrese un correo electronico valido";
        }
        if(!Validacion.esDecimal(txtIngreso.getText()) || Double.parseDouble(txtIngreso.getText().trim()) <= 0){
            return "Ingrese el ingreso mensual familiar";
        }
        if(esCredito() && Validacion.esVacio(txtBanco.getText())){
            return "Ingrese el banco del credito";
        }
        return null;
    }
    
    //Se crea la subclase segun la modalidad de compra
    Cliente crearCliente(){
        String dni = txtDni.getText().trim();
        String nombres = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String estadoCivil = cboEstadoCivil.getSelectedItem().toString();
        String dniConyuge = "";
        String nombreConyuge = "";
        if(tieneConyuge()){
            dniConyuge = txtDniConyuge.getText().trim();
            nombreConyuge = txtNombreConyuge.getText().trim();
        }
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        double ingreso = Double.parseDouble(txtIngreso.getText().trim());
        String modalidad = cboModalidad.getSelectedItem().toString();
        
        Cliente cliente;
        if(modalidad.equals("Contado")){
            cliente = new ClienteContado(dni, nombres, apellidos, estadoCivil, dniConyuge, nombreConyuge,
                    telefono, correo, ingreso);
        }else if(modalidad.equals("Credito Hipotecario")){
            cliente = new ClienteHipotecario(txtBanco.getText().trim(), dni, nombres, apellidos, estadoCivil,
                    dniConyuge, nombreConyuge, telefono, correo, ingreso);
        }else{
            cliente = new ClienteMivivienda(txtBanco.getText().trim(), dni, nombres, apellidos, estadoCivil,
                    dniConyuge, nombreConyuge, telefono, correo, ingreso);
        }
        return cliente;
    }
    
    private void agregar(JComponent componente, int x, int y, int ancho, int alto){
        componente.setBounds(x, y, ancho, alto);
        getContentPane().add(componente);
    }

    private void initComponents() {
        lblDni = new JLabel("DNI");
        lblNombres = new JLabel("Nombres");
        lblApellidos = new JLabel("Apellidos");
        lblEstadoCivil = new JLabel("Estado civil");
        lblDniConyuge = new JLabel("DNI conyuge");
        lblNombreConyuge = new JLabel("Nombre conyuge");
        lblTelefono = new JLabel("Telefono");
        lblCorreo = new JLabel("Correo");
        lblIngreso = new JLabel("Ingreso familiar");
        lblModalidad = new JLabel("Modalidad");
        lblBanco = new JLabel("Banco");
        txtDni = new JTextField();
        txtNombres = new JTextField();
        txtApellidos = new JTextField();
        cboEstadoCivil = new JComboBox<>(new String[]{"Soltero", "Casado", "Conviviente", "Divorciado", "Viudo"});
        txtDniConyuge = new JTextField();
        txtNombreConyuge = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();
        txtIngreso = new JTextField();
        cboModalidad = new JComboBox<>(new String[]{"Contado", "Credito Hipotecario", "Credito Mivivienda"});
        txtBanco = new JTextField();
        btnBuscar = new JButton("Buscar");
        btnRegistrar = new JButton("Registrar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        tbCliente = new JTable();
        jScrollPane1 = new JScrollPane(tbCliente);

        setTitle(".: Gestion de Clientes :.");
        getContentPane().setLayout(null);
        
        agregar(lblDni, 30, 20, 110, 25);
        agregar(txtDni, 150, 20, 110, 25);
        agregar(btnBuscar, 270, 20, 90, 25);
        agregar(lblNombres, 30, 55, 110, 25);
        agregar(txtNombres, 150, 55, 210, 25);
        agregar(lblApellidos, 30, 90, 110, 25);
        agregar(txtApellidos, 150, 90, 210, 25);
        agregar(lblEstadoCivil, 30, 125, 110, 25);
        agregar(cboEstadoCivil, 150, 125, 130, 25);
        agregar(lblDniConyuge, 30, 160, 110, 25);
        agregar(txtDniConyuge, 150, 160, 110, 25);
        agregar(lblNombreConyuge, 30, 195, 110, 25);
        agregar(txtNombreConyuge, 150, 195, 210, 25);
        agregar(lblTelefono, 400, 20, 110, 25);
        agregar(txtTelefono, 520, 20, 130, 25);
        agregar(lblCorreo, 400, 55, 110, 25);
        agregar(txtCorreo, 520, 55, 220, 25);
        agregar(lblIngreso, 400, 90, 110, 25);
        agregar(txtIngreso, 520, 90, 130, 25);
        agregar(lblModalidad, 400, 125, 110, 25);
        agregar(cboModalidad, 520, 125, 170, 25);
        agregar(lblBanco, 400, 160, 110, 25);
        agregar(txtBanco, 520, 160, 170, 25);
        agregar(btnRegistrar, 170, 240, 110, 30);
        agregar(btnActualizar, 290, 240, 110, 30);
        agregar(btnEliminar, 410, 240, 110, 30);
        agregar(btnLimpiar, 530, 240, 110, 30);
        agregar(jScrollPane1, 30, 285, 820, 220);

        cboEstadoCivil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                habilitarCampos();
            }
        });
        cboModalidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                habilitarCampos();
            }
        });
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiar();
            }
        });
        tbCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                navegar();
            }
        });

        setSize(890, 560);
    }

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {
        String error = validarFormulario();
        if(error != null){
            JOptionPane.showMessageDialog(this, error);
            return;
        }
        if(Sistema.gestionCliente.buscarPorDni(txtDni.getText().trim()) != null){
            JOptionPane.showMessageDialog(this, "Ya existe un cliente con el DNI " + txtDni.getText().trim());
            return;
        }
        if(Sistema.gestionCliente.registrar(crearCliente())){
            listarClientes();
            limpiar();
            JOptionPane.showMessageDialog(this, "Cliente registrado con exito");
        }else{
            JOptionPane.showMessageDialog(this, "No se pudo registrar el cliente");
        }
    }

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {
        String error = validarFormulario();
        if(error != null){
            JOptionPane.showMessageDialog(this, error);
            return;
        }
        if(Sistema.gestionCliente.actualizar(crearCliente())){
            listarClientes();
            JOptionPane.showMessageDialog(this, "Cliente actualizado con exito");
        }else{
            JOptionPane.showMessageDialog(this, "No se encontro el cliente");
        }
    }

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {
        String dni = txtDni.getText().trim();
        int respuesta = JOptionPane.showConfirmDialog(this, "Desea eliminar al cliente " + dni + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if(respuesta != JOptionPane.YES_OPTION){
            return;
        }
        if(Sistema.gestionCliente.eliminar(dni)){
            listarClientes();
            limpiar();
            JOptionPane.showMessageDialog(this, "Cliente eliminado con exito");
        }else{
            JOptionPane.showMessageDialog(this, "No se encontro el cliente");
        }
    }

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {
        Cliente cliente = Sistema.gestionCliente.buscarPorDni(txtDni.getText().trim());
        if(cliente != null){
            mostrar(cliente);
        }else{
            limpiar();
            JOptionPane.showMessageDialog(this, "No se encontro el cliente");
        }
    }
    
    void mostrar(Cliente cliente){
        txtDni.setText(cliente.getDni());
        txtNombres.setText(cliente.getNombres());
        txtApellidos.setText(cliente.getApellidos());
        cboEstadoCivil.setSelectedItem(cliente.getEstadoCivil());
        txtDniConyuge.setText(cliente.getDniConyuge());
        txtNombreConyuge.setText(cliente.getNombreConyuge());
        txtTelefono.setText(cliente.getTelefono());
        txtCorreo.setText(cliente.getCorreo());
        txtIngreso.setText("" + cliente.getIngresoMensual());
        cboModalidad.setSelectedItem(cliente.getModalidad());
        txtBanco.setText("");
        if(cliente instanceof ClienteHipotecario){
            txtBanco.setText(((ClienteHipotecario) cliente).getBanco());
        }else if(cliente instanceof ClienteMivivienda){
            txtBanco.setText(((ClienteMivivienda) cliente).getBanco());
        }
        txtDni.setEditable(false);
    }
    
    void navegar(){
        int fila = tbCliente.getSelectedRow();
        if(fila >= 0){
            mostrar(Sistema.gestionCliente.buscarPorDni("" + tbCliente.getValueAt(fila, 0)));
        }
    }

    // Variables declaration
    private JButton btnActualizar;
    private JButton btnBuscar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnRegistrar;
    private JComboBox<String> cboEstadoCivil;
    private JComboBox<String> cboModalidad;
    private JScrollPane jScrollPane1;
    private JLabel lblApellidos;
    private JLabel lblBanco;
    private JLabel lblCorreo;
    private JLabel lblDni;
    private JLabel lblDniConyuge;
    private JLabel lblEstadoCivil;
    private JLabel lblIngreso;
    private JLabel lblModalidad;
    private JLabel lblNombreConyuge;
    private JLabel lblNombres;
    private JLabel lblTelefono;
    private JTable tbCliente;
    private JTextField txtApellidos;
    private JTextField txtBanco;
    private JTextField txtCorreo;
    private JTextField txtDni;
    private JTextField txtDniConyuge;
    private JTextField txtIngreso;
    private JTextField txtNombreConyuge;
    private JTextField txtNombres;
    private JTextField txtTelefono;
    // End of variables declaration
}