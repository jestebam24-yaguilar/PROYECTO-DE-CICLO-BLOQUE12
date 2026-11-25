/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author Juan Esteban
 */
public class ClienteContado extends Cliente {
    private static final double DESCUENTO = 0.05;
    private static final double PORCENTAJE_INICIAL = 0.10;

    public ClienteContado(String dni, String nombres, String apellidos, String estadoCivil, String dniConyuge,
            String nombreConyuge, String telefono, String correo, double ingresoMensual) {
        super(dni, nombres, apellidos, estadoCivil, dniConyuge, nombreConyuge, telefono, correo, ingresoMensual);
    }

    @Override
    public String getModalidad() {
        return "Contado";
    }

    //El cliente al contado obtiene un descuento sobre el precio
    @Override
    public double aplicarCondicion(double precio) {
        return precio - precio * DESCUENTO;
    }

    @Override
    public double calcularInicialMinima(double precioTotal) {
        return precioTotal * PORCENTAJE_INICIAL;
    }

    //Al contado no interviene ningun banco
    @Override
    public double calcularMontoBanco(double precioTotal) {
        return 0;
    }
    
}