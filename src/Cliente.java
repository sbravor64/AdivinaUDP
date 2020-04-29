import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    private int puertoDestino;
    private int resultado;
    private String nombre, ipServidor;
    private int intentos;
    private InetAddress direccionDestino;
    private Tablero tablero;
    private Jugada jugada;

    private MulticastSocket multisocket;
    private InetAddress multicastIP;
    boolean continueRunning = true;

    public Cliente(String ip, int port) {
        this.puertoDestino = port;
        resultado = -1;
        intentos = 0;
        ipServidor = ip;
        jugada = new Jugada();
        try {
            multisocket = new MulticastSocket(5557);
            multicastIP = InetAddress.getByName("224.0.0.10");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            direccionDestino = InetAddress.getByName(ipServidor);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void setNombre(String nombre) {
        this.nombre =nombre;
    }

    public int getIntentos() {
        return intentos;
    }

    public void runCliente() throws IOException {
        byte [] receivedData = new byte[1024];
        int n;
        DatagramPacket packet;
        DatagramSocket socket = new DatagramSocket();

        System.out.println("Hola " + nombre + "! Comenzamos!");
        System.out.println("Adivina el Número");
        System.out.println("Escribe tu primer numero: ");

        while(resultado !=0 && resultado !=-2 && resultado !=3) {
            Scanner sc = new Scanner(System.in);
            n = sc.nextInt();
            jugada.nombre = nombre;
            jugada.num = n;

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(jugada);
            byte[] mensaje = os.toByteArray();

            packet = new DatagramPacket(mensaje, mensaje.length, direccionDestino, puertoDestino);
            socket.send(packet);
            packet = new DatagramPacket(receivedData, 1024);
            socket.setSoTimeout(5000);

            try {
                socket.receive(packet);
                resultado = getDataToRequest(packet.getData());
            }catch(SocketTimeoutException e) {
                System.out.println("El servidor no responde: " + e.getMessage());
                resultado =-2;
            }
        }
        socket.close();

        if(tablero.acabados < tablero.map_jugadores.size()) {
            multisocket.joinGroup(multicastIP);
            while (continueRunning) {
                DatagramPacket mpacket = new DatagramPacket(receivedData, 1024);
                multisocket.receive(mpacket);
                continueRunning = printData(mpacket.getData());
            }
            multisocket.leaveGroup(multicastIP);
            multisocket.close();
        }
    }

    private void setTablero(byte[] data) {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            tablero = (Tablero) ois.readObject();
            tablero.map_jugadores.forEach((k, v)-> System.out.println("Intentos: " + k + " -> " + v));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private boolean printData(byte[] data) {
        setTablero(data);
        if(tablero.acabados == tablero.map_jugadores.size()) return false;
        else return true;

    }

    private int getDataToRequest(byte[] data) {
        setTablero(data);
        String msg = null;
        switch (tablero.resultado) {
            case 0: msg = "Correcto"; break;
            case 1: msg = "Más pequeño"; break;
            case 2: msg = "Más grande"; break;
            case 3: msg = "Dew"; break;
        }
        System.out.println(msg);
        return tablero.resultado;
    }


    public int getResultado() {
        return resultado;
    }

    public void setResultado(int resultado) {
        this.resultado = resultado;
    }

    public static void main(String[] args) {
        String jugador, ipServidor;

        //Demanem la ip del servidor i nom del jugador
        System.out.println("IP del servidor?");
        Scanner sc = new Scanner(System.in);
        ipServidor = sc.next();
        System.out.println("Nombre jugador:");
        jugador = sc.next();

        Cliente clienteAdivina = new Cliente(ipServidor, 5556);

        clienteAdivina.setNombre(jugador);
        try {
            clienteAdivina.runCliente();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(clienteAdivina.getResultado() == 0) {
            System.out.println("Lo has conseguido con "+ clienteAdivina.tablero.map_jugadores.get(jugador).intValue() +" intentos");
            clienteAdivina.tablero.map_jugadores.forEach((k, v)-> System.out.println(k + "->" + v));
        } else {
            System.out.println("Has perdido");
        }
    }
}
