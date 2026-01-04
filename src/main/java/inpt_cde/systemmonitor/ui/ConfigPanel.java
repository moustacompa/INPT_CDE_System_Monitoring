package inpt_cde.systemmonitor.ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Panneau de configuration des seuils d'alerte
 */
class ConfigPanel extends JPanel {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSpinner cpuCriticalSpinner;
    private JSpinner cpuWarningSpinner;
    private JSpinner memoryCriticalSpinner;
    private JSpinner memoryWarningSpinner;
    private JSpinner diskCriticalSpinner;
    private JSpinner diskWarningSpinner;
    private JCheckBox emailNotificationCheckBox;
    private JTextField emailField;
    
    public ConfigPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        mainPanel.add(createThresholdsPanel());
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(createNotificationsPanel());
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(createActionsPanel());
        
        add(mainPanel, BorderLayout.NORTH);
    }
    
    private JPanel createThresholdsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Seuils d'alerte (%)",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // En-têtes
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(new JLabel("Critique"), gbc);
        gbc.gridx = 2;
        panel.add(new JLabel("Avertissement"), gbc);
        
        // CPU
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("CPU:"), gbc);
        
        gbc.gridx = 1;
        cpuCriticalSpinner = new JSpinner(new SpinnerNumberModel(85, 0, 100, 1));
        panel.add(cpuCriticalSpinner, gbc);
        
        gbc.gridx = 2;
        cpuWarningSpinner = new JSpinner(new SpinnerNumberModel(70, 0, 100, 1));
        panel.add(cpuWarningSpinner, gbc);
        
        // Mémoire
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Mémoire:"), gbc);
        
        gbc.gridx = 1;
        memoryCriticalSpinner = new JSpinner(new SpinnerNumberModel(90, 0, 100, 1));
        panel.add(memoryCriticalSpinner, gbc);
        
        gbc.gridx = 2;
        memoryWarningSpinner = new JSpinner(new SpinnerNumberModel(75, 0, 100, 1));
        panel.add(memoryWarningSpinner, gbc);
        
        // Disque
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Disque:"), gbc);
        
        gbc.gridx = 1;
        diskCriticalSpinner = new JSpinner(new SpinnerNumberModel(95, 0, 100, 1));
        panel.add(diskCriticalSpinner, gbc);
        
        gbc.gridx = 2;
        diskWarningSpinner = new JSpinner(new SpinnerNumberModel(80, 0, 100, 1));
        panel.add(diskWarningSpinner, gbc);
        
        return panel;
    }
    
    private JPanel createNotificationsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Notifications",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        
        emailNotificationCheckBox = new JCheckBox("Activer les notifications par email");
        emailNotificationCheckBox.addActionListener(e -> 
            emailField.setEnabled(emailNotificationCheckBox.isSelected()));
        
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel.add(new JLabel("Email:"));
        emailField = new JTextField(30);
        emailField.setEnabled(false);
        emailPanel.add(emailField);
        
        panel.add(emailNotificationCheckBox);
        panel.add(emailPanel);
        
        return panel;
    }
    
    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton saveBtn = new JButton("Enregistrer");
        saveBtn.addActionListener(e -> saveConfiguration());
        
        JButton resetBtn = new JButton("Réinitialiser");
        resetBtn.addActionListener(e -> resetToDefaults());
        
        panel.add(resetBtn);
        panel.add(saveBtn);
        
        return panel;
    }
    
    public void refresh(MonitoringController controller) {
        // TODO: Charger la configuration depuis le serveur
    }
    
    private void saveConfiguration() {
        // TODO: Enregistrer la configuration via RMI
        JOptionPane.showMessageDialog(this,
            "Configuration enregistrée avec succès",
            "Succès",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void resetToDefaults() {
        cpuCriticalSpinner.setValue(85);
        cpuWarningSpinner.setValue(70);
        memoryCriticalSpinner.setValue(90);
        memoryWarningSpinner.setValue(75);
        diskCriticalSpinner.setValue(95);
        diskWarningSpinner.setValue(80);
        emailNotificationCheckBox.setSelected(false);
        emailField.setText("");
        emailField.setEnabled(false);
    }
}

/**
 * Panneau d'historique et statistiques
 */
class HistoryPanel extends JPanel {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable historyTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> agentSelector;
    private JComboBox<String> metricSelector;
    
    public HistoryPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        add(createFilterPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }
    
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        panel.add(new JLabel("Agent:"));
        agentSelector = new JComboBox<>(new String[]{
            "Tous", "Agent-001", "Agent-002", "Agent-003"
        });
        panel.add(agentSelector);
        
        panel.add(new JLabel("Métrique:"));
        metricSelector = new JComboBox<>(new String[]{
            "Toutes", "CPU", "Mémoire", "Disque"
        });
        panel.add(metricSelector);
        
        JButton searchBtn = new JButton("Rechercher");
        panel.add(searchBtn);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"Date/Heure", "Agent", "Métrique", "Valeur", "Statut"};
        tableModel = new DefaultTableModel(columns, 0);
        historyTable = new JTable(tableModel);
        
        JScrollPane scrollPane = new JScrollPane(historyTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    public void refresh(MonitoringController controller) {
        // TODO: Charger l'historique depuis le serveur
    }
}

/**
 * Contrôleur pour la communication RMI avec le serveur
 */
class MonitoringController {
    
    private Registry registry;
    private Object monitoringService; // Interface RMI à définir
    private final String host;
    private final int port;
    
    public MonitoringController() throws Exception {
        this("localhost", 1099);
    }
    
    public MonitoringController(String host, int port) throws Exception {
        this.host = host;
        this.port = port;
        connect();
    }
    
    private void connect() throws Exception {
        try {
            registry = LocateRegistry.getRegistry(host, port);
            
            // TODO: Récupérer l'interface RMI du serveur
            // Exemple: monitoringService = registry.lookup("MonitoringService");
            
            System.out.println("Connecté au serveur RMI: " + host + ":" + port);
            
        } catch (Exception e) {
            throw new Exception("Impossible de se connecter au serveur RMI: " + e.getMessage(), e);
        }
    }
    
    /**
     * Récupère la liste de tous les agents
     */
    public java.util.List<?> getAllAgents() throws Exception {
        // TODO: Appel RMI
        // return monitoringService.getAllAgents();
        return new java.util.ArrayList<>();
    }
    
    /**
     * Récupère les métriques d'un agent
     */
    public java.util.List<?> getAgentMetrics(String agentId, long startTime, long endTime) throws Exception {
        // TODO: Appel RMI
        return new java.util.ArrayList<>();
    }
    
    /**
     * Récupère les alertes actives
     */
    public java.util.List<?> getActiveAlerts() throws Exception {
        // TODO: Appel RMI
        return new java.util.ArrayList<>();
    }
    
    /**
     * Récupère les alertes récentes
     */
    public java.util.List<?> getRecentAlerts() throws Exception {
        // TODO: Appel RMI
        return new java.util.ArrayList<>();
    }
    
    /**
     * Met à jour le statut d'une alerte
     */
    public void updateAlertStatus(String alertId, String status) throws Exception {
        // TODO: Appel RMI
    }
    
    /**
     * Récupère la configuration des seuils
     */
    public Object getThresholdConfiguration() throws Exception {
        // TODO: Appel RMI
        return null;
    }
    
    /**
     * Met à jour la configuration des seuils
     */
    public void updateThresholdConfiguration(Object config) throws Exception {
        // TODO: Appel RMI
    }
    
    /**
     * Récupère l'historique des métriques
     */
    public java.util.List<?> getMetricsHistory(String agentId, String metricType, 
                                                long startTime, long endTime) throws Exception {
        // TODO: Appel RMI
        return new java.util.ArrayList<>();
    }
    
    /**
     * Exporte les données
     */
    public void exportData(String format, String filePath) throws Exception {
        // TODO: Appel RMI ou traitement local
    }
    
    public void disconnect() {
        // Nettoyage si nécessaire
        System.out.println("Déconnecté du serveur RMI");
    }
}

/**
 * Dialogue de détails d'un agent
 */
class AgentDetailsDialog extends JDialog {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AgentDetailsDialog(Window owner, AgentData agent) {
        super(owner, "Détails de l'agent: " + agent.id, ModalityType.APPLICATION_MODAL);
        setSize(500, 400);
        setLocationRelativeTo(owner);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Informations générales
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informations"));
        
        infoPanel.add(new JLabel("ID:"));
        infoPanel.add(new JLabel(agent.id));
        
        infoPanel.add(new JLabel("Nom:"));
        infoPanel.add(new JLabel(agent.name));
        
        infoPanel.add(new JLabel("Adresse IP:"));
        infoPanel.add(new JLabel(agent.ipAddress));
        
        infoPanel.add(new JLabel("Statut:"));
        infoPanel.add(new JLabel(agent.status));
        
        infoPanel.add(new JLabel("Dernière MAJ:"));
        infoPanel.add(new JLabel(agent.lastUpdate));
        
        // Métriques actuelles
        JPanel metricsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        metricsPanel.setBorder(BorderFactory.createTitledBorder("Métriques actuelles"));
        
        metricsPanel.add(new JLabel("CPU:"));
        metricsPanel.add(new JLabel(String.format("%.1f%%", agent.cpu)));
        
        metricsPanel.add(new JLabel("Mémoire:"));
        metricsPanel.add(new JLabel(String.format("%.1f%%", agent.memory)));
        
        metricsPanel.add(new JLabel("Disque:"));
        metricsPanel.add(new JLabel(String.format("%.1f%%", agent.disk)));
        
        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        contentPanel.add(infoPanel);
        contentPanel.add(metricsPanel);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        // Bouton fermer
        JButton closeBtn = new JButton("Fermer");
        closeBtn.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(panel);
    }
}

/**
 * Dialogue de gestion des utilisateurs
 */
class UserManagementDialog extends JDialog {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserManagementDialog(Window owner) {
        super(owner, "Gestion des utilisateurs", ModalityType.APPLICATION_MODAL);
        setSize(600, 400);
        setLocationRelativeTo(owner);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Table des utilisateurs
        String[] columns = {"Nom d'utilisateur", "Email", "Rôle", "Statut"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        
        // Données de test
        tableModel.addRow(new Object[]{"admin", "admin@inpt.ma", "Administrateur", "Actif"});
        tableModel.addRow(new Object[]{"operator", "operator@inpt.ma", "Opérateur", "Actif"});
        
        JTable userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        
        // Boutons d'action
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton addBtn = new JButton("Ajouter");
        JButton editBtn = new JButton("Modifier");
        JButton deleteBtn = new JButton("Supprimer");
        JButton closeBtn = new JButton("Fermer");
        closeBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(closeBtn);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(panel);
    }
}

/**
 * Dialogue de configuration des seuils
 */
class ConfigDialog extends JDialog {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfigDialog(Window owner) {
        super(owner, "Configuration", ModalityType.APPLICATION_MODAL);
        setSize(400, 300);
        setLocationRelativeTo(owner);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel label = new JLabel("Options de configuration avancées");
        panel.add(label, BorderLayout.CENTER);
        
        JButton closeBtn = new JButton("Fermer");
        closeBtn.addActionListener(e -> dispose());
        panel.add(closeBtn, BorderLayout.SOUTH);
        
        setContentPane(panel);
    }
}