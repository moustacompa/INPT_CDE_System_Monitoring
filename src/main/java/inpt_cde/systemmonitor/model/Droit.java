package inpt_cde.systemmonitor.model;

import java.util.*;

/** @pdOid 8cbc0d95-5f38-435b-9bcd-c0688f73f731 */
public class Droit {
   /** @pdOid 0d59b8e8-e8e4-43c4-89e4-71932ba58d05 */
   private int id;
   /** @pdOid a35ca2c3-b463-42e8-950d-192c84200f38 */
   private String label;
   
   public Droit(int id, String label) {
	super();
	this.id = id;
	this.label = label;
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
	return "Droit [id=" + id + ", label=" + label + "]";
   }
   
   

}