/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion;
import clases.Administrador;
import clases.AsesorInmobiliario;
import clases.Empleado;
import clases.IngenieroResidente;

/**
 *
 * @author Lee
 */
public class GestionEmpleado {
    private Empleado[] empleados;
    private int totalEmpleados;

    public GestionEmpleado() {
        empleados = new Empleado[50];
        totalEmpleados = 0;
        
        //Inicializar datos: un usuario por rol para poder entrar al sistema
        registrar(new Administrador("70000001", "Richard", "Lee", "admin", "admin123"));
        registrar(new AsesorInmobiliario("70000002", "Juan", "Aguilar", "asesor", "asesor123"));
        registrar(new IngenieroResidente("CIP12345", "P001", "70000003", "Friddry", "Cueto", "ingeniero", "ing123"));
    }
    
    public Empleado buscarPorDni(String dni){
        for (int i = 0; i < totalEmpleados; i++) {
            if(empleados[i].getDni().equals(dni)){
                return empleados[i];
            }
        }
        return null;
    }
    
    public Empleado buscarPorUsuario(String usuario){
        for (int i = 0; i < totalEmpleados; i++) {
            if(empleados[i].getUsuario().equalsIgnoreCase(usuario)){
                return empleados[i];
            }
        }
        return null;
    }
    
    public boolean registrar(Empleado empleado){
        //Verificar si esta lleno el arreglo
        if(totalEmpleados == empleados.length){
            return false;
        }
        
        //evita que el DNI o usuario esten duplicados
        if(buscarPorDni(empleado.getDni()) != null){
            return false;
        }
        if(buscarPorUsuario(empleado.getUsuario()) != null){
            return false;
        }
        
        //registrar
        empleados[totalEmpleados] = empleado;
        totalEmpleados++;
        return true;
    }
    
    public boolean actualizar(Empleado empleado){
        //el usuario no puede pertenecer a otro empleado
        Empleado otro = buscarPorUsuario(empleado.getUsuario());
        if(otro != null && !otro.getDni().equals(empleado.getDni())){
            return false;
        }
        
        for (int i = 0; i < totalEmpleados; i++) {
            if(empleados[i].getDni().equals(empleado.getDni())){
                //Se reemplaza el objeto completo porque el rol puede cambiar
                empleados[i] = empleado;
                return true;
            }
        }
        return false;
    }
    
    public boolean eliminar(String dni){
        for (int i = 0; i < totalEmpleados; i++) {
            if(empleados[i].getDni().equals(dni)){
                
                //correr a la izquierda
                for (int j = i; j < totalEmpleados - 1; j++) {
                    empleados[j] = empleados[j+1];
                }
                
                //Limpiamos el ultimo casillero
                empleados[totalEmpleados - 1] = null;
                totalEmpleados--;
                return true;
            }
        }
        return false;
    }
    
    //sistema de login, devuelve el empleado si el usuario y la contrasena son correctos
    public Empleado validarAcceso(String usuario, String contrasena){
        for (int i = 0; i < totalEmpleados; i++) {
            if(empleados[i].validarAcceso(usuario, contrasena)){
                return empleados[i];
            }
        }
        return null;
    }
    
    public Empleado[] obtenerEmpleados(){
        return empleados;
    }
    
    public int obtenerTotalEmpleados(){
        return totalEmpleados;
    }
    
}
