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
public class Boleta implements Comprobante {
    private static final double TASA_IGV = 0.18;
    private String numero;
    private LocalDate fecha;
    private Venta venta;
    private String concepto;
    private double monto;
    private double interes;

    public Boleta(String numero, LocalDate fecha, Venta venta, String concepto, double monto, double interes) {
        this.numero = numero;
        this.fecha = fecha;
        this.venta = venta;
        this.concepto = concepto;
        this.monto = monto;
        this.interes = interes;
    }

    @Override
    public String getNumero() {
        return numero;
    }

    @Override
    public String getTipo() {
        return "Boleta";
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

    //La boleta se emite al DNI del cliente
    @Override
    public String getDocumentoCliente() {
        return venta.getCliente().getDni();
    }

    @Override
    public String getNombreCliente() {
        return venta.getCliente().getNombreCompleto();
    }

    //El 50% del pago corresponde a la construccion; el terreno no esta gravado
    @Override
    public double calcularValorConstruccion() {
        return monto * 0.50;
    }

    //El monto incluye IGV: se separa el 18% del valor de la construccion
    @Override
    public double calcularIGV() {
        return calcularValorConstruccion() * TASA_IGV / (1 + TASA_IGV);
    }

    @Override
    public double calcularTotal() {
        return monto + interes;
    }
    
}
