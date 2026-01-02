package inpt_cde.systemmonitor.pkg_agent.controller;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MetricsScheduler.start();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException ignored) {}
	}

	public Object getGreeting() {
		// TODO Auto-generated method stub
		return null;
	}

}
