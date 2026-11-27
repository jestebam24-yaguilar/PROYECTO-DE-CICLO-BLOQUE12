/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion;

import clases.Proyecto;
import java.time.LocalDate;

/**
 *
 * @author Cueto
 */
public class GestionProyecto {
    private Proyecto[] proyectos;
    private int totalProyectos;

    public GestionProyecto() {
        proyectos = new Proyecto[20];
        totalProyectos = 0;
        
        //Inicializar datos
        registrar(new Proyecto("P001", "Condominio Los Alamos", "Av. Primavera 1250", "Surco",
                2, 10, LocalDate.of(2027, 12, 15)));
        registrar(new Proyecto("P002", "Residencial Parque San Miguel", "Av. La Marina 2300", "San Miguel",
                3, 8, LocalDate.of(2028, 6, 30)));
    }
    
    public Proyecto buscarPorCodigo(String codigo){
        for (int i = 0; i < totalProyectos; i++) {
            if(proyectos[i].getCodigo().equalsIgnoreCase(codigo)){
                return proyectos[i];
            }
        }
        return null;
    }
    
    public boolean registrar(Proyecto proyecto){
        //Verificar si esta lleno el arreglo
        if(totalProyectos == proyectos.length){
            return false;
        }
        //Evitar duplicados
        if(buscarPorCodigo(proyecto.getCodigo()) != null){
            return false;
        }
        proyectos[totalProyectos] = proyecto;
        totalProyectos++;
        return true;
    }
    
    //La cantidad de torres no se modifica porque ya tienen unidades y partidas
    public boolean actualizar(Proyecto proyecto){
        Proyecto encontrado = buscarPorCodigo(proyecto.getCodigo());
        if(encontrado == null){
            return false;
        }
        encontrado.setNombre(proyecto.getNombre());
        encontrado.setDireccion(proyecto.getDireccion());
        encontrado.setDistrito(proyecto.getDistrito());
        encontrado.setCantidadPisos(proyecto.getCantidadPisos());
        encontrado.setFechaEntrega(proyecto.getFechaEntrega());
        return true;
    }
    
    public boolean eliminar(String codigo){
        for (int i = 0; i < totalProyectos; i++) {
            if(proyectos[i].getCodigo().equalsIgnoreCase(codigo)){
                
                //correr los demas elementos una posicion a la izquierda
                for (int j = i; j < totalProyectos - 1; j++) {
                    proyectos[j] = proyectos[j+1];
                }
                
                //Limpiamos el ultimo casillero
                proyectos[totalProyectos - 1] = null;
                totalProyectos--;
                return true;
            }
        }
        return false;
    }

    public Proyecto[] obtenerProyectos() {
        return proyectos;
    }

    public int obtenerTotalProyectos() {
        return totalProyectos;
    }
    
}
