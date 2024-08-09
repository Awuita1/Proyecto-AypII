package red.logica;

import java.util.*;
import java.util.Map.Entry;

import net.datastructures.AdjacencyMapGraph;
import net.datastructures.Edge;
import net.datastructures.Graph;
import net.datastructures.GraphAlgorithms;
import net.datastructures.Map;
import net.datastructures.Position;
import net.datastructures.PositionalList;
import net.datastructures.ProbeHashMap;
import net.datastructures.Vertex;
import red.modelo.*;

public class Logica {
    private Graph<Equipo, Conexion> red;
    private TreeMap<String, Vertex<Equipo>> vertices;
    
    /**
     * Constructor de logica, al ser llamado crea el grafo red
     * @param equipos
     * @param conexiones
     */
    public Logica(TreeMap<String, Equipo> equipos, List<Conexion> conexiones) {
        red = new AdjacencyMapGraph<>(false);

        // Cargar Equipos
        vertices = new TreeMap<>();
        for (Entry<String, Equipo> entry : equipos.entrySet()) {
            String key = entry.getKey();
            Equipo equipo = entry.getValue();
            Vertex<Equipo> vertex = red.insertVertex(equipo);
            vertices.put(key, vertex);
            System.out.println("Insertar vertice: " + equipo.getIpAddress() + " LLave " + key);
        }

        // Cargar Conexiones
        for (Conexion conexion : conexiones) {
            String ipSource = conexion.getSource().getIpAddress();
            String ipTarget = conexion.getTarget().getIpAddress();
            
            Vertex<Equipo> sourceVertex = vertices.get(conexion.getSource().getId());
            Vertex<Equipo> targetVertex = vertices.get(conexion.getTarget().getId());

            if (sourceVertex != null && targetVertex != null) {
                System.out.println("Insertar arco entre: " + ipSource + " y " + ipTarget);
                red.insertEdge(sourceVertex, targetVertex, conexion);
            } else {
                System.err.println("Error: Vertice no encontrado por IPs " + ipSource + " o " + ipTarget);
            }
        }
        
        System.out.println("Vertices: ");
        for (Vertex<Equipo> p : red.vertices()) {
        	System.out.println(p.getElement().getId());
        }
        
        System.out.println("Conexiones: ");
        for (Edge<Conexion> p : red.edges()) {
            System.out.println(p.getElement().getSource().getId() + " conectado a " + p.getElement().getTarget().getId());
        }
        
        
    }

    /**
     * adapta red para que pueda ser manejado por algoritmos como shortestPathList y MST
     * @return grafo sin equipos y conexiones falsas ademas de eliminar los vertices sin conectar
     * @throws IllegalArgumentException vertice no encontrado
     */
    private Graph<Equipo, Integer> adaptedGraph() throws IllegalArgumentException {
        // Crear una copia del grafo
        Graph<Equipo, Integer> adapted = new AdjacencyMapGraph<>(false);
        ProbeHashMap<String, Vertex<Equipo>> res = new ProbeHashMap<>();

        // Insertar vértices activos en el grafo adaptado
        for (Vertex<Equipo> result : red.vertices()) {
            if (result.getElement().isStatus()) { // Ignorar equipos inactivos
            	Vertex<Equipo> aux = adapted.insertVertex(result.getElement());
                res.put(result.getElement().getId(),aux);
            }
        }

        Vertex<Equipo> source, target;

        // Determinar el ancho de banda máximo para el cálculo de peso inverso
        int maxBandwidth = 0;
        for (Edge<Conexion> result : red.edges()) {
            if (result.getElement().getBandwidth() > maxBandwidth) maxBandwidth = result.getElement().getBandwidth();
        }

        // Insertar aristas activas en el grafo adaptado
        for (Edge<Conexion> result : red.edges()) {
            if (result.getElement().isStatus()) { // Ignorar conexiones inactivas
                source = res.get(result.getElement().getSource().getId());
                target = res.get(result.getElement().getTarget().getId());
                if (source != null && target != null) {
                    int weight = maxBandwidth / result.getElement().getBandwidth();
                    adapted.insertEdge(source, target, weight);
                    System.out.println("Insertar arista de " + source.getElement().getId() + " a " + target.getElement().getId());
                } else {
                    throw new IllegalArgumentException("Source o target no válido " + source.getElement().getId() + " " + target.getElement().getId());
                }
            }
        }
        
        Map<String, Vertex<Equipo>> aEliminar = new ProbeHashMap<>();
        for (Vertex<Equipo> p : adapted.vertices()) {
        	if (adapted.outDegree(p) == 0 && adapted.inDegree(p) == 0) aEliminar.put(p.getElement().getId(), p); //Guardo los elementos a borrar
        }
        
        for (String p : aEliminar.keySet()) {
        	adapted.removeVertex(aEliminar.get(p));
        }
        	
        
        
        for (Vertex<Equipo> p : adapted.vertices()) {
        	System.out.println("Insertar: " + p.getElement().getId());
        }
        return adapted;
        
    }
    
