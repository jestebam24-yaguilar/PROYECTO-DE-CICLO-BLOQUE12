/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import clases.Comprobante;
import clases.Cuota;
import clases.Venta;
import java.time.LocalDate;
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
public class frmPagos extends javax.swing.JFrame {
    private DefaultTableModel modeloCuota;
    private DefaultTableModel modeloComprobante;
    private Venta ventaActual;

    public frmPagos() {
        initComponents();
        modeloCuota = new DefaultTableModel();
        modeloCuota.addColumn("N");
        modeloCuota.addColumn("Vencimiento");
        modeloCuota.addColumn("Monto");
        modeloCuota.addColumn("Estado");
        modeloCuota.addColumn("Dias atraso");
        modeloCuota.addColumn("Interes moratorio");
        modeloCuota.addColumn("Fecha pago");
        tbCuota.setModel(modeloCuota);
        
        modeloComprobante = new DefaultTableModel();
        modeloComprobante.addColumn("Numero");
        modeloComprobante.addColumn("Tipo");
        modeloComprobante.addColumn("Fecha");
        modeloComprobante.addColumn("Documento");
        modeloComprobante.addColumn("Concepto");
        modeloComprobante.addColumn("Monto");
        modeloComprobante.addColumn("IGV incluido");
        modeloComprobante.addColumn("Interes");
        modeloComprobante.addColumn("Total");
        tbComprobante.setModel(modeloComprobante);
        
        txtFecha.setText(LocalDate.now().toString());
        habilitarFactura();
        setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
    }
    
    LocalDate fechaIngresada(){
        if(!Validacion.esFecha(txtFecha.getText())){
            JOptionPane.showMessageDialog(this, "Ingrese la fecha con formato aaaa-mm-dd");
            return null;
        }
        return Validacion.convertirFecha(txtFecha.getText());
    }
    
    //La factura exige RUC y razon social
    void habilitarFactura(){
        boolean esFactura = cboComprobante.getSelectedItem().toString().equals("Factura");
        txtRuc.setEnabled(esFactura);
        txtRazonSocial.setEnabled(esFactura);
    }
    
    void mostrarVenta(){
        modeloCuota.setNumRows(0);
        modeloComprobante.setNumRows(0);
        if(ventaActual == null){
            lblDatos.setText(" ");
            lblMontos.setText(" ");
            return;
        }
        LocalDate hoy = LocalDate.now();
        if(Validacion.esFecha(txtFecha.getText())){
            hoy = Validacion.convertirFecha(txtFecha.getText());
        }
        lblDatos.setText("Cliente: " + ventaActual.getCliente().getNombreCompleto() + " (" + ventaActual.getCliente().getModalidad()
                + ")  |  Unidad: " + ventaActual.getUnidad().getCodigo() + "  |  Estado: " + ventaActual.getEstado());
        lblMontos.setText("Total: " + Validacion.moneda(ventaActual.calcularPrecioTotal())
                + "  |  Pagado: " + Validacion.moneda(ventaActual.calcularTotalPagado())
                + "  |  Saldo: " + Validacion.moneda(ventaActual.calcularSaldoPendiente())
                + "  |  Banco: " + Validacion.moneda(ventaActual.calcularMontoBanco())
                + "  |  Cuotas vencidas: " + ventaActual.contarCuotasVencidas(hoy));
        
        for (int i = 0; i < ventaActual.obtenerTotalCuotas(); i++) {
            Cuota cuota = ventaActual.obtenerCronograma()[i];
            String fechaPago = "";
            double interes = cuota.calcularInteres(hoy);
            if(cuota.isPagada()){
                fechaPago = cuota.getFechaPago().toString();
                interes = cuota.getInteresPagado();
            }
            Object[] fila = {cuota.getNumero(), cuota.getFechaVencimiento(), Validacion.moneda(cuota.getMonto()),
                cuota.getEstado(hoy), cuota.calcularDiasAtraso(hoy), Validacion.moneda(interes), fechaPago};
            modeloCuota.addRow(fila);
        }
        
        for (int i = 0; i < Sistema.gestionVenta.obtenerTotalComprobantes(); i++) {
            Comprobante comprobante = Sistema.gestionVenta.obtenerComprobantes()[i];
            if(comprobante.getVenta() != ventaActual){
                continue;
            }
            Object[] fila = {comprobante.getNumero(), comprobante.getTipo(), comprobante.getFecha(),
                comprobante.getDocumentoCliente(), comprobante.getConcepto(), Validacion.moneda(comprobante.getMonto()),
                Validacion.moneda(comprobante.calcularIGV()), Validacion.moneda(comprobante.getInteres()),
                Validacion.moneda(comprobante.calcularTotal())};
            modeloComprobante.addRow(fila);
        }
    }
    
