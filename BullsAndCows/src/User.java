

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;




public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private UserGame game;
	private LocalDateTime from;
	private LocalDateTime to;
	
	
	private boolean takesPartInCompetition;
	private Competition subscribedCompetition;
	private int competitionId;
	
	public User(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public User(long id, LocalDateTime from, LocalDateTime to) {
		this.id = id;
		this.from = from;
		this.to = to;
	}
	
	public User(long id, String name, int compId) {
		this.id = id;
		this.name = name;
		this.competitionId = compId;
		this.takesPartInCompetition = true;
		
	}
	
	public LocalDateTime getDateTimeFrom() {
		return this.from;
	}
	
	public LocalDateTime getDateTimeTo() {
		return this.to;
	}
	
	public void setGame(long id, String name) throws Exception {
		this.game = new UserGame(id, name);
	
	}

	public UserGame getGame() {
		return this.game;
	}

	public String getName() {
		return name;
	}

	public long getId() {
		return id;
	}
	
	public ArrayList<UserGame> loadUserGames(LocalDateTime from, LocalDateTime to) throws IOException {
		ArrayList<UserGame> res =  new ArrayList<UserGame>();
		try (Stream<Path> walk = Files.walk( Paths.get( new File(".").getCanonicalPath() + "\\Profiles\\"+ getId() + "\\" ) )) {
		walk.filter(Files::isRegularFile).collect(Collectors.toList()).forEach( x -> {
				try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(new File(x.toString())))) {
	    			res.add((UserGame) reader.readObject());
				} catch (Exception e) {
					
				}
			});
		}
		return (ArrayList<UserGame>) res.stream().filter( n -> from.isBefore(n.getGameStart()) && to.isAfter(n.getGameFinish())).collect(Collectors.toList());
	}
	
	public void subscribeForCompetition(Competition competition) {
		this.subscribedCompetition = competition;
	}
	
	public Competition getCompetiton() {
		return this.subscribedCompetition;
	}

	public int getCompetitionId() {
		return this.competitionId;
	}
	
	public boolean isInCompetition() {
		return takesPartInCompetition;
	}
	
	public void setSubscribtion(boolean value) {
		this.takesPartInCompetition = value;
	}

	
	
}
