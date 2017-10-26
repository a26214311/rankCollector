package lib;

import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import senka.Collector;
import senka.Login;
import senka.Rank;
import senka.Util;

public class TimerTask {
	public static int id8 = 8156938;
	public static int id19 = 19154349;
	public static int id16 = 16118483;
	public static int id18 = 18158245;
	public static int id15 = 15155022;
	private static String user8 = "1127805853@qq.com";
	private static String user19 = "bot1@rewards.msharebox.com";
	private static String user20 = "bot2@rewards.msharebox.com";
	private static String user18 = "bot3@rewards.msharebox.com";
	private static String user16 = "bot4@rewards.msharebox.com";
	private static String user15 = "bot5@rewards.msharebox.com";
	private static String pass8 = "987654321";
	private static String pass19 = "987654321";
	private static String pass20 = "9876543210";
	private static String pass18 = "987654321";
	private static String pass16 = "987654321";
	private static String pass15 = "9876543210";
	
	private static int[] monthOfDay = new int[]{31,28,31,30,31,30,31,31,30,31,30,31};

	static{
		Date  now = new Date(new Date().getTime()+(new Date().getTimezoneOffset()+480)*60000);
		int left1 = (int)(43200000-(now.getTime()-18002000)%43200000)/1000;
		System.out.println("--------------------------------");
		System.out.println("will get exp after "+left1/60+"minutes");
		ScheduledThreadPoolExecutor stpe1 = new ScheduledThreadPoolExecutor(15);
		stpe1.scheduleAtFixedRate(new Runnable() {
			public void run() {
				System.out.println(new Date());
				System.out.println("-----------------will get exp now------------");
				collectorTask();
			}
		}, left1, 43200, TimeUnit.SECONDS);
		
		int left2 = (int)(43200000-(now.getTime()-21700000)%43200000)/1000;
		System.out.println("will get senka after "+left2/60+"minutes");
		ScheduledThreadPoolExecutor stpe2 = new ScheduledThreadPoolExecutor(15);
		stpe2.scheduleAtFixedRate(new Runnable() {
			public void run() {
				System.out.println(new Date());
				System.out.println("-----------------will get senka now------------");
				rankTask();
			}
		}, left2, 43200, TimeUnit.SECONDS);
		
		
		
		
		
		
		int left3 = (int)(3600000-(now.getTime()+60000)%3600000)/1000;
		System.out.println("--------------------------------");
		System.out.println("will do hourly task after "+left3/60+"minutes");
		ScheduledThreadPoolExecutor stpe3 = new ScheduledThreadPoolExecutor(15);
		stpe3.scheduleAtFixedRate(new Runnable() {
			public void run() {
				working=0;
				Date now = new Date(new Date().getTime()+(new Date().getTimezoneOffset()+480)*60000);
				if(now.getDate()==monthOfDay[now.getMonth()]){
					int hour = now.getHours();
					if(hour>=15&&hour<=23){
						System.out.println(new Date());
						System.out.println("-----------------will do hourly collect exp now------------");
						collectorTask();
					}else{
						System.out.println(new Date());
						System.out.println("-----------------hourly task------------");
					}
				}
			}
		}, left3, 3600, TimeUnit.SECONDS);
		System.out.println("--------------------------------");
		
		
		int left4 = (int)(3600000-(now.getTime()+2000000)%3600000)/1000;
		System.out.println("--------------------------------");
		System.out.println("will do random task after "+left4/60+"minutes");
		ScheduledThreadPoolExecutor stpe4 = new ScheduledThreadPoolExecutor(15);
		stpe4.scheduleAtFixedRate(new Runnable() {
			public void run() {
				if(now.getDate()<monthOfDay[now.getMonth()]){
					System.out.println("--------------------------------");
					System.out.println("will do random collect task");
					randomTask();
				}
			}
		}, left4, 3600, TimeUnit.SECONDS);
		System.out.println("--------------------------------");
	}
	
