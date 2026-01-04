package inpt_cde.systemmonitor.ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Panneau de gestion et affichage des alertes système
 */
public class AlertPanel extends JPanel {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable alertTable;
    private AlertTableModel tableModel;
    private JComboBox<String> severityFilter;
    private JComboBox<String> statusFilter;
    private JLabel countLabel;
    private JPanel alertDetailsPanel;
    private TableRowSorter<AlertTableModel> sorter;
    
    public AlertPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        add(createFilterPanel(), BorderLayout.NORTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createTablePanel());
        splitPane.setRightComponent(createDetailsPanel());
        splitPane.setDividerLocation(700);
        
        add(splitPane, BorderLayout.CENTER);
        add(createActionsPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // Filtre par sévérité
        JLabel severityLabel = new JLabel("Sévérité:");
        severityFilter = new JComboBox<>(new String[]{
            "Toutes", "INFO", "WARNING", "CRITICAL"
        });
        severityFilter.addActionListener(e -> filterAlerts());
        
        // Filtre par statut
        JLabel statusLabel = new JLabel("Statut:");
        statusFilter = new JComboBox<>(new String[]{
            "Tous", "Nouvelle", "En cours", "Résolue", "Ignorée"
        });
        statusFilter.addActionListener(e -> filterAlerts());
        
        JButton refreshBtn = new JButton("Actualiser");
        refreshBtn.addActionListener(e -> refresh(null));
        
        JButton clearBtn = new JButton("Effacer résolues");
        clearBtn.addActionListener(e -> clearResolvedAlerts());
        
        filterPanel.add(severityLabel);
        filterPanel.add(severityFilter);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(statusLabel);
        filterPanel.add(statusFilter);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(refreshBtn);
        filterPanel.add(clearBtn);
        
        countLabel = new JLabel("0 alerte(s)");
        countLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        panel.add(filterPanel, BorderLayout.WEST);
        panel.add(countLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Liste des alertes"));
        
        tableModel = new AlertTableModel();
        alertTable = new JTable(tableModel);
        
        alertTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        alertTable.setRowHeight(35);
        alertTable.getTableHeader().setReorderingAllowed(false);
        
        // Configuration des colonnes
        configureColumns();
        
        // Tri
        sorter = new TableRowSorter<>(tableModel);
        alertTable.setRowSorter(sorter);
        
        // Renderer personnalisé
        alertTable.setDefaultRenderer(Object.class, new AlertCellRenderer());
        
        // Listener pour sélection
        alertTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                displayAlertDetails();
            }
        });
        
        // Menu contextuel
        createContextMenu();
        
        JScrollPane scrollPane = new JScrollPane(alertTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void configureColumns() {
        TableColumnModel columnModel = alertTable.getColumnModel();
        
        columnModel.getColumn(0).setPreferredWidth(40);  // Icône
        columnModel.getColumn(1).setPreferredWidth(80);  // Sévérité
        columnModel.getColumn(2).setPreferredWidth(100); // Agent
        columnModel.getColumn(3).setPreferredWidth(250); // Message
        columnModel.getColumn(4).setPreferredWidth(120); // Date/Heure
        columnModel.getColumn(5).setPreferredWidth(80);  // Statut
    }
    
    private void createContextMenu() {
        JPopupMenu contextMenu = new JPopupMenu();
        
        JMenuItem markResolvedItem = new JMenuItem("Marquer comme résolue");
        markResolvedItem.addActionListener(e -> markAlertAsResolved());
        contextMenu.add(markResolvedItem);
        
        JMenuItem markIgnoredItem = new JMenuItem("Ignorer");
        markIgnoredItem.addActionListener(e -> markAlertAsIgnored());
        contextMenu.add(markIgnoredItem);
        
        contextMenu.addSeparator();
        
        JMenuItem viewAgentItem = new JMenuItem("Voir l'agent");
        viewAgentItem.addActionListener(e -> viewAgent());
        contextMenu.add(viewAgentItem);
        
        JMenuItem deleteItem = new JMenuItem("Supprimer");
        deleteItem.addActionListener(e -> deleteAlert());
        contextMenu.add(deleteItem);
        
        alertTable.setComponentPopupMenu(contextMenu);
    }
    
    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Détails de l'alerte"));
        panel.setPreferredSize(new Dimension(350, 0));
        
        alertDetailsPanel = new JPanel();
        alertDetailsPanel.setLayout(new BoxLayout(alertDetailsPanel, BoxLayout.Y_AXIS));
        alertDetailsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(alertDetailsPanel);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Boutons d'action
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton resolveBtn = new JButton("Résoudre");
        resolveBtn.addActionListener(e -> markAlertAsResolved());
        
        JButton ignoreBtn = new JButton("Ignorer");
        ignoreBtn.addActionListener(e -> markAlertAsIgnored());
        
        buttonPanel.add(resolveBtn);
        buttonPanel.add(ignoreBtn);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton exportBtn = new JButton("Exporter");
        exportBtn.addActionListener(e -> exportAlerts());
        
        JButton configBtn = new JButton("Configuration");
        configBtn.addActionListener(e -> openConfiguration());
        
        panel.add(exportBtn);
        panel.add(configBtn);
        
        return panel;
    }
    
    private void displayAlertDetails() {
        int selectedRow = alertTable.getSelectedRow();
        if (selectedRow == -1) {
            alertDetailsPanel.removeAll();
            alertDetailsPanel.revalidate();
            alertDetailsPanel.repaint();
            return;
        }
        
        int modelRow = alertTable.convertRowIndexToModel(selectedRow);
        AlertData alert = tableModel.getAlertAt(modelRow);
        
        alertDetailsPanel.removeAll();
        
        // Icône et sévérité
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel iconLabel = new JLabel(getSeverityIcon(alert.severity));
        iconLabel.setFont(new Font("Arial", Font.BOLD, 32));
        JLabel severityLabel = new JLabel(alert.severity);
        severityLabel.setFont(new Font("Arial", Font.BOLD, 18));
        severityLabel.setForeground(getSeverityColor(alert.severity));
        headerPanel.add(iconLabel);
        headerPanel.add(severityLabel);
        alertDetailsPanel.add(headerPanel);
        
        alertDetailsPanel.add(Box.createVerticalStrut(10));
        alertDetailsPanel.add(createDetailField("Agent", alert.agentId));
        alertDetailsPanel.add(createDetailField("Message", alert.message));
        alertDetailsPanel.add(createDetailField("Date/Heure", alert.timestamp));
        alertDetailsPanel.add(createDetailField("Statut", alert.status));
        
        if (alert.details != null && !alert.details.isEmpty()) {
            alertDetailsPanel.add(Box.createVerticalStrut(10));
            alertDetailsPanel.add(createSectionLabel("Détails supplémentaires"));
            JTextArea detailsArea = new JTextArea(alert.details);
            detailsArea.setEditable(false);
            detailsArea.setLineWrap(true);
            detailsArea.setWrapStyleWord(true);
            detailsArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            detailsArea.setBackground(new Color(245, 245, 245));
            alertDetailsPanel.add(detailsArea);
        }
        
        alertDetailsPanel.revalidate();
        alertDetailsPanel.repaint();
    }
    
    private JPanel createDetailField(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel nameLabel = new JLabel(label + ":");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nameLabel.setPreferredSize(new Dimension(100, 20));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panel.add(nameLabel, BorderLayout.WEST);
        panel.add(valueLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private void filterAlerts() {
        String severity = (String) severityFilter.getSelectedItem();
        String status = (String) statusFilter.getSelectedItem();
        
        List<RowFilter<AlertTableModel, Integer>> filters = new ArrayList<>();
        
        if (!"Toutes".equals(severity)) {
            filters.add(RowFilter.regexFilter(severity, 1));
        }
        
        if (!"Tous".equals(status)) {
            filters.add(RowFilter.regexFilter(status, 5));
        }
        
        if (filters.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.andFilter(filters));
        }
        
        updateCount();
    }
    
    private void updateCount() {
        int total = tableModel.getRowCount();
        int visible = alertTable.getRowCount();
        
        if (visible == total) {
            countLabel.setText(total + " alerte(s)");
        } else {
            countLabel.setText(visible + " / " + total + " alerte(s)");
        }
    }
    
    public void refresh(MonitoringController controller) {
        try {
            // TODO: Récupérer les alertes via RMI
            
            // Données de test
            List<AlertData> testData = new ArrayList<>();
            testData.add(new AlertData("CRITICAL", "Agent-002", "CPU critique: 88%", 
                "12:45:32", "Nouvelle", "Utilisation CPU au-dessus du seuil de 85%"));
            testData.add(new AlertData("WARNING", "Agent-002", "Mémoire élevée: 91%", 
                "12:46:15", "Nouvelle", "Utilisation mémoire au-dessus du seuil de 80%"));
            testData.add(new AlertData("WARNING", "Agent-004", "Disque saturé: 82%", 
                "12:48:21", "En cours", "Espace disque limité"));
            testData.add(new AlertData("INFO", "Agent-005", "Agent redémarré", 
                "12:50:10", "Résolue", "Redémarrage automatique effectué"));
            testData.add(new AlertData("CRITICAL", "Agent-001", "Perte de connexion", 
                "12:52:45", "Nouvelle", "L'agent ne répond plus depuis 5 minutes"));
            
            tableModel.setData(testData);
            updateCount();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void markAlertAsResolved() {
        updateAlertStatus("Résolue");
    }
    
    private void markAlertAsIgnored() {
        updateAlertStatus("Ignorée");
    }
    
    private void updateAlertStatus(String newStatus) {
        int selectedRow = alertTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        int modelRow = alertTable.convertRowIndexToModel(selectedRow);
        AlertData alert = tableModel.getAlertAt(modelRow);
        alert.status = newStatus;
        
        tableModel.fireTableRowsUpdated(modelRow, modelRow);
        displayAlertDetails();
    }
    
    private void viewAgent() {
        int selectedRow = alertTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        // TODO: Ouvrir la vue de l'agent
        JOptionPane.showMessageDialog(this,
            "Affichage de l'agent...",
            "Information",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteAlert() {
        int selectedRow = alertTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Supprimer cette alerte?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(alertTable.convertRowIndexToModel(selectedRow));
            updateCount();
        }
    }
    
    private void clearResolvedAlerts() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Supprimer toutes les alertes résolues?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeResolvedAlerts();
            updateCount();
        }
    }
    
    private void exportAlerts() {
        JOptionPane.showMessageDialog(this,
            "Export des alertes en cours...",
            "Information",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void openConfiguration() {
        ConfigDialog dialog = new ConfigDialog(SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
    }
    
    private String getSeverityIcon(String severity) {
        switch (severity) {
            case "CRITICAL": return "🔴";
            case "WARNING": return "⚠️";
            case "INFO": return "ℹ️";
            default: return "•";
        }
    }
    
    private Color getSeverityColor(String severity) {
        switch (severity) {
            case "CRITICAL": return new Color(231, 76, 60);
            case "WARNING": return new Color(243, 156, 18);
            case "INFO": return new Color(52, 152, 219);
            default: return Color.GRAY;
        }
    }
}

/**
 * Modèle de table pour les alertes
 */
class AlertTableModel extends AbstractTableModel {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String[] columnNames = {
        "", "Sévérité", "Agent", "Message", "Date/Heure", "Statut"
    };
    
    private List<AlertData> alerts = new ArrayList<>();
    
    public void setData(List<AlertData> data) {
        this.alerts = data;
        fireTableDataChanged();
    }
    
    public void removeRow(int row) {
        alerts.remove(row);
        fireTableRowsDeleted(row, row);
    }
    
    public void removeResolvedAlerts() {
        alerts.removeIf(alert -> "Résolue".equals(alert.status));
        fireTableDataChanged();
    }
    
    public AlertData getAlertAt(int row) {
        return alerts.get(row);
    }
    
    @Override
    public int getRowCount() {
        return alerts.size();
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        AlertData alert = alerts.get(row);
        switch (col) {
            case 0: return getSeverityIcon(alert.severity);
            case 1: return alert.severity;
            case 2: return alert.agentId;
            case 3: return alert.message;
            case 4: return alert.timestamp;
            case 5: return alert.status;
            default: return null;
        }
    }
    
    private String getSeverityIcon(String severity) {
        switch (severity) {
            case "CRITICAL": return "🔴";
            case "WARNING": return "⚠️";
            case "INFO": return "ℹ️";
            default: return "•";
        }
    }
}

/**
 * Classe de données pour une alerte
 */
class AlertData {
    String severity;
    String agentId;
    String message;
    String timestamp;
    String status;
    String details;
    
    public AlertData(String severity, String agentId, String message,
                     String timestamp, String status, String details) {
        this.severity = severity;
        this.agentId = agentId;
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;
        this.details = details;
    }
}

/**
 * Renderer personnalisé pour les cellules d'alerte
 */
class AlertCellRenderer extends DefaultTableCellRenderer {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if (!isSelected && column == 1) {
            String severity = (String) value;
            switch (severity) {
                case "CRITICAL":
                    c.setForeground(new Color(231, 76, 60));
                    break;
                case "WARNING":
                    c.setForeground(new Color(243, 156, 18));
                    break;
                case "INFO":
                    c.setForeground(new Color(52, 152, 219));
                    break;
            }
            setFont(getFont().deriveFont(Font.BOLD));
        }
        
        return c;
    }
}