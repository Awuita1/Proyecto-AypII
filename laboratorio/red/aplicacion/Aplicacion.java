package red.aplicacion;
import red.modelo.*;
import red.datos.*;
import red.interfaz.*;
import red.logica.*;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.IOException;
public class Aplicacion {
    public static void main(String[] args) throws FileNotFoundException {
    // Cargar parámetros
          try {
            CargarParametros.parametros();
        } catch (IOException e) {
            System.err.print("Error al cargar parámetros");
            System.exit(-1);
        }

        // Cargar datos
        TreeMap<String, Equipo> equipos = null;
        List<Conexion> conexiones = null;

        try {
            equipos = Dato.cargarEquipos(CargarParametros.getArchivoComputadoras(),CargarParametros.getArchivoRouters());
            conexiones = Dato.cargarConexiones(CargarParametros.getArchivoConexiones(), equipos);
        } catch (FileNotFoundException e) {
            System.err.print("Error al cargar archivos de datos");
            System.exit(-1);
        }
        // Ejemplo de uso:
        for (String id : equipos.keySet()) {
            System.out.println("Nodo ID: " + id);
        }
        for (Conexion conexion : conexiones) {
            System.out.println("Conexion de " + conexion.getSource().getId() + " a " + conexion.getTarget().getId());

        }


    // Cargar grafo
    Logica red = null;
    
    
    try {
        red = new Logica(equipos, conexiones);
        System.out.println("-----------Grafo cargado exitosamente.-----------");
    } catch (Exception e) {
        System.err.println("Error al cargar el grafo: " + e.getMessage());
        e.printStackTrace();
    }

    /* Interfaz */
    boolean on = true;
    while (on) {
        int opcion = Interfaz.opcion();
        switch (opcion) {
            case 3:
            Interfaz.ping(red);
                break;
            
            case 2:
            Interfaz.resultadoTraceroute(red);
            break;

            case 1:
            Interfaz.MST(red);
            break;
            
            case 0:
            Interfaz.salir();
            on = false;
            break;
            
            case -1:
            Interfaz.salir();
            on = false;
            	
            default:
            Interfaz.invalido();
                break;
        }
    }
    }
}