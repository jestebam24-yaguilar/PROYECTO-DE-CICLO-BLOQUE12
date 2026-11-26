/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion;

import clases.Cliente;
import clases.ClienteContado;
import clases.ClienteHipotecario;
import clases.ClienteMivivienda;

/**
 *
 * @author Juan Esteban
 */
public class GestionCliente {
    private Cliente[] clientes;
    private int totalClientes;

    public GestionCliente() {
        clientes = new Cliente[50];
        totalClientes = 0;
        
        //Inicializar datos: un cliente por modalidad
        registrar(new ClienteContado("45678912", "Maria", "Torres Diaz", "Soltero", "", "",
                "987654321", "maria.torres@gmail.com", 25000));
        registrar(new ClienteHipotecario("BCP", "41234567", "Carlos", "Ramirez Soto", "Casado",
                "42345678", "Ana Lopez Vega", "986543210", "carlos.ramirez@gmail.com", 18000));
        registrar(new ClienteMivivienda("Interbank", "47896541", "Lucia", "Fernandez Rojas", "Soltero",
                "", "", "985432109", "lucia.fernandez@gmail.com", 9000));
    }
    
    public Cliente buscarPorDni(String dni){
        for (int i = 0; i < totalClientes; i++) {
            if(clientes[i].getDni().equals(dni)){
                return clientes[i];
            }
        }
        return null;
    }
    
    public boolean registrar(Cliente cliente){
        //Verificar si esta lleno el arreglo
        if(totalClientes == clientes.length){
            return false;
        }
        //DNI unico
        if(buscarPorDni(cliente.getDni()) != null){
            return false;
        }
        clientes[totalClientes] = cliente;
        totalClientes++;
        return true;
    }
    
    //Se reemplaza el objeto porque la modalidad de compra puede cambiar
    public boolean actualizar(Cliente cliente){
        for (int i = 0; i < totalClientes; i++) {
            if(clientes[i].getDni().equals(cliente.getDni())){
                clientes[i] = cliente;
                return true;
            }
        }
        return false;
    }
    
    public boolean eliminar(String dni){
        for (int i = 0; i < totalClientes; i++) {
            if(clientes[i].getDni().equals(dni)){
                
                //correr los demas elementos una posicion a la izquierda
                for (int j = i; j < totalClientes - 1; j++) {
                    clientes[j] = clientes[j+1];
                }
                
                //Limpiamos el ultimo casillero
                clientes[totalClientes - 1] = null;
                totalClientes--;
                return true;
            }
        }
        return false;
    }

    public Cliente[] obtenerClientes() {
        return clientes;
    }

    public int obtenerTotalClientes() {
        return totalClientes;
    }
    
}

