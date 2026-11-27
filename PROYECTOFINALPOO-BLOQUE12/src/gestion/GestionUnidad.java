/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion;

import clases.Duplex;
import clases.Flat;
import clases.Penthouse;
import clases.UnidadInmobiliaria;

/**
 *
 * @author Cueto
 */
public class GestionUnidad {
    private UnidadInmobiliaria[] unidades;
    private int totalUnidades;

    public GestionUnidad() {
        unidades = new UnidadInmobiliaria[200];
        totalUnidades = 0;
        
        //Inicializar datos: proyecto P001, torres A y B, 10 pisos, 2 departamentos por piso
        String[] torres = {"A", "B"};
        for (int t = 0; t < torres.length; t++) {
            for (int piso = 1; piso <= 10; piso++) {
                for (int numero = 1; numero <= 2; numero++) {
                    String codigo = torres[t] + (piso * 100 + numero);
                    String vista;
                    if(numero == 1){
                        vista = "Exterior";
                    }else{
                        vista = "Interior";
                    }
                    if(piso <= 8){
                        registrar(new Flat(codigo, "P001", torres[t], piso, 70, 0, 2, vista, 6500));
                    }else if(piso == 9){
                        registrar(new Duplex(codigo, "P001", torres[t], piso, 110, 25, 3, vista, 6500));
                    }else{
                        registrar(new Penthouse(codigo, "P001", torres[t], piso, 130, 60, 3, vista, 6500));
                    }
                }
            }
        }
        //Algunas unidades del proyecto P002
        registrar(new Flat("A101", "P002", "A", 1, 65, 0, 2, "Exterior", 5200));
        registrar(new Flat("A102", "P002", "A", 1, 60, 0, 2, "Interior", 5200));
        registrar(new Flat("A501", "P002", "A", 5, 75, 0, 3, "Exterior", 5200));
        registrar(new Duplex("A701", "P002", "A", 7, 100, 20, 3, "Exterior", 5200));
        registrar(new Penthouse("A801", "P002", "A", 8, 120, 50, 3, "Exterior", 5200));
    }
    
    //El codigo es unico dentro de cada proyecto
    public UnidadInmobiliaria buscar(String codigoProyecto, String codigo){
        for (int i = 0; i < totalUnidades; i++) {
            if(unidades[i].getCodigoProyecto().equalsIgnoreCase(codigoProyecto)
                    && unidades[i].getCodigo().equalsIgnoreCase(codigo)){
                return unidades[i];
            }
        }
        return null;
    }
    
    public boolean registrar(UnidadInmobiliaria unidad){
        //Verificar si esta lleno el arreglo
        if(totalUnidades == unidades.length){
            return false;
        }
        //No se permiten dos unidades con el mismo codigo en un proyecto
        if(buscar(unidad.getCodigoProyecto(), unidad.getCodigo()) != null){
            return false;
        }
        unidades[totalUnidades] = unidad;
        totalUnidades++;
        return true;
    }
    
    //Se reemplaza el objeto porque el tipo (Flat, Duplex, Penthouse) puede cambiar
    public boolean actualizar(UnidadInmobiliaria unidad){
        for (int i = 0; i < totalUnidades; i++) {
            if(unidades[i].getCodigoProyecto().equalsIgnoreCase(unidad.getCodigoProyecto())
                    && unidades[i].getCodigo().equalsIgnoreCase(unidad.getCodigo())){
                //Solo se modifican unidades que aun no se han vendido
                if(!unidades[i].getEstado().equals("Disponible")){
                    return false;
                }
                unidades[i] = unidad;
                return true;
            }
        }
        return false;
    }
    
    public boolean eliminar(String codigoProyecto, String codigo){
        for (int i = 0; i < totalUnidades; i++) {
            if(unidades[i].getCodigoProyecto().equalsIgnoreCase(codigoProyecto)
                    && unidades[i].getCodigo().equalsIgnoreCase(codigo)){
                if(!unidades[i].getEstado().equals("Disponible")){
                    return false;
                }
                
                //correr los demas elementos una posicion a la izquierda
                for (int j = i; j < totalUnidades - 1; j++) {
                    unidades[j] = unidades[j+1];
                }
                
                //Limpiamos el ultimo casillero
                unidades[totalUnidades - 1] = null;
                totalUnidades--;
                return true;
            }
        }
        return false;
    }
    
    public int contarPorProyecto(String codigoProyecto){
        int contador = 0;
        for (int i = 0; i < totalUnidades; i++) {
            if(unidades[i].getCodigoProyecto().equalsIgnoreCase(codigoProyecto)){
                contador++;
            }
        }
        return contador;
    }
    
    public int contarPorEstado(String codigoProyecto, String torre, String estado){
        int contador = 0;
        for (int i = 0; i < totalUnidades; i++) {
            if(unidades[i].getCodigoProyecto().equalsIgnoreCase(codigoProyecto)
                    && unidades[i].getTorre().equalsIgnoreCase(torre)
                    && unidades[i].getEstado().equals(estado)){
                contador++;
            }
        }
        return contador;
    }

    public UnidadInmobiliaria[] obtenerUnidades() {
        return unidades;
    }

    public int obtenerTotalUnidades() {
        return totalUnidades;
    }
    
}
