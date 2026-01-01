package inpt_cde.systemmonitor.model;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/** @pdOid 596cef3c-31f4-4720-89a9-67aa8ce7b51d */
public class Metric {
   /** @pdOid 76005e46-bd97-4f5b-88b1-db9f4af9669b */
   private int id;
   /** @pdOid c21bdd46-bda4-45d3-a8a4-117ecb857473 */
   private Date timestamp;
   /** @pdOid e954d0c9-7bab-44ce-bd7e-b0317d4469cd */
   private double cpuUsage;
   /** @pdOid 32ed82fd-e055-4a41-aa8f-393af24c121c */
   private double memoryUsageMB;
   /** @pdOid 67d54906-3a9a-4430-95ef-9ca24af04266 */
   private double diskUsagePercents;   
   
   public Metric() {}
   
   public Metric(int id, Date timestamp, double cpuUsage, double memoryUsageMB, double diskUsagePercents) {
		super();
		this.id = id;
		this.timestamp = timestamp;
		this.cpuUsage = cpuUsage;
		this.memoryUsageMB = memoryUsageMB;
		this.diskUsagePercents = diskUsagePercents;
   }
   
   public int getId() {
	return id;
   }
   public void setId(int id) {
	this.id = id;
   }
   public Date getTimestamp() {
	return timestamp;
   }
   public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
   }
   public double getCpuUsage() {
	return cpuUsage;
   }
   public void setCpuUsage(double cpuUsage) {
	this.cpuUsage = cpuUsage;
   }
   public double getMemoryUsageMB() {
	return memoryUsageMB;
   }
   public void setMemoryUsageMB(double memoryUsageMB) {
	this.memoryUsageMB = memoryUsageMB;
   }
   public double getDiskUsagePercents() {
	return diskUsagePercents;
   }
   public void setDiskUsagePercents(double diskUsagePercents) {
	this.diskUsagePercents = diskUsagePercents;
   }

   @Override
   public String toString() {
	   String s= "";
	   DecimalFormat df = new DecimalFormat("#.00");
	   Instant instant = getTimestamp().toInstant();
	   ZonedDateTime dateHeure = instant.atZone(ZoneId.of("Africa/Casablanca"));
	   DateTimeFormatter formatter =
       DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.FRANCE);
	   s+="Timestamp;" + dateHeure.format(formatter)+"\n";
	   s+="CPU Usage: "+ df.format(getCpuUsage())+"%\n";
       s+="RAM Usage: "+ df.format(getMemoryUsageMB())+"%\n";
       s+="Disk Usage:"+ df.format(getDiskUsagePercents())+"%\n";
	return s;
   }
   
   

}