    /**
     * Método para verificar si un equipo con la dirección IP indicada está activo
     * @param ipAddress ip del equipo a revisar
     * @return estatus del equipo
     */
    public boolean ping(String ipAddress) {
        try {
            for (Vertex<Equipo> vertex : red.vertices()) {
                Equipo equipo = vertex.getElement();
                if (equipo == null) {
                    throw new NullPointerException("El vértice no tiene un elemento asociado.");
                }
                if (equipo.getIpAddress().equals(ipAddress)) {
                    return equipo.isStatus();
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
        return false; 
    }
      
    

    /**
     * Calcula el camino mas corto entre origen y destino, considera si existen tales vertices
     * @param idAdressOrigen origen
     * @param idAdressDestino destino
     * @return camino mas corto entre origen y destino
     */
    public PositionalList<Vertex<Equipo>> traceroute(String idAdressOrigen, String idAdressDestino) {
        // Adaptar el grafo
        Graph<Equipo, Integer> adapted = adaptedGraph();   
		
        // Obtener los vértices correspondientes a las direcciones ID
        Vertex<Equipo> source = vertices.get(idAdressOrigen);
        Vertex<Equipo> target = vertices.get(idAdressDestino);

        if (source == null || target == null) {
            throw new IllegalArgumentException("Una de las direcciones IP no se encuentra en el grafo.");
        }

        PositionalList<Vertex<Equipo>> lista = GraphAlgorithms.shortestPathList(adapted, source, target);


        return lista;
	}

    /**
     * Calciula el Arbol de expansion minimo del grafo red adaptado
     * @return lista de conexiones
     */
    public List<String> MST(){
        Graph<Equipo, Integer> mstGraph = adaptedGraph();
           
        PositionalList<Edge<Integer>> mst = GraphAlgorithms.MST(mstGraph);
        
        List<String> conexiones = new LinkedList<>();   
        
        for (Edge<Integer> p : mst) {
        	Vertex<Equipo>[] aux = mstGraph.endVertices(p);
        	conexiones.add(aux[0].getElement().getId() + "\t-->\t" + aux[1].getElement().getId() + "\t" + p.getElement() +"\n");
        	
        }
        
        return conexiones;
    }

    
      /**
       * 
       * @param ipAddressOrigen
       * @param ipAddressDestino
       * @return
       */
    
    /*
      public List<Conexion> traceroute(String ipAddressOrigen, String ipAddressDestino) {
          Vertex<Equipo> origen = vertices.get(ipAddressOrigen);
          Vertex<Equipo> destino = vertices.get(ipAddressDestino);

          // Validar que los equipos de origen y destino existen y están activos
          if (origen == null || destino == null || !origen.getElement().isStatus() || !destino.getElement().isStatus()) {
              System.err.println("Dirección IP de origen o destino no encontrada o desactivada.");
              return Collections.emptyList();
          }

          // Mapa para almacenar las distancias desde el origen
          Map<Vertex<Equipo>, Integer> distancias = new HashMap<>();
          // Mapa para almacenar el camino más corto encontrado
          Map<Vertex<Equipo>, Edge<Conexion>> camino = new HashMap<>();
          // Conjunto de vértices visitados
          Set<Vertex<Equipo>> visitados = new HashSet<>();
          // Cola de prioridad para seleccionar el vértice con la menor distancia
          PriorityQueue<PQEntry> pq = new PriorityQueue<>();

          // Inicializar distancias
          for (Vertex<Equipo> v : red.vertices()) {
              if (v == origen) {
                  distancias.put(v, 0);
                  pq.add(new PQEntry(v, 0));
              } else {
                  distancias.put(v, Integer.MAX_VALUE);
              }
          }

          // Ejecutar Dijkstra
          while (!pq.isEmpty()) {
              PQEntry currentEntry = pq.poll();
              Vertex<Equipo> current = currentEntry.vertex;

              if (!visitados.contains(current)) {
                  visitados.add(current);

                  for (Edge<Conexion> e : red.outgoingEdges(current)) {
                      if (!e.getElement().isStatus()) continue; // Ignorar conexiones inactivas

                      Vertex<Equipo> neighbor = red.opposite(current, e);
                      if (!neighbor.getElement().isStatus()) continue; // Ignorar vecinos inactivos

                      // El peso comparado será inversamente el valor del ansho de banda, mientras mas, menor sera el peso
                      if (!visitados.contains(neighbor)) {
                          int newDist = distancias.get(current) + 1 / e.getElement().getBandwidth();

                          if (newDist < distancias.get(neighbor)) {
                              distancias.put(neighbor, newDist);
                              camino.put(neighbor, e);
                              pq.add(new PQEntry(neighbor, newDist));
                          }
                      }
                  }
              }
          }

          // Reconstruir el camino desde el destino
          LinkedList<Conexion> resultado = new LinkedList<>();
          Vertex<Equipo> current = destino;

          while (current != origen) {
              Edge<Conexion> edge = camino.get(current);
              if (edge == null) {
                  System.err.println("No se encontró un camino desde " + ipAddressOrigen + " hasta " + ipAddressDestino);
                  return Collections.emptyList();
              }
              resultado.addFirst(edge.getElement());
              current = red.opposite(current, edge);
          }

          return resultado;
      }
      */
      
      /**
       * Metodo de utilidad que se utiliza para gestionar la cola de prioridad de Dijkstra
       */
}