
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class UserMethodsImpl implements UserMethods {
	private static final long serialVersionUID = 1L;

	private HashMap<Long, User> allUsers = new HashMap<>();
	private HashMap<CompetitionInfo, Competition> competitions = new HashMap<>();
	
	private String fileName = "users.dat";
	private long userId = 0;

	public static UserMethods getEmptyUser() {
		return new UserMethodsImpl();
	}

	private UserMethodsImpl() {
		File myFile = new File(fileName);
		if (!myFile.exists()) {
			try {
				myFile.createNewFile();
			} catch (Exception e) {

			}
		} else {
			try {
				loadFromFile(fileName);
			} catch (Exception e) {

			}
		}
	}

	@Override
	public UserCodes registerUser(User user) throws Exception {
		if (allUsers.putIfAbsent(user.getId(), user) != null) {
			throw new Exception("User with the given ID already exist");
		}
		saveToFile(fileName);
		return UserCodes.OK;
	}

	public void saveToFile(String fileName) throws Exception {
		try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(new File(fileName)))) {
			writer.writeObject(allUsers);
		} catch (Exception e) {

		}
	}

	@SuppressWarnings("unchecked")
	public void loadFromFile(String fileName) throws Exception {
		try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(new File(fileName)))) {
			Object result = reader.readObject();
			allUsers = (HashMap<Long, User>) result;
		} catch (Exception e) {

		}
	}

	@Override
	public UserCodes startNewGame(User user) throws Exception {
		userId = user.getId();
		if (allUsers.containsKey(userId)) {
			if ( (allUsers.get(userId).getGame() != null) && (!allUsers.get(userId).getGame().isGameFinished()) ) {
				throw new Exception("User didn`t finish game");
			}
			
			allUsers.get(userId).setGame(userId, allUsers.get(userId).getName());
			return UserCodes.OK;
		} else
			throw new Exception("User not exists");
	}

	@Override
	public ArrayList<String> makeMove(String number) throws Exception {
		if (allUsers.containsKey(userId)) {
			if (allUsers.get(userId).isInCompetition()) {
				LocalDateTime dt = LocalDateTime.now();
				if ( (allUsers.get(userId).getCompetiton().getStartTime().isBefore(dt)) && (allUsers.get(userId).getCompetiton().getFinishTime().isAfter(dt)) ) {
					allUsers.get(userId).getGame().setCompetitionGame(true);
					int compId = allUsers.get(userId).getCompetiton().getCompetitionId();
					String compName = allUsers.get(userId).getCompetiton().getCompetitionName();
					allUsers.get(userId).getGame().setCompetitionId(compId);
					allUsers.get(userId).getGame().setCompetitonName(compName);
				} else {
					allUsers.get(userId).getGame().setCompetitionGame(false);
				}
			}
			
			if (!allUsers.get(userId).getGame().isGameFinished()) {
				allUsers.get(userId).getGame().makeGameMove(number);
				
				return allUsers.get(userId).getGame().getGameHistory();
			} else
				throw new Exception("This game is finished");
		
		} else {
			throw new Exception("User not exists"); 
		}
			
		
	}

	@Override
	public Long getGameId(User user) {
		if (allUsers.containsKey(user.getId())) {
			return allUsers.get(userId).getGame().getGameId();
		}
		return null;
	}

	@Override
	public ArrayList<UserGame> getFinishedGame(User user) throws Exception {
		if (allUsers.containsKey(user.getId())) {
			return allUsers.get(user.getId()).loadUserGames(user.getDateTimeFrom(), user.getDateTimeTo());
		} else {
			throw new Exception("User is not registered ");
		}
		
	}

	@Override
	public CompetitionManager getCompetitionManager() throws Exception {
		return GameServerAppl.compManager;
	}

	@Override
	public Competition subscribeForCompetition(User user) throws Exception {
		try {
		Competition result = GameServerAppl.compManager.selectCompetitionById(user);
		userId = user.getId();
		allUsers.get(userId).subscribeForCompetition(result);
		allUsers.get(userId).setSubscribtion(true);
		return  result;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public ArrayList<User> getUsersInCompetition(Integer competitionId) throws Exception {
		return GameServerAppl.compManager.selectParticipentsByCompId(competitionId);
	}

}
