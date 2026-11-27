/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import clases.Cuota;
import clases.Venta;
import java.time.LocalDate;
import javax.swing.JButton;
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
public class frmEstadoCuenta extends javax.swing.JFrame {
    private DefaultTableModel modelo;

    public frmEstadoCuenta() {
        initComponents();
        modelo = new DefaultTableModel();
        modelo.addColumn("N");
        modelo.addColumn("Vencimiento");
        modelo.addColumn("Monto");
        modelo.addColumn("Estado");
        modelo.addColumn("Dias atraso");
        modelo.addColumn("Interes");
        tbCronograma.setModel(modelo);
        txtFechaCorte.setText(LocalDate.now().toString());
        setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
    }
    
    void mostrarEstadoCuenta(Venta venta, LocalDate corte){
        String texto = "ESTADO DE CUENTA - CONTRATO N " + venta.getNumeroContrato() + "  (al " + corte + ")\n";
        texto = texto + "Cliente: " + venta.getCliente().getNombreCompleto() + " - DNI " + venta.getCliente().getDni()
                + " - " + venta.getCliente().getModalidad() + "\n";
        if(venta.obtenerTotalCopropietarios() > 0){
            texto = texto + "Copropietarios: ";
            for (int i = 0; i < venta.obtenerTotalCopropietarios(); i++) {
                texto = texto + venta.obtenerCopropietarios()[i].getNombreCompleto() + "; ";
            }
            texto = texto + "\n";
        }
        texto = texto + "Unidad: " + venta.getUnidad().getCodigo() + " (" + venta.getUnidad().getTipo() + ") Torre "
                + venta.getUnidad().getTorre() + " Piso " + venta.getUnidad().getPiso() + " - Proyecto "
                + venta.getUnidad().getCodigoProyecto() + "\n";
        texto = texto + "Estado de la venta: " + venta.getEstado() + "\n\n";
        
        texto = texto + "Precio de lista de la unidad:   " + Validacion.moneda(venta.getUnidad().calcularPrecio()) + "\n";
        texto = texto + "Precio con descuento/bono:      " + Validacion.moneda(venta.getPrecioUnidad()) + "\n";
        for (int i = 0; i < venta.obtenerTotalAdicionales(); i++) {
            texto = texto + "  + " + venta.obtenerAdicionales()[i].getNombre() + ": "
                    + Validacion.moneda(venta.obtenerAdicionales()[i].getPrecio()) + "\n";
        }
        for (int i = 0; i < venta.obtenerTotalPaquetes(); i++) {
            texto = texto + "  + " + venta.obtenerPaquetes()[i].getNombre() + ": "
                    + Validacion.moneda(venta.obtenerPaquetes()[i].getPrecioPromocional()) + "\n";
        }
        texto = texto + "PRECIO TOTAL:                   " + Validacion.moneda(venta.calcularPrecioTotal()) + "\n";
        texto = texto + "IGV incluido (sobre el 50% de construccion): " + Validacion.moneda(venta.calcularIGV()) + "\n\n";
        
        texto = texto + "Separacion: " + Validacion.moneda(venta.getMontoSeparacion()) + " - pagada\n";
        texto = texto + "Cuota inicial: " + Validacion.moneda(venta.getCuotaInicial());
        if(venta.isInicialPagada()){
            texto = texto + " - pagada\n";
        }else{
            texto = texto + " - pendiente\n";
        }
        if(venta.calcularMontoBanco() > 0){
            texto = texto + "Desembolso del banco: " + Validacion.moneda(venta.calcularMontoBanco());
            if(venta.isDesembolsoRegistrado()){
                texto = texto + " - recibido\n";
            }else{
                texto = texto + " - pendiente\n";
            }
        }
        texto = texto + "TOTAL PAGADO:      " + Validacion.moneda(venta.calcularTotalPagado()) + "\n";
        texto = texto + "Cuotas vencidas:   " + venta.contarCuotasVencidas(corte) + " ("
                + Validacion.moneda(venta.calcularMontoVencido(corte)) + ")\n";
        texto = texto + "Intereses moratorios pendientes: " + Validacion.moneda(venta.calcularInteresesPendientes(corte)) + "\n";
        texto = texto + "Intereses moratorios pagados:    " + Validacion.moneda(venta.calcularInteresesPagados()) + "\n";
        texto = texto + "SALDO PENDIENTE:   " + Validacion.moneda(venta.calcularSaldoPendiente()) + "\n\n";
        
        texto = texto + "HISTORIAL\n";
        for (int i = 0; i < venta.obtenerTotalHistorial(); i++) {
            texto = texto + venta.obtenerHistorial()[i] + "\n";
        }
        txaEstadoCuenta.setText(texto);
        txaEstadoCuenta.setCaretPosition(0);
        
        modelo.setNumRows(0);
        for (int i = 0; i < venta.obtenerTotalCuotas(); i++) {
            Cuota cuota = venta.obtenerCronograma()[i];
            Object[] fila = {cuota.getNumero(), cuota.getFechaVencimiento(), Validacion.moneda(cuota.getMonto()),
                cuota.getEstado(corte), cuota.calcularDiasAtraso(corte), Validacion.moneda(cuota.calcularInteres(corte))};
            modelo.addRow(fila);
        }
    }
    
