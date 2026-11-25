/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author Lee
 */
public class AsesorInmobiliario extends Empleado {

    public AsesorInmobiliario(String dni, String nombres, String apellidos, String usuario, String contrasena) {
        super(dni, nombres, apellidos, usuario, contrasena);
    }

    @Override
    public String getRol() {
        return "Asesor Inmobiliario";
    }

    @Override
    public boolean puedeAcceder(String modulo) {
        //El asesor solo gestiona clientes, ventas y pagos
        if (modulo.equalsIgnoreCase("Clientes") || modulo.equalsIgnoreCase("Ventas")
                || modulo.equalsIgnoreCase("Pagos")) {
            return true;
        }
        return false;
    }
    
}