	public static void randomTask(){
		Date now = new Date(new Date().getTime()+(new Date().getTimezoneOffset()+480)*60000);
		final int num;
		if(now.getHours()>=3&&now.getHours()<=5){
			num=666;
		}else{
			num=66;
		}
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token = getToken(8);
				if(token.length()>2){
					Collector.randomCollect(token, 8, num);
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token = getToken(16);
				if(token.length()>2){
					Collector.randomCollect(token, 16, num);
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token = getToken(19);
				if(token.length()>2){
					Collector.randomCollect(token, 19, num);
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token = getToken(15);
				if(token.length()>2){
					Collector.randomCollect(token, 15, num);
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token = getToken(18);
				if(token.length()>2){
					Collector.randomCollect(token, 18, num);
				}
			}
		}).start();
	}
	
	public static void init(){

	}
	
	public static void collectorTask(){
		working=0;
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token8 = getToken(8);
				if(token8.length()>2){
					working++;
					Collector.collectByLastSenka(token8, 8);
					working--;
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token19 = getToken(19);
				if(token19.length()>2){
					working++;
					Collector.collectByLastSenka(token19, 19);
					working--;
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token16 = getToken(16);
				if(token16.length()>2){
					working++;
					Collector.collectByLastSenka(token16, 16);
					working--;
				}
			}
		}).start();		
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token18 = getToken(18);
				if(token18.length()>2){
					working++;
					Collector.collectByLastSenka(token18, 18);
					working--;
				}
			}
		}).start();		
		new Thread(new Runnable() {
			@Override
			public void run() {
				String token15 = getToken(15);
				if(token15.length()>2){
					working++;
					Collector.collectByLastSenka(token15, 15);
					working--;
				}
			}
		}).start();		
	}
	
	public static int working=0;
	public static void rankTask(){
		working=0;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String token8 = getToken(8);
					if(token8.length()>2){
						working++;
						Rank.runRankTask(token8, 8, id8);
						Collector.collectByLastSenka(token8, 8);
						working--;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String token19 = getToken(19);
					if(token19.length()>2){
						working++;
						Rank.runRankTask(token19, 19, id19);
						Collector.collectByLastSenka(token19, 19);
						working--;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String token16 = getToken(16);
					if(token16.length()>2){
						working++;
						Rank.runRankTask(token16, 16, id16);
						Collector.collectByLastSenka(token16, 16);
						working--;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String token18 = getToken(18);
					if(token18.length()>2){
						working++;
						Rank.runRankTask(token18, 18, id18);
						Collector.collectByLastSenka(token18, 18);
						working--;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String token15 = getToken(15);
					if(token15.length()>2){
						working++;
						Rank.runRankTask(token15, 15, id15);
						Collector.collectByLastSenka(token15, 15);
						working--;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	
	
	public static String getToken(int server){
		DBCollection cl_token = Util.db.getCollection("cl_token");
		BasicDBObject query = new BasicDBObject("_id",server);
		DBObject tokenData = cl_token.findOne(query);
		String token;
		Date now = new Date();
		if(tokenData==null){
			token = login(server);
			if(token.length()>2&&token.length()<100){
				cl_token.save(new BasicDBObject("_id",server).append("token", token).append("ts", now));
			}
		}else{
			Date then = (Date)tokenData.get("ts");
			if(now.getTime()-then.getTime()>40000000){
				token = login(server);
				if(token.length()>2&&token.length()<100){
					cl_token.save(new BasicDBObject("_id",server).append("token", token).append("ts", now));
				}else{
					token = tokenData.get("token").toString();
				}
			}else{
				token = tokenData.get("token").toString();
			}
		}
		return token;
	}
	
	public static String login(int server){
		String token="";
		if(server==8){
			token=Login.login(user8,pass8);
		}else if(server==19){
			token=Login.login(user19,pass19);
		}else if(server==18){
			token=Login.login(user18,pass18);
		}else if(server==20){
			token=Login.login(user20,pass20);
		}else if(server==16){
			token=Login.login(user16,pass16);
		}else if(server==15){
			token=Login.login(user15,pass15);
		}
		return token;
	}
	
	
	public static void main(String[] args){
		System.out.println(123);
//		System.out.println(getToken(8));
	}
}
