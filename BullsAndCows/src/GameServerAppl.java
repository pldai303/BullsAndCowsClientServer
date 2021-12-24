

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;







public class GameServerAppl {
	
	private static String name = "Bulls & Cows 2.0 (Server)";
	public static CompetitionManager compManager = new CompetitionManager();
	public static ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
    public static List<ScheduledFuture<Competition>> results = new ArrayList<ScheduledFuture<Competition>>();
    public static CompetitionSheduler sheduler = new CompetitionSheduler(); 
	
	@SuppressWarnings("deprecation")
	private static Properties configServer() throws Exception{
		Properties props = new Properties();
		File config = new File("server.properties");
		if (config.exists()) {
			props.load(new FileInputStream("server.properties"));
		} else {
			props.setProperty("Port", "2000");
			props.setProperty("Protocol", "TcpJavaServer"); //"TcpJavaServer"; "UdpJavaServer"
			config.createNewFile();
			props.save(new FileOutputStream(config), "properties for server");
		}
		return props;
	}
	
	public static void main(String[] args) throws Exception {
		try {	
		Properties props = configServer();		
		ApplProtocolJava protocol = new GameProtocol(UserMethodsImpl.getEmptyUser());
		Class<?> clazz = (Class<?>)Class.forName(props.getProperty("Protocol"));
		NetJavaServer server = (NetJavaServer)clazz.getConstructor(int.class, ApplProtocolJava.class).newInstance(Integer.parseInt(props.getProperty("Port")), protocol);
		server.start(); 
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		InputOutput io = new ConsoleInputOutput();
		Item[] menuItems = GameActions.getServerItems();
		Menu menu = new Menu(name, menuItems);
		menu.perform(io);
		try {
			if (sheduler.Size() > 0)
			lounchSheduling();
		} catch (Exception e) {
				
		}
	}
	
	public static void lounchSheduling() {
		ArrayList<Competition> competitionsToLunch = new ArrayList<Competition>();
		
		for (Competition comp : compManager.getCompetitions().values() ) {
			competitionsToLunch.add(comp);
		}
		for (CompetitionInfo compInfo : compManager.getCompetitions().keySet()) {
			sheduler.createScheduledElement(compInfo.getName(), compInfo.getStartAt(), compInfo.getFinishAt());
		}
		for (int i = 0; i < sheduler.Size(); i++) 
        {
            try {
				Thread.sleep(sheduler.getDelay(i));
			} catch (InterruptedException e1) {
			}
        	ScheduledFuture<Competition> result = executor.schedule(new CompetitionController(competitionsToLunch.get(i)), sheduler.geCompetitionTimeDurations(i),  TimeUnit.SECONDS);
        }
        executor.shutdown();
	}
	
	
}

