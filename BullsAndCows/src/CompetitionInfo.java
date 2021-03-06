import java.io.Serializable;
import java.time.LocalDateTime;

public class CompetitionInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	private LocalDateTime startAt;
	private LocalDateTime finishAt;
	private Integer competitionId;
	private String competitionName;
	public CompetitionInfo(String competitionName, LocalDateTime startAt, LocalDateTime finishAt) {
		this.startAt = startAt;
		this.finishAt = finishAt;
		this.competitionName = competitionName;
	}
	
	
	public void setCompetitionId(Integer competitionId) {
		this.competitionId = competitionId;
	}
	
	public LocalDateTime getStartAt() {
		return startAt;
	}
	
	public Integer getCompetitionId() {
		return competitionId;
	}
	
	public LocalDateTime getFinishAt() {
		return finishAt;
	}
	
	public String getName() {
		return competitionName;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((competitionId == null) ? 0 : competitionId.hashCode());
		result = prime * result + ((competitionName == null) ? 0 : competitionName.hashCode());
		result = prime * result + ((finishAt == null) ? 0 : finishAt.hashCode());
		result = prime * result + ((startAt == null) ? 0 : startAt.hashCode());
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
		CompetitionInfo other = (CompetitionInfo) obj;
		if (competitionId == null) {
			if (other.competitionId != null)
				return false;
		} else if (!competitionId.equals(other.competitionId))
			return false;
		if (competitionName == null) {
			if (other.competitionName != null)
				return false;
		} else if (!competitionName.equals(other.competitionName))
			return false;
		if (finishAt == null) {
			if (other.finishAt != null)
				return false;
		} else if (!finishAt.equals(other.finishAt))
			return false;
		if (startAt == null) {
			if (other.startAt != null)
				return false;
		} else if (!startAt.equals(other.startAt))
			return false;
		return true;
	}


	


}
