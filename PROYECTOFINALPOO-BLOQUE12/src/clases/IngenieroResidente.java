/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author Lee
 */
public class IngenieroResidente extends Empleado {
    private String cip;
    private String codigoProyecto;
//resident evil xd
    public IngenieroResidente(String cip, String codigoProyecto, String dni, String nombres,
            String apellidos, String usuario, String contrasena) {
        super(dni, nombres, apellidos, usuario, contrasena);
        this.cip = cip;
        this.codigoProyecto = codigoProyecto;
    }

    public String getCip() {
        return cip;
    }

    public void setCip(String cip) {
        this.cip = cip;
    }

    public String getCodigoProyecto() {
        return codigoProyecto;
    }

    public void setCodigoProyecto(String codigoProyecto) {
        this.codigoProyecto = codigoProyecto;
    }

    @Override
    public String getRol() {
        return "Ingeniero Residente";
    }

    @Override
    public boolean puedeAcceder(String modulo) {
        if (modulo.equalsIgnoreCase("Avance") || modulo.equalsIgnoreCase("Entrega")
                || modulo.equalsIgnoreCase("Postventa")) {
            return true;
        }
        return false;
    }
    
}
