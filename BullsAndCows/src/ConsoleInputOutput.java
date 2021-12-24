
import java.io.*;




public class ConsoleInputOutput implements InputOutput {
private String gameTitle = "|     GAME ID     | DURATION (MIN)   |     MOVE COUNT    |                         GAME LOG                         |";
BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	@Override
	public String readString(String prompt) {
		writeObjectLine(prompt);
		String res = null;
		try {
			res =  reader.readLine();
			
		} catch (IOException e) {
			
		}
		return res;
	}

	@Override
	public void writeObject(Object obj) {
		System.out.print(obj);

	}

	
	
	@Override
	public void writeGameTitle() {
		String spliterDown = "_".repeat(gameTitle.length());
		String spliterUp = "-".repeat(gameTitle.length());
		System.out.println(spliterDown);
		System.out.println(gameTitle);
		System.out.println(spliterUp);
	}

	@Override
	public void writeGameInfo(UserGame object) {
		String id =  Long.toString(object.getGameId());
		String duration = Long.toString(object.getDuration());
		String moveCount = Long.toString(object.getMoveCount());
		String rightSplitter = " ".repeat(16 - id.length()) + " ";
		System.out.print(String.format("|%s" + rightSplitter, id));
		rightSplitter = " ".repeat(16 - duration.length()) + "  ";
		System.out.print(String.format("|%s" + rightSplitter, duration));
		rightSplitter = " ".repeat(16 - moveCount.length()) + "   |";
		System.out.print(String.format("|%s" + rightSplitter, moveCount));
		String sp =  "|" + " ".repeat(56) + "|";
		int count = 0;
		int size = object.getGameHistory().size();
		for (String str : object.getGameHistory()) {
			System.out.println(str + " ".repeat(58 - str.length()) + "|");
			count++;
			if (count != size)
			System.out.print(sp);
			
			
		} 
		System.out.println("-".repeat(gameTitle.length()));
	}

}
