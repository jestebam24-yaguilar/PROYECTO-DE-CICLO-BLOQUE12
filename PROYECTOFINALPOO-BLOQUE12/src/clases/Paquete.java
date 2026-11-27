/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author Cueto
 */
public class Paquete {
    private String codigo;
    private String nombre;
    private String codigoProyecto;
    private double precioPromocional;
    private Adicional[] adicionales;
    private int totalAdicionales;

    public Paquete(String codigo, String nombre, String codigoProyecto, double precioPromocional) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.codigoProyecto = codigoProyecto;
        this.precioPromocional = precioPromocional;
        adicionales = new Adicional[10];
        totalAdicionales = 0;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoProyecto() {
        return codigoProyecto;
    }

    //El precio del paquete es cerrado: no se recalcula con los adicionales
    public double getPrecioPromocional() {
        return precioPromocional;
    }

    public void setPrecioPromocional(double precioPromocional) {
        this.precioPromocional = precioPromocional;
    }
    
    public boolean agregarAdicional(Adicional adicional){
        if(totalAdicionales == adicionales.length){
            return false;
        }
        adicionales[totalAdicionales] = adicional;
        totalAdicionales++;
        return true;
    }
    
    //Cuantas veces aparece un adicional en el paquete (ej. dos estacionamientos)
    public int contarAdicional(Adicional adicional){
        int contador = 0;
        for (int i = 0; i < totalAdicionales; i++) {
            if(adicionales[i] == adicional){
                contador++;
            }
        }
        return contador;
    }
    
    //Un paquete solo se puede vender si hay stock de todos sus adicionales
    public boolean hayStock(){
        for (int i = 0; i < totalAdicionales; i++) {
            if(adicionales[i].getCantidadDisponible() < contarAdicional(adicionales[i])){
                return false;
            }
        }
        return true;
    }
    
    public void descontarStock(){
        for (int i = 0; i < totalAdicionales; i++) {
            adicionales[i].descontarStock();
        }
    }
    
    public void devolverStock(){
        for (int i = 0; i < totalAdicionales; i++) {
            adicionales[i].devolverStock();
        }
    }
    
    public String obtenerNombresAdicionales(){
        String texto = "";
        for (int i = 0; i < totalAdicionales; i++) {
            if(i > 0){
                texto = texto + ", ";
            }
            texto = texto + adicionales[i].getNombre();
        }
        return texto;
    }
    
    public double calcularPrecioNormal(){
        double suma = 0;
        for (int i = 0; i < totalAdicionales; i++) {
            suma = suma + adicionales[i].getPrecio();
        }
        return suma;
    }

    public Adicional[] obtenerAdicionales() {
        return adicionales;
    }

    public int obtenerTotalAdicionales() {
        return totalAdicionales;
    }
    
}
