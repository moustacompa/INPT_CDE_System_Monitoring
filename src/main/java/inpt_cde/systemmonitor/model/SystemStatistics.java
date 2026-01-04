package inpt_cde.systemmonitor.model;

import java.io.Serializable;

/**
 * Classe pour les statistiques globales du système
 * Utilisée par RMI pour retourner les stats à l'UI
 */
public class SystemStatistics implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int totalAgents;
    private int activeAgents;
    private int inactiveAgents;
    
    private int totalAlerts;
    private int criticalAlerts;  // Severity 3
    private int warningAlerts;   // Severity 2
    private int infoAlerts;      // Severity 1
    
    private double averageCpu;
    private double averageMemory;
    private double averageDisk;
    
    public SystemStatistics() {}
    
    // Getters et Setters
    
    public int getTotalAgents() {
        return totalAgents;
    }
    
    public void setTotalAgents(int totalAgents) {
        this.totalAgents = totalAgents;
    }
    
    public int getActiveAgents() {
        return activeAgents;
    }
    
    public void setActiveAgents(int activeAgents) {
        this.activeAgents = activeAgents;
    }
    
    public int getInactiveAgents() {
        return inactiveAgents;
    }
    
    public void setInactiveAgents(int inactiveAgents) {
        this.inactiveAgents = inactiveAgents;
    }
    
    public int getTotalAlerts() {
        return totalAlerts;
    }
    
    public void setTotalAlerts(int totalAlerts) {
        this.totalAlerts = totalAlerts;
    }
    
    public int getCriticalAlerts() {
        return criticalAlerts;
    }
    
    public void setCriticalAlerts(int criticalAlerts) {
        this.criticalAlerts = criticalAlerts;
    }
    
    public int getWarningAlerts() {
        return warningAlerts;
    }
    
    public void setWarningAlerts(int warningAlerts) {
        this.warningAlerts = warningAlerts;
    }
    
    public int getInfoAlerts() {
        return infoAlerts;
    }
    
    public void setInfoAlerts(int infoAlerts) {
        this.infoAlerts = infoAlerts;
    }
    
    public double getAverageCpu() {
        return averageCpu;
    }
    
    public void setAverageCpu(double averageCpu) {
        this.averageCpu = averageCpu;
    }
    
    public double getAverageMemory() {
        return averageMemory;
    }
    
    public void setAverageMemory(double averageMemory) {
        this.averageMemory = averageMemory;
    }
    
    public double getAverageDisk() {
        return averageDisk;
    }
    
    public void setAverageDisk(double averageDisk) {
        this.averageDisk = averageDisk;
    }
    
    @Override
    public String toString() {
        return "SystemStatistics{" +
                "totalAgents=" + totalAgents +
                ", activeAgents=" + activeAgents +
                ", criticalAlerts=" + criticalAlerts +
                ", avgCpu=" + averageCpu +
                '}';
    }
}