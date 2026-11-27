/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilitario;
import clases.Empleado;
import gestion.GestionEmpleado;
//Clases de Cueto
import gestion.GestionProyecto;
import gestion.GestionUnidad;
import gestion.GestionAdicional;

//Clases de Juan Esteban
import gestion.GestionCliente;





/**
 *
 * @author Lee
 */
public class Sistema {
    //Objetos de gestion compartidos: todas las ventanas usan los mismos arreglos
    public static GestionEmpleado gestionEmpleado = new GestionEmpleado();
    
    //Gestiones de Cueto: proyectos, unidades, adicionales y postventa
    
    //Gestiones de Juan Esteban: clientes y ventas
    public static GestionCliente gestionCliente = new GestionCliente();
    
    //Empleado que inicio sesion
    public static Empleado empleadoActual;
    
}
