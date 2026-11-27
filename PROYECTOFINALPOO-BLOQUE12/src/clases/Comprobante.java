/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package clases;

import java.time.LocalDate;

/**
 *
 * @author Juan Esteban
 */
public interface Comprobante {
    String getNumero();
    String getTipo();
    LocalDate getFecha();
    Venta getVenta();
    String getConcepto();
    double getMonto();
    double getInteres();
    String getDocumentoCliente();
    String getNombreCliente();
    double calcularValorConstruccion();
    double calcularIGV();
    double calcularTotal();
    
}