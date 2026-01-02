package inpt_cde.systemmonitor.pkg_server;

import java.io.IOException;
import java.util.logging.*;

import inpt_cde.systemmonitor.model.Alert;
import inpt_cde.systemmonitor.model.Metric;

public class ServerLogger {

    private static final Logger logger = Logger.getLogger(ServerLogger.class.getName());

    public static void saveLogs(int type, Metric m, Alert a) {

        try {
            // Créer le fichier de log
            FileHandler fileHandler = new FileHandler("application.log", true);
            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);
            logger.setLevel(Level.INFO);
            if (type==1 && m!=null) {
            	logger.info(m.toString());
			}
            if (type==2 && a!=null) {
            	switch (a.getSeverity()) {
					case 1: {
						logger.info(m.toString());
					};
					case 2: {
						logger.warning(a.toString());
					};
					case 3: {
						logger.severe(a.toString());
					}
					default:{
						throw new IllegalArgumentException("Unexpected value: " + a.getSeverity());
					}
				}
        	}

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void saveLogs(int type, String s) {

        try {
            // Créer le fichier de log
            FileHandler fileHandler = new FileHandler("application.log", true);
            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);
            logger.setLevel(Level.INFO);
            logger.info(s);
            fileHandler.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
