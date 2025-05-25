package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Clase utilitaria para encriptar contraseñas usando SHA-256.
 * Utilizada por el sistema de login y gestión de usuarios.
 * Esta clase es estática y no requiere instanciarse.
 *
 * @author Javier Lopez
 */
public class Encriptador {

    /**
     * Encripta una cadena de texto utilizando SHA-256.
     *
     * @param texto Texto plano a encriptar
     * @return Cadena encriptada en formato hexadecimal
     */
    public static String encriptar(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(texto.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);

                if (hex.length() == 1) {
                    hexString.append('0');
                }

                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            // En caso de error, imprime el stack trace y retorna null
            e.printStackTrace();
            return null;
        }
    }
}
