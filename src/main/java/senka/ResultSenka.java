package senka;

import java.util.Date;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class ResultSenka {
	private static int[] monthOfDay = new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
	public static void main(String[] args) {
		try {
			getResultSenka(8,5);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static String handleResultApi(Map<String, String[]> data)throws Exception{
		int server = Integer.valueOf(data.get("server")[0]);
		int month;
		if(data.containsKey("m")){
			month=Integer.valueOf(data.get("m")[0]);
		}else{
			month=new Date().getMonth();
		}
		JSONObject j = new JSONObject();
		j.put("rs",getResultSenka(server, month));
		return j.toString();
	}
	
	public static JSONArray getResultSenka(int server,int month){
		JSONArray ja = new JSONArray();
		DBCursor dbc = null;
		DBCollection cl_result_senka = Util.dbr.getCollection("cl_result_senka");
		try {
			Date now = new Date();
			Date from = new Date();
			from.setMonth(month);
			from.setDate(1);
			from.setHours(0);
			from.setMinutes(0);
			from.setSeconds(0);
			Date to = new Date();
			to.setMonth(month);
			to.setDate(monthOfDay[month]);
			to.setHours(0);
			to.setMinutes(0);
			to.setSeconds(0);
			
			BasicDBObject query = new BasicDBObject("_id",new BasicDBObject("$gt",server+"_"+from.getTime()).append("$lt", server+"_"+to.getTime()));
			dbc = cl_result_senka.find(query);
			while (dbc.hasNext()) {
				DBObject dbObject = (DBObject) dbc.next();
				ja.put(dbObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(dbc !=null){
				dbc.close();
			}
		}
		return ja;
		
	}

}
