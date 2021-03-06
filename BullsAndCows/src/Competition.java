//---------10.12
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Competition implements Serializable{

	private ArrayList<User> competitionParticipants = new ArrayList<>();
	private boolean competitionIsOpen;
	private boolean outOfDate;
	private String compName;
	private Integer compId;
	private LocalDateTime start;
	private LocalDateTime finsih;
	
	
	public Competition(String compName, Integer compId, LocalDateTime start, LocalDateTime finish) {
		this.competitionIsOpen = false;
		this.outOfDate = false;
		this.compName = compName;
		this.compId = compId;
		this.start = start;
		this.finsih = finish;
	}
	
	public void finishCompetition() {
		this.competitionIsOpen = false;
		this.outOfDate = true;
		System.out.println("Competition " + this.compName + " finished at: " + LocalDateTime.now());
	}
	
	public void startCompetition() {
		this.competitionIsOpen = true;
		System.out.println("Competition " + this.compName + " opened at: " + LocalDateTime.now());
	}
	
	public ArrayList<User> getCompetitionParticipants() {
		return competitionParticipants;
	}
	
	public void addParticipant(User user) throws Exception{
		for (User u : competitionParticipants) {
			if (u.getId() == user.getId()) {
				throw new Exception("This user already takes part in this competition!");
			}
		}
		competitionParticipants.add(user);
		competitionParticipants.stream().forEach(n -> System.out.println(n.getId() + " " + n.getName()));
	}
	
	public Integer getCompetitionId() {
		return compId;
	}
	
	public String getCompetitionName() {
		return compName;
	}
	
	public LocalDateTime getStartTime() {
		return this.start;
	}

	public LocalDateTime getFinishTime() {
		return this.finsih;
	}
	
	
}
