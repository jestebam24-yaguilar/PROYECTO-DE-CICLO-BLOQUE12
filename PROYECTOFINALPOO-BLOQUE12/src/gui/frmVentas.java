/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import clases.Adicional;
import clases.Cliente;
import clases.Paquete;
import clases.UnidadInmobiliaria;
import clases.Venta;
import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import utilitario.Sistema;
import utilitario.Validacion;

/**
 *
 * @author Juan Esteban
 */
public class frmVentas extends javax.swing.JFrame {
    private DefaultTableModel modelo;
    private Cliente clienteActual;
    //Lo que se va eligiendo para la venta
    private Cliente[] copropietarios = new Cliente[3];
    private int totalCopropietarios = 0;
    private Adicional[] adicionales = new Adicional[10];
    private int totalAdicionales = 0;
    private Paquete[] paquetes = new Paquete[5];
    private int totalPaquetes = 0;

    public frmVentas() {
        initComponents();
        modelo = new DefaultTableModel();
        modelo.addColumn("Contrato");
        modelo.addColumn("Fecha");
        modelo.addColumn("Cliente");
        modelo.addColumn("Unidad");
        modelo.addColumn("Modalidad");
        modelo.addColumn("Precio total");
        modelo.addColumn("Estado");
        tbVenta.setModel(modelo);
        txtFecha.setText(LocalDate.now().toString());
        
        cargarProyectos();
        listarVentas();
        setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
    }
    
    void cargarProyectos(){
        cboProyecto.removeAllItems();
        for (int i = 0; i < Sistema.gestionProyecto.obtenerTotalProyectos(); i++) {
            cboProyecto.addItem(Sistema.gestionProyecto.obtenerProyectos()[i].getCodigo());
        }
    }
    
    String proyectoSeleccionado(){
        if(cboProyecto.getSelectedItem() == null){
            return "";
        }
        return cboProyecto.getSelectedItem().toString();
    }
    
    //Se muestran todas las unidades con su estado para que se vea por que se rechaza una venta
    void cambiarProyecto(){
        cboUnidad.removeAllItems();
        cboAdicional.removeAllItems();
        cboPaquete.removeAllItems();
        for (int i = 0; i < Sistema.gestionUnidad.obtenerTotalUnidades(); i++) {
            UnidadInmobiliaria unidad = Sistema.gestionUnidad.obtenerUnidades()[i];
            if(unidad.getCodigoProyecto().equals(proyectoSeleccionado())){
                cboUnidad.addItem(unidad.getCodigo() + " - " + unidad.getTipo() + " - " + unidad.getEstado());
            }
        }
        for (int i = 0; i < Sistema.gestionAdicional.obtenerTotalAdicionales(); i++) {
            Adicional adicional = Sistema.gestionAdicional.obtenerAdicionales()[i];
            if(adicional.getCodigoProyecto().equals(proyectoSeleccionado())){
                cboAdicional.addItem(adicional.getCodigo() + " - " + adicional.getNombre());
            }
        }
        for (int i = 0; i < Sistema.gestionAdicional.obtenerTotalPaquetes(); i++) {
            Paquete paquete = Sistema.gestionAdicional.obtenerPaquetes()[i];
            if(paquete.getCodigoProyecto().equals(proyectoSeleccionado())){
                cboPaquete.addItem(paquete.getCodigo() + " - " + paquete.getNombre());
            }
        }
        //Los adicionales elegidos eran del otro proyecto
        totalAdicionales = 0;
        totalPaquetes = 0;
        mostrarSeleccion();
    }
    
    UnidadInmobiliaria unidadSeleccionada(){
        if(cboUnidad.getSelectedItem() == null){
            return null;
        }
        String codigo = cboUnidad.getSelectedItem().toString().split(" - ")[0];
        return Sistema.gestionUnidad.buscar(proyectoSeleccionado(), codigo);
    }
    
    void listarVentas(){
        modelo.setNumRows(0);
        for (int i = 0; i < Sistema.gestionVenta.obtenerTotalVentas(); i++) {
            Venta venta = Sistema.gestionVenta.obtenerVentas()[i];
            Object[] fila = {
                venta.getNumeroContrato(),
                venta.getFechaSeparacion(),
                venta.getCliente().getNombreCompleto(),
                venta.getUnidad().getCodigoProyecto() + " / " + venta.getUnidad().getCodigo(),
                venta.getCliente().getModalidad(),
                Validacion.moneda(venta.calcularPrecioTotal()),
                venta.getEstado()
            };
            modelo.addRow(fila);
        }
    }
    
