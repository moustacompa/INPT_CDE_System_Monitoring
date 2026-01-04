package inpt_cde.systemmonitor.ui;

import javax.swing.*;
import javax.swing.border.*;

import inpt_cde.systemmonitor.model.*;

import java.awt.*;

/**
 * Panneau principal affichant une vue d'ensemble du système
 */
public class DashboardPanel extends JPanel {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel totalAgentsLabel;
    private JLabel activeAgentsLabel;
    private JLabel criticalAlertsLabel;
    private JLabel avgCpuLabel;
    private JLabel avgMemoryLabel;
    
    private JPanel agentCardsPanel;
    private JPanel alertsPanel;
    private JTextArea recentActivityArea;
    
    public DashboardPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Panneau supérieur avec statistiques globales
        add(createStatsPanel(), BorderLayout.NORTH);
        
        // Panneau central avec cartes des agents
        add(createAgentsPanel(), BorderLayout.CENTER);
        
        // Panneau droit avec alertes et activité
        add(createSidePanel(), BorderLayout.EAST);
    }
    
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        statsPanel.setPreferredSize(new Dimension(0, 120));
        
        // Carte Total Agents
        totalAgentsLabel = new JLabel("0", SwingConstants.CENTER);
        statsPanel.add(createStatCard("Total Agents", totalAgentsLabel, new Color(52, 152, 219)));
        
        // Carte Agents Actifs
        activeAgentsLabel = new JLabel("0", SwingConstants.CENTER);
        statsPanel.add(createStatCard("Agents Actifs", activeAgentsLabel, new Color(46, 204, 113)));
        
        // Carte Alertes Critiques
        criticalAlertsLabel = new JLabel("0", SwingConstants.CENTER);
        statsPanel.add(createStatCard("Alertes Critiques", criticalAlertsLabel, new Color(231, 76, 60)));
        
        // Carte CPU Moyen
        avgCpuLabel = new JLabel("0%", SwingConstants.CENTER);
        statsPanel.add(createStatCard("CPU Moyen", avgCpuLabel, new Color(155, 89, 182)));
        
        // Carte Mémoire Moyenne
        avgMemoryLabel = new JLabel("0%", SwingConstants.CENTER);
        statsPanel.add(createStatCard("Mémoire Moyenne", avgMemoryLabel, new Color(243, 156, 18)));
        
        return statsPanel;
    }
    
    private JPanel createStatCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color, 2, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(Color.DARK_GRAY);
        
        valueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valueLabel.setForeground(color);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createAgentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Agents Connectés",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        
        agentCardsPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        agentCardsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(agentCardsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel(new BorderLayout(0, 10));
        sidePanel.setPreferredSize(new Dimension(300, 0));
        
        // Panneau des alertes récentes
        alertsPanel = new JPanel();
        alertsPanel.setLayout(new BoxLayout(alertsPanel, BoxLayout.Y_AXIS));
        alertsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Alertes Récentes",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12)
        ));
        
        JScrollPane alertsScroll = new JScrollPane(alertsPanel);
        alertsScroll.setPreferredSize(new Dimension(0, 250));
        
        // Zone d'activité récente
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Activité Récente",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12)
        ));
        
        recentActivityArea = new JTextArea();
        recentActivityArea.setEditable(false);
        recentActivityArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        recentActivityArea.setLineWrap(true);
        recentActivityArea.setWrapStyleWord(true);
        
        JScrollPane activityScroll = new JScrollPane(recentActivityArea);
        activityPanel.add(activityScroll, BorderLayout.CENTER);
        
        sidePanel.add(alertsScroll, BorderLayout.NORTH);
        sidePanel.add(activityPanel, BorderLayout.CENTER);
        
        return sidePanel;
    }
    
    private JPanel createAgentCard(int agentId, double cpu, double memory, double disk, String status) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(getStatusColor(status), 2, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);
        
        // En-tête avec ID et statut
        JPanel header = new JPanel(new BorderLayout());
        JLabel idLabel = new JLabel("Agent " + agentId);
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel statusLabel = new JLabel(status);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        statusLabel.setForeground(getStatusColor(status));
        
        header.add(idLabel, BorderLayout.WEST);
        header.add(statusLabel, BorderLayout.EAST);
        
        // Métriques
        JPanel metricsPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        metricsPanel.setOpaque(false);
        
        metricsPanel.add(createMetricBar("CPU", cpu, Color.BLUE));
        metricsPanel.add(createMetricBar("RAM", memory, Color.GREEN));
        metricsPanel.add(createMetricBar("Disque", disk, Color.ORANGE));
        
        card.add(header, BorderLayout.NORTH);
        card.add(metricsPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createMetricBar(String label, double value, Color color) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(label + ":");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        nameLabel.setPreferredSize(new Dimension(60, 20));
        
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue((int) value);
        bar.setString(String.format("%.1f%%", value));
        bar.setStringPainted(true);
        bar.setForeground(color);
        
        panel.add(nameLabel, BorderLayout.WEST);
        panel.add(bar, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAlertItem(String agentId, String message, String type) {
        JPanel item = new JPanel(new BorderLayout(5, 5));
        item.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            new EmptyBorder(5, 5, 5, 5)
        ));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        Color alertColor = type.equals("CRITICAL") ? Color.RED : 
                          type.equals("WARNING") ? Color.ORANGE : Color.BLUE;
        
        JLabel iconLabel = new JLabel("⚠");
        iconLabel.setFont(new Font("Arial", Font.BOLD, 20));
        iconLabel.setForeground(alertColor);
        
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        
        JLabel agentLabel = new JLabel(agentId);
        agentLabel.setFont(new Font("Arial", Font.BOLD, 11));
        
        JLabel msgLabel = new JLabel("<html>" + message + "</html>");
        msgLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        msgLabel.setForeground(Color.DARK_GRAY);
        
        textPanel.add(agentLabel, BorderLayout.NORTH);
        textPanel.add(msgLabel, BorderLayout.CENTER);
        
        item.add(iconLabel, BorderLayout.WEST);
        item.add(textPanel, BorderLayout.CENTER);
        
        return item;
    }
    
    private Color getStatusColor(String status) {
        switch (status.toUpperCase()) {
            case "ACTIF":
            case "OK":
                return new Color(46, 204, 113);
            case "ALERTE":
            case "WARNING":
                return new Color(243, 156, 18);
            case "CRITIQUE":
            case "CRITICAL":
                return new Color(231, 76, 60);
            default:
                return Color.GRAY;
        }
    }
    
    public void refresh(MonitoringController controller) {
        try {
            // Mettre à jour les statistiques globales
            updateGlobalStats(controller);
            
            // Mettre à jour les cartes des agents
            updateAgentCards(controller);
            
            // Mettre à jour les alertes
            updateAlerts(controller);
            
            // Mettre à jour l'activité récente
            updateRecentActivity(controller);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void updateGlobalStats(MonitoringController controller) throws Exception {
        SystemStatistics stats = controller.getSystemStatistics();
        
        totalAgentsLabel.setText(String.valueOf(stats.getTotalAgents()));
        activeAgentsLabel.setText(String.valueOf(stats.getActiveAgents()));
        criticalAlertsLabel.setText(String.valueOf(stats.getCriticalAlerts()));
        avgCpuLabel.setText(String.format("%.1f%%", stats.getAverageCpu()));
        avgMemoryLabel.setText(String.format("%.1f%%", stats.getAverageMemory()));
    }
    
    private void updateAgentCards(MonitoringController controller) throws Exception {
        agentCardsPanel.removeAll();
        
        // TODO: Récupérer les agents via RMI
        // List<Agent> agents = controller.getAllAgents();
        
        // Exemple de données de test
        agentCardsPanel.add(createAgentCard(1, 45.5, 62.3, 78.1, "ACTIF"));
        agentCardsPanel.add(createAgentCard(2, 88.2, 91.5, 45.2, "CRITIQUE"));
        agentCardsPanel.add(createAgentCard(3, 32.1, 48.7, 55.9, "ACTIF"));
        agentCardsPanel.add(createAgentCard(4, 65.4, 72.8, 82.3, "ALERTE"));
        agentCardsPanel.add(createAgentCard(5, 25.8, 35.2, 41.5, "ACTIF"));
        
        agentCardsPanel.revalidate();
        agentCardsPanel.repaint();
    }
    
    private void updateAlerts(MonitoringController controller) throws Exception {
        alertsPanel.removeAll();
        
        java.util.List<Alert> alerts = controller.getRecentAlerts(5);
        
        for (Alert alert : alerts) {
            String severityStr = alert.getSeverity() == 3 ? "CRITICAL" :
                               alert.getSeverity() == 2 ? "WARNING" : "INFO";
            alertsPanel.add(createAlertItem("Agent", alert.getMessage(), severityStr));
        }
        
        alertsPanel.revalidate();
        alertsPanel.repaint();
    }
    
    private void updateRecentActivity(MonitoringController controller) throws Exception {
        // TODO: Récupérer l'activité via RMI
        StringBuilder activity = new StringBuilder();
        activity.append("[12:45:32] Agent-002 connecté\n");
        activity.append("[12:46:15] Alerte CPU sur Agent-002\n");
        activity.append("[12:47:03] Agent-005 mis à jour\n");
        activity.append("[12:48:21] Configuration modifiée\n");
        activity.append("[12:49:45] Export des données\n");
        
        recentActivityArea.setText(activity.toString());
    }
}