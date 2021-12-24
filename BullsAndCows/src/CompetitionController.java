import java.util.concurrent.Callable;

public class CompetitionController implements Callable<Competition> {
	
	private Competition competition;

	public CompetitionController(Competition competition) {
		this.competition = competition;
		this.competition.startCompetition();
	}
	
	@Override
	public Competition call() throws Exception {
		this.competition.finishCompetition();
		return this.competition;
	}



}