    void mostrarSeleccion(){
        String texto = "Copropietarios: ";
        for (int i = 0; i < totalCopropietarios; i++) {
            texto = texto + copropietarios[i].getNombreCompleto() + "; ";
        }
        texto = texto + "\nAdicionales: ";
        for (int i = 0; i < totalAdicionales; i++) {
            texto = texto + adicionales[i].getNombre() + "; ";
        }
        texto = texto + "\nPaquetes: ";
        for (int i = 0; i < totalPaquetes; i++) {
            texto = texto + paquetes[i].getNombre() + "; ";
        }
        txaSeleccion.setText(texto);
    }
    
    void limpiar(){
        clienteActual = null;
        txtDni.setText("");
        lblCliente.setText("Cliente: -");
        txtDniCopropietario.setText("");
        txtMontoSeparacion.setText("");
        txtCuotaInicial.setText("");
        txtNumeroCuotas.setText("");
        txtFecha.setText(LocalDate.now().toString());
        totalCopropietarios = 0;
        totalAdicionales = 0;
        totalPaquetes = 0;
        mostrarSeleccion();
        txaResumen.setText("");
        cambiarProyecto();
        txtDni.requestFocus();
    }
    
    //Arma el objeto Venta con lo ingresado; devuelve null y avisa si falta algo
    Venta crearVenta(){
        if(clienteActual == null){
            JOptionPane.showMessageDialog(this, "Busque primero al cliente por su DNI");
            return null;
        }
        UnidadInmobiliaria unidad = unidadSeleccionada();
        if(unidad == null){
            JOptionPane.showMessageDialog(this, "Seleccione la unidad");
            return null;
        }
        if(!Validacion.esDecimal(txtMontoSeparacion.getText()) || !Validacion.esDecimal(txtCuotaInicial.getText())){
            JOptionPane.showMessageDialog(this, "Ingrese el monto de separacion y la cuota inicial");
            return null;
        }
        if(!Validacion.esEntero(txtNumeroCuotas.getText())){
            JOptionPane.showMessageDialog(this, "Ingrese el numero de cuotas directas (1 a 24)");
            return null;
        }
        if(!Validacion.esFecha(txtFecha.getText())){
            JOptionPane.showMessageDialog(this, "Ingrese la fecha con formato aaaa-mm-dd");
            return null;
        }
        String dniAsesor = "";
        if(Sistema.empleadoActual != null){
            dniAsesor = Sistema.empleadoActual.getDni();
        }
        
        Venta venta = new Venta(clienteActual, unidad, dniAsesor, Validacion.convertirFecha(txtFecha.getText()),
                Double.parseDouble(txtMontoSeparacion.getText().trim()),
                Double.parseDouble(txtCuotaInicial.getText().trim()),
                Integer.parseInt(txtNumeroCuotas.getText().trim()));
        for (int i = 0; i < totalCopropietarios; i++) {
            venta.agregarCopropietario(copropietarios[i]);
        }
        for (int i = 0; i < totalAdicionales; i++) {
            venta.agregarAdicional(adicionales[i]);
        }
        for (int i = 0; i < totalPaquetes; i++) {
            venta.agregarPaquete(paquetes[i]);
        }
        return venta;
    }
    
    void mostrarResumen(Venta venta){
        Cliente cliente = venta.getCliente();
        double limite = cliente.getIngresoMensual() * 0.30;
        txaResumen.setText(
                "Precio de lista de la unidad: " + Validacion.moneda(venta.getUnidad().calcularPrecio())
                + "\nPrecio con condicion " + cliente.getModalidad() + ": " + Validacion.moneda(venta.getPrecioUnidad())
                + "\nAdicionales: " + Validacion.moneda(venta.calcularPrecioAdicionales())
                + "\nPaquetes: " + Validacion.moneda(venta.calcularPrecioPaquetes())
                + "\nPRECIO TOTAL: " + Validacion.moneda(venta.calcularPrecioTotal())
                + "\nCuota inicial minima: " + Validacion.moneda(cliente.calcularInicialMinima(venta.calcularPrecioTotal()))
                + "\nDesembolso del banco: " + Validacion.moneda(venta.calcularMontoBanco())
                + "\nMonto en cuotas directas: " + Validacion.moneda(venta.calcularMontoDirecto())
                + "\nCuota mensual: " + Validacion.moneda(venta.calcularCuotaMensual())
                + "   (maximo 30% del ingreso: " + Validacion.moneda(limite) + ")");
    }
    
