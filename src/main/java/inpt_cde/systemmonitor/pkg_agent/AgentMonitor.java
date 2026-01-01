package inpt_cde.systemmonitor.pkg_agent;

import java.time.Instant;
import java.util.Date;

import inpt_cde.systemmonitor.model.Alert;
import inpt_cde.systemmonitor.model.Metric;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;

public class AgentMonitor {

	public static Metric getPerf() throws InterruptedException {

		Metric metric = new Metric();
        SystemInfo systemInfo = new SystemInfo();

        // CPU
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Thread.sleep(1000); // mesure sur 1 seconde
        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;

        // RAM
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        long totalMemory = memory.getTotal();
        long availableMemory = memory.getAvailable();
        double ramUsage = ((double) (totalMemory - availableMemory) / totalMemory) * 100;

        // DISQUE
        FileSystem fileSystem = systemInfo.getOperatingSystem().getFileSystem();
        long totalDisk = 0;
        long usableDisk = 0;

        for (OSFileStore store : fileSystem.getFileStores()) {
            totalDisk += store.getTotalSpace();
            usableDisk += store.getUsableSpace();
        }

        double diskUsage = ((double) (totalDisk - usableDisk) / totalDisk) * 100;
        metric.setTimestamp(Date.from(Instant.now()));
        metric.setCpuUsage(cpuLoad);
        metric.setDiskUsagePercents(diskUsage);
        metric.setMemoryUsageMB(ramUsage);
        NetworkClient.sendMetric(metric);
        System.out.println("Vérification et envoi des alertes");
        // vérification et envoi des alertes
        Seuils t = SeuilsPreferences.load();
        System.out.println("Seuils = "+t);
        if (cpuLoad > t.getCpu()) {
		    Alert alert = new Alert();
		    alert.setMessage("CPU Usage too hight");
		    double delta = cpuLoad-t.getCpu();
		    int severity;
		    if(delta>(t.getCpu()*0.33)) {
		    	severity = 1;
		    }else if (delta>(t.getCpu()*0.66)) {
				severity=2;
			}else {
				severity = 3;
			}
		    alert.setSeverity(severity);
		    alert.setTimesptamp(Date.from(Instant.now()));
		    alert.setThreshold(1);
		    alert.setValue(cpuLoad);
        	NetworkClient.sendAlert(alert);
        	System.out.println("Dans AgentMonitor et Alert = "+alert);
		}
        if (ramUsage > t.getRam()) {
		    Alert alert = new Alert();
		    alert.setMessage("RAM Usage too hight");
		    double delta = ramUsage-t.getRam();
		    int severity;
		    if(delta>(t.getRam()*0.33)) {
		    	severity = 1;
		    }else if (delta>(t.getRam()*0.66)) {
				severity=2;
			}else {
				severity = 3;
			}
		    alert.setSeverity(severity);
		    alert.setTimesptamp(Date.from(Instant.now()));
		    alert.setThreshold(2);
		    alert.setValue(ramUsage);
		    NetworkClient.sendAlert(alert);
		    System.out.println("Dans AgentMonitor et Alert = "+alert);
		}

        if (diskUsage > t.getDisk()) {
		    Alert alert = new Alert();
		    alert.setMessage("Disk Usage too hight");
		    double delta = diskUsage-t.getDisk();
		    int severity;
		    if(delta>(t.getDisk()*0.33)) {
		    	severity = 1;
		    }else if (delta>(t.getDisk()*0.66)) {
				severity=2;
			}else {
				severity = 3;
			}
		    alert.setSeverity(severity);
		    alert.setTimesptamp(Date.from(Instant.now()));
		    alert.setThreshold(3);
		    alert.setValue(diskUsage);
		    NetworkClient.sendAlert(alert);
		    System.out.println("Dans AgentMonitor et Alert = "+alert);
		}
        return metric;
    }
}
