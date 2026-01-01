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

public class MonitoringServer {
	
	public static ServerSocket TCPSvr ;
	public static int TCP_SRV_PORT = 7405;
	public static int UDP_SRV_PORT = 9999;
	public static String SRV_ADR = "localhost";
	
	private static void TCPRecption() {
		String ret = "";
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
                    System.out.println("Received: " + line);
                }

                clientSocket.close();
                System.out.println("Client disconnected");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block  
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
            }
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		TCPRecption();
		UDPReception();
		
	}

	

}
