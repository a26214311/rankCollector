package lib;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
	public static int id18 = 18177412;
	public static int id15 = 15155022;
	public static int id20 = 20601245;
	public static int id14 = 14161524;
	private static String user8 = "1127805853@qq.com";
	private static String user19 = "bot1@rewards.msharebox.com";
	private static String user20 = "bot2@rewards.msharebox.com";
	private static String user18 = "b09@rewards.msharebox.com";
	private static String user16 = "bot4@rewards.msharebox.com";
	private static String user15 = "bot5@rewards.msharebox.com";
	private static String user14 = "b04@rewards.msharebox.com";
	private static String pass8 = "987654321";
	private static String pass19 = "987654321";
	private static String pass20 = "987654321";
	private static String pass18 = "987654321";
	private static String pass16 = "9876543210";
	private static String pass15 = "9876543210";
	private static String pass14 = "9876543210";
	
	/*
	 * backup id:
	 * 
	 * 16:
	 * bot14@rewards.msharebox.com    9876543210
	 * 
	 * 14:
	 * 
	 * 
	 * 
	 * 
	 */
	
	private static int[] monthOfDay = new int[]{31,28,31,30,31,30,31,31,30,31,30,31};

	static{
		Date  now = new Date();
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
					if(hour>=14&&hour<=23){
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
		
		
		int left4 = (int)(3600000-(now.getTime()+1600000)%3600000)/1000;
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
				int serverid = 18;
				nowworking.put(serverid, 1);
				try {
					String token = getToken(serverid);
					if(token.length()>2){
						working++;
						Collector.collectByLastSenka(token, serverid);
						working--;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					nowworking.remove(serverid);
				}
			}
		}).start();		
		
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				int serverid = 8;
				nowworking.put(serverid, 1);
				try {
					String token = getToken(serverid);
					if(token.length()>2){
						working++;
						Collector.collectByLastSenka(token, serverid);
						working--;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					nowworking.remove(serverid);
				}
			}
		}).start();		
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				int serverid = 14;
				nowworking.put(serverid, 1);
				try {
					String token = getToken(serverid);
					if(token.length()>2){
						working++;
						Collector.collectByLastSenka(token, serverid);
						working--;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					nowworking.remove(serverid);
				}
			}
		}).start();		
	
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				int serverid = 16;
				nowworking.put(serverid, 1);
				try {
					String token = getToken(serverid);
					if(token.length()>2){
						working++;
						Collector.collectByLastSenka(token, serverid);
						working--;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					nowworking.remove(serverid);
				}
			}
		}).start();		
	}
	
	
	public static Map<Integer, Integer> nowworking = new HashMap<>();
	public static int working=0;
	public static void rankTask(){
		working=0;

		
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				int serverid = 18;
//				nowworking.put(serverid, 1);
//				try {
//					String token = getToken(serverid);
//					if(token.length()>2){
//						working++;
//						Rank.runRankTask(token, serverid, id18);
//						Collector.collectByLastSenka(token, serverid);
//						working--;
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}finally{
//					nowworking.remove(serverid);
//				}
//			}
//		}).start();
		
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				int serverid = 8;
				nowworking.put(serverid, 1);
				try {
					String token = getToken(serverid);
					if(token.length()>2){
						working++;
						Rank.runRankTask(token, serverid, id8);
						Collector.collectByLastSenka(token, serverid);
						working--;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					nowworking.remove(serverid);
				}
			}
		}).start();

//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				int serverid = 14;
//				nowworking.put(serverid, 1);
//				try {
//					String token = getToken(serverid);
//					if(token.length()>2){
//						working++;
//						Rank.runRankTask(token, serverid, id14);
//						Collector.collectByLastSenka(token, serverid);
//						working--;
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}finally{
//					nowworking.remove(serverid);
//				}
//			}
//		}).start();
		
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				int serverid = 16;
//				nowworking.put(serverid, 1);
//				try {
//					String token = getToken(serverid);
//					if(token.length()>2){
//						working++;
//						Rank.runRankTask(token, serverid, id16);
//						Collector.collectByLastSenka(token, serverid);
//						working--;
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}finally{
//					nowworking.remove(serverid);
//				}
//			}
//		}).start();
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
		}else if(server==14){
			token=Login.login(user14,pass14);
		}
		return token;
	}
	
	
	public static void main(String[] args){
		System.out.println(123);
//		System.out.println(getToken(8));
	}
}
