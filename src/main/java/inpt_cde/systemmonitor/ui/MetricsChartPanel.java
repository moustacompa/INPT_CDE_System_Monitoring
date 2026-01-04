package inpt_cde.systemmonitor.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Panneau affichant les graphiques des métriques en temps réel
 * Utilise des composants Swing personnalisés pour les graphiques
 */
public class MetricsChartPanel extends JPanel {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> agentSelector;
    private JComboBox<String> timeRangeSelector;
    private LineChartPanel cpuChartPanel;
    private LineChartPanel memoryChartPanel;
    private LineChartPanel diskChartPanel;
    private LineChartPanel networkChartPanel;
    
    public MetricsChartPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        add(createControlPanel(), BorderLayout.NORTH);
        add(createChartsPanel(), BorderLayout.CENTER);
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Sélecteur d'agent
        JLabel agentLabel = new JLabel("Agent:");
        agentSelector = new JComboBox<>(new String[]{
            "Tous les agents", "Agent-001", "Agent-002", "Agent-003", "Agent-004", "Agent-005"
        });
        agentSelector.addActionListener(e -> updateCharts());
        
        // Sélecteur de plage temporelle
        JLabel timeLabel = new JLabel("Période:");
        timeRangeSelector = new JComboBox<>(new String[]{
            "Dernière heure", "Dernières 6 heures", "Dernières 24 heures", 
            "Dernière semaine", "Dernier mois"
        });
        timeRangeSelector.addActionListener(e -> updateCharts());
        
        JButton refreshBtn = new JButton("Actualiser");
        refreshBtn.addActionListener(e -> refresh(null));
        
        JButton exportBtn = new JButton("Exporter");
        exportBtn.addActionListener(e -> exportCharts());
        
        panel.add(agentLabel);
        panel.add(agentSelector);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(timeLabel);
        panel.add(timeRangeSelector);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(refreshBtn);
        panel.add(exportBtn);
        
