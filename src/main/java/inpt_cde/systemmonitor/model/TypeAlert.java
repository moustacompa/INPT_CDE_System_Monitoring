package inpt_cde.systemmonitor.model;

import java.util.*;

/** @pdOid c227250c-f1cd-499b-b60c-98dec0934d0f */
public class TypeAlert {
   /** @pdOid 1c9ea92b-18a5-4e3a-b419-4a6b4520a866 */
   private int id;
   /** @pdOid 3d4baddc-41c8-47a4-b490-6eb61e22ee1f */
   private String label;
   
   /** @pdRoleInfo migr=no name=Alert assc=association6 coll=java.util.Collection impl=java.util.HashSet mult=0..* */
   public java.util.Collection<Alert> alert;
   
   
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

}