/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestion;

import clases.Adicional;
import clases.Paquete;

/**
 *
 * @author Cueto
 */
public class GestionAdicional {
    private Adicional[] adicionales;
    private int totalAdicionales;
    private Paquete[] paquetes;
    private int totalPaquetes;

    public GestionAdicional() {
        adicionales = new Adicional[50];
        totalAdicionales = 0;
        paquetes = new Paquete[20];
        totalPaquetes = 0;
        
        //Inicializar datos del proyecto P001
        registrarAdicional(new Adicional("AD01", "Estacionamiento", 35000, "P001", 30));
        registrarAdicional(new Adicional("AD02", "Deposito", 12000, "P001", 20));
        registrarAdicional(new Adicional("AD03", "Cocina equipada", 18000, "P001", 15));
        registrarAdicional(new Adicional("AD04", "Closets", 8000, "P001", 25));
        registrarAdicional(new Adicional("AD05", "Mejora a porcelanato", 9500, "P001", 25));
        registrarAdicional(new Adicional("AD01", "Estacionamiento", 30000, "P002", 10));
        
        Paquete full = new Paquete("PK01", "Pack Full Equipado", "P001", 30000);
        full.agregarAdicional(buscarAdicional("P001", "AD03"));
        full.agregarAdicional(buscarAdicional("P001", "AD04"));
        full.agregarAdicional(buscarAdicional("P001", "AD05"));
        registrarPaquete(full);
        
        Paquete familia = new Paquete("PK02", "Pack Familia", "P001", 62000);
        familia.agregarAdicional(buscarAdicional("P001", "AD01"));
        familia.agregarAdicional(buscarAdicional("P001", "AD01"));
        registrarPaquete(familia);
    }
    
    //---------------- Adicionales ----------------
    
    public Adicional buscarAdicional(String codigoProyecto, String codigo){
        for (int i = 0; i < totalAdicionales; i++) {
            if(adicionales[i].getCodigoProyecto().equalsIgnoreCase(codigoProyecto)
                    && adicionales[i].getCodigo().equalsIgnoreCase(codigo)){
                return adicionales[i];
            }
        }
        return null;
    }
    
    public boolean registrarAdicional(Adicional adicional){
        if(totalAdicionales == adicionales.length){
            return false;
        }
        if(buscarAdicional(adicional.getCodigoProyecto(), adicional.getCodigo()) != null){
            return false;
        }
        adicionales[totalAdicionales] = adicional;
        totalAdicionales++;
        return true;
    }
    
    public boolean actualizarAdicional(Adicional adicional){
        Adicional encontrado = buscarAdicional(adicional.getCodigoProyecto(), adicional.getCodigo());
        if(encontrado == null){
            return false;
        }
        encontrado.setNombre(adicional.getNombre());
        encontrado.setPrecio(adicional.getPrecio());
        encontrado.setCantidadDisponible(adicional.getCantidadDisponible());
        return true;
    }
    
    public boolean eliminarAdicional(String codigoProyecto, String codigo){
        Adicional adicional = buscarAdicional(codigoProyecto, codigo);
        if(adicional == null){
            return false;
        }
        //No se elimina si forma parte de un paquete
        for (int i = 0; i < totalPaquetes; i++) {
            if(paquetes[i].contarAdicional(adicional) > 0){
                return false;
            }
        }
        for (int i = 0; i < totalAdicionales; i++) {
            if(adicionales[i] == adicional){
                for (int j = i; j < totalAdicionales - 1; j++) {
                    adicionales[j] = adicionales[j+1];
                }
                adicionales[totalAdicionales - 1] = null;
                totalAdicionales--;
                return true;
            }
        }
        return false;
    }
    
    //---------------- Paquetes ----------------
    
    public Paquete buscarPaquete(String codigo){
        for (int i = 0; i < totalPaquetes; i++) {
            if(paquetes[i].getCodigo().equalsIgnoreCase(codigo)){
                return paquetes[i];
            }
        }
        return null;
    }
    
    public boolean registrarPaquete(Paquete paquete){
        if(totalPaquetes == paquetes.length){
            return false;
        }
        if(buscarPaquete(paquete.getCodigo()) != null){
            return false;
        }
        //Un paquete se compone de adicionales existentes
        if(paquete.obtenerTotalAdicionales() == 0){
            return false;
        }
        paquetes[totalPaquetes] = paquete;
        totalPaquetes++;
        return true;
    }
    
    public boolean eliminarPaquete(String codigo){
        for (int i = 0; i < totalPaquetes; i++) {
            if(paquetes[i].getCodigo().equalsIgnoreCase(codigo)){
                for (int j = i; j < totalPaquetes - 1; j++) {
                    paquetes[j] = paquetes[j+1];
                }
                paquetes[totalPaquetes - 1] = null;
                totalPaquetes--;
                return true;
            }
        }
        return false;
    }

    public Adicional[] obtenerAdicionales() {
        return adicionales;
    }

    public int obtenerTotalAdicionales() {
        return totalAdicionales;
    }

    public Paquete[] obtenerPaquetes() {
        return paquetes;
    }

    public int obtenerTotalPaquetes() {
        return totalPaquetes;
    }
    
}
