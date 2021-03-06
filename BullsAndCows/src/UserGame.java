
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserGame implements Serializable{

	private static final long serialVersionUID = 1L;
	private long userId;
	private Long gameId;
	private String userName;
	private LocalDateTime dateTimeOfStart;
	private LocalDateTime dateTimeOfFinish;
	private ArrayList<String> gameHistory;
	private String secretNumber;
	private File gameFile;
	private String gameFilePath;
	private boolean gameIsFinished;
	private long moveCount;
	private long duration;
	private boolean isCompetitionGame;
	private int compId;
	private String number;
	private String competitionMove;
	private String competitionName;
	
	public UserGame(long userId, String userName) {
		this.userId = userId;
		this.userName = userName;
		this.dateTimeOfStart = LocalDateTime.now();
		this.gameId = generateGameId(this.dateTimeOfStart);
		this.secretNumber = generateRandomValues();
		this.gameHistory = new ArrayList<String>();
		this.gameIsFinished = false;
		this.moveCount = 0;
		this.isCompetitionGame = false;
	}
	
	private UserGame(long userId, Long gameId, LocalDateTime start, 
			LocalDateTime finish,  ArrayList<String> gameHistory, long duration, long moveCount) {
		this.userId = userId;
		this.dateTimeOfStart = start;
		this.dateTimeOfFinish = finish;
		this.gameHistory = gameHistory;
		this.gameId = gameId;
		this.duration = duration; 
		this.moveCount = moveCount;
	}
	
	
	//Should be in helper file
	private String createUserGameFile(long userId, String fName) {
		String path = "";
		String profileFile = "\\Profiles\\" + userId + "\\" + fName + ".dat";
		try {
			path = new File(".").getCanonicalPath();
			path = path + profileFile;
			gameFile = new File(path);
			gameFile.getParentFile().mkdirs();
			gameFile.createNewFile();
		} catch (IOException e) {

		}
	    return path;
	}
	

	private String createUserCompetitionsFile(int gameId, String gameName) {
		String path = "";
		String profileFile = "\\FinishedCompetitions\\" + "ID" + gameId + "_" + gameName + ".dat";
		try {
			path = new File(".").getCanonicalPath();
			path = path + profileFile;
			gameFile = new File(path);
			gameFile.getParentFile().mkdirs();
			if (!gameFile.exists())
			gameFile.createNewFile();
		} catch (IOException e) {

		}
	    return path;
	}

	
	
	
	private Long generateGameId(LocalDateTime dt) {
		String id = String.format("%s%s%s%s%s%s", dt.getYear(),dt.getMonthValue(),dt.getDayOfMonth(),
									dt.getHour(),dt.getMinute(),dt.getSecond()); 
		return Long.parseLong(id);
	}

	public Long getGameId() {
		return this.gameId;
	}
	
	public long getUserId() {
		return this.userId;
	}
	
	private String generateRandomValues() {
		ArrayList<Integer> arr = new ArrayList<>();
		while (arr.size() != 4 ) {
			int rnd = (int) ( (Math.random() * 9) + 1) ;
			if (!arr.contains(rnd)) {
				arr.add(rnd);
			}
		}
		String res = arr.stream().map(Object::toString).collect(Collectors.joining("")); 
		System.out.println(String.format("Secret number [%s] for user %d", res, getUserId()));
		return res;
	}
	
	public String makeGameMove(String userNumber) throws Exception {
		if (gameIsFinished) {
			throw new Exception("This game is finished");
		}
		moveCount++;
		
		String res = "";
		int cows = 0;
		int bulls = 0;
		int secretNumberLength = secretNumber.length(); 
		int userNumberLength = userNumber.length();
		if (userNumberLength > secretNumberLength)
		userNumberLength = secretNumberLength;
		for (int i = 0; i < secretNumberLength; i++) {
			char secretChar = secretNumber.charAt(i);
			for (int j=0; j<userNumberLength; j++ ) {
				char userChar = userNumber.charAt(j);
				if (secretChar == userChar) {
					cows++;
					if (i == j) {
						cows--;
						bulls++;
					}
				}
			}
		}
		
		String mode = "[Training mode]";
		if (this.isCompetitionGame) {
			mode = "[Competition mode]";
		}
		
		
		if ((bulls == 4) && (cows == 0)) {
		this.gameIsFinished = true;	
		res = String.format("Your move: %s - Bulls:%s; Cows:%s; %s", userNumber, bulls, cows, mode);	
		gameHistory.add(res);
		saveFinishedGame();
		} else {
		res = String.format("Your move: %s - Bulls:%s; Cows:%s; %s", userNumber, bulls, cows, mode);
		gameHistory.add(res);
		}
		if (gameIsFinished) {
			gameHistory.add(String.format("CONGRATS, %s! YOU WON! You made %d moves!", userName.toUpperCase(), moveCount));
			
		}
		return res;
	}
	
	private void saveFinishedGame() {
		if (this.isCompetitionGame) {
		saveCompetitionGame();
		} else {
		saveTrainingFile();
		}
	}
	
	
	private void saveTrainingFile() {
		dateTimeOfFinish = LocalDateTime.now();
		String finishedGameFName = String.format("%d_%s_%d_%d_%d_%d_%d", 
				gameId,userName,
				dateTimeOfFinish.getYear(),
				dateTimeOfFinish.getMonthValue(),
				dateTimeOfFinish.getDayOfMonth(),
				dateTimeOfFinish.getHour(),
				dateTimeOfFinish.getMinute()); 
		UserGame finishedGame = new UserGame(
				this.userId, this.gameId, this.dateTimeOfStart, dateTimeOfFinish, 
				gameHistory, ChronoUnit.MINUTES.between(this.dateTimeOfStart, dateTimeOfFinish), moveCount);
		try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(new File(createUserGameFile(userId, finishedGameFName))))) {
			writer.writeObject(finishedGame);
		} catch (Exception e) {

		}
	}
	
	private void saveCompetitionGame() {
		String filePath = createUserCompetitionsFile(this.compId, this.competitionName);
		ArrayList<CompetitionObject> res =  new ArrayList<CompetitionObject>();
				try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(new File(filePath)))) {
	    			res.add((CompetitionObject) reader.readObject());
				} catch (Exception e) {
					
				}
		CompetitionObject compObj = new CompetitionObject(this.compId, this.userName, this.gameId, this.gameHistory.size(), this.gameHistory);	
		res.add(compObj);
		
		try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(new File(filePath)))) {
			res.stream().forEach(n->{
				try {
					writer.writeObject(n);
				} catch (IOException e) {
					
				}
				});
		writer.close();	
		} catch (Exception e) {

		}
	}
	
	public boolean isGameFinished() {
		return this.gameIsFinished;
	}

	public ArrayList<String> getGameHistory(){
		return this.gameHistory;
	}
	
	public String getPathToGameFile() {
		return this.gameFilePath;
	}
	
	public long getDuration() {
		return this.duration;
	}
	
	public LocalDateTime getGameStart() {
		return this.dateTimeOfStart;
	}
	
	public LocalDateTime getGameFinish() {
		return this.dateTimeOfFinish;
	}
	
	public Long getMoveCount() {
		return this.moveCount;
	}
	
	public void setCompetitionGame(boolean value) {
		this.isCompetitionGame = value;
	}
	
	public void setCompetitionId(int id) {
		this.compId = id;
	}
	
	public void setCompetitonName(String name) {
		this.competitionName = name;
	}
	
	
}
