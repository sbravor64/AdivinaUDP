import java.io.*;
import java.net.*;

public class ServidorUDP {
    DatagramSocket socket;
    int puerto, fin, acabados, multipuerto=5557;
    Adivina adivina;
    boolean acabado;
    Tablero tablero;
    MulticastSocket multisocket;
    InetAddress multicastIp;

    public ServidorUDP(int puerto, int max) {
        try {
            socket = new DatagramSocket(puerto);
            multisocket = new MulticastSocket(multipuerto);
            multicastIp = InetAddress.getByName("224.0.0.10");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.puerto = puerto;
        adivina = new Adivina();
        tablero = new Tablero();
        acabado = false;
        acabados = 0;
        fin =-1;
    }

    private void runServer() throws IOException {
        byte [] receivingData = new byte[1024];
        byte [] sendingData;
        InetAddress clientIP;
        int clientPort;

        System.out.println("Palabra: " + adivina.palabra);

        while(acabados < tablero.map_jugadores.size() || acabados ==0){

            DatagramPacket packet = new DatagramPacket(receivingData, receivingData.length);
            socket.receive(packet);
            sendingData = processData(packet.getData());
            clientIP = packet.getAddress();
            clientPort = packet.getPort();
            packet = new DatagramPacket(sendingData, sendingData.length,
                    clientIP, clientPort);
            socket.send(packet);
            DatagramPacket multipacket = new DatagramPacket(sendingData, sendingData.length,
                    multicastIp, multipuerto);
            multisocket.send(multipacket);
        }
        socket.close();
    }

    private byte[] processData(byte[] data) {
        Jugada j = null;
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            j = (Jugada) ois.readObject();
            System.out.println("Jugador: " + j.nombre + " " + j.letra);
            if(!tablero.map_jugadores.containsKey(j.nombre)) tablero.map_jugadores.put(j.nombre, 1);
            else {
                int intentos = tablero.map_jugadores.get(j.nombre) +1;
                tablero.map_jugadores.put(j.nombre, intentos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        adivina.palabraAdivinadaHastaElMomento = adivina.actualizarLetraAdivinadaHastaElMomento(j.letra.charAt(0));
        tablero.palabraAdivinadaHastaElMomento = adivina.palabraAdivinadaHastaElMomento;

        fin = tablero.resultado = adivina.comprueba();

        if(fin == 0) {
            acabado = true;
            acabados++;
            tablero.acabados++;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(tablero);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] resposta = bos.toByteArray();
        return resposta;
    }

    public static void main(String[] args) {
        ServidorUDP servidor = new ServidorUDP(5556, 100);

        try {
            servidor.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Final Servidor");
    }
}
