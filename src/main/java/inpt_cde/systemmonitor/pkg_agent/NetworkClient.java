package inpt_cde.systemmonitor.pkg_agent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import inpt_cde.systemmonitor.model.Alert;
import inpt_cde.systemmonitor.model.Metric;
import inpt_cde.systemmonitor.pkg_server.MonitoringServer;

public class NetworkClient {
	
	public static int sendMetric(Metric m) {
		int ret = 1;
		byte[] data = m.toString().getBytes(StandardCharsets.UTF_8);
        InetAddress serverAddress = null;
		try {
			serverAddress = InetAddress.getByName(MonitoringServer.SRV_ADR);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        DatagramPacket packet =
                new DatagramPacket(data, data.length, serverAddress, MonitoringServer.UDP_SRV_PORT);
        
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.send(packet);
            System.out.println("Metrics sent to server");
        }catch (Exception e) {
			ret = 0;
		}
        return ret;
	}
	
	public static int sendAlert(Alert alert) {
		int ret = 0;
		try (Socket socket = new Socket(MonitoringServer.SRV_ADR, MonitoringServer.TCP_SRV_PORT)) {

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true
            );
            out.println(alert);
            System.out.println("Alert sent to server");
        } catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
}
