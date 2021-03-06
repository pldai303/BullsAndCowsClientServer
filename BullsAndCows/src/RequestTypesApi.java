
public interface RequestTypesApi {
	String REGISTER_USER = "users/register"; 
	String SAVE_USERS = "users/save"; //not used
	String LOAD_USERS = "users/load"; //not used
	String START_NEW_GAME = "users/startnewgame"; 
	String MAKE_MOVE = "users/makemove";
	String GET_GAMEID = "users/getgameid";
	String GET_FINISHED_GAMES = "users/getfinishedgames";
	
	String GET_COMP_MANAGER = "users/getcompetitionmanager"; 
	String SUBSCRIBE_FOR_COMPETITON = "users/subscribeforcompetition";
	String GET_USERS_IN_COMPETITION = "users/getusersincompetition";
}
