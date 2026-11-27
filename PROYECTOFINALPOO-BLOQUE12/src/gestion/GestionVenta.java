/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion;

import clases.Adicional;
import clases.Cliente;
import clases.Boleta;
import clases.Comprobante;
import clases.Cuota;
import clases.Factura;
import clases.Paquete;
import clases.UnidadInmobiliaria;
import clases.Venta;
import java.time.LocalDate;

/**
 *
 * @author Juan Esteban
 */
public class GestionVenta {
    private static final double PORCENTAJE_MAXIMO_INGRESO = 0.30;
    private Venta[] ventas;
    private int totalVentas;
    private int siguienteContrato;
    private Comprobante[] comprobantes;
    private int totalComprobantes;
    private int siguienteBoleta;
    private int siguienteFactura;

    public GestionVenta() {
        ventas = new Venta[100];
        totalVentas = 0;
        //Numero de contrato correlativo automatico
        siguienteContrato = 1001;
        comprobantes = new Comprobante[500];
        totalComprobantes = 0;
        siguienteBoleta = 1;
        siguienteFactura = 1;
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
        emitirComprobante(venta, "Separacion", venta.getMontoSeparacion(), 0, venta.getFechaSeparacion(),
                "Boleta", "", "");
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
    
    private void devolverStock(Venta venta){
        for (int i = 0; i < venta.obtenerTotalAdicionales(); i++) {
            venta.obtenerAdicionales()[i].devolverStock();
        }
        for (int i = 0; i < venta.obtenerTotalPaquetes(); i++) {
            venta.obtenerPaquetes()[i].devolverStock();
        }
    }
    
    //---------------- Flujo de estados ----------------
    
    //Separaciones vencidas pasan a Desistida y 3 cuotas vencidas pasan a En Morosidad
    public int actualizarEstados(LocalDate hoy){
        int cambios = 0;
        for (int i = 0; i < totalVentas; i++) {
            Venta venta = ventas[i];
            if(venta.separacionVencida(hoy)){
                venta.cambiarEstado("Desistida", hoy, "Separacion vencida sin firma de contrato");
                venta.getUnidad().setEstado("Disponible");
                devolverStock(venta);
                cambios++;
            }else if(venta.getEstado().equals("Contrato Firmado") && venta.contarCuotasVencidas(hoy) >= 3){
                venta.cambiarEstado("En Morosidad", hoy, venta.contarCuotasVencidas(hoy) + " cuotas vencidas");
                cambios++;
            }
        }
        return cambios;
    }
    
    private String validarDatosComprobante(String tipo, String ruc, String razonSocial){
        if(tipo.equals("Factura")){
            if(!ruc.matches("[0-9]{11}")){
                return "Para emitir Factura ingrese un RUC de 11 digitos";
            }
            if(razonSocial.trim().isEmpty()){
                return "Para emitir Factura ingrese la razon social";
            }
        }
        return null;
    }
    
    public String firmarContrato(int numeroContrato, LocalDate fecha, String tipo, String ruc, String razonSocial){
        Venta venta = buscarPorContrato(numeroContrato);
        if(venta == null){
            return "No existe el contrato " + numeroContrato;
        }
        if(!venta.esModificable()){
            return "La venta esta " + venta.getEstado() + " y ya no se puede modificar";
        }
        if(venta.separacionVencida(fecha)){
            actualizarEstados(fecha);
            return "La separacion vencio (15 dias) y la venta paso a Desistida";
        }
        if(!venta.getEstado().equals("Separada")){
            return "Solo se firma el contrato de una venta Separada (estado actual: " + venta.getEstado() + ")";
        }
        String error = validarDatosComprobante(tipo, ruc, razonSocial);
        if(error != null){
            return error;
        }
        venta.setInicialPagada(true);
        venta.cambiarEstado("Contrato Firmado", fecha, "Pago de cuota inicial S/ "
                + String.format("%,.2f", venta.getCuotaInicial()));
        venta.getUnidad().setEstado("Vendida");
        emitirComprobante(venta, "Cuota inicial", venta.getCuotaInicial(), 0, fecha, tipo, ruc, razonSocial);
        return null;
    }
    
    public String registrarPagoCuota(int numeroContrato, int numeroCuota, LocalDate fecha,
            String tipo, String ruc, String razonSocial){
        Venta venta = buscarPorContrato(numeroContrato);
        if(venta == null){
            return "No existe el contrato " + numeroContrato;
        }
        if(!venta.getEstado().equals("Contrato Firmado") && !venta.getEstado().equals("En Morosidad")){
            return "Solo se registran cuotas de ventas con contrato firmado (estado actual: " + venta.getEstado() + ")";
        }
        Cuota cuota = venta.buscarCuota(numeroCuota);
        if(cuota == null){
            return "La cuota " + numeroCuota + " no existe";
        }
        if(cuota.isPagada()){
            return "La cuota " + numeroCuota + " ya fue pagada";
        }
        String error = validarDatosComprobante(tipo, ruc, razonSocial);
        if(error != null){
            return error;
        }
        double interes = cuota.calcularInteres(fecha);
        cuota.pagar(fecha);
        emitirComprobante(venta, "Cuota " + numeroCuota, cuota.getMonto(), interes, fecha, tipo, ruc, razonSocial);
        venta.agregarHistorial(fecha + " - Pago de cuota " + numeroCuota + ": S/ "
                + String.format("%,.2f", cuota.getMonto()) + " + interes S/ " + String.format("%,.2f", interes));
        
        //Si regulariza y le quedan menos de 3 cuotas vencidas, sale de morosidad
        if(venta.getEstado().equals("En Morosidad") && venta.contarCuotasVencidas(fecha) < 3){
            venta.cambiarEstado("Contrato Firmado", fecha, "Regularizo sus cuotas vencidas");
        }
        return null;
    }
    
    public String registrarDesembolso(int numeroContrato, LocalDate fecha){
        Venta venta = buscarPorContrato(numeroContrato);
        if(venta == null){
            return "No existe el contrato " + numeroContrato;
        }
        if(venta.calcularMontoBanco() == 0){
            return "La modalidad " + venta.getCliente().getModalidad() + " no tiene desembolso bancario";
        }
        if(!venta.getEstado().equals("Contrato Firmado") && !venta.getEstado().equals("En Morosidad")){
            return "El desembolso se registra despues de firmar el contrato (estado actual: " + venta.getEstado() + ")";
        }
        if(venta.isDesembolsoRegistrado()){
            return "El desembolso del banco ya fue registrado";
        }
        venta.setDesembolsoRegistrado(true);
        venta.agregarHistorial(fecha + " - Desembolso del banco: S/ " + String.format("%,.2f", venta.calcularMontoBanco()));
        emitirComprobante(venta, "Desembolso bancario", venta.calcularMontoBanco(), 0, fecha, "Boleta", "", "");
        return null;
    }
    
    //La unidad se entrega solo con la torre al 100% y sin saldo pendiente
    public String entregarUnidad(int numeroContrato, double avanceTorre, LocalDate fecha){
        Venta venta = buscarPorContrato(numeroContrato);
        if(venta == null){
            return "No existe el contrato " + numeroContrato;
        }
        if(venta.getEstado().equals("Entregada")){
            return "La unidad ya fue entregada";
        }
        if(!venta.getEstado().equals("Contrato Firmado")){
            return "Solo se entrega una venta con Contrato Firmado (estado actual: " + venta.getEstado() + ")";
        }
        if(avanceTorre < 100){
            return "La Torre " + venta.getUnidad().getTorre() + " tiene " + String.format("%.2f", avanceTorre)
                    + "% de avance; la entrega requiere 100%";
        }
        if(venta.calcularSaldoPendiente() > 0){
            return "La venta tiene un saldo pendiente de S/ " + String.format("%,.2f", venta.calcularSaldoPendiente());
        }
        venta.cambiarEstado("Entregada", fecha, "Entrega de la unidad " + venta.getUnidad().getCodigo());
        venta.getUnidad().setEstado("Entregada");
        return null;
    }
    
    //---------------- Comprobantes ----------------
    
    private Comprobante emitirComprobante(Venta venta, String concepto, double monto, double interes,
            LocalDate fecha, String tipo, String ruc, String razonSocial){
        Comprobante comprobante;
        if(tipo.equals("Factura")){
            comprobante = new Factura(ruc, razonSocial, String.format("F001-%08d", siguienteFactura),
                    fecha, venta, concepto, monto, interes);
            siguienteFactura++;
        }else{
            comprobante = new Boleta(String.format("B001-%08d", siguienteBoleta), fecha, venta, concepto, monto, interes);
            siguienteBoleta++;
        }
        if(totalComprobantes < comprobantes.length){
            comprobantes[totalComprobantes] = comprobante;
            totalComprobantes++;
        }
        return comprobante;
    }
    
    public Comprobante obtenerUltimoComprobante(){
        if(totalComprobantes == 0){
            return null;
        }
        return comprobantes[totalComprobantes - 1];
    }

    public Comprobante[] obtenerComprobantes() {
        return comprobantes;
    }

    public int obtenerTotalComprobantes() {
        return totalComprobantes;
    }

    public Venta[] obtenerVentas() {
        return ventas;
    }

    public int obtenerTotalVentas() {
        return totalVentas;
    }
    
}
