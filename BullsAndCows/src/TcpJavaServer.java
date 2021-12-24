import java.io.IOException;
import java.net.*;




public class TcpJavaServer extends NetJavaServer {
private ServerSocket tcpServerSocket;
private int tcpPort;
private ApplProtocolJava protocol;

		public TcpJavaServer(int tcpPort, ApplProtocolJava protocol)  {
		this.tcpPort = tcpPort;
		this.protocol = protocol;
		try {
			tcpServerSocket = new ServerSocket(this.tcpPort);
		} catch (IOException e) {
			System.out.println("Port in use " + this.tcpPort);
		}
	}

	@Override
	public void run() {
		System.out.println("TCP port: " + tcpPort);
		try {
			while(true) {
				Socket socket = tcpServerSocket.accept();
				Thread thread = new Thread(new TcpClientServer(socket, protocol));
				thread.start();
			}
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
