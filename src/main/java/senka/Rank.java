package senka;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import lib.TimerTask;

public class Rank {
	
	public static void main(String[] args) {
		System.out.println("start1");
		try {
			getRank();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("finish");
	}
	private static int[] MAGIC_R_NUMS = new int[]{ 8931, 1201, 1156, 5061, 4569, 4732, 3779, 4568, 5695, 4619, 4912, 5669, 6586 };
	private static Random rd = new Random();
	public static void getRank()throws Exception{
		String path = "/kcsapi/api_req_ranking/mxltvkpyuklh";
		int page=1;
		String token = "27cdc52a5b680d60e014fae484ee38f7c6d88e87";
		int server = 8;
		int userid = 8156938;
		String ranking = generateRankKey(userid);
		
		String param = "api%5Fpageno="+page+"&api%5Fverno=1&api%5Franking="+ranking+"&api%5Ftoken="+token;
		System.out.println(param);
		String ret = Lib.ApiPost(path, param, token, server);
		System.out.println(ret);
		if(ret.startsWith("svdata=")){
			JSONObject j = new JSONObject(ret.substring(7));
		}
	}
	
	
//	String token = "76c73d1d3b775575e1ffc3116aec113835e8cd37";
//	int server = 8;
//	int userid = 8156938;
	
	private static int calMagicNum = 28;
	
	public static void runRank(Map<String, String[]> data)throws Exception{
		int server = Integer.valueOf(data.get("server")[0]);
		String token = TimerTask.getToken(server);
		int userid = Integer.valueOf(data.get("uid")[0]);
		runRankTask(token, server, userid);
	}
	
	public static void runRankTask(final String token,final int server,final int userid)throws Exception{
		String path = "/kcsapi/api_req_ranking/mxltvkpyuklh";
		String ranking = generateRankKey(userid);
		DBCollection cl_u_senka = Util.db.getCollection("cl_u_senka_"+server);
		Date now = new Date();
		int rankNo = Util.getRankDateNo(now);
		int year = now.getYear();
		int month = now.getMonth();
		String key = year + "_" + month + "_" + rankNo + "_" + server;
		BasicDBObject query = new BasicDBObject("_id",key);
		DBObject uData = cl_u_senka.findOne(query);
		ArrayList<JSONObject> tdata = new ArrayList<>();
		int ma;
		System.out.println(123);
		if(uData==null){
			for(int i=1;i<100;i++){
				String param = "api%5Fpageno="+i+"&api%5Fverno=1&api%5Franking="+ranking+"&api%5Ftoken="+token;
				String ret = Lib.ApiPost(path, param, token, server);
				if(ret.startsWith("svdata=")){
					JSONObject j = new JSONObject(ret.substring(7));
					tdata.addAll(addData(j));
				}
			}
			ma = calMagic(tdata);
			System.out.println("magic:"+ma);
			cl_u_senka.save(new BasicDBObject("_id",key).append("d", tdata.toString()).append("magic", ma));
		}else{
			String t1 = uData.get("d").toString();
			JSONArray j2 = new JSONArray(t1);
			System.out.println(j2);
			for(int i=0;i<j2.length();i++){
				tdata.add(j2.getJSONObject(i));
			}
			ma = Integer.valueOf(uData.get("magic").toString());
		}
		handleSenkaList(server,ma,tdata);
	}
	


	public static ArrayList<JSONObject> addData(JSONObject j)throws Exception{
		ArrayList<JSONObject> ta = new ArrayList<>();
		if(j.getInt("api_result")==1){
			JSONArray list = j.getJSONObject("api_data").getJSONArray("api_list");
			for(int i=0;i<list.length();i++){
				JSONObject jd = list.getJSONObject(i);
				int no=jd.getInt("api_mxltvkpyuklh");
				long key=jd.getLong("api_wuhnhojjxmke");
				if(key%MAGIC_R_NUMS[no%13]==0){
					ta.add(jd);
				}
			}
			return ta;
		}else{
			System.out.println("---------------error get senka----------------");
			System.out.println(j);
			return new ArrayList<>();
		}
	}
	

	
	public static int calMagic(ArrayList<JSONObject> tdata)throws Exception{
		ArrayList<Integer> la = new ArrayList<>();
		while(la.size()<calMagicNum){
			JSONObject jd = tdata.get(la.size());
			int no=jd.getInt("api_mxltvkpyuklh");
			long key=jd.getLong("api_wuhnhojjxmke");
			if(key%MAGIC_R_NUMS[no%13]==0){
				System.out.println(key);
				System.out.println(key/MAGIC_R_NUMS[no%13]);
				la.add((int)(key/MAGIC_R_NUMS[no%13]));
			}else{
				System.out.println(jd);
				la.add(0);
			}
		}
		System.out.println(la);
		return EA(la);
	}
	
	public static void handleSenkaList(int server,int ma,ArrayList<JSONObject> tdata)throws Exception{
		DBCollection cl_n_senka = Util.db.getCollection("cl_n_senka_"+server);
		Date now = new Date();
		int dateNo = Util.getRankDateNo(now);
		for(int i=0;i<tdata.size();i++){
			JSONObject jdd = tdata.get(i);
			int no=jdd.getInt("api_mxltvkpyuklh");
			long key=jdd.getLong("api_wuhnhojjxmke");
			int lrate = (int)(key / MAGIC_R_NUMS[no%13]);
			int senka = lrate/ma - 91;
			String name = jdd.getString("api_mtjmdcwtvhdr");
			String cmt = jdd.getString("api_itbrdpdbkynm");
			BasicDBObject insert = new BasicDBObject();
			insert.append("cmt", cmt);
			insert.append("senka", senka);
			insert.append("no", no);
			insert.append("ts", dateNo);
			cl_n_senka.update(new BasicDBObject("_id",name),
					new BasicDBObject("$push",new BasicDBObject("d"+now.getMonth(),insert)).append("$set", new BasicDBObject("ts",now))
					,true,false);
		}
	}
	
	
	private static void needUpdateUserDate(DBObject user,DBCollection cl_senka){
		Object us = user.get("senka");
		Object ud = user.get("exp");
		boolean needupdate = false;
		Date now = new Date();
		BasicDBObject update = new BasicDBObject();
		if(us!=null){
			BasicDBList dbls = (BasicDBList) us;
			for(int i=0;i<dbls.size();i++){
				DBObject senkad = (DBObject)dbls.get(i);
				Date then = (Date)senkad.get("ts");
				if(now.getTime()-then.getTime()>86400000L*60){
					dbls.remove(i);
					needupdate = true;
				}else{
					break;
				}
			}
			if(needupdate){
				update.append("$set", new BasicDBObject("senka",dbls));
			}
		}
		boolean needupdate2 = false;
		if(ud!=null){
			BasicDBList dbld = (BasicDBList) ud;
			for(int i=0;i<dbld.size();i++){
				DBObject senkad = (DBObject)dbld.get(i);
				Date then = (Date)senkad.get("ts");
				if(now.getTime()-then.getTime()>86400000L*60){
					dbld.remove(i);
					needupdate2 = true;
				}else{
					break;
				}
			}
			if(needupdate2){
				update.append("$set", new BasicDBObject("exp",dbld));
			}
		}
		if(needupdate||needupdate2){
			cl_senka.update(user, update);
		}
	}
	
	
	
	public static int EA(int max,int min){
		if(max%min==0){
			return min;
		}else{
			return EA(min, max%min);
		}
	}
	
	public static int EA(ArrayList<Integer> list){
		if(list.size()==0){
			return 1;
		}
		int ret = list.get(0);
		for(int i=1;i<list.size();i++){
			ret = EA(ret,list.get(i));
		}
		return ret;
	}
	
	//private static long[] Il = new long[]{9137, 9740, 4458, 2139, 2130, 4132653, 1033183, 3749, 3601, 4294, 13, 6523, 3791, 10, 5424, 7481, 1000, 1875979};
//	private static long[] Il = new long[]{8188, 8093, 5187, 2139, 5638, 4132653, 1033183, 6469, 6090, 9676, 13, 8706, 3791, 10, 5097, 9023, 1000, 1875979};
//	private static long[] Il = new long[]{7796, 6074, 5621, 2139, 3065, 4132653, 1033183, 4791, 7545, 4617, 13, 6048, 3791, 10, 6913, 9038, 1000, 1875979};
	private static long[] Il = new long[]{6496, 4829, 7323, 2139, 2361, 4132653, 1033183, 2137, 6013, 7597, 13, 5122, 3791, 10, 7877, 3732, 1000, 1875979};
	//private static long[] Il = new long[]{2979, 2906, 2043, 2139, 9521, 4132653, 1033183, 2744, 8632, 8175, 13, 4564, 3791, 10, 3830, 4887, 1000, 1875979};
	private static int[] I1 = new int[]{3, 7, 8, 0, 11, 4, 2, 9, 15, 14};
	private static String generateRankKey1(int userid){
		String ret = "";
		long s3 = 32768L+rd.nextInt(32767);
		long frontuserid = Long.valueOf((userid+"").substring(0, 4));
		String f1 = userid%Il[16] + rd.nextInt(9)*Il[16]+Il[16]+"";
		String f2 = (long)((Il[5]+s3)*(frontuserid+Il[16])-new Date().getTime()/1000+Il[17]+s3*9-userid) * Il[I1[userid%10]]+"";
		String f3 = rd.nextInt(9*(int)Il[16])+Il[16]+"";
		String f = f1+f2+f3;
		ret = rd.nextInt(10)+f.substring(0, 7)+rd.nextInt(10)+f.substring(7, 16)+rd.nextInt(10)+f.substring(16)+s3;
		return ret;
	}
	
	private static long[] PORT_API_SEED = new long[]{6119,8922,9226,5027,8277,3976,3907,4855,7833,6656};
	
	private static long getSeed(int t){
		    return PORT_API_SEED[t % 10];
		}
	

	private static String generateRankKey(int userid){
		int t=userid;
		    long e = getSeed(t);
		    long i = new Date().getTime()/ 1000;
		    long n = 1000 * (rd.nextInt(9) + 1) + t % 1000;
		    long o = rd.nextInt(8999) + 1000;
		    long r = rd.nextInt(32767) + 32768;
		    long s = rd.nextInt(10);
		    long a = rd.nextInt(10);
		    long _ = rd.nextInt(10);
		    long u = Integer.valueOf((t+"").substring(0, 4));
		    long l = (4132653 + r) * (u + 1000) - i + (1875979 + 9 * r);
		    long c = l - t;
		    long h = c * e;
		    String p = ""+n + ""+h+"" + o;
		    p = s+"" + p;
		    String d = p.substring(0, 8);
		    String f = p.substring(8);
		    p = d + a + f;
		    d = p.substring(0, 18);
		    f = p.substring(18);
		    p = d + _ + f;
		    return p + r;
	}

}
