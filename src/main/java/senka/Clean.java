package senka;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class Clean {
	public static void main(String[] args){
		System.out.println(111);
		Date d = new Date();
		d.setMonth(0);
		d.setDate(1);
		d.setHours(1);
		d.setMinutes(5);
		System.out.println(d);
		System.out.println(isKeyExpTs(d));
	}
	
	public static void cleanExpIfNecessary(DBCollection cl_senka,DBObject userData){
		
		BasicDBList exp = (BasicDBList)userData.get("exp");
		if(exp.size()>300){
			System.out.println("clear user data");
			System.out.println(userData.get("name"));
			Date now = new Date();
			int limitmon = now.getYear()*12+now.getMonth()-2;
			int maxerase = exp.size()-200;
			int erase = 0;
			BasicDBList expnew = new BasicDBList();
			Map<Integer, Integer> monmap= new HashMap<>();
			for(int i=1;i<exp.size();i++){
				DBObject expd = (DBObject)exp.get(i);
				Date ts = (Date)expd.get("ts");
				int mon = ts.getYear()*12+ts.getMonth();
				Object d = monmap.get(mon);
				if(!isKeyExpTs(ts)&&d!=null&&erase<maxerase&&mon<limitmon){
					erase++;
					//erase
				}else{
					monmap.put(mon, 1);
					expnew.add(expd);
				}
			}
			BasicDBObject update = new BasicDBObject();
			update.append("exp", expnew);
			cl_senka.update(userData, new BasicDBObject("$set",update));
		}
	}
	private static int[] monthOfDay = new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
	public static boolean isKeyExpTs(Date ts){
		int mon = ts.getMonth();
		int date = ts.getDate();
		int hour = ts.getHours();
		int min = ts.getMinutes();
		if(date==monthOfDay[mon]){
			if((hour==8&&min>30)||(hour==9&&min<30)){
				return true;
			}
		}
		if(date==1){
			if((hour==0&&min>30)||(hour==1&&min<30)){
				return true;
			}
		}
		return false;
	}
	

}
