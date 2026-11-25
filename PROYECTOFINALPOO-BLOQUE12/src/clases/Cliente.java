/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author Juan Esteban
 */
public abstract class Cliente {
    protected String dni;
    protected String nombres;
    protected String apellidos;
    protected String estadoCivil;
    protected String dniConyuge;
    protected String nombreConyuge;
    protected String telefono;
    protected String correo;
    protected double ingresoMensual;

    public Cliente(String dni, String nombres, String apellidos, String estadoCivil, String dniConyuge,
            String nombreConyuge, String telefono, String correo, double ingresoMensual) {
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.estadoCivil = estadoCivil;
        this.dniConyuge = dniConyuge;
        this.nombreConyuge = nombreConyuge;
        this.telefono = telefono;
        this.correo = correo;
        this.ingresoMensual = ingresoMensual;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getDniConyuge() {
        return dniConyuge;
    }

    public void setDniConyuge(String dniConyuge) {
        this.dniConyuge = dniConyuge;
    }

    public String getNombreConyuge() {
        return nombreConyuge;
    }

    public void setNombreConyuge(String nombreConyuge) {
        this.nombreConyuge = nombreConyuge;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public double getIngresoMensual() {
        return ingresoMensual;
    }

    public void setIngresoMensual(double ingresoMensual) {
        this.ingresoMensual = ingresoMensual;
    }
    
    public String getNombreCompleto(){
        return nombres + " " + apellidos;
    }
    
    //Metodo concreto: por defecto cualquier modalidad puede comprar la unidad
    public boolean puedeComprar(double precio){
        return true;
    }
    
    //Metodos abstractos: cada modalidad tiene condiciones distintas
    public abstract String getModalidad();
    public abstract double aplicarCondicion(double precio);
    public abstract double calcularInicialMinima(double precioTotal);
    public abstract double calcularMontoBanco(double precioTotal);
    
}

//RF03