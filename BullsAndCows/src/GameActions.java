

import java.io.Serializable;
import java.time.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class GameActions implements Serializable {
	private static final long serialVersionUID = 1L;
	private static UserMethods usersServices;
	private static User user;
	

	private GameActions() {

	}
	
	//---10.12
	public static Item[] getServerItems() throws Exception {
		return getServerMenuItems();
	} 
	
	private static Item[] getServerMenuItems() throws Exception {
		Item createCompetition = Item.of("Create competition", GameActions::createCompetition);
		Item watchActiveCompetitions = Item.of("Show active competitions", GameActions::showActiveCompetition);
		Item watchFinishedCompetitions = Item.of("Lounch competitions", GameActions::startAllCompetitions);
		Item gracefullyShutDown = Item.of("Shutdown server", GameActions::gracefullyShutDown);
		Item[] items = {createCompetition, watchActiveCompetitions, watchFinishedCompetitions, gracefullyShutDown};
		return items;
	}
	
	
	private static void createCompetition(InputOutput io) {
		String competitionName = io.readString("Enter competition name");
		LocalDateTime competitionStartAt = io.readDateTime("Enter date and time of competition start",  "dd.MM.yyyy HH:mm:ss");
		LocalDateTime competitionFinishAt = io.readDateTime("Enter date and time of competition finish",  "dd.MM.yyyy HH:mm:ss");
		CompetitionInfo compInfo = new CompetitionInfo(competitionName, competitionStartAt, competitionFinishAt);
		try {
			Integer compId = GameServerAppl.compManager.getMaxCompetitionId();
			Competition competition = new Competition(competitionName, compId+1, competitionStartAt, competitionFinishAt); 
			GameServerAppl.compManager.addCompetition(compInfo, competition);
			io.writeObjectLine( "Competition created successfully!");
		} catch (Exception e) {
			io.writeObjectLine(e.getMessage());
		}
	}
	
	private static void showActiveCompetition(InputOutput io) {
		printActiveCompetitionsList(io, GameServerAppl.compManager.getCompetitions());
	}
	
	private static void startAllCompetitions(InputOutput io) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(() -> {
			GameServerAppl.lounchSheduling();
		});
	}
	
	private static void gracefullyShutDown(InputOutput io) {
		
	}
	
	
	//----10.12
	public static Item[] getUsersItems(UserMethods usersServices) throws Exception {
		GameActions.usersServices = usersServices;
		return getItems();
	}

	private static LocalDateTime getDateTimeStart(InputOutput io) {
		return io.readDateTime("Enter date and time from (dd.MM.yyyy HH:mm:ss)", "dd.MM.yyyy HH:mm:ss");
	}

	private static LocalDateTime getDateTinmeFinish(InputOutput io) {
		return io.readDateTime("Enter date and time to (dd.MM.yyyy HH:mm:ss)", "dd.MM.yyyy HH:mm:ss");
	}

	private static long getId(InputOutput io) {
		return io.readLong("Enter id");
	}

	private static String getName(InputOutput io) {
		return io.readString("Enter user name");
	}

	private static Item[] getItems() throws Exception {
		Item regUser = Item.of("Register user / Login", GameActions::registerUser);
		Item startNewGame = Item.of("Start new game", GameActions::startNewGame);
		Item enterNumber = Item.of("Enter number", GameActions::enterNumber);
		Item displayHistory = Item.of("Display history of finished games", GameActions::displayHistoryFinishedGames);
		Item getAvailableCompetitions = Item.of("Get available competitions", GameActions::displayAvailableCompetitions);
		Item subscribeForCompetition = Item.of("Subscribe for competition", GameActions::subscribeForCompetition);
		Item[] items = { regUser, startNewGame, enterNumber, displayHistory, getAvailableCompetitions, subscribeForCompetition, Item.exit() };
		return items;
	}

	private static void registerUser(InputOutput io) {
		user = new User(getId(io), getName(io));
		try {
			io.writeObjectLine(usersServices.registerUser(user));
		} catch (Exception e) {
			io.writeObjectLine(e.getMessage());
		}

	}

	private static void startNewGame(InputOutput io) {
		user = new User(getId(io), "");
		UserCodes res = UserCodes.NOT_EXISTS;
		try {
			res = usersServices.startNewGame(user);
			if (res == UserCodes.NOT_EXISTS) {
				io.writeObjectLine(res);
			} else {
				io.writeObjectLine("Game ID: " + usersServices.getGameId(user));
			}
		} catch (Exception e) {
			io.writeObjectLine(e.getMessage());
		}
	}

	private static void enterNumber(InputOutput io) {
		if (user != null) {
			try {
				String number = io.readString("Enter number");
				ArrayList<String> res = usersServices.makeMove(number);
				res.stream().forEach(System.out::println);
			} catch (Exception e) {
				io.writeObjectLine(e.toString());
			}
		} else {
			io.writeObjectLine("Game is not active");
		}
	}

	private static void displayHistoryFinishedGames(InputOutput io) {
		user = new User(getId(io), getDateTimeStart(io), getDateTinmeFinish(io));
		try {
			ArrayList<UserGame> finishedGames = usersServices.getFinishedGame(user);
			if (finishedGames.isEmpty()) {
				io.writeObjectLine("No games for specified period");
			} else {
				io.writeGameTitle();
				finishedGames.stream().forEach(x -> {
					io.writeGameInfo(x);
				});
			}
		} catch (Exception e) {
			io.writeObjectLine(e.getMessage());
		}
	}

	private static void displayAvailableCompetitions(InputOutput io) {
		try {
		printActiveCompetitionsList(io, usersServices.getCompetitionManager().getCompetitions());
		} catch (Exception e) {
			
		}
	}
	
	private static void subscribeForCompetition(InputOutput io) {
		try {
		if (user != null) {
			HashMap<CompetitionInfo, Competition> competitionMap = usersServices.getCompetitionManager().getCompetitions();
			if (competitionMap != null) {
			printActiveCompetitionsList(io, competitionMap);
			int compId = io.readInt("Enter competiton ID to subscribe:");
			user = new User(user.getId(), user.getName(), compId);
			Competition subscribtion = usersServices.subscribeForCompetition(user);
			competitionMap = usersServices.getCompetitionManager().getCompetitions();
			printActiveCompetitionsList(io, competitionMap);		
		}
		} else {
			io.writeObjectLine("User didn`t registered or logined!");
		}
		} catch (Exception e) {
			io.writeObjectLine(e.toString());
		}
	}
	
	//move to helper
	private static void printActiveCompetitionsList(InputOutput io, Map<CompetitionInfo, Competition> availableCompetitions) {
		if (availableCompetitions.size() > 0) {
			io.writeObjectLine(" ".repeat(41) + "Active competitions" + " ".repeat(41));
			io.writeObjectLine("-".repeat(111));
			io.writeObjectLine("|   Comp. ID:   |         Comp. name:          |      Time of start      |       Time of end       | Players |");
			io.writeObjectLine("-".repeat(111));
			availableCompetitions.entrySet().stream().forEach(
					n-> {
						CompetitionInfo compInfo = n.getKey();
						Competition comp = n.getValue();
						String str = "";
						String id =   String.valueOf(comp.getCompetitionId()); //15
						int pos = (15 - id.length()) / 2;
						str = "|" + " ".repeat(pos) + id + " ".repeat(pos) + "|";		
						String compName = comp.getCompetitionName();//30
						pos = (30 - compName.length()) / 2;
						str = str + " ".repeat(pos) + compName + " ".repeat(pos) + "| ";
						String dtStart =  compInfo.getStartAt().toString();//25
						pos = (25 - dtStart.length()) / 2;
						str = str + " ".repeat(pos) + dtStart + " ".repeat(pos) + "|";
						String dtFinish =  compInfo.getFinishAt().toString();//25
						pos = (25 - dtFinish.length()) / 2;
						str = str + " ".repeat(pos) + dtFinish + " ".repeat(pos) + "|";
						String partCount = "";
						try {
						partCount = String.valueOf(usersServices.getUsersInCompetition(comp.getCompetitionId()).size()); //9
						} catch (Exception e) {
							partCount = String.valueOf(n.getValue().getCompetitionParticipants().size());
						}
						pos = (9 - partCount.length()) / 2;
						str = str + " ".repeat(pos) + partCount + " ".repeat(pos) + "|";
						io.writeObjectLine(str);
					}
					);
			io.writeObjectLine("-".repeat(111));
		}
	}
	
	
}
