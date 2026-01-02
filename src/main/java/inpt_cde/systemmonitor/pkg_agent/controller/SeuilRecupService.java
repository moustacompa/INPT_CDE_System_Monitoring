package inpt_cde.systemmonitor.pkg_agent.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import inpt_cde.systemmonitor.pkg_server.MonitoringServer;

public class SeuilRecupService {

    public static Seuils fetchFromServer() {
        Seuils s = null;
        //s = new Seuils(10.0, 20.0, 30.0);
    	Socket st;
		try {
			st = new Socket(MonitoringServer.SRV_ADR, MonitoringServer.TCP_ALERTS_SRV_PORT);
			BufferedReader br = new BufferedReader(new InputStreamReader(st.getInputStream()));
			String str = br.readLine();
			//System.out.println("Dans fetchFromServer et str="+str);
			s = new Seuils(str);
		} catch (IOException ex) {
			System.out.println("Dans fetchFromServer et erreur de connexion au serveur au port "+MonitoringServer.TCP_ALERTS_SRV_PORT);
			ex.printStackTrace();
		}
        return s;
    }
    
    private static Seuils current;

    static {
        // Chargement au démarrage
        current = SeuilsPreferences.load();
    }

    public static void start() {
    	
    	//Reset les préférences pour forcer le téléchargement
    	Preferences prefs = Preferences.userNodeForPackage(SeuilsPreferences.class);
    	try {
    	    prefs.clear();   
    	    prefs.flush();   
    	} catch (BackingStoreException e) {
    	    e.printStackTrace();
    	}

        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                Seuils t = SeuilRecupService.fetchFromServer();
                SeuilsPreferences.save(t);
                current = t;
                System.out.println("Thresholds updated");
            } catch (Exception e) {
                System.err.println("Using cached thresholds");
            }
        }, 0, 10, TimeUnit.MINUTES);
    }

    public static Seuils get() {
        return current;
    }
}
