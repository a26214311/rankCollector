package senka;

import java.util.Date;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class Zcal {

	
	public static DBObject getBaseExpData(BasicDBList explist,int month){
		Date now = new Date();
		for(int i=0;i<explist.size();i++){
			DBObject expdata = (DBObject)explist.get(explist.size()-i-1);
			Date ts = (Date)expdata.get("ts");
			if(month==11){
				if(ts.getMonth()>0&&ts.getMonth()<month){
					return expdata;
				}
			}else{
				if(ts.getYear()*12+ts.getMonth()<now.getYear()*12+month){
					return expdata;
				}
			}
			
		}
		return null;
	}
	
	
	public static DBObject getFirstExpData(BasicDBList explist,int month){
		for(int i=0;i<explist.size();i++){
			DBObject expdata = (DBObject)explist.get(i);
			Date ts = (Date)expdata.get("ts");
			if(ts.getMonth()==month){
				if(ts.getDate()==1&&ts.getHours()<1){
					
				}else{
					return expdata;
				}
			}
		}
		return null;
	}
}
