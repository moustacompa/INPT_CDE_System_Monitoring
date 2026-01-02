package inpt_cde.systemmonitor.pkg_agent.controller;

public class Seuils {
	private double cpu;
    private double ram;
    private double disk;

    public Seuils(double cpu, double ram, double disk) {
        this.cpu = cpu;
        this.ram = ram;
        this.disk = disk;
    }
    
    public Seuils(String s) {
    	String parts[] = s.substring(8, s.length()-1).split(",");
    	this.cpu = Double.parseDouble(parts[0].split("=")[1].trim());
    	this.ram = Double.parseDouble(parts[1].split("=")[1].trim());
    	this.disk = Double.parseDouble(parts[2].split("=")[1].trim());
    }

    public double getCpu() { return cpu; }
    public double getRam() { return ram; }
    public double getDisk() { return disk; }

	@Override
	public String toString() {
		return "Seuils [cpu=" + cpu + ", ram=" + ram + ", disk=" + disk + "]";
	}
}
