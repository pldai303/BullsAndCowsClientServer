import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;




public class CompetitionManager implements Serializable{
	private static final long serialVersionUID = 1L;
	private File gameFile;
	private String fName = "CreatedGames";
	private LinkedHashMap<CompetitionInfo, Competition> competitions;
	
	public CompetitionManager() {
		competitions = new LinkedHashMap<>();
		try {
		readCompetitionsFile();
		} catch (Exception e) {
			
		}
	}
	
	public Competition selectCompetitionById(User user) throws Exception{
		Competition selectedCompetition = null; 
		for (Map.Entry<CompetitionInfo, Competition> entry : competitions.entrySet()) {
	        if (entry.getValue().getCompetitionId().equals(user.getCompetitionId())) {
	        	selectedCompetition = entry.getValue(); 
	        	try {
	        	selectedCompetition.addParticipant(user);
	        	} catch (Exception e) {
	        		throw e;
	        	}
	        }
	    }
		return selectedCompetition;
	}
	
	public ArrayList<User> selectParticipentsByCompId(Integer compId) {
		ArrayList<User> participents = null; 
		for (Map.Entry<CompetitionInfo, Competition> entry : competitions.entrySet()) {
	        if (entry.getValue().getCompetitionId().equals(compId)) {
	        	participents = entry.getValue().getCompetitionParticipants(); 
	        	
	        }
	    }
		return participents;
	}
	
	public Integer getMaxCompetitionId() {
		return competitions.entrySet().stream().mapToInt(v -> v.getValue().getCompetitionId()).max().orElse(0);
	}
	
	private String createPathToActiveGames() {
		String path = "";
		String profileFile = "\\Competitions\\" + fName + ".dat";
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
	
	@SuppressWarnings("unchecked")
	
	private void readCompetitionsFile() throws Exception {
		competitions.clear();
		  File file = new File("e:\\BullsAndCowsClientServer\\BullsAndCows\\Competitions\\" + fName + ".dat");
		    FileInputStream f = new FileInputStream(file);
		    ObjectInputStream s = new ObjectInputStream(f);
		    competitions = (LinkedHashMap<CompetitionInfo, Competition>) s.readObject();
		    s.close();
	}
	
	public void addCompetition(CompetitionInfo competitionInfo, Competition competition) throws Exception{
		
		if (competitions.containsKey(competitionInfo)) {
			throw new Exception("Such competition already exists");
		} else {
			competitionInfo.setCompetitionId(competitions.size());
			competitions.putIfAbsent(competitionInfo, competition);
			
			try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(new File(createPathToActiveGames())))) {
				writer.writeObject(competitions);
				writer.close();
			} catch (Exception e) {
				
			}
		}
	}
	
	public HashMap<CompetitionInfo, Competition> getCompetitions(){
		return competitions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((competitions == null) ? 0 : competitions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompetitionManager other = (CompetitionManager) obj;
		if (competitions == null) {
			if (other.competitions != null)
				return false;
		} else if (!competitions.equals(other.competitions))
			return false;
		return true;
	}
	
	
	
	
}
