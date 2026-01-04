package inpt_cde.systemmonitor.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Composants principaux
    private JPanel mainPanel;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private JLabel statusLabel;
    
    // Panneaux de contenu
    private DashboardPanel dashboardPanel;
    private AgentTablePanel agentTablePanel;
    private MetricsChartPanel metricsChartPanel;
    private AlertPanel alertPanel;
    private ConfigPanel configPanel;
    private HistoryPanel historyPanel;
    
    // Controleur RMI
    private MonitoringController controller;
    
    // Timer pour rafraîchissement automatique
    private Timer refreshTimer;
    private int refreshInterval = 5000; // 5 secondes
    
    public MainWindow() {
        initializeUI();
        initializeController();
        startAutoRefresh();
    }
    
    private void initializeUI() {
        setTitle("Système de Surveillance Distribué - INPT");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        
        // Look and Feel moderne
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Création du menu
        createMenuBar();
        
        // Création de la barre d'outils
        createToolBar();
        
        // Panel principal avec BorderLayout
        mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // Initialisation des panneaux
        initializePanels();
        
        // Panel de contenu avec CardLayout pour navigation
        contentPanel = new JPanel(new CardLayout());
        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(agentTablePanel, "agents");
        contentPanel.add(metricsChartPanel, "metrics");
        contentPanel.add(alertPanel, "alerts");
        contentPanel.add(configPanel, "config");
        contentPanel.add(historyPanel, "history");
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Barre de statut
        createStatusBar();
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        
        // Afficher le dashboard par défaut
        showPanel("dashboard");
    }
    
    private void createMenuBar() {
        menuBar = new JMenuBar();
        
        // Menu Fichier
        JMenu fileMenu = new JMenu("Fichier");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        
        JMenuItem exportItem = new JMenuItem("Exporter les données...");
        // Pas d'icône pour éviter NullPointerException
        exportItem.addActionListener(e -> exportData());
        fileMenu.add(exportItem);
        
        fileMenu.addSeparator();
        
        JMenuItem exitItem = new JMenuItem("Quitter");
        exitItem.setMnemonic(KeyEvent.VK_Q);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        exitItem.addActionListener(e -> exitApplication());
        fileMenu.add(exitItem);
        
        // Menu Vue
        JMenu viewMenu = new JMenu("Vue");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        
        JMenuItem dashboardItem = new JMenuItem("Tableau de bord");
        dashboardItem.addActionListener(e -> showPanel("dashboard"));
        viewMenu.add(dashboardItem);
        
        JMenuItem agentsItem = new JMenuItem("Liste des agents");
        agentsItem.addActionListener(e -> showPanel("agents"));
        viewMenu.add(agentsItem);
        
        JMenuItem metricsItem = new JMenuItem("Graphiques");
        metricsItem.addActionListener(e -> showPanel("metrics"));
        viewMenu.add(metricsItem);
        
        JMenuItem alertsItem = new JMenuItem("Alertes");
        alertsItem.addActionListener(e -> showPanel("alerts"));
        viewMenu.add(alertsItem);
        
        JMenuItem historyItem = new JMenuItem("Historique");
        historyItem.addActionListener(e -> showPanel("history"));
        viewMenu.add(historyItem);
        
        // Menu Configuration
        JMenu configMenu = new JMenu("Configuration");
        configMenu.setMnemonic(KeyEvent.VK_C);
        
        JMenuItem thresholdsItem = new JMenuItem("Seuils d'alerte");
        thresholdsItem.addActionListener(e -> showPanel("config"));
        configMenu.add(thresholdsItem);
        
        JMenuItem refreshItem = new JMenuItem("Intervalle de rafraîchissement...");
        refreshItem.addActionListener(e -> configureRefreshInterval());
        configMenu.add(refreshItem);
        
        JMenuItem usersItem = new JMenuItem("Gestion des utilisateurs");
        usersItem.addActionListener(e -> openUserManagement());
        configMenu.add(usersItem);
        
        // Menu Aide
        JMenu helpMenu = new JMenu("Aide");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        JMenuItem aboutItem = new JMenuItem("À propos");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(configMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void createToolBar() {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        // Bouton Dashboard
        JButton dashboardBtn = createToolBarButton("Dashboard", "/icons/dashboard.png");
        dashboardBtn.addActionListener(e -> showPanel("dashboard"));
        toolBar.add(dashboardBtn);
        
        // Bouton Agents
        JButton agentsBtn = createToolBarButton("Agents", "/icons/agents.png");
        agentsBtn.addActionListener(e -> showPanel("agents"));
        toolBar.add(agentsBtn);
        
        // Bouton Graphiques
        JButton metricsBtn = createToolBarButton("Graphiques", "/icons/chart.png");
        metricsBtn.addActionListener(e -> showPanel("metrics"));
        toolBar.add(metricsBtn);
        
        toolBar.addSeparator();
        
        // Bouton Alertes
        JButton alertsBtn = createToolBarButton("Alertes", "/icons/alert.png");
        alertsBtn.addActionListener(e -> showPanel("alerts"));
        toolBar.add(alertsBtn);
        
        toolBar.addSeparator();
        
        // Bouton Rafraîchir
        JButton refreshBtn = createToolBarButton("Rafraîchir", "/icons/refresh.png");
        refreshBtn.addActionListener(e -> refreshData());
        toolBar.add(refreshBtn);
        
        // Bouton Exporter
        JButton exportBtn = createToolBarButton("Exporter", "/icons/export.png");
        exportBtn.addActionListener(e -> exportData());
        toolBar.add(exportBtn);
        
        mainPanel.add(toolBar, BorderLayout.NORTH);
    }
    
    private JButton createToolBarButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        
        // Les icônes sont optionnelles - on utilise juste le texte
        // Si vous voulez ajouter des icônes plus tard, placez-les dans src/resources/icons/
        
        return button;
    }
    
    private void createStatusBar() {
        statusLabel = new JLabel("Prêt");
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY),
            new EmptyBorder(2, 5, 2, 5)
        ));
    }
    
    private void initializePanels() {
        dashboardPanel = new DashboardPanel();
        agentTablePanel = new AgentTablePanel();
        metricsChartPanel = new MetricsChartPanel();
        alertPanel = new AlertPanel();
        configPanel = new ConfigPanel();
        historyPanel = new HistoryPanel();
    }
    
    private void initializeController() {
        try {
            controller = new MonitoringController();
            updateStatus("Connecté au serveur de surveillance");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Impossible de se connecter au serveur RMI:\n" + e.getMessage(),
                "Erreur de connexion",
                JOptionPane.ERROR_MESSAGE);
            updateStatus("Déconnecté");
        }
    }
    
    private void startAutoRefresh() {
        refreshTimer = new Timer(refreshInterval, e -> refreshData());
        refreshTimer.start();
    }
    
    private void showPanel(String panelName) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, panelName);
        updateStatus("Affichage: " + getPanelTitle(panelName));
    }
    
    private String getPanelTitle(String panelName) {
        switch (panelName) {
            case "dashboard": return "Tableau de bord";
            case "agents": return "Liste des agents";
            case "metrics": return "Graphiques des métriques";
            case "alerts": return "Gestion des alertes";
            case "config": return "Configuration";
            case "history": return "Historique";
            default: return panelName;
        }
    }
    
    private void refreshData() {
        updateStatus("Rafraîchissement des données...");
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                if (controller != null) {
                    dashboardPanel.refresh(controller);
                    agentTablePanel.refresh(controller);
                    metricsChartPanel.refresh(controller);
                    alertPanel.refresh(controller);
                }
                return null;
            }
            
            @Override
            protected void done() {
                updateStatus("Données mises à jour");
            }
        };
        
        worker.execute();
    }
    
    private void exportData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exporter les données");
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            // Implémenter l'export
            updateStatus("Export des données en cours...");
            // TODO: Implémenter la logique d'export
        }
    }
    
    private void configureRefreshInterval() {
        String input = JOptionPane.showInputDialog(this,
            "Intervalle de rafraîchissement (secondes):",
            refreshInterval / 1000);
        
        if (input != null) {
            try {
                int newInterval = Integer.parseInt(input) * 1000;
                if (newInterval >= 1000) {
                    refreshInterval = newInterval;
                    refreshTimer.setDelay(refreshInterval);
                    updateStatus("Intervalle mis à jour: " + (refreshInterval/1000) + "s");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Valeur invalide",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void openUserManagement() {
        UserManagementDialog dialog = new UserManagementDialog(this);
        dialog.setVisible(true);
    }
    
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "Système de Surveillance Distribué\n" +
            "Version 1.0\n\n" +
            "Développé dans le cadre du cours\n" +
            "Distributed Systems\n" +
            "INPT - 2025/2026",
            "À propos",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void updateStatus(String message) {
        statusLabel.setText(message);
    }
    
    private void exitApplication() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Voulez-vous vraiment quitter l'application?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            if (refreshTimer != null) {
                refreshTimer.stop();
            }
            dispose();
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}