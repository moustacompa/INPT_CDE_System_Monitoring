package inpt_cde.systemmonitor.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

/**
 * Panneau affichant la liste détaillée de tous les agents
 * avec filtrage et recherche
 */
public class AgentTablePanel extends JPanel {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable agentTable;
    private AgentTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private JLabel countLabel;
    private TableRowSorter<AgentTableModel> sorter;
    
    public AgentTablePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Panneau supérieur avec recherche et filtres
        add(createSearchPanel(), BorderLayout.NORTH);
        
        // Table des agents
        add(createTablePanel(), BorderLayout.CENTER);
        
        // Panneau inférieur avec actions
        add(createActionsPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Zone de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Rechercher:");
        searchField = new JTextField(20);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable();
            }
        });
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        
        // Filtre par statut
        JLabel filterLabel = new JLabel("Statut:");
        filterComboBox = new JComboBox<>(new String[]{
            "Tous", "Actif", "Inactif", "Alerte", "Critique"
        });
        filterComboBox.addActionListener(e -> filterTable());
        
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(filterLabel);
        searchPanel.add(filterComboBox);
        
        // Bouton actualiser
        JButton refreshBtn = new JButton("Actualiser");
        refreshBtn.addActionListener(e -> refresh(null));
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(refreshBtn);
        
        // Compteur d'agents
        countLabel = new JLabel("0 agent(s)");
        countLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        panel.add(searchPanel, BorderLayout.WEST);
        panel.add(countLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Créer le modèle de table
        tableModel = new AgentTableModel();
        agentTable = new JTable(tableModel);
        
        // Configuration de la table
        agentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        agentTable.setRowHeight(30);
        agentTable.getTableHeader().setReorderingAllowed(false);
        agentTable.setAutoCreateRowSorter(true);
        
        // Configurer les colonnes
        configureColumns();
        
        // Ajouter le tri
        sorter = new TableRowSorter<>(tableModel);
        agentTable.setRowSorter(sorter);
        
        // Renderer personnalisé pour les cellules
        agentTable.setDefaultRenderer(Object.class, new AgentCellRenderer());
        
        // Menu contextuel
        createContextMenu();
        
        JScrollPane scrollPane = new JScrollPane(agentTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void configureColumns() {
        TableColumnModel columnModel = agentTable.getColumnModel();
        
        // ID
        columnModel.getColumn(0).setPreferredWidth(100);
        
        // Nom
        columnModel.getColumn(1).setPreferredWidth(150);
        
        // Adresse IP
        columnModel.getColumn(2).setPreferredWidth(120);
        
        // Statut
        columnModel.getColumn(3).setPreferredWidth(80);
        
        // CPU
        columnModel.getColumn(4).setPreferredWidth(80);
        columnModel.getColumn(4).setCellRenderer(new ProgressBarRenderer());
        
        // Mémoire
        columnModel.getColumn(5).setPreferredWidth(80);
        columnModel.getColumn(5).setCellRenderer(new ProgressBarRenderer());
        
        // Disque
        columnModel.getColumn(6).setPreferredWidth(80);
        columnModel.getColumn(6).setCellRenderer(new ProgressBarRenderer());
        
        // Dernière mise à jour
        columnModel.getColumn(7).setPreferredWidth(140);
    }
    
    private void createContextMenu() {
        JPopupMenu contextMenu = new JPopupMenu();
        
        JMenuItem viewDetailsItem = new JMenuItem("Voir les détails");
        viewDetailsItem.addActionListener(e -> viewAgentDetails());
        contextMenu.add(viewDetailsItem);
        
        JMenuItem viewMetricsItem = new JMenuItem("Voir les graphiques");
        viewMetricsItem.addActionListener(e -> viewAgentMetrics());
        contextMenu.add(viewMetricsItem);
        
        contextMenu.addSeparator();
        
        JMenuItem exportItem = new JMenuItem("Exporter l'agent");
        exportItem.addActionListener(e -> exportAgent());
        contextMenu.add(exportItem);
        
        JMenuItem deleteItem = new JMenuItem("Supprimer");
        deleteItem.addActionListener(e -> deleteAgent());
        contextMenu.add(deleteItem);
        
        agentTable.setComponentPopupMenu(contextMenu);
    }
    
    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton detailsBtn = new JButton("Détails");
        detailsBtn.addActionListener(e -> viewAgentDetails());
        
        JButton metricsBtn = new JButton("Graphiques");
        metricsBtn.addActionListener(e -> viewAgentMetrics());
        
        JButton exportBtn = new JButton("Exporter tout");
        exportBtn.addActionListener(e -> exportAllAgents());
        
        panel.add(detailsBtn);
        panel.add(metricsBtn);
        panel.add(exportBtn);
        
        return panel;
    }
    
    private void filterTable() {
        String searchText = searchField.getText().toLowerCase();
        String statusFilter = (String) filterComboBox.getSelectedItem();
        
        List<RowFilter<AgentTableModel, Integer>> filters = new ArrayList<>();
        
        // Filtre de recherche
        if (!searchText.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + searchText));
        }
        
        // Filtre de statut
        if (!"Tous".equals(statusFilter)) {
            filters.add(RowFilter.regexFilter(statusFilter, 3)); // Colonne statut
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
        int visible = agentTable.getRowCount();
        
        if (visible == total) {
            countLabel.setText(total + " agent(s)");
        } else {
            countLabel.setText(visible + " / " + total + " agent(s)");
        }
    }
    
    public void refresh(MonitoringController controller) {
        try {
            // TODO: Récupérer les agents via RMI
            // List<Agent> agents = controller.getAllAgents();
            // tableModel.setAgents(agents);
            
            // Données de test
            List<AgentData> testData = new ArrayList<>();
            testData.add(new AgentData("AG-001", "Agent-Production-01", "192.168.1.10", "Actif", 45.5, 62.3, 78.1, "12:45:32"));
            testData.add(new AgentData("AG-002", "Agent-Production-02", "192.168.1.11", "Critique", 88.2, 91.5, 45.2, "12:46:15"));
            testData.add(new AgentData("AG-003", "Agent-Dev-01", "192.168.1.20", "Actif", 32.1, 48.7, 55.9, "12:47:03"));
            testData.add(new AgentData("AG-004", "Agent-Test-01", "192.168.1.30", "Alerte", 65.4, 72.8, 82.3, "12:48:21"));
            testData.add(new AgentData("AG-005", "Agent-Backup-01", "192.168.1.40", "Actif", 25.8, 35.2, 41.5, "12:49:45"));
            
            tableModel.setData(testData);
            updateCount();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Erreur lors du rafraîchissement: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void viewAgentDetails() {
        int selectedRow = agentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un agent",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int modelRow = agentTable.convertRowIndexToModel(selectedRow);
        AgentData agent = tableModel.getAgentAt(modelRow);
        
        AgentDetailsDialog dialog = new AgentDetailsDialog(
            SwingUtilities.getWindowAncestor(this),
            agent
        );
        dialog.setVisible(true);
    }
    
    private void viewAgentMetrics() {
        int selectedRow = agentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un agent",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // TODO: Ouvrir le panneau des graphiques pour cet agent
        JOptionPane.showMessageDialog(this,
            "Affichage des graphiques de l'agent...",
            "Information",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exportAgent() {
        int selectedRow = agentTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        // TODO: Implémenter l'export
        JOptionPane.showMessageDialog(this,
            "Export de l'agent en cours...",
            "Information",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exportAllAgents() {
        // TODO: Implémenter l'export de tous les agents
        JOptionPane.showMessageDialog(this,
            "Export de tous les agents en cours...",
            "Information",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteAgent() {
        int selectedRow = agentTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer cet agent?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // TODO: Supprimer l'agent via RMI
            tableModel.removeRow(agentTable.convertRowIndexToModel(selectedRow));
            updateCount();
        }
    }
}

/**
 * Modèle de table pour les agents
 */
class AgentTableModel extends AbstractTableModel {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String[] columnNames = {
        "ID", "Nom", "Adresse IP", "Statut", "CPU", "Mémoire", "Disque", "Dernière MAJ"
    };
    
    private List<AgentData> agents = new ArrayList<>();
    
    public void setData(List<AgentData> data) {
        this.agents = data;
        fireTableDataChanged();
    }
    
    public void removeRow(int row) {
        agents.remove(row);
        fireTableRowsDeleted(row, row);
    }
    
    public AgentData getAgentAt(int row) {
        return agents.get(row);
    }
    
    @Override
    public int getRowCount() {
        return agents.size();
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
        AgentData agent = agents.get(row);
        switch (col) {
            case 0: return agent.id;
            case 1: return agent.name;
            case 2: return agent.ipAddress;
            case 3: return agent.status;
            case 4: return agent.cpu;
            case 5: return agent.memory;
            case 6: return agent.disk;
            case 7: return agent.lastUpdate;
            default: return null;
        }
    }
    
    @Override
    public Class<?> getColumnClass(int col) {
        if (col >= 4 && col <= 6) return Double.class;
        return String.class;
    }
}

/**
 * Classe de données pour un agent
 */
class AgentData {
    String id;
    String name;
    String ipAddress;
    String status;
    double cpu;
    double memory;
    double disk;
    String lastUpdate;
    
    public AgentData(String id, String name, String ipAddress, String status,
                     double cpu, double memory, double disk, String lastUpdate) {
        this.id = id;
        this.name = name;
        this.ipAddress = ipAddress;
        this.status = status;
        this.cpu = cpu;
        this.memory = memory;
        this.disk = disk;
        this.lastUpdate = lastUpdate;
    }
}

/**
 * Renderer personnalisé pour les cellules
 */
class AgentCellRenderer extends DefaultTableCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        // Colonne statut avec couleur
        if (column == 3 && value instanceof String) {
            String status = (String) value;
            if (!isSelected) {
                switch (status) {
                    case "Actif":
                        c.setForeground(new Color(46, 204, 113));
                        break;
                    case "Alerte":
                        c.setForeground(new Color(243, 156, 18));
                        break;
                    case "Critique":
                        c.setForeground(new Color(231, 76, 60));
                        break;
                    default:
                        c.setForeground(Color.GRAY);
                }
            }
            setFont(getFont().deriveFont(Font.BOLD));
        }
        
        return c;
    }
}

/**
 * Renderer de barre de progression pour les métriques
 */
class ProgressBarRenderer extends JProgressBar implements TableCellRenderer {
    
    public ProgressBarRenderer() {
        super(0, 100);
        setStringPainted(true);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        if (value instanceof Double) {
            double val = (Double) value;
            setValue((int) val);
            setString(String.format("%.1f%%", val));
            
            // Couleur selon le niveau
            if (val < 60) {
                setForeground(new Color(46, 204, 113));
            } else if (val < 80) {
                setForeground(new Color(243, 156, 18));
            } else {
                setForeground(new Color(231, 76, 60));
            }
        }
        
        return this;
    }
}