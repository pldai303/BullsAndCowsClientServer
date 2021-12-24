import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CompetitionSheduler {
	private ArrayList<Long> delaysBetweenCompetitions;
	private ArrayList<Long> competitionTimeDurations;
	private ArrayList<String> competitionNames;
	private LocalDateTime next;
	private int sizeOfMyShedule;
	
	
	public CompetitionSheduler() {
		this.delaysBetweenCompetitions = new ArrayList<Long>();
		this.competitionTimeDurations = new ArrayList<Long>();
		this.competitionNames = new ArrayList<String>();
		this.sizeOfMyShedule = 0;
	}
	
	public void createScheduledElement(String compName, LocalDateTime start, LocalDateTime finish) {
		if (delaysBetweenCompetitions.size() == 0) {
			delaysBetweenCompetitions.add(Duration.between(LocalDateTime.now(), start).getSeconds()*1000);
			
		} else {
			delaysBetweenCompetitions.add(Duration.between(next, start).getSeconds()*1000);
		}
		next = finish;
		competitionTimeDurations.add(Duration.between(start, finish).getSeconds());
		competitionNames.add(compName);
		this.sizeOfMyShedule++;
	} 
	
	
	public long getDelay(int idx) {
		return delaysBetweenCompetitions.get(idx);
	}
	public long geCompetitionTimeDurations(int idx) {
		return competitionTimeDurations.get(idx);
	}
	public String getName(int idx) {
		return competitionNames.get(idx);
	}
	public int Size() {
		return sizeOfMyShedule;
	}
	
}
