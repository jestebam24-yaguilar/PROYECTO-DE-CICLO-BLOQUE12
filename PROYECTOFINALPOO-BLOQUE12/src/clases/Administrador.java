/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author Lee
 */
public class Administrador extends Empleado {

    public Administrador(String dni, String nombres, String apellidos, String usuario, String contrasena) {
        super(dni, nombres, apellidos, usuario, contrasena);
    }

    @Override
    public String getRol() {
        return "Administrador";
    }

    @Override
    public boolean puedeAcceder(String modulo) {
        if (modulo.equalsIgnoreCase("Personal") || modulo.equalsIgnoreCase("Proyectos")
                || modulo.equalsIgnoreCase("Adicionales") || modulo.equalsIgnoreCase("Reportes")) {
            return true;
        }
        return false;
    }
    
}
