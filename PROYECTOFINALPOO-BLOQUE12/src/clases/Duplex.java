/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author Cueto
 */
public class Duplex extends UnidadInmobiliaria {
    //La terraza (area libre) se valoriza al 50% del precio por m2
    private static final double FACTOR_TERRAZA = 0.50;

    public Duplex(String codigo, String codigoProyecto, String torre, int piso, double areaTechada,
            double areaLibre, int dormitorios, String vista, double precioM2) {
        super(codigo, codigoProyecto, torre, piso, areaTechada, areaLibre, dormitorios, vista, precioM2);
    }

    @Override
    public double calcularPrecio() {
        return calcularPrecioBase() + areaLibre * precioM2 * FACTOR_TERRAZA;
    }

    @Override
    public String getTipo() {
        return "Duplex";
    }
    
}