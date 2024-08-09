package red.datos;

import red.modelo.*;
import java.io.*;
import java.util.*;


public class Dato {

	/**
	 * 
	 * @param archivoRouters ruta de acceso para el archivo de routers
	 * @return mapa con todos los routers asociados a su id
	 * @throws FileNotFoundException archivo de routers no existe
	 */
    private static TreeMap<String, Equipo> cargarRouters(String archivoRouters) throws FileNotFoundException {
		Scanner read;

		TreeMap<String, Equipo> equipo = new TreeMap<String, Equipo>();
		
		
			read = new Scanner(new File(archivoRouters));
			read.useDelimiter("\\s*;\\s*");
		
			String id, ipAddress, macAddress, status, ubicacion, modelo, firmware, throughput;
			
			while (read.hasNext()) {
				id = read.next();
				ipAddress = read.next();
                macAddress = read.next();
                status = read.next();
                ubicacion = read.next();
				modelo = read.next();
                firmware = read.next();
                throughput= read.next();
                equipo.put(id, new Router(id, ipAddress, macAddress, Boolean.parseBoolean(status), ubicacion, modelo, firmware, Integer.parseInt(throughput)));
                
			}
			read.close();
		
		return equipo;
	}
    /**
     * 
     * @param archivoComputadoras ruta de acceso al archivo de computadoras
     * @return mapa con los valores de cada comutadora asociado con su id
     * @throws FileNotFoundException el archivo de computadoras no existe
     */
	private static TreeMap<String, Equipo> cargarComputadoras(String archivoComputadoras) throws FileNotFoundException {
		Scanner read;
		TreeMap<String, Equipo> equipo = new TreeMap<String, Equipo>();
		
			read = new Scanner(new File(archivoComputadoras));
			read.useDelimiter("\\s*;\\s*");
			
			String id, ipAddress, macAddress, status, ubicacion;
			while (read.hasNext()) {				
                id = read.next();
				ipAddress = read.next();
                macAddress = read.next();
				status = read.next();
                ubicacion = read.next();
				equipo.put(id,new Computadora(id, ipAddress, macAddress, Boolean.parseBoolean(status), ubicacion));
			}
			read.close();
		
		return equipo;
	}



	/**
	 * combina los dos mapas de computadoras y routers en uno solo
	 * @param archivoComputadoras ruta de acceso para el archivo de computadoras
	 * @param archivoRouters ruta de acceso para el archivo de touters
	 * @return mapa con los valores de los equipos asociados a su id
	 * @throws FileNotFoundException archivo de computadoras y/o archivo de routers no encontrado
	 */
    public static TreeMap<String, Equipo> cargarEquipos(String archivoComputadoras, String archivoRouters) throws FileNotFoundException {
        TreeMap<String, Equipo> equipos = new TreeMap<>();

        equipos.putAll(Dato.cargarRouters(archivoRouters));
        equipos.putAll(Dato.cargarComputadoras(archivoComputadoras));
        
        return equipos;
    }

    /**
     * lee el archivo de conexiones y las carga a una lista
     * @param archivoConexiones ruta de acceso al archivo de los routers
     * @param equipos mapa de los equipos
     * @return lista de conexiones
     * @throws FileNotFoundException archivo de conexiones no encontrado
     */
    public static List<Conexion> cargarConexiones(String archivoConexiones, TreeMap<String, Equipo> equipos) throws FileNotFoundException {
        Scanner read;
        List<Conexion> conexiones = new ArrayList<Conexion>();

        read = new Scanner(new File(archivoConexiones));
        read.useDelimiter("\\s*;\\s*");
        Equipo e1, e2;
        String idEquipo1, idEquipo2, tipoConexion, bandwidth, latencia, errorRate;
        
        while (read.hasNext()) {
        	idEquipo1 = read.next();
            idEquipo2 = read.next();
        	if (equipos.containsKey(idEquipo1) && equipos.containsKey(idEquipo2)) {
        		e1 = equipos.get(idEquipo1);
        		e2 = equipos.get(idEquipo2);
        		tipoConexion = read.next();
        		bandwidth = read.next();
        		latencia = read.next();
        		errorRate = read.next();
        		conexiones.add(new Conexion(e1, e2, tipoConexion, Integer.parseInt(bandwidth), Integer.parseInt(latencia), Double.parseDouble(errorRate)));
        	} else {
        		System.out.println("Error IDs de los equipos no encontrados en el mapa");
        	}
        }
	    read.close();

	    return conexiones;
    }
}

