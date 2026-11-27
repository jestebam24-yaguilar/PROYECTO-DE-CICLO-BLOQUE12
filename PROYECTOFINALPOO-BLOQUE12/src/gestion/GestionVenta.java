/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion;

import clases.Adicional;
import clases.Cliente;
import clases.Paquete;
import clases.UnidadInmobiliaria;
import clases.Venta;

/**
 *
 * @author Juan Esteban
 */
public class GestionVenta {
    private static final double PORCENTAJE_MAXIMO_INGRESO = 0.30;
    private Venta[] ventas;
    private int totalVentas;
    private int siguienteContrato;

    public GestionVenta() {
        ventas = new Venta[100];
        totalVentas = 0;
        //Numero de contrato correlativo automatico
        siguienteContrato = 1001;
    }
    
    public Venta buscarPorContrato(int numeroContrato){
        for (int i = 0; i < totalVentas; i++) {
            if(ventas[i].getNumeroContrato() == numeroContrato){
                return ventas[i];
            }
        }
        return null;
    }
    
    //Devuelve null si la venta se registro, o el mensaje con la causa del rechazo
    public String registrarSeparacion(Venta venta){
        if(totalVentas == ventas.length){
            return "No hay espacio para registrar mas ventas";
        }
        UnidadInmobiliaria unidad = venta.getUnidad();
        Cliente cliente = venta.getCliente();
        
        //(a) La unidad debe estar Disponible
        if(unidad.getEstado().equals("Separada")){
            return "El departamento " + unidad.getCodigo() + " de la Torre " + unidad.getTorre()
                    + " ya fue separado por otro cliente";
        }
        if(!unidad.getEstado().equals("Disponible")){
            return "El departamento " + unidad.getCodigo() + " de la Torre " + unidad.getTorre()
                    + " no esta disponible (estado: " + unidad.getEstado() + ")";
        }
        if(!cliente.puedeComprar(unidad.calcularPrecio())){
            return "El departamento " + unidad.getCodigo() + " cuesta S/ " + String.format("%,.2f", unidad.calcularPrecio())
                    + " y supera el valor maximo permitido para " + cliente.getModalidad();
        }
        if(venta.getMontoSeparacion() <= 0){
            return "Ingrese el monto pagado por la separacion";
        }
        if(venta.getNumeroCuotas() < 1 || venta.getNumeroCuotas() > 24){
            return "El numero de cuotas directas debe estar entre 1 y 24";
        }
        
        //(b) Cuota inicial minima segun la modalidad (polimorfismo)
        double inicialMinima = cliente.calcularInicialMinima(venta.calcularPrecioTotal());
        if(venta.getCuotaInicial() < inicialMinima){
            return "La cuota inicial minima para " + cliente.getModalidad() + " es S/ "
                    + String.format("%,.2f", inicialMinima);
        }
        
        //(c) La cuota mensual no puede superar el 30% del ingreso familiar
        double limite = cliente.getIngresoMensual() * PORCENTAJE_MAXIMO_INGRESO;
        if(venta.calcularCuotaMensual() > limite){
            return "La cuota mensual de S/ " + String.format("%,.2f", venta.calcularCuotaMensual())
                    + " supera el 30% del ingreso familiar (maximo S/ " + String.format("%,.2f", limite)
                    + "). Aumente la cuota inicial o el numero de cuotas";
        }
        
        //(d) Deben existir los adicionales en el proyecto
        String mensajeStock = validarStock(venta);
        if(mensajeStock != null){
            return mensajeStock;
        }
        
        //Registrar la venta
        descontarStock(venta);
        venta.setNumeroContrato(siguienteContrato);
        siguienteContrato++;
        venta.setSeparacionPagada(true);
        venta.cambiarEstado("Separada", venta.getFechaSeparacion(), "Pago de separacion S/ "
                + String.format("%,.2f", venta.getMontoSeparacion()) + ", valida por 15 dias");
        venta.generarCronograma();
        unidad.setEstado("Separada");
        ventas[totalVentas] = venta;
        totalVentas++;
        return null;
    }
    
    //Cuantas unidades de un adicional pide la venta (sueltos y dentro de paquetes)
    private int contarDemanda(Venta venta, Adicional adicional){
        int contador = 0;
        for (int i = 0; i < venta.obtenerTotalAdicionales(); i++) {
            if(venta.obtenerAdicionales()[i] == adicional){
                contador++;
            }
        }
        for (int i = 0; i < venta.obtenerTotalPaquetes(); i++) {
            contador = contador + venta.obtenerPaquetes()[i].contarAdicional(adicional);
        }
        return contador;
    }
    
    private String validarStock(Venta venta){
        String codigoProyecto = venta.getUnidad().getCodigoProyecto();
        for (int i = 0; i < venta.obtenerTotalAdicionales(); i++) {
            Adicional adicional = venta.obtenerAdicionales()[i];
            if(!adicional.getCodigoProyecto().equalsIgnoreCase(codigoProyecto)){
                return "El adicional " + adicional.getNombre() + " no pertenece al proyecto " + codigoProyecto;
            }
            if(adicional.getCantidadDisponible() < contarDemanda(venta, adicional)){
                return "No hay " + adicional.getNombre() + " disponibles en el proyecto " + codigoProyecto;
            }
        }
        for (int i = 0; i < venta.obtenerTotalPaquetes(); i++) {
            Paquete paquete = venta.obtenerPaquetes()[i];
            if(!paquete.getCodigoProyecto().equalsIgnoreCase(codigoProyecto)){
                return "El paquete " + paquete.getNombre() + " no pertenece al proyecto " + codigoProyecto;
            }
            for (int j = 0; j < paquete.obtenerTotalAdicionales(); j++) {
                Adicional adicional = paquete.obtenerAdicionales()[j];
                if(adicional.getCantidadDisponible() < contarDemanda(venta, adicional)){
                    return "No hay " + adicional.getNombre() + " suficientes para el paquete " + paquete.getNombre();
                }
            }
        }
        return null;
    }
    
    private void descontarStock(Venta venta){
        for (int i = 0; i < venta.obtenerTotalAdicionales(); i++) {
            venta.obtenerAdicionales()[i].descontarStock();
        }
        for (int i = 0; i < venta.obtenerTotalPaquetes(); i++) {
            venta.obtenerPaquetes()[i].descontarStock();
        }
    }

    public Venta[] obtenerVentas() {
        return ventas;
    }

    public int obtenerTotalVentas() {
        return totalVentas;
    }
    
}
