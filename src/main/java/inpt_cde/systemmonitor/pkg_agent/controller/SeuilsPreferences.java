package inpt_cde.systemmonitor.pkg_agent.controller;

import java.util.prefs.Preferences;

public class SeuilsPreferences {

    private static final Preferences prefs =
            Preferences.userNodeForPackage(SeuilsPreferences.class);

    private static final String CPU = "cpu_threshold";
    private static final String RAM = "ram_threshold";
    private static final String DISK = "disk_threshold";

    // valeurs par défaut (fallback)
    private static final double DEFAULT_CPU = 80.0;
    private static final double DEFAULT_RAM = 80.0;
    private static final double DEFAULT_DISK = 80.0;

    public static void save(Seuils t) {
        prefs.putDouble(CPU, t.getCpu());
        prefs.putDouble(RAM, t.getRam());
        prefs.putDouble(DISK, t.getDisk());
    }

    public static Seuils load() {
    	Seuils s = null;
    	String cpucheck = prefs.get(CPU, null);

    	if (cpucheck == null) {
    	    s = SeuilRecupService.fetchFromServer();
    	} else {
    		s = new Seuils(prefs.getDouble(CPU, DEFAULT_CPU),prefs.getDouble(RAM, DEFAULT_RAM),prefs.getDouble(DISK, DEFAULT_DISK));
    	}
        return s;
    }
    
    public static boolean isSeuilsChanged(Seuils s) {
    	boolean rep = false;
		if (prefs.getDouble(CPU, 0)!=s.getCpu()) {
			rep = true;
		}
		if (prefs.getDouble(RAM, 0)!=s.getRam()) {
			rep = true;
		}
		if (prefs.getDouble(DISK, 0)!=s.getDisk()) {
			rep = true;
		}
		// update if changed
		if (rep) {
			save(s);
		}
    	return rep;
    }
    
}
