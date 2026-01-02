package inpt_cde.systemmonitor.model;

import java.util.*;

/** @pdOid b16ece07-79b1-4c8c-9d37-1699b668b4ad */
public class Utilisateur {
   /** @pdOid 785db87d-2d7b-40e5-a1b5-748f83acb03f */
   private int id;
   /** @pdOid 72901451-9000-4057-ac0c-38e0486d3211 */
   private String login;
   /** @pdOid 4aca202d-2c85-466a-9ea5-5ddab6bf617f */
   private String pwd;
   /** @pdOid 61103c66-21d8-47d8-8a74-8694aecfdad3 */
   private Date lastLogin;
   /** @pdOid acfe1196-19ac-4759-80e2-c209cccfea78 */
   private boolean active;
   
   /** @pdRoleInfo migr=no name=Droit assc=association4 coll=java.util.Collection impl=java.util.HashSet mult=0..* */
   public java.util.Collection<Droit> droit;
   /** @pdRoleInfo migr=no name=Trace assc=association5 coll=java.util.Collection impl=java.util.HashSet mult=0..* */
   public java.util.Collection<Trace> trace;
   
   public Utilisateur(int id, String login, String pwd, Date lastLogin, boolean active, Collection<Droit> droit,
		Collection<Trace> trace) {
	super();
	this.id = id;
	this.login = login;
	this.pwd = pwd;
	this.lastLogin = lastLogin;
	this.active = active;
	this.droit = droit;
	this.trace = trace;
}

   /** @pdGenerated default getter */
   public java.util.Collection<Droit> getDroit() {
      if (droit == null)
         droit = new java.util.HashSet<Droit>();
      return droit;
   }
   
   /** @pdGenerated default iterator getter */
   public java.util.Iterator getIteratorDroit() {
      if (droit == null)
         droit = new java.util.HashSet<Droit>();
      return droit.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newDroit */
   public void setDroit(java.util.Collection<Droit> newDroit) {
      removeAllDroit();
      for (java.util.Iterator iter = newDroit.iterator(); iter.hasNext();)
         addDroit((Droit)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newDroit */
   public void addDroit(Droit newDroit) {
      if (newDroit == null)
         return;
      if (this.droit == null)
         this.droit = new java.util.HashSet<Droit>();
      if (!this.droit.contains(newDroit))
         this.droit.add(newDroit);
   }
   
   /** @pdGenerated default remove
     * @param oldDroit */
   public void removeDroit(Droit oldDroit) {
      if (oldDroit == null)
         return;
      if (this.droit != null)
         if (this.droit.contains(oldDroit))
            this.droit.remove(oldDroit);
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllDroit() {
      if (droit != null)
         droit.clear();
   }
   /** @pdGenerated default getter */
   public java.util.Collection<Trace> getTrace() {
      if (trace == null)
         trace = new java.util.HashSet<Trace>();
      return trace;
   }
   
   /** @pdGenerated default iterator getter */
   public java.util.Iterator getIteratorTrace() {
      if (trace == null)
         trace = new java.util.HashSet<Trace>();
      return trace.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newTrace */
   public void setTrace(java.util.Collection<Trace> newTrace) {
      removeAllTrace();
      for (java.util.Iterator iter = newTrace.iterator(); iter.hasNext();)
         addTrace((Trace)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newTrace */
   public void addTrace(Trace newTrace) {
      if (newTrace == null)
         return;
      if (this.trace == null)
         this.trace = new java.util.HashSet<Trace>();
      if (!this.trace.contains(newTrace))
         this.trace.add(newTrace);
   }
   
   /** @pdGenerated default remove
     * @param oldTrace */
   public void removeTrace(Trace oldTrace) {
      if (oldTrace == null)
         return;
      if (this.trace != null)
         if (this.trace.contains(oldTrace))
            this.trace.remove(oldTrace);
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllTrace() {
      if (trace != null)
         trace.clear();
   }

   public int getId() {
	return id;
   }

   public void setId(int id) {
	this.id = id;
   }

   public String getLogin() {
	return login;
   }

   public void setLogin(String login) {
	this.login = login;
   }

   public String getPwd() {
	return pwd;
   }

   public void setPwd(String pwd) {
	this.pwd = pwd;
   }

   public Date getLastLogin() {
	return lastLogin;
   }

   public void setLastLogin(Date lastLogin) {
	this.lastLogin = lastLogin;
   }

   public boolean isActive() {
	return active;
   }

   public void setActive(boolean active) {
	this.active = active;
   }

   @Override
   public String toString() {
	return "Utilisateur [id=" + id + ", login=" + login + ", pwd=" + pwd + ", lastLogin=" + lastLogin + ", active="
			+ active + ", droit=" + droit + ", trace=" + trace + "]";
   }
   
}