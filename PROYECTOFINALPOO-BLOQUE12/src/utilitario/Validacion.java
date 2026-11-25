/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package utilitario;
import java.time.LocalDate;

/**
 *
 * @author Lee
 */
public class Validacion {
    
    public static boolean esVacio(String texto){
        return texto.trim().isEmpty();
    }
    
    public static boolean esEntero(String texto){
        try {
            Integer.parseInt(texto.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean esDecimal(String texto){
        try {
            Double.parseDouble(texto.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    //es el dni de 8 digitos
    public static boolean esDni(String texto){
        return texto.trim().matches("[0-9]{8}");
    }
    
    //fecha en formato aaaa-mm-dd, por ejemplo 2026-11-24
    public static boolean esFecha(String texto){
        try {
            LocalDate.parse(texto.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static LocalDate convertirFecha(String texto){
        return LocalDate.parse(texto.trim());
    }
    
    public static String moneda(double monto){
        return String.format("S/ %,.2f", monto);
    }
    
}