    private void agregar(JComponent componente, int x, int y, int ancho, int alto){
        componente.setBounds(x, y, ancho, alto);
        getContentPane().add(componente);
    }

    private void initComponents() {
        lblDni = new JLabel("DNI cliente");
        txtDni = new JTextField();
        btnBuscarCliente = new JButton("Buscar");
        lblCliente = new JLabel("Cliente: -");
        lblProyecto = new JLabel("Proyecto");
        cboProyecto = new JComboBox<>();
        lblUnidad = new JLabel("Unidad");
        cboUnidad = new JComboBox<>();
        lblCopropietario = new JLabel("DNI copropietario");
        txtDniCopropietario = new JTextField();
        btnAgregarCopropietario = new JButton("Agregar");
        lblAdicional = new JLabel("Adicional");
        cboAdicional = new JComboBox<>();
        btnAgregarAdicional = new JButton("Agregar");
        lblPaquete = new JLabel("Paquete");
        cboPaquete = new JComboBox<>();
        btnAgregarPaquete = new JButton("Agregar");
        txaSeleccion = new JTextArea();
        jScrollPane3 = new JScrollPane(txaSeleccion);
        lblMontoSeparacion = new JLabel("Monto separacion");
        txtMontoSeparacion = new JTextField();
        lblCuotaInicial = new JLabel("Cuota inicial");
        txtCuotaInicial = new JTextField();
        lblNumeroCuotas = new JLabel("N cuotas (1-24)");
        txtNumeroCuotas = new JTextField();
        lblFecha = new JLabel("Fecha separacion");
        txtFecha = new JTextField();
        btnCalcular = new JButton("Calcular");
        btnRegistrar = new JButton("Registrar separacion");
        btnLimpiar = new JButton("Limpiar");
        txaResumen = new JTextArea();
        jScrollPane2 = new JScrollPane(txaResumen);
        tbVenta = new JTable();
        jScrollPane1 = new JScrollPane(tbVenta);

        setTitle(".: Separacion y Venta de Unidades :.");
        getContentPane().setLayout(null);
        
        agregar(lblDni, 30, 20, 120, 25);
        agregar(txtDni, 150, 20, 100, 25);
        agregar(btnBuscarCliente, 260, 20, 90, 25);
        agregar(lblCliente, 30, 50, 420, 25);
        agregar(lblProyecto, 30, 85, 120, 25);
        agregar(cboProyecto, 150, 85, 100, 25);
        agregar(lblUnidad, 30, 120, 120, 25);
        agregar(cboUnidad, 150, 120, 300, 25);
        agregar(lblCopropietario, 30, 155, 120, 25);
        agregar(txtDniCopropietario, 150, 155, 100, 25);
        agregar(btnAgregarCopropietario, 260, 155, 90, 25);
        agregar(lblAdicional, 30, 190, 120, 25);
        agregar(cboAdicional, 150, 190, 200, 25);
        agregar(btnAgregarAdicional, 360, 190, 90, 25);
        agregar(lblPaquete, 30, 225, 120, 25);
        agregar(cboPaquete, 150, 225, 200, 25);
        agregar(btnAgregarPaquete, 360, 225, 90, 25);
        txaSeleccion.setEditable(false);
        agregar(jScrollPane3, 30, 260, 420, 70);
        agregar(lblMontoSeparacion, 490, 20, 120, 25);
        agregar(txtMontoSeparacion, 620, 20, 110, 25);
        agregar(lblCuotaInicial, 490, 55, 120, 25);
        agregar(txtCuotaInicial, 620, 55, 110, 25);
        agregar(lblNumeroCuotas, 490, 90, 120, 25);
        agregar(txtNumeroCuotas, 620, 90, 60, 25);
        agregar(lblFecha, 490, 125, 120, 25);
        agregar(txtFecha, 620, 125, 110, 25);
        agregar(btnCalcular, 750, 20, 170, 28);
        agregar(btnRegistrar, 750, 55, 170, 28);
        agregar(btnLimpiar, 750, 90, 170, 28);
        txaResumen.setEditable(false);
        agregar(jScrollPane2, 490, 160, 430, 170);
        agregar(jScrollPane1, 30, 345, 890, 190);

        btnBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienteActionPerformed(evt);
            }
        });
        cboProyecto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiarProyecto();
            }
        });
        btnAgregarCopropietario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCopropietarioActionPerformed(evt);
            }
        });
        btnAgregarAdicional.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarAdicionalActionPerformed(evt);
            }
        });
        btnAgregarPaquete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarPaqueteActionPerformed(evt);
            }
        });
        btnCalcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularActionPerformed(evt);
            }
        });
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiar();
            }
        });

        setSize(960, 590);
    }

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {
        clienteActual = Sistema.gestionCliente.buscarPorDni(txtDni.getText().trim());
        if(clienteActual == null){
            lblCliente.setText("Cliente: -");
            JOptionPane.showMessageDialog(this, "No existe el cliente. Registrelo primero en Clientes");
            return;
        }
        lblCliente.setText("Cliente: " + clienteActual.getNombreCompleto() + " | " + clienteActual.getModalidad()
                + " | Ingreso: " + Validacion.moneda(clienteActual.getIngresoMensual()));
    }

    private void btnAgregarCopropietarioActionPerformed(java.awt.event.ActionEvent evt) {
        Cliente copropietario = Sistema.gestionCliente.buscarPorDni(txtDniCopropietario.getText().trim());
        if(copropietario == null){
            JOptionPane.showMessageDialog(this, "El copropietario debe estar registrado como cliente");
            return;
        }
        if(clienteActual != null && copropietario.getDni().equals(clienteActual.getDni())){
            JOptionPane.showMessageDialog(this, "El copropietario no puede ser el mismo titular");
            return;
        }
        if(totalCopropietarios == copropietarios.length){
            JOptionPane.showMessageDialog(this, "Maximo 3 copropietarios");
            return;
        }
        copropietarios[totalCopropietarios] = copropietario;
        totalCopropietarios++;
        txtDniCopropietario.setText("");
        mostrarSeleccion();
    }

    private void btnAgregarAdicionalActionPerformed(java.awt.event.ActionEvent evt) {
        if(cboAdicional.getSelectedItem() == null || totalAdicionales == adicionales.length){
            return;
        }
        String codigo = cboAdicional.getSelectedItem().toString().split(" - ")[0];
        adicionales[totalAdicionales] = Sistema.gestionAdicional.buscarAdicional(proyectoSeleccionado(), codigo);
        totalAdicionales++;
        mostrarSeleccion();
    }

    private void btnAgregarPaqueteActionPerformed(java.awt.event.ActionEvent evt) {
        if(cboPaquete.getSelectedItem() == null || totalPaquetes == paquetes.length){
            return;
        }
        String codigo = cboPaquete.getSelectedItem().toString().split(" - ")[0];
        paquetes[totalPaquetes] = Sistema.gestionAdicional.buscarPaquete(codigo);
        totalPaquetes++;
        mostrarSeleccion();
    }

    private void btnCalcularActionPerformed(java.awt.event.ActionEvent evt) {
        Venta venta = crearVenta();
        if(venta != null){
            mostrarResumen(venta);
        }
    }

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {
        Venta venta = crearVenta();
        if(venta == null){
            return;
        }
        mostrarResumen(venta);
        //La gestion valida (a) disponibilidad, (b) inicial minima, (c) 30% del ingreso y (d) stock
        String error = Sistema.gestionVenta.registrarSeparacion(venta);
        if(error == null){
            listarVentas();
            JOptionPane.showMessageDialog(this, "Separacion registrada. Contrato N " + venta.getNumeroContrato()
                    + "\nTiene 15 dias para firmar el contrato");
            limpiar();
        }else{
            JOptionPane.showMessageDialog(this, error, "Venta rechazada", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Variables declaration
    private JButton btnAgregarAdicional;
    private JButton btnAgregarCopropietario;
    private JButton btnAgregarPaquete;
    private JButton btnBuscarCliente;
    private JButton btnCalcular;
    private JButton btnLimpiar;
    private JButton btnRegistrar;
    private JComboBox<String> cboAdicional;
    private JComboBox<String> cboPaquete;
    private JComboBox<String> cboProyecto;
    private JComboBox<String> cboUnidad;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JLabel lblAdicional;
    private JLabel lblCliente;
    private JLabel lblCopropietario;
    private JLabel lblCuotaInicial;
    private JLabel lblDni;
    private JLabel lblFecha;
    private JLabel lblMontoSeparacion;
    private JLabel lblNumeroCuotas;
    private JLabel lblPaquete;
    private JLabel lblProyecto;
    private JLabel lblUnidad;
    private JTable tbVenta;
    private JTextArea txaResumen;
    private JTextArea txaSeleccion;
    private JTextField txtCuotaInicial;
    private JTextField txtDni;
    private JTextField txtDniCopropietario;
    private JTextField txtFecha;
    private JTextField txtMontoSeparacion;
    private JTextField txtNumeroCuotas;
    // End of variables declaration
}

