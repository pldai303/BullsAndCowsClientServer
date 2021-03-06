

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;




public class GameProtocol implements ApplProtocolJava {
//server send
	UserMethods userMethods;

	private ResponseJava getWrongDataResponse(Exception e) {
		return new ResponseJava(ResponseCode.WRONG_REQUEST_DATA, e);
	}
	public GameProtocol(UserMethods employeesMethods) {
	this.userMethods = employeesMethods;
	}
	@Override
	public ResponseJava getResponse(RequestJava request) {
		try {
			String methodName = request.requestType.replace("/", "_");
			Method requestMethod = GameProtocol.class.getDeclaredMethod(methodName,Serializable.class);
			return (ResponseJava) requestMethod.invoke(this, request.data);
			} catch (Exception e) {
			return new ResponseJava(ResponseCode.WRONG_REQUEST_TYPE, request.requestType);
			}
	}
	@SuppressWarnings("unused")
	private ResponseJava users_getfinishedgames(Serializable data) {
		try {
			return new ResponseJava(ResponseCode.OK, new ArrayList<UserGame>((Collection<UserGame>)userMethods.getFinishedGame((User)data)   )) ;
		} catch (Exception e) {
			return getWrongDataResponse(e);
		}
	}
	
	//server send
	@SuppressWarnings("unused")
	private ResponseJava users_getgameid(Serializable data) {
		try {
			return new ResponseJava(ResponseCode.OK, (Long)userMethods.getGameId((User)data));
		} catch (Exception e) {
			return getWrongDataResponse(e);
		}
	}
	@SuppressWarnings("unused")
	private ResponseJava users_makemove(Serializable data) {
		try {
			return new ResponseJava(ResponseCode.OK, new ArrayList<String>((Collection<String>)userMethods.makeMove((String)data)));
		} catch (Exception e) {
			return getWrongDataResponse(e);
		}
	}
	@SuppressWarnings("unused")
	private ResponseJava users_startnewgame(Serializable data) {
		try {
			return new ResponseJava(ResponseCode.OK, userMethods.startNewGame((User)data));
		} catch (Exception e) {
			return getWrongDataResponse(e);
		}
	}
	@SuppressWarnings("unused")
	private ResponseJava users_register(Serializable data) {
		try {
			return new ResponseJava(ResponseCode.OK, userMethods.registerUser((User)data));
		} catch (Exception e) {
			return getWrongDataResponse(e);
		}
	}
	
	@SuppressWarnings("unused")
	private ResponseJava users_getcompetitionmanager(Serializable data) {
		try {            
		return new ResponseJava(ResponseCode.OK, userMethods.getCompetitionManager());
		} catch (Exception e) {
			return getWrongDataResponse(e);
		}
	}
	
	@SuppressWarnings("unused")
	private ResponseJava users_subscribeforcompetition(Serializable data) {
		try {
			return new ResponseJava(ResponseCode.OK, userMethods.subscribeForCompetition((User)data));
		} catch (Exception e) {
			return getWrongDataResponse(e);
		}
	}
	
	@SuppressWarnings("unused")
	private ResponseJava users_getusersincompetition(Serializable data) {
		try {
			return new ResponseJava(ResponseCode.OK, new ArrayList<User>((Collection<User>)userMethods.getUsersInCompetition((Integer)data))) ;
		} catch (Exception e) {
			return getWrongDataResponse(e);
		}
	}
	
	
	
}
