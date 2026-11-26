/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author Juan Esteban
 */
public class ClienteHipotecario extends Cliente {
    private static final double PORCENTAJE_INICIAL = 0.10;
    private static final double PORCENTAJE_BANCO = 0.80;
    private String banco;

    public ClienteHipotecario(String banco, String dni, String nombres, String apellidos, String estadoCivil,
            String dniConyuge, String nombreConyuge, String telefono, String correo, double ingresoMensual) {
        super(dni, nombres, apellidos, estadoCivil, dniConyuge, nombreConyuge, telefono, correo, ingresoMensual);
        this.banco = banco;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    @Override
    public String getModalidad() {
        return "Credito Hipotecario";
    }

    //El credito hipotecario no tiene descuento
    @Override
    public double aplicarCondicion(double precio) {
        return precio;
    }

    @Override
    public double calcularInicialMinima(double precioTotal) {
        return precioTotal * PORCENTAJE_INICIAL;
    }

    //El banco desembolsa el 80% del precio
    @Override
    public double calcularMontoBanco(double precioTotal) {
        return precioTotal * PORCENTAJE_BANCO;
    }
    
}
