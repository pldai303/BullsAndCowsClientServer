import java.io.Serializable;
import java.util.ArrayList;

public class CompetitionObject implements Serializable {

	Integer compId;
	String userName;
	Long gameId;
	Integer movesCount;
	ArrayList<String> history;
	
	
	public CompetitionObject(Integer compId, String userName, Long gameId, Integer movesCount, ArrayList<String> history) {
	this.compId = compId;
	this.userName = userName;
	this.gameId = gameId;
	this.movesCount = movesCount;
	this.history = history;
	}
	
	
	
}
