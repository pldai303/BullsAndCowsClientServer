import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


class Task implements Callable<String> 
{
    private final String name;
 
    public Task(String name) {
        this.name = name;
        System.out.println("Task " + this.name + " was started at " + LocalDateTime.now().toString());
    }
 
    @Override
    public String call() throws Exception {
        return "Task [" + name + "] executed on : " + LocalDateTime.now().toString();
    }
}


class MySheduler {
	private ArrayList<Long> delaysBetweenCompetitions;
	private ArrayList<Long> competitionTimeDurations;
	private ArrayList<String> competitionNames;
	private LocalDateTime next;
	private int sizeOfMyShedule;
	
	
	public MySheduler() {
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


public class MyTests {
	
	public static void main(String[] args) throws IOException {
	String min = "16";
		
		LocalDateTime ld_s1 = LocalDateTime.parse("21.12.2021 21:" + min + ":00", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
		LocalDateTime ld_f1 = LocalDateTime.parse("21.12.2021 21:" + min + ":10", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
		
		LocalDateTime ld_s2 = LocalDateTime.parse("21.12.2021 21:"+ min + ":30", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
		LocalDateTime ld_f2 = LocalDateTime.parse("21.12.2021 21:"+ min + ":40", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
		
		LocalDateTime ld_s3 = LocalDateTime.parse("21.12.2021 21:"+ min + ":50", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
		LocalDateTime ld_f3 = LocalDateTime.parse("21.12.2021 21:"+ min + ":59", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
		

		MySheduler mSh = new MySheduler();
		mSh.createScheduledElement("Competition 1", ld_s1, ld_f1);
		mSh.createScheduledElement("Competition 2", ld_s2, ld_f2);
		mSh.createScheduledElement("Competition 3", ld_s3, ld_f3);
		
		
	
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
        List<ScheduledFuture<String>> results = new ArrayList<ScheduledFuture<String>>();
 
        for (int i = 0; i < mSh.Size(); i++) 
        {
            try {
				Thread.sleep(mSh.getDelay(i));
			} catch (InterruptedException e1) {
				
			}
        	ScheduledFuture<String> result = executor.schedule(new Task(mSh.getName(i)), mSh.geCompetitionTimeDurations(i),  TimeUnit.SECONDS);
            try {
				System.out.println(result.get());
			} catch (Exception e) {
		
			} 
        }
        executor.shutdown();
		
	}

}
