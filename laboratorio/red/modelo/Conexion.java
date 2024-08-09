package red.modelo;

public class Conexion {
    private Equipo source;
    private Equipo target;
    private String tipoConexion;
    private int bandwidth;
    private int latencia;
    private boolean status;
    private double errorRate;

    public Conexion(Equipo source, Equipo target, String tipoConexion, int bandwidth, int latencia, double errorRate) {
        this.source = source;
        this.target = target;
        this.tipoConexion = tipoConexion;
        this.bandwidth = bandwidth;
        this.latencia = latencia;
        this.status = getStatus();
        this.errorRate = errorRate;
    }

    // Getters and setters
    public Equipo getSource() {
        return source;
    }

    public void setSource(Equipo source) {
        this.source = source;
    }

    public Equipo getTarget() {
        return target;
    }

    public void setTarget(Equipo target) {
        this.target = target;
    }

    public String getTipoConexion() {
        return tipoConexion;
    }

    public void setTipoConexion(String tipoConexion) {
        this.tipoConexion = tipoConexion;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }

    public int getLatencia() {
        return latencia;
    }

    public void setLatencia(int latencia) {
        this.latencia = latencia;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(double errorRate) {
        this.errorRate = errorRate;
    }
    
    /**
     * Si el vertice src o target resulta ser falso entonces la conexion tambien lo es
     * @return status de la conexion
     */
    private boolean getStatus() {
    	return (this.source.isStatus() && this.target.isStatus());
    }
    @Override
    public String toString() {
        return "Conexion{" +
                "source=" + source +
                ", target=" + target +
                ", tipoConexion='" + tipoConexion + '\'' +
                ", bandwidth=" + bandwidth +
                ", latencia=" + latencia +
                ", status=" + status +
                ", errorRate=" + errorRate +
                '}';
    }
}