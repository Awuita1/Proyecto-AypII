package red.modelo;
public abstract class Equipo {
    private String id;
    private String ipAddress;
    private String macAddress;
    private boolean status;
    private String ubicacion;

    public Equipo(String id, String ipAddress, String macAddress, boolean status, String ubicacion) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.status = status;
        this.ubicacion = ubicacion;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Override
    public String toString() {
        return "Equipo{" +
                "id='" + id + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", status=" + status +
                ", ubicacion='" + ubicacion + '\'' +
                '}';
    }
}