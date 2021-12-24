import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;




public class HttpJavaServer extends NetJavaServer {
private ServerSocket httpServerSocket;
private int httpPort;
private ApplProtocolJava protocol;

		public HttpJavaServer(int httpPort, ApplProtocolJava protocol)  {
		this.httpPort = httpPort;
		this.protocol = protocol;
		try {
			httpServerSocket = new ServerSocket(this.httpPort);
		} catch (IOException e) {
			System.out.println("HTTP port in use " + this.httpPort);
		}
	}

	@Override
	public void run() {
		System.out.println("HTTP port: " + httpPort);
		try {
			while(true) {
				Socket socket = httpServerSocket.accept();
				handleClient(socket);
				Thread thread = new Thread(new TcpClientServer(socket, protocol));
				thread.start();
			}
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	
	private static List<Integer> findWordUpgrade(String textString, String word) {
	    List<Integer> indexes = new ArrayList<Integer>();
	    StringBuilder output = new StringBuilder();
	    String lowerCaseTextString = textString.toLowerCase();
	    String lowerCaseWord = word.toLowerCase();
	    int wordLength = 0;

	    int index = 0;
	    while(index != -1){
	        index = lowerCaseTextString.indexOf(lowerCaseWord, index + wordLength);  // Slight improvement
	        if (index != -1) {
	            indexes.add(index);
	        }
	        wordLength = word.length();
	    }
	    return indexes;
	}
	
	private static void handleClient(Socket client) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while (!(line = br.readLine()).isBlank()) {
            requestBuilder.append(line + "\r\n");
        }
        String request = requestBuilder.toString();
        String[] requestsLines = request.split("\r\n");
        String[] requestLine = requestsLines[0].split(" ");
        String method = requestLine[0];
        String path = requestLine[1];
        String version = requestLine[2];
        String host = requestsLines[1].split(" ")[1];
        List<String> headers = new ArrayList<>();
        for (int h = 2; h < requestsLines.length; h++) {
            String header = requestsLines[h];
            headers.add(header);
        }
        String accessLog = String.format("Client %s, method %s, path %s, version %s, host %s, headers %s",
                client.toString(), method, path, version, host, headers.toString());
        System.out.println(accessLog);
        Path filePath = getFilePath(path);
        if (Files.exists(filePath)) {
            String contentType = guessContentType(filePath);
            
            byte[] bytes = Files.readAllBytes(filePath);
    		String s = new String(bytes, StandardCharsets.UTF_8);
    		String wordToFind = "img src=";
    		List<Integer> arrayList = findWordUpgrade(s, wordToFind);
    		for (int i = 0; i < arrayList.size(); i++) {
    			int el = arrayList.get(i);
    			arrayList.set(i, el + wordToFind.length()+1);
    		}
    		
    		String appStartUpPath = System.getProperty("user.dir") + "\\";
    		for (int i = 0; i < arrayList.size(); i++) {
    			int index = s.indexOf("\"", arrayList.get(i));
    			String file = s.substring(arrayList.get(i) + "images".length() + 1, index );
    			byte[] img = Files.readAllBytes(Paths.get(appStartUpPath + "images\\" + file));
    			String str = Base64.getEncoder().encodeToString(img);
    			s = s.replace("images/"+file, "data:image/jpg;base64,"+str); 
    		}
    		
    		
    		

            
            
            
            sendResponse(client, "200 OK", contentType, s.getBytes());//Files.readAllBytes(filePath));
        } else {
            // 404
            byte[] notFoundContent = "<h1>Not found</h1>".getBytes();
            sendResponse(client, "404 Not Found", "text/html", notFoundContent);
        }
        
        
	}
	
	private static String guessContentType(Path filePath) throws IOException {
        return Files.probeContentType(filePath);
    }
	
	private static Path getFilePath(String path) {
        if ("/".equals(path)) {
            path = "index.html";
        }
        return Paths.get("", path);
    }
	
	 private static void sendResponse(Socket client, String status, String contentType, byte[] content) throws IOException {
	        OutputStream clientOutput = client.getOutputStream();
	        clientOutput.write(("HTTP/1.1 \r\n" + status).getBytes());
	        clientOutput.write(("ContentType: " + contentType + "\r\n").getBytes());
	        clientOutput.write("\r\n".getBytes());
	        clientOutput.write(content);
	        clientOutput.write("\r\n\r\n".getBytes());
	        clientOutput.flush();
	        client.close();
	    }
	
	

}
