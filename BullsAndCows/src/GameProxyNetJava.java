import java.util.ArrayList;
import java.util.Map;


public class GameProxyNetJava implements UserMethods {
	private NetJavaClient client;

	public GameProxyNetJava(NetJavaClient client)  {
		this.client = client;
		
	}
	
	private static final long serialVersionUID = 1L;
	@Override
	public UserCodes registerUser(User user) throws Exception {
		return client.send(RequestTypesApi.REGISTER_USER, user);
	}
	@Override
	public void saveToFile(String fileName) throws Exception {
		client.send(RequestTypesApi.SAVE_USERS, fileName);
	}
	@Override
	public void loadFromFile(String fileName) throws Exception {
		client.send(RequestTypesApi.LOAD_USERS, fileName);
	}
	@Override
	public UserCodes startNewGame(User user) throws Exception {
		return client.send(RequestTypesApi.START_NEW_GAME, user);
	}
	@Override
	public ArrayList<String> makeMove(String number) throws Exception {
		return client.send(RequestTypesApi.MAKE_MOVE, number);
	}
	@Override
	public Long getGameId(User user) throws Exception {
		return client.send(RequestTypesApi.GET_GAMEID, user);
	}
	@Override
	public ArrayList<UserGame> getFinishedGame(User user) throws Exception {
		return client.send(RequestTypesApi.GET_FINISHED_GAMES, user);
	}
	@Override
	public CompetitionManager getCompetitionManager() throws Exception {
		return client.send(RequestTypesApi.GET_COMP_MANAGER, null);
	}
	@Override
	public Competition subscribeForCompetition(User user) throws Exception {
		return client.send(RequestTypesApi.SUBSCRIBE_FOR_COMPETITON, user);
	}
	@Override
	public ArrayList<User> getUsersInCompetition(Integer competitionId) throws Exception {
		 return client.send(RequestTypesApi.GET_USERS_IN_COMPETITION, competitionId);
	}

}
