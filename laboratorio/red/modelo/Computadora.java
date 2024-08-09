package red.modelo;
public class Computadora extends Equipo {
    public Computadora(String id, String ipAddress, String macAddress, boolean status, String ubicacion) {
        super(id, ipAddress, macAddress, status, ubicacion);
    }

    @Override
    public String toString() {
        return "Computadora{" +
                "id='" + getId() + '\'' +
                ", ipAddress='" + getIpAddress() + '\'' +
                ", macAddress='" + getMacAddress() + '\'' +
                ", status=" + isStatus() +
                ", ubicacion='" + getUbicacion() + '\'' +
                '}';
    }
}