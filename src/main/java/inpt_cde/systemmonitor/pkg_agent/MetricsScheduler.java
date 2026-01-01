package inpt_cde.systemmonitor.pkg_agent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import inpt_cde.systemmonitor.model.Metric;

public class MetricsScheduler {

    private static final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    public static void start() {

        Runnable task = () -> {
            try {
                collectMetrics();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        // Démarrage immédiat, puis toutes les 10 minutes
        scheduler.scheduleAtFixedRate(
                task,
                0,
                1,
                TimeUnit.MINUTES
        );
    }

    private static void collectMetrics() {
        Metric m = null;
		try {
			
			m = AgentMonitor.getPerf();
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (m!=null) {
			System.out.println(m);
		}
    }

    public static void stop() {
        scheduler.shutdown();
    }
}
