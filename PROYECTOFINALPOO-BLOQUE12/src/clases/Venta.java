/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.time.LocalDate;

/**
 *
 * @author Juan Esteban
 */
public class Venta {
    private static final double TASA_IGV = 0.18;
    private static final double PORCENTAJE_CONSTRUCCION = 0.50;
    private static final int DIAS_SEPARACION = 15;
    
    private int numeroContrato;
    private Cliente cliente;
    private UnidadInmobiliaria unidad;
    private String dniAsesor;
    private LocalDate fechaSeparacion;
    private double montoSeparacion;
    private double cuotaInicial;
    private int numeroCuotas;
    private double precioUnidad;
    private String estado;
    
    private Cliente[] copropietarios;
    private int totalCopropietarios;
    private Adicional[] adicionales;
    private int totalAdicionales;
    private Paquete[] paquetes;
    private int totalPaquetes;
    private Cuota[] cronograma;
    private int totalCuotas;
    private String[] historial;
    private int totalHistorial;
    
    private boolean separacionPagada;
    private boolean inicialPagada;
    private boolean desembolsoRegistrado;

    public Venta(Cliente cliente, UnidadInmobiliaria unidad, String dniAsesor, LocalDate fechaSeparacion,
            double montoSeparacion, double cuotaInicial, int numeroCuotas) {
        this.cliente = cliente;
        this.unidad = unidad;
        this.dniAsesor = dniAsesor;
        this.fechaSeparacion = fechaSeparacion;
        this.montoSeparacion = montoSeparacion;
        this.cuotaInicial = cuotaInicial;
        this.numeroCuotas = numeroCuotas;
        //El precio de la unidad se fija con la condicion de la modalidad del cliente
        this.precioUnidad = cliente.aplicarCondicion(unidad.calcularPrecio());
        this.estado = "Nueva";
        
        copropietarios = new Cliente[3];
        totalCopropietarios = 0;
        adicionales = new Adicional[10];
        totalAdicionales = 0;
        paquetes = new Paquete[5];
        totalPaquetes = 0;
        cronograma = new Cuota[24];
        totalCuotas = 0;
        historial = new String[50];
        totalHistorial = 0;
    }
    
    public boolean agregarCopropietario(Cliente copropietario){
        if(totalCopropietarios == copropietarios.length){
            return false;
        }
        //No puede repetirse ni ser el mismo cliente titular
        if(copropietario.getDni().equals(cliente.getDni())){
            return false;
        }
        for (int i = 0; i < totalCopropietarios; i++) {
            if(copropietarios[i].getDni().equals(copropietario.getDni())){
                return false;
            }
        }
        copropietarios[totalCopropietarios] = copropietario;
        totalCopropietarios++;
        return true;
    }
    
    public boolean agregarAdicional(Adicional adicional){
        if(totalAdicionales == adicionales.length){
            return false;
        }
        adicionales[totalAdicionales] = adicional;
        totalAdicionales++;
        return true;
    }
    
    public boolean agregarPaquete(Paquete paquete){
        if(totalPaquetes == paquetes.length){
            return false;
        }
        paquetes[totalPaquetes] = paquete;
        totalPaquetes++;
        return true;
    }
    
    //---------------- Precios ----------------
    
    public double calcularPrecioAdicionales(){
        double suma = 0;
        for (int i = 0; i < totalAdicionales; i++) {
            suma = suma + adicionales[i].getPrecio();
        }
        return suma;
    }
    
    public double calcularPrecioPaquetes(){
        double suma = 0;
        for (int i = 0; i < totalPaquetes; i++) {
            suma = suma + paquetes[i].getPrecioPromocional();
        }
        return suma;
    }
    
    public double calcularPrecioTotal(){
        return precioUnidad + calcularPrecioAdicionales() + calcularPrecioPaquetes();
    }
    
    //El precio incluye IGV, pero solo grava la construccion (50%); el terreno no
    public double calcularIGV(){
        double valorConstruccion = calcularPrecioTotal() * PORCENTAJE_CONSTRUCCION;
        return valorConstruccion * TASA_IGV / (1 + TASA_IGV);
    }
    
    public double calcularMontoBanco(){
        return cliente.calcularMontoBanco(calcularPrecioTotal());
    }
    
    //Lo que se paga en cuotas directas con la inmobiliaria
    public double calcularMontoDirecto(){
        double monto = calcularPrecioTotal() - montoSeparacion - cuotaInicial - calcularMontoBanco();
        if(monto < 0){
            return 0;
        }
        return monto;
    }
    
    public double calcularCuotaMensual(){
        if(numeroCuotas == 0){
            return 0;
        }
        return calcularMontoDirecto() / numeroCuotas;
    }
    
    public void generarCronograma(){
        totalCuotas = 0;
        double montoCuota = calcularCuotaMensual();
        for (int i = 1; i <= numeroCuotas; i++) {
            cronograma[totalCuotas] = new Cuota(i, fechaSeparacion.plusMonths(i), montoCuota);
            totalCuotas++;
        }
    }
    
    //---------------- Pagos ----------------
    