    //Despues de cada operacion se informa el comprobante emitido
    void informarResultado(String error){
        mostrarVenta();
        if(error == null){
            Comprobante comprobante = Sistema.gestionVenta.obtenerUltimoComprobante();
            JOptionPane.showMessageDialog(this, "Operacion registrada. Se emitio " + comprobante.getTipo() + " "
                    + comprobante.getNumero() + " por " + Validacion.moneda(comprobante.calcularTotal()));
        }else{
            JOptionPane.showMessageDialog(this, error, "No se pudo registrar", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void agregar(JComponent componente, int x, int y, int ancho, int alto){
        componente.setBounds(x, y, ancho, alto);
        getContentPane().add(componente);
    }

    private void initComponents() {
        lblContrato = new JLabel("N de contrato");
        txtContrato = new JTextField();
        btnBuscar = new JButton("Buscar");
        lblFecha = new JLabel("Fecha de pago");
        txtFecha = new JTextField();
        lblComprobante = new JLabel("Comprobante");
        cboComprobante = new JComboBox<>(new String[]{"Boleta", "Factura"});
        lblRuc = new JLabel("RUC");
        txtRuc = new JTextField();
        lblRazonSocial = new JLabel("Razon social");
        txtRazonSocial = new JTextField();
        lblDatos = new JLabel(" ");
        lblMontos = new JLabel(" ");
        btnFirmar = new JButton("Firmar contrato (pagar inicial)");
        btnPagarCuota = new JButton("Pagar cuota seleccionada");
        btnDesembolso = new JButton("Registrar desembolso del banco");
        btnActualizarEstados = new JButton("Revisar vencimientos");
        tbCuota = new JTable();
        jScrollPane1 = new JScrollPane(tbCuota);
        tbComprobante = new JTable();
        jScrollPane2 = new JScrollPane(tbComprobante);

        setTitle(".: Pagos y Comprobantes :.");
        getContentPane().setLayout(null);
        
        agregar(lblContrato, 30, 20, 100, 25);
        agregar(txtContrato, 130, 20, 90, 25);
        agregar(btnBuscar, 230, 20, 90, 25);
        agregar(lblFecha, 350, 20, 100, 25);
        agregar(txtFecha, 450, 20, 100, 25);
        agregar(lblComprobante, 30, 55, 100, 25);
        agregar(cboComprobante, 130, 55, 100, 25);
        agregar(lblRuc, 250, 55, 40, 25);
        agregar(txtRuc, 290, 55, 120, 25);
        agregar(lblRazonSocial, 430, 55, 90, 25);
        agregar(txtRazonSocial, 520, 55, 250, 25);
        agregar(lblDatos, 30, 90, 900, 25);
        lblMontos.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        agregar(lblMontos, 30, 115, 900, 25);
        agregar(btnFirmar, 30, 150, 220, 28);
        agregar(btnPagarCuota, 260, 150, 200, 28);
        agregar(btnDesembolso, 470, 150, 230, 28);
        agregar(btnActualizarEstados, 710, 150, 190, 28);
        agregar(jScrollPane1, 30, 190, 870, 190);
        agregar(jScrollPane2, 30, 390, 870, 140);

        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        cboComprobante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                habilitarFactura();
            }
        });
        btnFirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirmarActionPerformed(evt);
            }
        });
        btnPagarCuota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagarCuotaActionPerformed(evt);
            }
        });
        btnDesembolso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesembolsoActionPerformed(evt);
            }
        });
        btnActualizarEstados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarEstadosActionPerformed(evt);
            }
        });

        setSize(940, 590);
    }

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {
        if(!Validacion.esEntero(txtContrato.getText())){
            JOptionPane.showMessageDialog(this, "Ingrese el numero de contrato");
            return;
        }
        ventaActual = Sistema.gestionVenta.buscarPorContrato(Integer.parseInt(txtContrato.getText().trim()));
        mostrarVenta();
        if(ventaActual == null){
            JOptionPane.showMessageDialog(this, "No existe el contrato " + txtContrato.getText().trim());
        }
    }

    private void btnFirmarActionPerformed(java.awt.event.ActionEvent evt) {
        LocalDate fecha = fechaIngresada();
        if(ventaActual == null || fecha == null){
            return;
        }
        String error = Sistema.gestionVenta.firmarContrato(ventaActual.getNumeroContrato(), fecha,
                cboComprobante.getSelectedItem().toString(), txtRuc.getText().trim(), txtRazonSocial.getText().trim());
        informarResultado(error);
    }

    private void btnPagarCuotaActionPerformed(java.awt.event.ActionEvent evt) {
        LocalDate fecha = fechaIngresada();
        if(ventaActual == null || fecha == null){
            return;
        }
        int fila = tbCuota.getSelectedRow();
        if(fila < 0){
            JOptionPane.showMessageDialog(this, "Seleccione la cuota a pagar en la tabla");
            return;
        }
        int numeroCuota = Integer.parseInt("" + tbCuota.getValueAt(fila, 0));
        String error = Sistema.gestionVenta.registrarPagoCuota(ventaActual.getNumeroContrato(), numeroCuota, fecha,
                cboComprobante.getSelectedItem().toString(), txtRuc.getText().trim(), txtRazonSocial.getText().trim());
        informarResultado(error);
    }

    private void btnDesembolsoActionPerformed(java.awt.event.ActionEvent evt) {
        LocalDate fecha = fechaIngresada();
        if(ventaActual == null || fecha == null){
            return;
        }
        String error = Sistema.gestionVenta.registrarDesembolso(ventaActual.getNumeroContrato(), fecha);
        informarResultado(error);
    }

    private void btnActualizarEstadosActionPerformed(java.awt.event.ActionEvent evt) {
        LocalDate fecha = fechaIngresada();
        if(fecha == null){
            return;
        }
        int cambios = Sistema.gestionVenta.actualizarEstados(fecha);
        mostrarVenta();
        JOptionPane.showMessageDialog(this, "Revision al " + fecha + ": " + cambios
                + " venta(s) cambiaron a Desistida o En Morosidad");
    }

    // Variables declaration
    private JButton btnActualizarEstados;
    private JButton btnBuscar;
    private JButton btnDesembolso;
    private JButton btnFirmar;
    private JButton btnPagarCuota;
    private JComboBox<String> cboComprobante;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JLabel lblComprobante;
    private JLabel lblContrato;
    private JLabel lblDatos;
    private JLabel lblFecha;
    private JLabel lblMontos;
    private JLabel lblRazonSocial;
    private JLabel lblRuc;
    private JTable tbComprobante;
    private JTable tbCuota;
    private JTextField txtContrato;
    private JTextField txtFecha;
    private JTextField txtRazonSocial;
    private JTextField txtRuc;
    // End of variables declaration
}
