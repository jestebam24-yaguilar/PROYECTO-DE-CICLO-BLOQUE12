/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author Juan Esteban
 */
public class ClienteMivivienda extends Cliente {
    //Valores referenciales del Fondo Mivivienda (verificar los vigentes)
    private static final double VALOR_MAXIMO = 488800;
    private static final double BONO_BUEN_PAGADOR = 27400;
    private static final double PORCENTAJE_INICIAL = 0.075;
    private static final double PORCENTAJE_BANCO = 0.85;
    private String banco;

    public ClienteMivivienda(String banco, String dni, String nombres, String apellidos, String estadoCivil,
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
        return "Credito Mivivienda";
    }
    
    //Solo se puede usar Mivivienda si la unidad no supera el valor maximo
    @Override
    public boolean puedeComprar(double precio) {
        return precio <= VALOR_MAXIMO;
    }

    //Se descuenta el Bono del Buen Pagador
    @Override
    public double aplicarCondicion(double precio) {
        if(puedeComprar(precio)){
            return precio - BONO_BUEN_PAGADOR;
        }
        return precio;
    }

    @Override
    public double calcularInicialMinima(double precioTotal) {
        return precioTotal * PORCENTAJE_INICIAL;
    }

    @Override
    public double calcularMontoBanco(double precioTotal) {
        return precioTotal * PORCENTAJE_BANCO;
    }
    
    public double getValorMaximo(){
        return VALOR_MAXIMO;
    }
    
}
