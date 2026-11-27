/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author Cueto
 */
public class Penthouse extends UnidadInmobiliaria {
    //Recargo premium por vista y valor de la azotea privada
    private static final double PREMIUM_VISTA_EXTERIOR = 0.15;
    private static final double PREMIUM_VISTA_INTERIOR = 0.05;
    private static final double FACTOR_AZOTEA = 0.60;

    public Penthouse(String codigo, String codigoProyecto, String torre, int piso, double areaTechada,
            double areaLibre, int dormitorios, String vista, double precioM2) {
        super(codigo, codigoProyecto, torre, piso, areaTechada, areaLibre, dormitorios, vista, precioM2);
    }

    @Override
    public double calcularPrecio() {
        double premium;
        if(vista.equalsIgnoreCase("Exterior")){
            premium = PREMIUM_VISTA_EXTERIOR;
        }else{
            premium = PREMIUM_VISTA_INTERIOR;
        }
        double precioDepartamento = calcularPrecioBase() * (1 + premium);
        double precioAzotea = areaLibre * precioM2 * FACTOR_AZOTEA;
        return precioDepartamento + precioAzotea;
    }

    @Override
    public String getTipo() {
        return "Penthouse";
    }
    
}