        return panel;
    }
    
    private JPanel createChartsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        
        // Graphique CPU
        cpuChartPanel = new LineChartPanel("Utilisation CPU", "Temps", "Pourcentage (%)", Color.BLUE);
        panel.add(createChartContainer("CPU", cpuChartPanel));
        
        // Graphique Mémoire
        memoryChartPanel = new LineChartPanel("Utilisation Mémoire", "Temps", "Pourcentage (%)", Color.GREEN);
        panel.add(createChartContainer("Mémoire", memoryChartPanel));
        
        // Graphique Disque
        diskChartPanel = new LineChartPanel("Utilisation Disque", "Temps", "Pourcentage (%)", Color.ORANGE);
        panel.add(createChartContainer("Disque", diskChartPanel));
        
        // Graphique Réseau (optionnel)
        networkChartPanel = new LineChartPanel("Trafic Réseau", "Temps", "MB/s", new Color(156, 39, 176));
        panel.add(createChartContainer("Réseau", networkChartPanel));
        
        return panel;
    }
    
    private JPanel createChartContainer(String title, LineChartPanel chart) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            title,
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        container.add(chart, BorderLayout.CENTER);
        return container;
    }
    
    private void updateCharts() {
        String selectedAgent = (String) agentSelector.getSelectedItem();
        String timeRange = (String) timeRangeSelector.getSelectedItem();
        
        // TODO: Récupérer les données via RMI selon l'agent et la période
        // Pour le moment, génération de données aléatoires
        generateTestData();
    }
    
    private void generateTestData() {
        Random random = new Random();
        int points = 50;
        
        // Données CPU
        List<Double> cpuData = new ArrayList<>();
        for (int i = 0; i < points; i++) {
            cpuData.add(30 + random.nextDouble() * 40);
        }
        cpuChartPanel.setData(cpuData);
        
        // Données Mémoire
        List<Double> memData = new ArrayList<>();
        for (int i = 0; i < points; i++) {
            memData.add(50 + random.nextDouble() * 30);
        }
        memoryChartPanel.setData(memData);
        
        // Données Disque
        List<Double> diskData = new ArrayList<>();
        for (int i = 0; i < points; i++) {
            diskData.add(60 + random.nextDouble() * 20);
        }
        diskChartPanel.setData(diskData);
        
        // Données Réseau
        List<Double> netData = new ArrayList<>();
        for (int i = 0; i < points; i++) {
            netData.add(5 + random.nextDouble() * 15);
        }
        networkChartPanel.setData(netData);
    }
    
    public void refresh(MonitoringController controller) {
        try {
            // TODO: Récupérer les données via RMI
            updateCharts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void exportCharts() {
        JOptionPane.showMessageDialog(this,
            "Export des graphiques en cours...",
            "Information",
            JOptionPane.INFORMATION_MESSAGE);
    }
}

/**
 * Panneau personnalisé pour afficher un graphique linéaire
 */
class LineChartPanel extends JPanel {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
    private String xLabel;
    private String yLabel;
    private Color lineColor;
    private List<Double> data;
    private double maxValue = 100.0;
    private boolean autoScale = false;
    
    public LineChartPanel(String title, String xLabel, String yLabel, Color lineColor) {
        this.title = title;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.lineColor = lineColor;
        this.data = new ArrayList<>();
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 300));
    }
    
    public void setData(List<Double> newData) {
        this.data = new ArrayList<>(newData);
        
        if (autoScale && !data.isEmpty()) {
            maxValue = data.stream().max(Double::compare).orElse(100.0) * 1.2;
        }
        
        repaint();
    }
    
    public void setMaxValue(double max) {
        this.maxValue = max;
        this.autoScale = false;
    }
    
    public void setAutoScale(boolean autoScale) {
        this.autoScale = autoScale;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (data == null || data.isEmpty()) {
            drawNoData(g);
            return;
        }
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        int padding = 50;
        int labelPadding = 25;
        
        // Dessiner les axes
        g2.setColor(Color.BLACK);
        g2.drawLine(padding, height - padding, padding, padding); // Axe Y
        g2.drawLine(padding, height - padding, width - padding, height - padding); // Axe X
        
        // Dessiner les graduations Y
        int numberYDivisions = 10;
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding;
            int x1 = padding - 5;
            int y0 = height - ((i * (height - padding * 2)) / numberYDivisions + padding);
            
            g2.drawLine(x0, y0, x1, y0);
            
            // Label
            String yLabel = String.format("%.0f", (maxValue / numberYDivisions) * i);
            FontMetrics metrics = g2.getFontMetrics();
            int labelWidth = metrics.stringWidth(yLabel);
            g2.drawString(yLabel, x0 - labelWidth - 10, y0 + (metrics.getHeight() / 2) - 3);
        }
        
        // Dessiner les graduations X (toutes les 10 valeurs)
        int numberXDivisions = Math.min(10, data.size() - 1);
        if (numberXDivisions > 0) {
            for (int i = 0; i < numberXDivisions + 1; i++) {
                int x0 = i * (width - padding * 2) / numberXDivisions + padding;
                int y0 = height - padding;
                int y1 = y0 + 5;
                
                g2.drawLine(x0, y0, x0, y1);
                
                // Label simplifié
                String xLabel = String.valueOf(i * (data.size() / numberXDivisions));
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(xLabel);
                g2.drawString(xLabel, x0 - labelWidth / 2, y1 + metrics.getHeight() + 3);
            }
        }
        
        // Dessiner la ligne de données
        g2.setColor(lineColor);
        g2.setStroke(new BasicStroke(2f));
        
        int[] xPoints = new int[data.size()];
        int[] yPoints = new int[data.size()];
        
        double xScale = (double) (width - 2 * padding) / (data.size() - 1);
        double yScale = (double) (height - 2 * padding) / maxValue;
        
        for (int i = 0; i < data.size(); i++) {
            xPoints[i] = (int) (i * xScale + padding);
            yPoints[i] = (int) ((height - padding) - (data.get(i) * yScale));
        }
        
        g2.drawPolyline(xPoints, yPoints, data.size());
        
        // Remplir l'aire sous la courbe (optionnel)
        g2.setColor(new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), 50));
        int[] fillYPoints = new int[data.size() + 2];
        int[] fillXPoints = new int[data.size() + 2];
        
        System.arraycopy(yPoints, 0, fillYPoints, 0, data.size());
        System.arraycopy(xPoints, 0, fillXPoints, 0, data.size());
        
        fillYPoints[data.size()] = height - padding;
        fillXPoints[data.size()] = xPoints[data.size() - 1];
        fillYPoints[data.size() + 1] = height - padding;
        fillXPoints[data.size() + 1] = xPoints[0];
        
        g2.fillPolygon(fillXPoints, fillYPoints, data.size() + 2);
        
        // Afficher la valeur actuelle
        if (!data.isEmpty()) {
            double currentValue = data.get(data.size() - 1);
            String valueText = String.format("Actuel: %.1f", currentValue);
            
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.drawString(valueText, width - 150, 20);
            
            // Indicateur de statut
            Color statusColor = currentValue > 80 ? Color.RED : 
                               currentValue > 60 ? Color.ORANGE : Color.GREEN;
            g2.setColor(statusColor);
            g2.fillOval(width - 165, 12, 10, 10);
        }
    }
    
    private void drawNoData(Graphics g) {
        String message = "Aucune donnée disponible";
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(message)) / 2;
        int y = getHeight() / 2;
        
        g.setColor(Color.GRAY);
        g.drawString(message, x, y);
    }
}