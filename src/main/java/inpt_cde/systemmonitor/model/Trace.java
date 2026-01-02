package inpt_cde.systemmonitor.model;

import java.util.*;

/** @pdOid 24de7ea7-f7ad-45f3-91c1-39dd6331d500 */
public class Trace {
   /** @pdOid aec421f0-fb53-40ff-8aa6-04f4294e3518 */
   private int id;
   /** @pdOid 5a38a48b-dde2-412a-8cdb-0ea991c24312 */
   private String label;
   /** @pdOid c0fb7151-4082-4140-827e-43b6b26e14e6 */
   private Date timestamp;
   
   public Trace(int id, String label, Date timestamp) {
	super();
	this.id = id;
	this.label = label;
	this.timestamp = timestamp;
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

   public Date getTimestamp() {
	return timestamp;
   }

   public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
   }
   
   

}