package inpt_cde.systemmonitor.pkg_agent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SeuilRecupService {

    public static Seuils fetchFromServer() {
        Seuils s = new Seuils(20.0, 50.0, 60.0);
        return s;
    }
    
    private static Seuils current;

    static {
        // Chargement au démarrage
        current = SeuilsPreferences.load();
    }

    public static void start() {

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
