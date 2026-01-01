package inpt_cde.systemmonitor.model;

import java.util.*;

/** @pdOid fd3d9ffd-46f5-4bf5-902b-1d7af1e61617 */
public class Alert {
   /** @pdOid babbee35-f3a6-4d3f-8bfc-c66b65325ca5 */
   private int id;
   /** @pdOid 0b7d2926-6bd6-4733-92fa-2c3e3b02ee51 */
   private int severity;
   /** @pdOid 111fbf36-5a71-47b6-934b-7e60ba96adb5 */
   private String message;
   /** @pdOid b8ef3e73-876d-4a15-9061-4c54a9cb48a5 */
   private double threshold;
   /** @pdOid 0aae42d9-3442-4d49-8848-462cded49d1d */
   private double value;
   /** @pdOid 9ce2e895-3bc4-49ff-b223-8439d55c9a09 */
   private Date timesptamp;
   public Alert() {
	super();
   }
   public Alert(int id, int severity, String message, double threshold, double value, Date timesptamp) {
	super();
	this.id = id;
	this.severity = severity;
	this.message = message;
	this.threshold = threshold;
	this.value = value;
	this.timesptamp = timesptamp;
   }
   public int getId() {
	return id;
   }
   public void setId(int id) {
	this.id = id;
   }
   public int getSeverity() {
	return severity;
   }
   public void setSeverity(int severity) {
	this.severity = severity;
   }
   public String getMessage() {
	return message;
   }
   public void setMessage(String message) {
	this.message = message;
   }
   public double getThreshold() {
	return threshold;
   }
   public void setThreshold(double threshold) {
	this.threshold = threshold;
   }
   public double getValue() {
	return value;
   }
   public void setValue(double value) {
	this.value = value;
   }
   public Date getTimesptamp() {
	return timesptamp;
   }
   public void setTimesptamp(Date timesptamp) {
	this.timesptamp = timesptamp;
   }
   @Override
   public String toString() {
	return "Alert [id=" + id + ", severity=" + severity + ", message=" + message + ", threshold=" + threshold
			+ ", value=" + value + ", timesptamp=" + timesptamp + "]";
   }
   
}