    public double calcularTotalPagado(){
        double pagado = 0;
        if(separacionPagada){
            pagado = pagado + montoSeparacion;
        }
        if(inicialPagada){
            pagado = pagado + cuotaInicial;
        }
        for (int i = 0; i < totalCuotas; i++) {
            if(cronograma[i].isPagada()){
                pagado = pagado + cronograma[i].getMonto();
            }
        }
        if(desembolsoRegistrado){
            pagado = pagado + calcularMontoBanco();
        }
        return pagado;
    }
    
    public double calcularSaldoPendiente(){
        double saldo = calcularPrecioTotal() - calcularTotalPagado();
        if(saldo < 0.01){
            return 0;
        }
        return saldo;
    }
    
    public int contarCuotasVencidas(LocalDate hoy){
        int contador = 0;
        for (int i = 0; i < totalCuotas; i++) {
            if(cronograma[i].estaVencida(hoy)){
                contador++;
            }
        }
        return contador;
    }
    
    public double calcularMontoVencido(LocalDate hoy){
        double suma = 0;
        for (int i = 0; i < totalCuotas; i++) {
            if(cronograma[i].estaVencida(hoy)){
                suma = suma + cronograma[i].getMonto();
            }
        }
        return suma;
    }
    
    public double calcularInteresesPendientes(LocalDate hoy){
        double suma = 0;
        for (int i = 0; i < totalCuotas; i++) {
            suma = suma + cronograma[i].calcularInteres(hoy);
        }
        return suma;
    }
    
    public double calcularInteresesPagados(){
        double suma = 0;
        for (int i = 0; i < totalCuotas; i++) {
            suma = suma + cronograma[i].getInteresPagado();
        }
        return suma;
    }
    
    public long calcularDiasMaximoAtraso(LocalDate hoy){
        long maximo = 0;
        for (int i = 0; i < totalCuotas; i++) {
            if(cronograma[i].calcularDiasAtraso(hoy) > maximo){
                maximo = cronograma[i].calcularDiasAtraso(hoy);
            }
        }
        return maximo;
    }
    
    public Cuota buscarCuota(int numero){
        for (int i = 0; i < totalCuotas; i++) {
            if(cronograma[i].getNumero() == numero){
                return cronograma[i];
            }
        }
        return null;
    }
    
    //---------------- Estados ----------------
    
    public boolean separacionVencida(LocalDate hoy){
        return estado.equals("Separada") && !inicialPagada
                && hoy.isAfter(fechaSeparacion.plusDays(DIAS_SEPARACION));
    }
    
    //Una venta Entregada o Desistida ya no se puede modificar
    public boolean esModificable(){
        return !estado.equals("Entregada") && !estado.equals("Desistida");
    }
    
    public boolean cambiarEstado(String nuevoEstado, LocalDate fecha, String detalle){
        if(!esModificable()){
            return false;
        }
        estado = nuevoEstado;
        agregarHistorial(fecha + " - " + nuevoEstado + ": " + detalle);
        return true;
    }
    
    public void agregarHistorial(String texto){
        if(totalHistorial < historial.length){
            historial[totalHistorial] = texto;
            totalHistorial++;
        }
    }
    
    //---------------- Getters y setters ----------------

    public int getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(int numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public UnidadInmobiliaria getUnidad() {
        return unidad;
    }

    public String getDniAsesor() {
        return dniAsesor;
    }

    public LocalDate getFechaSeparacion() {
        return fechaSeparacion;
    }

    public double getMontoSeparacion() {
        return montoSeparacion;
    }

    public double getCuotaInicial() {
        return cuotaInicial;
    }

    public int getNumeroCuotas() {
        return numeroCuotas;
    }

    public double getPrecioUnidad() {
        return precioUnidad;
    }

    public String getEstado() {
        return estado;
    }

    public boolean isSeparacionPagada() {
        return separacionPagada;
    }

    public void setSeparacionPagada(boolean separacionPagada) {
        this.separacionPagada = separacionPagada;
    }

    public boolean isInicialPagada() {
        return inicialPagada;
    }

    public void setInicialPagada(boolean inicialPagada) {
        this.inicialPagada = inicialPagada;
    }

    public boolean isDesembolsoRegistrado() {
        return desembolsoRegistrado;
    }

    public void setDesembolsoRegistrado(boolean desembolsoRegistrado) {
        this.desembolsoRegistrado = desembolsoRegistrado;
    }

    public Cliente[] obtenerCopropietarios() {
        return copropietarios;
    }

    public int obtenerTotalCopropietarios() {
        return totalCopropietarios;
    }

    public Adicional[] obtenerAdicionales() {
        return adicionales;
    }

    public int obtenerTotalAdicionales() {
        return totalAdicionales;
    }

    public Paquete[] obtenerPaquetes() {
        return paquetes;
    }

    public int obtenerTotalPaquetes() {
        return totalPaquetes;
    }

    public Cuota[] obtenerCronograma() {
        return cronograma;
    }

    public int obtenerTotalCuotas() {
        return totalCuotas;
    }

    public String[] obtenerHistorial() {
        return historial;
    }

    public int obtenerTotalHistorial() {
        return totalHistorial;
    }
    
}
