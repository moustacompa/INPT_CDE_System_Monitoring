package inpt_cde.systemmonitor.pkg_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import inpt_cde.systemmonitor.pkg_agent.controller.Seuils;

public class MonitoringServer {
	
	public static int TCP_SRV_PORT = 7405;
	public static int TCP_ALERTS_SRV_PORT = 8405;
	public static int UDP_SRV_PORT = 9999;
	public static String SRV_ADR = "localhost";
	private static MonitoringServiceImpl rmiService;
	
	private static void TCPRecption() {
		String ret = "";
		ServerSocket TCPSvr; 
		try {
			TCPSvr = new ServerSocket(MonitoringServer.TCP_SRV_PORT);
			System.out.println("TCP Server listening on port "+TCP_SRV_PORT);
			while (true) {
				Socket clientSocket = TCPSvr.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream())
                );

                String line;
                while ((line = reader.readLine()) != null) {
                    ret+=line;
                	System.out.println("Received: " + line);
                }
                ServerLogger.saveLogs(1, ret);
                clientSocket.close();
                System.out.println("Client disconnected");
			}
		} catch (IOException e) {
			System.err.println("Impossible d'écouter sur le port " + MonitoringServer.TCP_SRV_PORT);
			e.printStackTrace();
		}
	}
	
	private static void UDPReception() {
		byte[] buffer = new byte[4096];

        try (DatagramSocket socket = new DatagramSocket(MonitoringServer.UDP_SRV_PORT)) {
            System.out.println("UDP Server listening on port " + MonitoringServer.UDP_SRV_PORT);

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(
                        packet.getData(),
                        0,
                        packet.getLength(),
                        StandardCharsets.UTF_8
                );

                System.out.println("Received from via UDP "
                        + packet.getAddress() + ":" + packet.getPort());
                System.out.println(message);
                ServerLogger.saveLogs(1, message);
            }
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void seuilService() {
		ServerSocket TCPSeuilsSvr;
		try {
			TCPSeuilsSvr = new ServerSocket(MonitoringServer.TCP_ALERTS_SRV_PORT);
			System.out.println("Seuil Service à l'écoute sur le port "+MonitoringServer.TCP_ALERTS_SRV_PORT);
			while (true) {
				Socket client = TCPSeuilsSvr.accept();
				PrintWriter out  = new PrintWriter(client.getOutputStream(), true);
				Seuils s = new Seuils(30.0, 45.0, 90.0);
				out.println(s+"\n");
			}
		} catch (IOException e) {
			System.err.println("Impossible d'écouter sur le port " + MonitoringServer.TCP_ALERTS_SRV_PORT);
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println("====================================");
        System.out.println("  SERVEUR DE SURVEILLANCE - INPT");
        System.out.println("====================================\n");
        
        startRMIService();
        
		new Thread(() -> {
			seuilService();
		}).start();
		new Thread(() -> {
			TCPRecption();
		}).start();
		new Thread(() -> {
			UDPReception();
		}).start();
		
		System.out.println("\n✓ Tous les services sont opérationnels !\n");
	}

	private static void startRMIService() {
		try {
			System.out.println("Démarrage du service RMI");
			
			rmiService = new MonitoringServiceImpl();
			
			Registry registry;
			try {
				registry = LocateRegistry.createRegistry(1099);
				System.out.println(" - Registre RMI crée sur le port 1099.");
			} catch (Exception e) {
				registry = LocateRegistry.getRegistry(1099);
				System.out.println(" - Registre RMI existant utilisé.");
			}
			
			registry.rebind("MonitoringService", rmiService);
			System.out.println(" Service RMI 'MonitoringService' enregistré.\n");
		} catch (Exception e) {
			System.err.println(" Erreur de démarrage RMI: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
