/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package clases;

/**
 *
 * @author Lee
 */
public interface Acceso {
    boolean validarAcceso(String usuario, String contrasena);
    String getRol();
    boolean puedeAcceder(String modulo);
}
