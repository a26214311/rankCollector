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
		DBCollection cl_senka = Util.db.getCollection("cl_senka_19");
		DBObject user = cl_senka.findOne(new BasicDBObject("name","片翼の堕天使"));
		cleanExpIfNecessary(cl_senka,user);
	}
	
	public static void cleanExpIfNecessary(DBCollection cl_senka,DBObject userData){
		
		BasicDBList exp = (BasicDBList)userData.get("exp");
		if(exp.size()>300){
			System.out.println("clear user data");
			System.out.println(userData.get("name"));
			Date now = new Date();
			int limitmon = now.getYear()*12+now.getMonth()-1;
			int maxerase = exp.size()-200;
			int erase = 0;
			BasicDBList expnew = new BasicDBList();
			Map<Integer, Integer> monmap= new HashMap<>();
			for(int i=1;i<exp.size();i++){
				DBObject expd = (DBObject)exp.get(i);
				Date ts = (Date)expd.get("ts");
				int mon = ts.getYear()*12+ts.getMonth();
				Object d = monmap.get(mon);
				if(d!=null&&erase<maxerase&&mon<limitmon){
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
}
