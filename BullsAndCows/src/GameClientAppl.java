import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class GameClientAppl {

	
	private static final String HOST = "127.0.0.1";
	
	private static String name = "Bulls & Cows 2.0 (Client)";

	private static Properties configClient() throws Exception{
		Properties props = new Properties();
		File config = new File("client.properties");
		if (config.exists()) {
			props.load(new FileInputStream("client.properties"));
		} else {
			props.setProperty("Port", "2000");
			props.setProperty("Host", "localhost");
			props.setProperty("Protocol", "TcpJavaClient"); //"TcpJavaClient"; "UdpJavaClient"
			config.createNewFile();
			props.save(new FileOutputStream(config), "properties for client");
		}
		return props;
	}
	public static void main(String[] args) throws Exception {
		Properties props = configClient();
		InputOutput io = new ConsoleInputOutput();
		UserMethods userService = null;
		
		try {
			Class<?> clazz = (Class<?>)Class.forName(props.getProperty("Protocol"));
			NetJavaClient client = (NetJavaClient)clazz.getConstructor(String.class, int.class).newInstance("localhost", Integer.parseInt(props.getProperty("Port")));
			userService = new GameProxyNetJava(client);
		} catch (Exception e) {
			io.writeObjectLine(e.getMessage());
		}
		try {
			Item[] items = GameActions.getUsersItems(userService);
			Menu menu = new Menu(name, items);
			menu.perform(io);
		} catch (Exception e) {
			io.writeObjectLine("No server connection");
		}
	}
}
		

	

