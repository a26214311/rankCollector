package senka;

import java.util.Date;

import org.json.JSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class Zcal {

	
	public static DBObject getBaseExpData(BasicDBList explist,int month){
		Date now = new Date();
		for(int i=0;i<explist.size();i++){
			DBObject expdata = (DBObject)explist.get(explist.size()-i-1);
			Date ts = (Date)expdata.get("ts");
			if(month==11){
				if(ts.getMonth()>0&&(ts.getYear()*12+ts.getMonth()<month+now.getYear()*12-12)){
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
	private static int[] monthOfDay = new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
	public static int getFullSub(BasicDBList explist,int month,JSONObject lastPair){
		Date now = new Date();
		int lastMonth = month-1;
		if(lastMonth==-1){
			lastMonth=lastMonth+12;
		}
		Date y0 = new Date();
		y0.setMonth(0);
		y0.setDate(0);
		y0.setHours(0);
		y0.setMinutes(0);
		y0.setSeconds(0);
		Date yb = new Date(y0.getTime()-60000*160);
		Date ym = null;
		int ymexp=0;
		
		Date then = new Date(now.getTime()+(now.getTimezoneOffset()+480)*60000);
		then.setMonth(lastMonth);
		then.setDate(monthOfDay[lastMonth]);
		then.setHours(21);
		then.setMinutes(20);
		then.setSeconds(0);
		if(lastMonth==11){
			then.setYear(now.getYear()-1);
		}
		
		
		Date nz = new Date(now.getTime()+(now.getTimezoneOffset()+480)*60000);
		nz.setDate(1);
		nz.setHours(1);
		nz.setMinutes(0);
		nz.setSeconds(0);
		
		int firstexp=0;
		Date firstts=null;
		int lastexp=0;
		Date lastts=null;
		int uexp=0;
		Date uts=null;
		
		for(int i=0;i<explist.size();i++){
			DBObject expData = (DBObject)explist.get(i);
			Date thents = (Date)expData.get("ts");
			int thenexp = Integer.valueOf(expData.get("d").toString());
			if(thents.getYear()*12+thents.getMonth()<now.getYear()*12+lastMonth){
				firstexp=thenexp;
				firstts=thents;
			}
			if(thents.getTime()<then.getTime()){
				lastexp=thenexp;
				lastts=thents;
			}
			
			if(uexp==0&&thents.getTime()>nz.getTime()){
				uexp=thenexp;
				uts = thents;
			}
			if(thents.getTime()<yb.getTime()){
				ym=thents;
				ymexp = thenexp;
			}
		}
		int subexp = lastPair.getInt("exp")-lastexp;
		int subsenka = subexp*7/10000;
//		System.out.println("=================");
//		System.out.println(lastexp);
//		System.out.println(lastts);
//		System.out.println(lastPair);

//		System.out.println(subsenka);
//		System.out.println("=================");
		if(month==0&&uexp>0){
			return lastPair.getInt("senka")-subsenka;
		}else{
			return -1;
		}
		
		
		
		
		
		
		

	}
}
