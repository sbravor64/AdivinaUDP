import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Tablero implements Serializable{
    public Map<String, Integer> map_jugadores;
    public int resultado = 3, acabados;

    public String palabraAdivinadaHastaElMomento;

    public Tablero() {
        map_jugadores = new HashMap<>();
        acabados = 0;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Intentos\n");
        map_jugadores.forEach((k, v) -> stringBuilder.append(k + " - " + v + "\n"));
        return stringBuilder.toString();
    }
}

class Jugada implements Serializable {
    String nombre;
    String letra;
    Integer intentos;

}
