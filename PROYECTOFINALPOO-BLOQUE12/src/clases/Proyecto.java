/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.time.LocalDate;

/**
 *
 * @author Antonio
 */
public class Proyecto {
    private String codigo;
    private String nombre;
    private String direccion;
    private String distrito;
    private int cantidadTorres;
    private int cantidadPisos;
    private LocalDate fechaEntrega;

    public Proyecto(String codigo, String nombre, String direccion, String distrito, int cantidadTorres, int cantidadPisos, LocalDate fechaEntrega) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.distrito = distrito;
        this.cantidadTorres = cantidadTorres;
        this.cantidadPisos = cantidadPisos;
        this.fechaEntrega = fechaEntrega;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public int getCantidadTorres() {
        return cantidadTorres;
    }

    public void setCantidadTorres(int cantidadTorres) {
        this.cantidadTorres = cantidadTorres;
    }

    public int getCantidadPisos() {
        return cantidadPisos;
    }

    public void setCantidadPisos(int cantidadPisos) {
        this.cantidadPisos = cantidadPisos;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
    
   //Las torres se llaman A,B,C según su posición
    
    public String nombreTorre(int indice){
        return String.valueOf((char)('A' + indice));
    }
    
    public boolean existeTore(String torre){
        for (int i = 0; i < cantidadTorres; i++) {
            if (nombreTorre(i).equalsIgnoreCase(torre)) {
                return true;
            }
        }
        return false;
    }
}
