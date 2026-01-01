package inpt_cde.systemmonitor.model;

import java.util.*;

/** @pdOid b73769aa-742f-4c94-9391-ac8d701bcdd4 */
public class Agent {
   /** @pdOid 4239de5c-33d2-459a-8d8b-9ba6e67eaf6e */
   private int id;
   /** @pdOid e1beaaec-40f3-45a8-93f5-71b7c30824c7 */
   private String hostname;
   /** @pdOid 57ceabc4-03b8-4c41-9d53-70606b0d8358 */
   private String ipAddress;
   /** @pdOid 69695302-e5aa-4eaf-8b84-1d7ea0074902 */
   private String macAddress;
   /** @pdOid ba01bbea-a1e5-4987-bff4-f04262384ec4 */
   private String typeOS;
   /** @pdOid f102a20b-d4db-4077-b1c3-f874c8ff822f */
   private Date dateInstallation;
   /** @pdOid 29f69dd1-7bb5-4268-81d1-e04d6b6631ec */
   private boolean isOnline;
   /** @pdOid ab7742ca-1022-4e24-bb4d-eb2471ede6ef */
   private Date lastAlertTime;
   /** @pdOid 8ff1facd-2ec7-4d06-a6ec-b3cd36782d95 */
   private Date lastMetricsTime;
   /** @pdOid cedbcd23-8b0d-4383-bd5d-97b034f2d9c1 */
   private String tags;
   
   /** @pdRoleInfo migr=no name=Alert assc=association1 coll=java.util.Collection impl=java.util.HashSet mult=0..* */
   public java.util.Collection<Alert> alert;
   /** @pdRoleInfo migr=no name=Metric assc=association2 coll=java.util.Collection impl=java.util.HashSet mult=0..* */
   public java.util.Collection<Metric> metric;
   
   
   /** @pdGenerated default getter */
   public java.util.Collection<Alert> getAlert() {
      if (alert == null)
         alert = new java.util.HashSet<Alert>();
      return alert;
   }
   
   /** @pdGenerated default iterator getter */
   public java.util.Iterator getIteratorAlert() {
      if (alert == null)
         alert = new java.util.HashSet<Alert>();
      return alert.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newAlert */
   public void setAlert(java.util.Collection<Alert> newAlert) {
      removeAllAlert();
      for (java.util.Iterator iter = newAlert.iterator(); iter.hasNext();)
         addAlert((Alert)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newAlert */
   public void addAlert(Alert newAlert) {
      if (newAlert == null)
         return;
      if (this.alert == null)
         this.alert = new java.util.HashSet<Alert>();
      if (!this.alert.contains(newAlert))
         this.alert.add(newAlert);
   }
   
   /** @pdGenerated default remove
     * @param oldAlert */
   public void removeAlert(Alert oldAlert) {
      if (oldAlert == null)
         return;
      if (this.alert != null)
         if (this.alert.contains(oldAlert))
            this.alert.remove(oldAlert);
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllAlert() {
      if (alert != null)
         alert.clear();
   }
   /** @pdGenerated default getter */
   public java.util.Collection<Metric> getMetric() {
      if (metric == null)
         metric = new java.util.HashSet<Metric>();
      return metric;
   }
   
   /** @pdGenerated default iterator getter */
   public java.util.Iterator getIteratorMetric() {
      if (metric == null)
         metric = new java.util.HashSet<Metric>();
      return metric.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newMetric */
   public void setMetric(java.util.Collection<Metric> newMetric) {
      removeAllMetric();
      for (java.util.Iterator iter = newMetric.iterator(); iter.hasNext();)
         addMetric((Metric)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newMetric */
   public void addMetric(Metric newMetric) {
      if (newMetric == null)
         return;
      if (this.metric == null)
         this.metric = new java.util.HashSet<Metric>();
      if (!this.metric.contains(newMetric))
         this.metric.add(newMetric);
   }
   
   /** @pdGenerated default remove
     * @param oldMetric */
   public void removeMetric(Metric oldMetric) {
      if (oldMetric == null)
         return;
      if (this.metric != null)
         if (this.metric.contains(oldMetric))
            this.metric.remove(oldMetric);
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllMetric() {
      if (metric != null)
         metric.clear();
   }

}