/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author Antonio
 */
public class Flat extends UnidadInmobiliaria {
    //Reacargo de 1% por cada piso a partir del quinto
    
    private static final double RECARGO_POR_PISO = 0.01;

    public Flat(String codigo, String codigoProyecto, String torre, int piso, double areaTechada, double arealibre, int dormitorios, String vistas, double precioM2, String estado) {
        super(codigo, codigoProyecto, torre, piso, areaTechada, arealibre, dormitorios, vistas, precioM2, estado);
    }

    @Override
    public double calcularPrecio() {
        double precio = calcularPrecioBase();
        if (piso >= 5) {
            int pisosConRecargo = piso - 4;
            precio = precio + precio * RECARGO_POR_PISO * pisosConRecargo;
        }
        return precio;
    }

    @Override
    public String getTipo() {
        return "Flat";
    }
    
    
}
