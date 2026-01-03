package inpt_cde.systemmonitor.pkg_server.modele;

import inpt_cde.systemmonitor.model.Alert;
import inpt_cde.systemmonitor.model.Metric;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe représentant les informations d'un agent de surveillance
 * Stocke l'historique des métriques et alertes pour chaque agent
 */
public class AgentInfo {
    private String agentId;
    private String ipAddress;
    private Date lastContact;
    private boolean isOnline;
    
    // Historique des métriques (max 100 dernières)
    private List<Metric> metricsHistory;
    
    // Historique des alertes (max 50 dernières)
    private List<Alert> alertsHistory;
    
    // Dernière métrique reçue
    private Metric lastMetric;
    
    // Dernière alerte reçue
    private Alert lastAlert;
    
    public AgentInfo(String agentId, String ipAddress) {
        this.agentId = agentId;
        this.ipAddress = ipAddress;
        this.lastContact = new Date();
        this.isOnline = true;
        this.metricsHistory = new ArrayList<>();
        this.alertsHistory = new ArrayList<>();
    }
    
    /**
     * Ajoute une métrique à l'historique
     * Limite l'historique à 100 métriques max
     */
    public synchronized void addMetric(Metric metric) {
        this.lastMetric = metric;
        this.lastContact = new Date();
        this.isOnline = true;
        
        metricsHistory.add(0, metric); // Ajoute au début
        
        // Limite l'historique à 100 métriques
        if (metricsHistory.size() > 100) {
            metricsHistory.remove(metricsHistory.size() - 1);
        }
    }
    
    /**
     * Ajoute une alerte à l'historique
     * Limite l'historique à 50 alertes max
     */
    public synchronized void addAlert(Alert alert) {
        this.lastAlert = alert;
        this.lastContact = new Date();
        this.isOnline = true;
        
        alertsHistory.add(0, alert); // Ajoute au début
        
        // Limite l'historique à 50 alertes
        if (alertsHistory.size() > 50) {
            alertsHistory.remove(alertsHistory.size() - 1);
        }
    }
    
    /**
     * Vérifie si l'agent est considéré comme en ligne
     * Un agent est offline s'il n'a pas envoyé de données depuis 5 minutes
     */
    public synchronized boolean checkIfOnline() {
        long timeSinceLastContact = new Date().getTime() - lastContact.getTime();
        isOnline = timeSinceLastContact < 300000; // 5 minutes
        return isOnline;
    }
    
    // Getters
    public String getAgentId() { return agentId; }
    public String getIpAddress() { return ipAddress; }
    public Date getLastContact() { return lastContact; }
    public boolean isOnline() { return checkIfOnline(); }
    public Metric getLastMetric() { return lastMetric; }
    public Alert getLastAlert() { return lastAlert; }
    public List<Metric> getMetricsHistory() { return new ArrayList<>(metricsHistory); }
    public List<Alert> getAlertsHistory() { return new ArrayList<>(alertsHistory); }
    
    // Setters
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    
    @Override
    public String toString() {
        return String.format("AgentInfo[id=%s, ip=%s, online=%s, lastContact=%s, metrics=%d, alerts=%d]",
                agentId, ipAddress, isOnline(), lastContact, metricsHistory.size(), alertsHistory.size());
    }
}
