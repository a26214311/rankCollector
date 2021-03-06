package senka;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import javax.naming.ldap.Rdn;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import lib.TimerTask;

public class Collector {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(123123);
		long t1 = new Date().getTime();
		try {
//			System.out.println(Collector.collectById(19119783, "ccfd764d5e639a567a4157ef01210a61998918f7", 19));
			collectById(8081106, "2805578c33a6f5ee81609d8ce77d32b9257d3c34", 8);
//			System.out.println(collectById(19161718, "162ebccc78cb0c33676c3b107502515ffd013da5", 19));
		} catch (Exception e) {
			e.printStackTrace();
		}
		long t2 = new Date().getTime();
		System.out.println("finish:"+(t2-t1));
	}
	
	public static void test()throws Exception{
		String path = "/kcsapi/api_req_member/get_practice_enemyinfo";
		String id = "8156938";
		String token = "8c3f8fa5533a18f92ac54c65022491eb2900125e";
		String param = "api%5Ftoken="+token+"&api%5Fmember%5Fid="+id+"&api%5Fverno=1";
		String s = Lib.ApiPost(path, param, token,8);
		System.out.println(s);
	}
	
	public static void runCollector(Map<String, String[]> data)throws Exception{
		final String type = data.get("type")[0];
		final String server = data.get("server")[0];
		final String token = TimerTask.getToken(Integer.valueOf(server));
		if(type.equals("init")){
			final int from = Integer.valueOf(data.get("from")[0]); 
			final int to = Integer.valueOf(data.get("to")[0]); 
			final int thread = Integer.valueOf(data.get("thread")[0]); 
			if(from<700000||to-from>500000||thread>20){
				return;
			}else{
				for(int i=0;i<thread;i++){
					final int begin = from+i*(to-from)/thread;
					final int end = from+(i+1)*(to-from)/thread;
					new Thread(new Runnable() {
						public void run() {
							int f = 0;
							for(int j=begin;j<end;j++){
								String path = "/kcsapi/api_req_member/get_practice_enemyinfo";
								int id=j;
								String param = "api%5Ftoken="+token+"&api%5Fmember%5Fid="+id+"&api%5Fverno=1";
								try {
									String r = Lib.ApiPost(path, param, token, Integer.valueOf(server));
									if(r.startsWith("svdata="));
									JSONObject jd = new JSONObject(r.substring(7));
									if(j%100==0){
										System.out.println(j);
									}
									int resultCode = jd.getInt("api_result");
									if(resultCode==1){
										JSONObject data = jd.getJSONObject("api_data");
										save(data,server+"");
									}else{
										f++;
										System.out.println(param);
										System.out.print("\nfailed get info:"+j+"\n");
										System.out.print(jd);
									}
									if(f>5){
										break;
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								
							}							
						}
					}).start();
				}
			}
		}
	}
	
	public static JSONObject collectById(int id,String token,int server)throws Exception{
		return collectById_D(id, token, server,0);
	}
	
	public static JSONObject collectById_D(int id,String token,int server,int failedc)throws Exception{
		if(failedc>1){
			return null;
		}
		String path = "/kcsapi/api_req_member/get_practice_enemyinfo";
		String param = "api%5Ftoken="+token+"&api%5Fmember%5Fid="+id+"&api%5Fverno=1";
		try {
			String r = Lib.ApiPost(path, param, token, server);
			if(r.startsWith("svdata="));
			JSONObject jd = new JSONObject(r.substring(7));
			int resultCode = jd.getInt("api_result");
			if(resultCode==1){
				JSONObject data = jd.getJSONObject("api_data");
				System.out.println(data);
				save(data,server+"");
				return data;
			}else{
				return collectById_D(id, token, server,failedc+1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return collectById_D(id, token, server,failedc+1);
		}
	}
	
	private static Random rd = new Random();
	public static void randomCollect(String token,int server,int num){
		if(server!=8&&server!=18){
			return;
		}
		DBCollection cl_senka = Util.db.getCollection("cl_senka_"+server);
		long count = cl_senka.count();
		if(server==8){
			count=count-87306;
		}
		try {
			for(int i=0;i<num;i++){
				int id = rd.nextInt((int)count)+server*1000000+1;
				DBObject user = cl_senka.findOne(new BasicDBObject("_id",id));
				String info = user.get("info").toString();
				JSONObject infoj = new JSONObject(info);
				int rank = infoj.getInt("api_rank");
				if(rank>1){
					System.out.println(id);
					collectById(id, token, server);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void collectByServer(Map<String, String[]> data){
		int server = Integer.valueOf(data.get("server")[0]);
		String token = TimerTask.getToken(server);
		collectByLastSenka(token, server);
	}
	
	
	
	public static void collectByLastSenka(String token,int server){
		if(server!=8&&server!=18){
			return;
		}
		DBCollection cl_n_senka = Util.db.getCollection("cl_n_senka_"+server);
		DBCursor dbc = null;
		Date now = new Date();
		long searchBefore = 60000000L;
		boolean monthinit = false;
		if(now.getDate()==1&&now.getHours()<3){
			searchBefore = 86400000L*28;
			monthinit=true;
		}
	
		int rankNo = Util.getRankDateNo(now);
		try {
			System.out.println("start collect");
			String key = "d"+now.getMonth();
			BasicDBObject proj = new BasicDBObject();
			proj.append(key, 1);
			proj.append("id", 1);
			proj.append("_id", 1);
			BasicDBObject query = new BasicDBObject("ts",new BasicDBObject("$gt",new Date(now.getTime()-searchBefore)));
			//query.append("_id", "　");
			dbc = cl_n_senka.find(query,proj); //1000 min
			int all=0;
			while (dbc.hasNext()) {
				all++;
				if(all%100==0){
					System.out.println(all);
				}
				DBObject dbObject = (DBObject) dbc.next();
				
				String name = dbObject.get("_id").toString();
				
				if(monthinit){
					NameHandler.handleName(name,null,-1,1,server,token);
					continue;
				}
				BasicDBList dbl = (BasicDBList)dbObject.get(key);
				ArrayList<DBObject> matched = new ArrayList<>();
				for(int i=dbl.size();i>0;i--){
					DBObject senkainfo = (DBObject)dbl.get(i-1);
					int thenDateNo = Integer.valueOf(senkainfo.get("ts").toString());
					if(thenDateNo==rankNo){
						matched.add(senkainfo);
					}else{
						break;
					}
				}
				if(matched.size()==0){
					
				}else if(matched.size()==1){
					DBObject nameData = matched.get(0);
					Object tido = nameData.get("id");
					int tid=0;
					if(tido!=null){
						tid = Integer.valueOf(tido.toString());
					}
					NameHandler.handleName(name,matched.get(0),tid,1,server,token);
				}else if(matched.size()>1){
					System.out.println(matched);
					System.out.println(name);
					NameHandler.handleName(name,matched.get(0),-1,matched.size(),server,token);
				}
			}
			System.out.println("all:"+all);
			Calculator.calculateTask(server);
			System.out.println("finish:"+server);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(dbc!=null){
				dbc.close();
			}
		}
	}
	
	
	
	public static int save(JSONObject data,String server)throws Exception{
		Date now = new Date();
		String name = data.getString("api_nickname");
		JSONArray expa = data.getJSONArray("api_experience");
		int exp = expa.getInt(0);
		int id = data.getInt("api_member_id");
		BasicDBObject update = new BasicDBObject();
		BasicDBObject expdata = new BasicDBObject();
		expdata.append("d", exp);
		expdata.append("ts",now);
		int lv = data.getInt("api_level");
		int rank = data.getInt("api_rank");
		update.append("$set", new BasicDBObject("name", name).append("e", exp).append("lv", lv).append("rank", rank).append("info",data.toString()).append("ts", now));
		update.append("$push", new BasicDBObject("exp",expdata));
		BasicDBObject query = new BasicDBObject();
		query.append("_id", id);
		DBCollection cl_senka=Util.db.getCollection("cl_senka_"+server);
		System.out.println("save name:"+name);
		cl_senka.update(query, update,true,false);
		return 0;
	}
	
	
	
	 public static String  Post(String urlStr,String param,String token) throws Exception{
	         URL url = new URL(urlStr);
	         HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	         conn.setRequestMethod("POST");
	         conn.setConnectTimeout(10000);
	         conn.setReadTimeout(12000);
	         conn.setDoInput(true);
	         conn.setDoOutput(true);
	         conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	         conn.setRequestProperty("X-Powered-By","PHP/5.3.3");
	         conn.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8");
	         conn.setRequestProperty("Referer","http://203.104.248.135/kcs/mainD2.swf?api_token="+token+"&api_starttime="+new Date().getTime()+"/[[DYNAMIC]]/1");
	         conn.setRequestProperty("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.75 Safari/537.36");
	         conn.setRequestProperty("X-Requested-With","ShockwaveFlash/25.0.0.171");
	         OutputStream os = conn.getOutputStream();     
	         os.write(param.toString().getBytes("utf-8"));     
	         os.close();         
	         BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	         String line ;
	         String result ="";
	         while( (line =br.readLine()) != null ){
	             result += line;
	         }
	         br.close();
	         return result;
     }

}
