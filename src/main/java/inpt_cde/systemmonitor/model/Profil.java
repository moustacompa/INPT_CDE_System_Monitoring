package inpt_cde.systemmonitor.model;

import java.util.*;

/** @pdOid dc32fa6c-85e0-479d-988c-2a0d86a43b2e */
public class Profil {
   /** @pdOid 3777fb27-2787-482f-b383-d1ed18eefeac */
   private int id;
   /** @pdOid 0e5cd543-b1d8-44ca-9063-4135e86d0868 */
   private String label;
   
   /** @pdRoleInfo migr=no name=Utilisateur assc=association3 coll=java.util.Collection impl=java.util.HashSet mult=0..* */
   public java.util.Collection<Utilisateur> utilisateur;
   
   
   public Profil(int id, String label, Collection<Utilisateur> utilisateur) {
		super();
		this.id = id;
		this.label = label;
		this.utilisateur = utilisateur;
	}
   

   /** @pdGenerated default getter */
   public java.util.Collection<Utilisateur> getUtilisateur() {
      if (utilisateur == null)
         utilisateur = new java.util.HashSet<Utilisateur>();
      return utilisateur;
   }
   
   /** @pdGenerated default iterator getter */
   public java.util.Iterator getIteratorUtilisateur() {
      if (utilisateur == null)
         utilisateur = new java.util.HashSet<Utilisateur>();
      return utilisateur.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newUtilisateur */
   public void setUtilisateur(java.util.Collection<Utilisateur> newUtilisateur) {
      removeAllUtilisateur();
      for (java.util.Iterator iter = newUtilisateur.iterator(); iter.hasNext();)
         addUtilisateur((Utilisateur)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newUtilisateur */
   public void addUtilisateur(Utilisateur newUtilisateur) {
      if (newUtilisateur == null)
         return;
      if (this.utilisateur == null)
         this.utilisateur = new java.util.HashSet<Utilisateur>();
      if (!this.utilisateur.contains(newUtilisateur))
         this.utilisateur.add(newUtilisateur);
   }
   
   /** @pdGenerated default remove
     * @param oldUtilisateur */
   public void removeUtilisateur(Utilisateur oldUtilisateur) {
      if (oldUtilisateur == null)
         return;
      if (this.utilisateur != null)
         if (this.utilisateur.contains(oldUtilisateur))
            this.utilisateur.remove(oldUtilisateur);
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllUtilisateur() {
      if (utilisateur != null)
         utilisateur.clear();
   }


   public int getId() {
	return id;
   }


   public void setId(int id) {
	this.id = id;
   }


   public String getLabel() {
	return label;
   }


   public void setLabel(String label) {
	this.label = label;
   }


   @Override
   public String toString() {
	return "Profil [id=" + id + ", label=" + label + ", utilisateur=" + utilisateur + "]";
   }
   
   

}