package inpt_cde.systemmonitor.pkg_server;

import inpt_cde.systemmonitor.model.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


public class MonitoringServiceImpl extends UnicastRemoteObject 
                                   implements MonitoringServiceInterface {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Stockage (thread-safe)
    private final Map<Integer, Agent> agents = new ConcurrentHashMap<>();
    private final Map<Integer, Queue<Metric>> metricsHistory = new ConcurrentHashMap<>();
    private final List<Alert> alerts = Collections.synchronizedList(new ArrayList<>());
    private final List<Trace> traces = Collections.synchronizedList(new ArrayList<>());
    private final List<TypeAlert> alertTypes = Collections.synchronizedList(new ArrayList<>());
    private final List<Utilisateur> users = Collections.synchronizedList(new ArrayList<>());
    private final List<Profil> profils = Collections.synchronizedList(new ArrayList<>());
    private final List<Droit> droits = Collections.synchronizedList(new ArrayList<>());
    
    private static final int MAX_HISTORY_SIZE = 1000;
    
    public MonitoringServiceImpl() throws RemoteException {
        super();
        initializeDefaultData();
        System.out.println("✓ Service RMI initialisé");
    }
    
    private void initializeDefaultData() {
        // Types d'alertes 
        TypeAlert cpuAlert = new TypeAlert();
        cpuAlert.setId(1);
        cpuAlert.setLabel("CPU");
        alertTypes.add(cpuAlert);
        
        TypeAlert memAlert = new TypeAlert();
        memAlert.setId(2);
        memAlert.setLabel("RAM");
        alertTypes.add(memAlert);
        
        TypeAlert diskAlert = new TypeAlert();
        diskAlert.setId(3);
        diskAlert.setLabel("Disque");
        alertTypes.add(diskAlert);
        
        // Profil admin
        Profil adminProfil = new Profil();
        adminProfil.setId(1);
        adminProfil.setLabel("Administrateur");
        profils.add(adminProfil);
        
        // Utilisateur admin
        Utilisateur admin = new Utilisateur();
        admin.setId(1);
        admin.setLogin("admin");
        admin.setPwd("admin");
        admin.setLastLogin(new Date());
        admin.setActive(true);
        users.add(admin);
        
        // Droits
        Droit droitAdmin = new Droit(1, "Administration complète");
        droits.add(droitAdmin);
        
        System.out.println("✓ Données par défaut initialisées");
    }
    
    // ===== AGENTS =====
    
    @Override
    public List<Agent> getAllAgents() throws RemoteException {
        return new ArrayList<>(agents.values());
    }
    
    @Override
    public Agent getAgent(int agentId) throws RemoteException {
        return agents.get(agentId);
    }
    
    @Override
    public int getAgentCount() throws RemoteException {
        return agents.size();
    }
    
    @Override
    public List<Agent> getActiveAgents() throws RemoteException {
        return agents.values().stream()
                .filter(Agent::isOnline)
                .collect(Collectors.toList());
    }
    
    // ===== MÉTRIQUES =====
    
    @Override
    public Metric getLatestMetric(int agentId) throws RemoteException {
        Queue<Metric> history = metricsHistory.get(agentId);
        if (history != null && !history.isEmpty()) {
            return ((LinkedList<Metric>) history).getLast();
        }
        return null;
    }
    
    @Override
    public List<Metric> getMetricsHistory(int agentId, int limit) throws RemoteException {
        Queue<Metric> history = metricsHistory.get(agentId);
        if (history == null) {
            return new ArrayList<>();
        }
        
        List<Metric> result = new ArrayList<>(history);
        int size = result.size();
        int start = Math.max(0, size - limit);
        
        return result.subList(start, size);
    }
    
    @Override
    public Map<Integer, Metric> getAllLatestMetrics() throws RemoteException {
        Map<Integer, Metric> result = new HashMap<>();
        
        for (Map.Entry<Integer, Queue<Metric>> entry : metricsHistory.entrySet()) {
            Queue<Metric> history = entry.getValue();
            if (!history.isEmpty()) {
                result.put(entry.getKey(), ((LinkedList<Metric>) history).getLast());
            }
        }
        
        return result;
    }
    
    // ===== ALERTES =====
    
    @Override
    public List<Alert> getAllAlerts() throws RemoteException {
        synchronized (alerts) {
            return new ArrayList<>(alerts);
        }
    }
    
    @Override
    public List<Alert> getAlertsBySeverity(int severity) throws RemoteException {
        synchronized (alerts) {
            return alerts.stream()
                    .filter(alert -> alert.getSeverity() == severity)
                    .collect(Collectors.toList());
        }
    }
    
    @Override
    public List<Alert> getRecentAlerts(int limit) throws RemoteException {
        synchronized (alerts) {
            int size = alerts.size();
            int start = Math.max(0, size - limit);
            return new ArrayList<>(alerts.subList(start, size));
        }
    }
    
    @Override
    public Map<Integer, Integer> getAlertCountBySeverity() throws RemoteException {
        Map<Integer, Integer> counts = new HashMap<>();
        counts.put(1, 0);
        counts.put(2, 0);
        counts.put(3, 0);
        
        synchronized (alerts) {
            for (Alert alert : alerts) {
                int sev = alert.getSeverity();
                counts.put(sev, counts.getOrDefault(sev, 0) + 1);
            }
        }
        
        return counts;
    }
    
    // ===== UTILISATEURS =====
    
    @Override
    public Utilisateur authenticate(String login, String password) throws RemoteException {
        synchronized (users) {
            for (Utilisateur user : users) {
                if (user.getLogin().equals(login) && user.getPwd().equals(password)) {
                    user.setLastLogin(new Date());
                    return user;
                }
            }
        }
        return null;
    }
    
    @Override
    public List<Utilisateur> getAllUsers() throws RemoteException {
        synchronized (users) {
            return new ArrayList<>(users);
        }
    }
    
    // ===== PROFILS =====
    
    @Override
    public Profil getProfil(int profilId) throws RemoteException {
        synchronized (profils) {
            return profils.stream()
                    .filter(p -> p.getId() == profilId)
                    .findFirst()
                    .orElse(null);
        }
    }
    
    @Override
    public List<Profil> getAllProfils() throws RemoteException {
        synchronized (profils) {
            return new ArrayList<>(profils);
        }
    }
    
    // ===== DROITS =====
    
    @Override
    public List<Droit> getAllDroits() throws RemoteException {
        synchronized (droits) {
            return new ArrayList<>(droits);
        }
    }
    
    // ===== TRACES =====
    
    @Override
    public List<Trace> getRecentTraces(int limit) throws RemoteException {
        synchronized (traces) {
            int size = traces.size();
            int start = Math.max(0, size - limit);
            return new ArrayList<>(traces.subList(start, size));
        }
    }
    
    @Override
    public void addTrace(String label) throws RemoteException {
        Trace trace = new Trace(traces.size() + 1, label, new Date());
        synchronized (traces) {
            traces.add(trace);
        }
    }
    
    // ===== TYPES D'ALERTES =====
    
    @Override
    public List<TypeAlert> getAllAlertTypes() throws RemoteException {
        synchronized (alertTypes) {
            return new ArrayList<>(alertTypes);
        }
    }
    // ===== STATISTIQUES =====
    
    @Override
    public SystemStatistics getSystemStatistics() throws RemoteException {
        SystemStatistics stats = new SystemStatistics();
        
        // Agents
        stats.setTotalAgents(agents.size());
        long activeCount = agents.values().stream().filter(Agent::isOnline).count();
        stats.setActiveAgents((int) activeCount);
        stats.setInactiveAgents(agents.size() - (int) activeCount);
        
        // Alertes
        Map<Integer, Integer> alertCounts = getAlertCountBySeverity();
        stats.setTotalAlerts(alerts.size());
        stats.setCriticalAlerts(alertCounts.get(3));
        stats.setWarningAlerts(alertCounts.get(2));
        stats.setInfoAlerts(alertCounts.get(1));
        
        // Moyennes métriques
        Map<Integer, Metric> latestMetrics = getAllLatestMetrics();
        if (!latestMetrics.isEmpty()) {
            double totalCpu = 0, totalMem = 0, totalDisk = 0;
            int count = 0;
            
            for (Metric metric : latestMetrics.values()) {
                totalCpu += metric.getCpuUsage();
                totalMem += metric.getMemoryUsageMB();
                totalDisk += metric.getDiskUsagePercents();  // diskUsagePercents du code réel
                count++;
            }
            
            if (count > 0) {
                stats.setAverageCpu(totalCpu / count);
                stats.setAverageMemory(totalMem / count);
                stats.setAverageDisk(totalDisk / count);
            }
        }
        
        return stats;
    }
    
    // ===== MÉTHODES INTERNES (appelées par UDP/TCP) =====
    
    public void registerOrUpdateAgent(Agent agent) {
        int id = agent.getId();
        agents.put(id, agent);
    }
    
    public void addMetric(int agentId, Metric metric) {
        Queue<Metric> history = metricsHistory.computeIfAbsent(
            agentId, k -> new LinkedList<>()
        );
        
        history.offer(metric);
        
        while (history.size() > MAX_HISTORY_SIZE) {
            history.poll();
        }
        
        // Vérifier seuils et créer alertes
        checkThresholdsAndCreateAlerts(agentId, metric);
    }
    
    public void addAlert(Alert alert) {
        synchronized (alerts) {
            alerts.add(alert);
            String severityStr = alert.getSeverity() == 3 ? "CRITICAL" : 
                               alert.getSeverity() == 2 ? "WARNING" : "INFO";
            System.out.println("🔔 ALERTE " + severityStr + " - " + alert.getMessage());
        }
    }
    
    /**
     */
    private int calculateSeverity(double performance, double seuil) {
        double delta = performance - seuil;
        
        if (delta <= 0) {
            return 0; // Pas d'alerte
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
    
    private void checkThresholdsAndCreateAlerts(int agentId, Metric metric) {
        double CPU_SEUIL = 30.0;
        double RAM_SEUIL = 45.0;
        double DISK_SEUIL = 90.0;
        
        // Vérifier CPU
        int cpuSeverity = calculateSeverity(metric.getCpuUsage(), CPU_SEUIL);
        if (cpuSeverity > 0) {
            Alert alert = new Alert();
            alert.setId(alerts.size() + 1);
            alert.setSeverity(cpuSeverity);
            alert.setMessage("CPU: " + String.format("%.1f%%", metric.getCpuUsage()));
            alert.setThreshold(CPU_SEUIL);
            alert.setValue(metric.getCpuUsage());
            alert.setTimesptamp(metric.getTimestamp());
            addAlert(alert);
        }
        
        // Vérifier RAM
        int ramSeverity = calculateSeverity(metric.getMemoryUsageMB(), RAM_SEUIL);
        if (ramSeverity > 0) {
            Alert alert = new Alert();
            alert.setId(alerts.size() + 1);
            alert.setSeverity(ramSeverity);
            alert.setMessage("RAM: " + String.format("%.1f MB", metric.getMemoryUsageMB()));
            alert.setThreshold(RAM_SEUIL);
            alert.setValue(metric.getMemoryUsageMB());
            alert.setTimesptamp(metric.getTimestamp());
            addAlert(alert);
        }
        
        // Vérifier Disque
        int diskSeverity = calculateSeverity(metric.getDiskUsagePercents(), DISK_SEUIL);
        if (diskSeverity > 0) {
            Alert alert = new Alert();
            alert.setId(alerts.size() + 1);
            alert.setSeverity(diskSeverity);
            alert.setMessage("Disque: " + String.format("%.1f%%", metric.getDiskUsagePercents()));
            alert.setThreshold(DISK_SEUIL);
            alert.setValue(metric.getDiskUsagePercents());
            alert.setTimesptamp(metric.getTimestamp());
            addAlert(alert);
        }
    }
    
    public void markAgentOffline(int agentId) {
        Agent agent = agents.get(agentId);
        if (agent != null) {
            agent.setOnline(false);
            agent.setLastAlertTime(new Date());
            System.out.println("Agent déconnecté: " + agentId);
        }
    }
}