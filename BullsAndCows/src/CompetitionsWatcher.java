import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompetitionsWatcher {

	public static void main(String[] args) throws IOException {
		ArrayList<CompetitionObject> res =  new ArrayList<CompetitionObject>();
		
		try (Stream<Path> walk = Files.walk( Paths.get( "e:\\BullsAndCowsClientServer\\BullsAndCows\\FinishedCompetitions\\" ) )) {
		walk.filter(Files::isRegularFile).collect(Collectors.toList()).forEach( x -> {
				try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(new File(x.toString())))) {
	    			res.add((CompetitionObject) reader.readObject());
				} catch (Exception e) {
					
				}
			});
		}
		
		res.stream().forEach( n -> {
			System.out.println(n.compId + " " + n.gameId + " " + n.userName + " " + n.movesCount + " " + n.history.toString() );
			
		});
		
		
		

	}

}
