/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author Antonio
 */
public class Duplex extends UnidadInmobiliaria {
    private static final double FACTOR_TERRAZA = 0.50;

    public Duplex(String codigo, String codigoProyecto, String torre, int piso, double areaTechada, double arealibre, int dormitorios, String vistas, double precioM2, String estado) {
        super(codigo, codigoProyecto, torre, piso, areaTechada, arealibre, dormitorios, vistas, precioM2, estado);
    }

    @Override
    public double calcularPrecio() {
        return calcularPrecioBase() +  arealibre * precioM2 * FACTOR_TERRAZA;
    }

    @Override
    public String getTipo() {
        return "Duplex";
    }
    
    
}
