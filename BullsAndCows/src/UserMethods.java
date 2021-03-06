
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public interface UserMethods extends Serializable{

//GAME methods
UserCodes registerUser(User user) throws Exception;	
UserCodes startNewGame(User user) throws Exception;
void saveToFile(String fileName) throws Exception;
void loadFromFile(String fileName) throws Exception;
ArrayList<String> makeMove(String number) throws Exception;
ArrayList<UserGame> getFinishedGame(User user) throws Exception;
Long getGameId(User user) throws Exception;

CompetitionManager getCompetitionManager() throws Exception;
Competition subscribeForCompetition(User user) throws Exception;
ArrayList<User> getUsersInCompetition(Integer competitionId) throws Exception;

}
