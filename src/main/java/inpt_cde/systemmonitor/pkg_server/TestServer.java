package inpt_cde.systemmonitor.pkg_server;

import inpt_cde.systemmonitor.model.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Serveur de test avec données simulées
 * Adapté aux VRAIES classes du projet
 */
public class TestServer {
    
    private static MonitoringServiceImpl rmiService;
    private static Random random = new Random();
    private static int alertIdCounter = 1;
    
    public static void main(String[] args) {
        try {
            System.out.println("========================================");
            System.out.println("  SERVEUR DE TEST - DONNÉES SIMULÉES");
            System.out.println("========================================\n");
            
            startRMIService();
            createTestAgents();
            startMetricsGenerator();
            
            System.out.println("\n✓ Serveur de test opérationnel !");
            System.out.println("✓ Génération automatique de métriques toutes les 5s");
            System.out.println("\nLancez maintenant l'UI:");
            System.out.println("  ./gradlew runUI");
            System.out.println("\nAppuyez sur Ctrl+C pour arrêter...\n");
            
            Thread.currentThread().join();
            
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void startRMIService() throws Exception {
        System.out.println("Démarrage du service RMI...");
        
        rmiService = new MonitoringServiceImpl();
        
        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(1099);
            System.out.println("  → Registre RMI créé sur le port 1099");
        } catch (Exception e) {
            registry = LocateRegistry.getRegistry(1099);
            System.out.println("  → Registre RMI existant utilisé");
        }
        
        registry.rebind("MonitoringService", rmiService);
        System.out.println("✓ Service RMI 'MonitoringService' enregistré");
    }
    
    private static void createTestAgents() {
        System.out.println("\nCréation des agents de test...");
        
        String[] types = {"Ubuntu 22.04", "CentOS 8", "Windows Server 2022", "Debian 11", "RHEL 9"};
        
        for (int i = 1; i <= 5; i++) {
            Agent agent = new Agent();
            
            // Utiliser le wrapper pour définir les propriétés
            agent.setId(i);
            agent.setHostname("agent-prod-" + String.format("%02d", i));
            agent.setIpAddress("192.168.1." + (10 + i));
            agent.setMacAddress("00:1B:44:11:3A:" + String.format("%02X", i));
            agent.setTypeOS(types[i-1]);
            agent.setDateInstallation(new Date(System.currentTimeMillis() - (3600000L * 24 * i)));
            agent.setOnline(i <= 4); // Le 5e est offline
            agent.setLastAlertTime(new Date(System.currentTimeMillis() - 300000));
            agent.setLastMetricsTime(new Date());
            
            rmiService.registerOrUpdateAgent(agent);
            System.out.println("  ✓ Agent " + i + ": " + agent.getHostname() + 
                             " (" + agent.getTypeOS() + ")");
        }
    }
    
    private static void startMetricsGenerator() {
        Timer timer = new Timer(true);
        
        // Générer métriques toutes les 5 secondes
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    for (int agentId = 1; agentId <= 4; agentId++) {
                        generateMetrics(agentId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 5000);
        
        // Générer alertes aléatoires toutes les 20 secondes
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (random.nextDouble() < 0.4) {
                        generateRandomAlert();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 10000, 20000);
    }
    
    private static void generateMetrics(int agentId) {
        try {
            Metric metric = new Metric();
            metric.setId(agentId * 10000 + (int)(System.currentTimeMillis() % 10000));
            metric.setTimestamp(new Date());
            
            // Valeurs réalistes avec variation
            double baseCpu = 25 + (agentId * 8);
            double baseMem = 1024 + (agentId * 512);
            double baseDisk = 45 + (agentId * 8);
            
            metric.setCpuUsage(baseCpu + random.nextDouble() * 25);
            metric.setMemoryUsageMB(baseMem + random.nextDouble() * 512);
            metric.setDiskUsagePercents(baseDisk + random.nextDouble() * 20);  // diskUsagePercents
            
            // 10% de chance de pic
            if (random.nextDouble() < 0.1) {
                if (random.nextBoolean()) {
                    metric.setCpuUsage(35 + random.nextDouble() * 30);  // Au-dessus du seuil 30
                } else {
                    metric.setDiskUsagePercents(92 + random.nextDouble() * 5);  // Au-dessus du seuil 90
                }
            }
            
            rmiService.addMetric(agentId, metric);
            
            // Mettre à jour agent
            Agent agent = rmiService.getAgent(agentId);
            if (agent != null) {
            	agent.setLastMetricsTime(new Date());
                rmiService.registerOrUpdateAgent(agent);
            }
            
        } catch (Exception e) {
            System.err.println("Erreur génération métriques: " + e.getMessage());
        }
    }
    
    private static void generateRandomAlert() {
        try {
            int agentId = 1 + random.nextInt(4);
            Agent agent = rmiService.getAgent(agentId);
            
            if (agent == null) return;
            
            Alert alert = new Alert();
            alert.setId(alertIdCounter++);
            alert.setTimesptamp(new Date());  // timesptamp avec la faute de frappe
            
            // Type d'alerte aléatoire
            int type = random.nextInt(3);
            
            switch (type) {
                case 0: // CPU
                    double cpuValue = 35 + random.nextDouble() * 40;
                    int cpuSeverity = calculateSeverity(cpuValue, 30.0);
                    
                    alert.setSeverity(cpuSeverity);
                    alert.setMessage("CPU: " + String.format("%.1f%%", cpuValue));
                    alert.setThreshold(30.0);
                    alert.setValue(cpuValue);
                    break;
                    
                case 1: // RAM
                    double ramValue = 1500 + random.nextDouble() * 1000;
                    int ramSeverity = calculateSeverity(ramValue, 45.0);
                    
                    alert.setSeverity(ramSeverity);
                    alert.setMessage("RAM: " + String.format("%.0f MB", ramValue));
                    alert.setThreshold(45.0);
                    alert.setValue(ramValue);
                    break;
                    
                case 2: // Disque
                    double diskValue = 92 + random.nextDouble() * 7;
                    int diskSeverity = calculateSeverity(diskValue, 90.0);
                    
                    alert.setSeverity(diskSeverity);
                    alert.setMessage("Disque: " + String.format("%.1f%%", diskValue));
                    alert.setThreshold(90.0);
                    alert.setValue(diskValue);
                    break;
            }
            
            rmiService.addAlert(alert);
            
        } catch (Exception e) {
            System.err.println("Erreur génération alerte: " + e.getMessage());
        }
    }
    
    private static int calculateSeverity(double performance, double seuil) {
        double delta = performance - seuil;
        
        if (delta <= 0) {
            return 1;
        }
        
        double percentOverThreshold = (delta / seuil) * 100;
        
        if (percentOverThreshold <= 33) {
            return 1; // INFO
        } else if (percentOverThreshold <= 66) {
            return 2; // WARNING
        } else {
            return 3; // CRITICAL
        }
    }
}