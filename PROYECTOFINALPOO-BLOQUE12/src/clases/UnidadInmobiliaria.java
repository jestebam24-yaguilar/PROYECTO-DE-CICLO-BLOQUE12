/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author Antonio
 */
public abstract class UnidadInmobiliaria {
    protected String codigo;
    protected String codigoProyecto;
    protected String torre;
    protected int piso;
    protected double areaTechada;
    protected double arealibre;
    protected int dormitorios;
    protected String vistas;
    protected double precioM2;
    protected String estado;

    public UnidadInmobiliaria(String codigo, String codigoProyecto, String torre, int piso, double areaTechada, double arealibre, int dormitorios, String vistas, double precioM2, String estado) {
        this.codigo = codigo;
        this.codigoProyecto = codigoProyecto;
        this.torre = torre;
        this.piso = piso;
        this.areaTechada = areaTechada;
        this.arealibre = arealibre;
        this.dormitorios = dormitorios;
        this.vistas = vistas;
        this.precioM2 = precioM2;
        this.estado = "Disponible";
        
        
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoProyecto() {
        return codigoProyecto;
    }

    public void setCodigoProyecto(String codigoProyecto) {
        this.codigoProyecto = codigoProyecto;
    }

    public String getTorre() {
        return torre;
    }

    public void setTorre(String torre) {
        this.torre = torre;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }

    public double getAreaTechada() {
        return areaTechada;
    }

    public void setAreaTechada(double areaTechada) {
        this.areaTechada = areaTechada;
    }

    public double getArealibre() {
        return arealibre;
    }

    public void setArealibre(double arealibre) {
        this.arealibre = arealibre;
    }

    public int getDormitorios() {
        return dormitorios;
    }

    public void setDormitorios(int dormitorios) {
        this.dormitorios = dormitorios;
    }

    public String getVistas() {
        return vistas;
    }

    public void setVistas(String vistas) {
        this.vistas = vistas;
    }

    public double getPrecioM2() {
        return precioM2;
    }

    public void setPrecioM2(double precioM2) {
        this.precioM2 = precioM2;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    //Metodo concreto: Precio base solo por el area techada
    public double calcularPrecioBase(){
        return areaTechada * precioM2;
    }
    
    //Metodos Abstractos: Cada tipo calcula su precio de forma distinta
    
    public abstract double calcularPrecio();
    public abstract String getTipo();
}
