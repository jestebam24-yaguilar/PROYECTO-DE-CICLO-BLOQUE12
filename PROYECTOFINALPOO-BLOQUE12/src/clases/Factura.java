/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.time.LocalDate;

/**
 *
 * @author Juan Esteban
 */
public class Factura implements Comprobante {
    private static final double TASA_IGV = 0.18;
    private String numero;
    private LocalDate fecha;
    private Venta venta;
    private String concepto;
    private double monto;
    private double interes;
    private String ruc;
    private String razonSocial;

    public Factura(String ruc, String razonSocial, String numero, LocalDate fecha, Venta venta,
            String concepto, double monto, double interes) {
        this.ruc = ruc;
        this.razonSocial = razonSocial;
        this.numero = numero;
        this.fecha = fecha;
        this.venta = venta;
        this.concepto = concepto;
        this.monto = monto;
        this.interes = interes;
    }

    public String getRuc() {
        return ruc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    @Override
    public String getNumero() {
        return numero;
    }

    @Override
    public String getTipo() {
        return "Factura";
    }

    @Override
    public LocalDate getFecha() {
        return fecha;
    }

    @Override
    public Venta getVenta() {
        return venta;
    }

    @Override
    public String getConcepto() {
        return concepto;
    }

    @Override
    public double getMonto() {
        return monto;
    }

    @Override
    public double getInteres() {
        return interes;
    }

    //La factura se emite al RUC y razon social
    @Override
    public String getDocumentoCliente() {
        return ruc;
    }

    @Override
    public String getNombreCliente() {
        return razonSocial;
    }

    @Override
    public double calcularValorConstruccion() {
        return monto * 0.50;
    }

    @Override
    public double calcularIGV() {
        return calcularValorConstruccion() * TASA_IGV / (1 + TASA_IGV);
    }

    @Override
    public double calcularTotal() {
        return monto + interes;
    }
    
}
