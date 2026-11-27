/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author Juan Esteban
 */
public class Cuota {
    //Interes moratorio de 0.05% por cada dia de atraso
    private static final double TASA_MORA_DIARIA = 0.0005;
    private int numero;
    private LocalDate fechaVencimiento;
    private double monto;
    private boolean pagada;
    private LocalDate fechaPago;
    private double interesPagado;

    public Cuota(int numero, LocalDate fechaVencimiento, double monto) {
        this.numero = numero;
        this.fechaVencimiento = fechaVencimiento;
        this.monto = monto;
        this.pagada = false;
        this.interesPagado = 0;
    }

    public int getNumero() {
        return numero;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public double getMonto() {
        return monto;
    }

    public boolean isPagada() {
        return pagada;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public double getInteresPagado() {
        return interesPagado;
    }
    
    public boolean estaVencida(LocalDate hoy){
        return !pagada && hoy.isAfter(fechaVencimiento);
    }
    
    public long calcularDiasAtraso(LocalDate hoy){
        if(estaVencida(hoy)){
            return ChronoUnit.DAYS.between(fechaVencimiento, hoy);
        }
        return 0;
    }
    
    public double calcularInteres(LocalDate hoy){
        return monto * TASA_MORA_DIARIA * calcularDiasAtraso(hoy);
    }
    
    public void pagar(LocalDate fecha){
        interesPagado = calcularInteres(fecha);
        pagada = true;
        fechaPago = fecha;
    }
    
    public String getEstado(LocalDate hoy){
        if(pagada){
            return "Pagada";
        }
        if(estaVencida(hoy)){
            return "Vencida";
        }
        return "Pendiente";
    }
    
}