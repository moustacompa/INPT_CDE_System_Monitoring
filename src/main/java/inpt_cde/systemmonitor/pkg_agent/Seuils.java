package inpt_cde.systemmonitor.pkg_agent;

public class Seuils {
	private double cpu;
    private double ram;
    private double disk;

    public Seuils(double cpu, double ram, double disk) {
        this.cpu = cpu;
        this.ram = ram;
        this.disk = disk;
    }

    public double getCpu() { return cpu; }
    public double getRam() { return ram; }
    public double getDisk() { return disk; }

	@Override
	public String toString() {
		return "Seuils [cpu=" + cpu + ", ram=" + ram + ", disk=" + disk + "]";
	}
}