    private void agregar(JComponent componente, int x, int y, int ancho, int alto){
        componente.setBounds(x, y, ancho, alto);
        getContentPane().add(componente);
    }

    private void initComponents() {
        lblContrato = new JLabel("N de contrato");
        txtContrato = new JTextField();
        lblFechaCorte = new JLabel("Fecha de corte");
        txtFechaCorte = new JTextField();
        btnConsultar = new JButton("Consultar");
        txaEstadoCuenta = new JTextArea();
        jScrollPane1 = new JScrollPane(txaEstadoCuenta);
        tbCronograma = new JTable();
        jScrollPane2 = new JScrollPane(tbCronograma);

        setTitle(".: Estado de Cuenta y Cronograma de Pagos :.");
        getContentPane().setLayout(null);
        
        agregar(lblContrato, 30, 20, 100, 25);
        agregar(txtContrato, 130, 20, 90, 25);
        agregar(lblFechaCorte, 240, 20, 100, 25);
        agregar(txtFechaCorte, 340, 20, 100, 25);
        agregar(btnConsultar, 460, 20, 110, 25);
        txaEstadoCuenta.setEditable(false);
        txaEstadoCuenta.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        agregar(jScrollPane1, 30, 60, 460, 470);
        agregar(jScrollPane2, 510, 60, 420, 470);

        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });

        setSize(970, 590);
    }

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {
        if(!Validacion.esEntero(txtContrato.getText())){
            JOptionPane.showMessageDialog(this, "Ingrese el numero de contrato");
            return;
        }
        if(!Validacion.esFecha(txtFechaCorte.getText())){
            JOptionPane.showMessageDialog(this, "Ingrese la fecha de corte con formato aaaa-mm-dd");
            return;
        }
        Venta venta = Sistema.gestionVenta.buscarPorContrato(Integer.parseInt(txtContrato.getText().trim()));
        if(venta == null){
            txaEstadoCuenta.setText("");
            modelo.setNumRows(0);
            JOptionPane.showMessageDialog(this, "No existe el contrato " + txtContrato.getText().trim());
            return;
        }
        mostrarEstadoCuenta(venta, Validacion.convertirFecha(txtFechaCorte.getText()));
    }

    // Variables declaration
    private JButton btnConsultar;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JLabel lblContrato;
    private JLabel lblFechaCorte;
    private JTable tbCronograma;
    private JTextArea txaEstadoCuenta;
    private JTextField txtContrato;
    private JTextField txtFechaCorte;
    // End of variables declaration
}
