package inpt_cde.systemmonitor.pkg_server;

import inpt_cde.systemmonitor.model.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * Interface RMI pour le service de monitoring
 * Adaptée aux VRAIES classes du projet (avec leurs imperfections)
 */
public interface MonitoringServiceInterface extends Remote {
    
    // ===== GESTION DES AGENTS =====
    
    /**
     * Récupère tous les agents enregistrés
     */
    List<Agent> getAllAgents() throws RemoteException;
    
    /**
     * Récupère un agent spécifique par son ID
     */
    Agent getAgent(int agentId) throws RemoteException;
    
    /**
     * Récupère le nombre total d'agents
     */
    int getAgentCount() throws RemoteException;
    
    /**
     * Récupère les agents actifs (isOnline = true)
     */
    List<Agent> getActiveAgents() throws RemoteException;
    
    
    // ===== MÉTRIQUES =====
    
    /**
     * Récupère la dernière métrique pour un agent
     */
    Metric getLatestMetric(int agentId) throws RemoteException;
    
    /**
     * Récupère l'historique des métriques d'un agent
     */
    List<Metric> getMetricsHistory(int agentId, int limit) throws RemoteException;
    
    /**
     * Récupère toutes les dernières métriques de tous les agents
     */
    Map<Integer, Metric> getAllLatestMetrics() throws RemoteException;
    
    
    // ===== ALERTES =====
    
    /**
     * Récupère toutes les alertes
     */
    List<Alert> getAllAlerts() throws RemoteException;
    
    /**
     * Récupère les alertes par sévérité
     * @param severity 1=INFO, 2=WARNING, 3=CRITICAL
     */
    List<Alert> getAlertsBySeverity(int severity) throws RemoteException;
    
    /**
     * Récupère les N dernières alertes
     */
    List<Alert> getRecentAlerts(int limit) throws RemoteException;
    
    /**
     * Compte les alertes par sévérité
     */
    Map<Integer, Integer> getAlertCountBySeverity() throws RemoteException;
    
    
    // ===== UTILISATEURS =====
    
    /**
     * Authentifie un utilisateur
     */
    Utilisateur authenticate(String login, String password) throws RemoteException;
    
    /**
     * Récupère tous les utilisateurs
     */
    List<Utilisateur> getAllUsers() throws RemoteException;
    
    
    // ===== PROFILS =====
    
    /**
     * Récupère un profil par ID
     */
    Profil getProfil(int profilId) throws RemoteException;
    
    /**
     * Récupère tous les profils
     */
    List<Profil> getAllProfils() throws RemoteException;
    
    
    // ===== DROITS =====
    
    /**
     * Récupère tous les droits
     */
    List<Droit> getAllDroits() throws RemoteException;
    
    
    // ===== TRACES (LOGS) =====
    
    /**
     * Récupère les traces récentes
     */
    List<Trace> getRecentTraces(int limit) throws RemoteException;
    
    /**
     * Ajoute une trace (log d'activité)
     */
    void addTrace(String label) throws RemoteException;
    
    
    // ===== TYPES D'ALERTES =====
    
    /**
     * Récupère tous les types d'alertes configurés
     */
    List<TypeAlert> getAllAlertTypes() throws RemoteException;
    
    /**
     * Met à jour le seuil d'un type d'alerte
     */
    void updateAlertThreshold(int typeAlertId, double newThreshold) throws RemoteException;
    
    
    // ===== STATISTIQUES =====
    
    /**
     * Récupère les statistiques globales du système
     */
    SystemStatistics getSystemStatistics() throws RemoteException;
}