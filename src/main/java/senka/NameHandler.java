package senka;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class NameHandler {
	private static int totalcount = 0;
	public static void getIdByName(String name,int server){
		DBCursor dbc = null;
		DBCollection cl_senka = Util.db.getCollection("cl_senka_"+server);
		ArrayList<Integer> idlist = new ArrayList<>();
 		try {
			dbc = cl_senka.find(new BasicDBObject("name",name));
			ArrayList<DBObject> userlist = new ArrayList<>();
			while (dbc.hasNext()) {
				DBObject userdata = dbc.next();
				userlist.add(userdata);
			}
			System.out.println(name+":"+userlist.size());
			
			if(userlist.size()==0){
				System.out.println("------------------------------------------");
				System.out.println("need fetch users");
			}else if(userlist.size()==1){
				idlist.add(Integer.valueOf(userlist.get(0).get("_id").toString()));
			}else{
				totalcount = totalcount + userlist.size();
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(dbc!=null){
				dbc.close();
			}
		}
		System.out.println("total users:"+totalcount);
	}
	
	public static void handleSenkaList2(int server,ArrayList<JSONObject> senkaList)throws Exception{
		DBCollection cl_senka = Util.db.getCollection("cl_senka_"+server);
		BasicDBList idlist = new BasicDBList();
		for(int i=0;i<senkaList.size();i++){
			JSONObject jd = senkaList.get(i);
			int senka = jd.getInt("senka");
			String name = jd.getString("name");
			String cmt = jd.getString("cmt");
			int no = jd.getInt("no");
			DBCursor dbc = null;
			try {
				dbc = cl_senka.find(new BasicDBObject("name",name));
				ArrayList<DBObject> userlist = new ArrayList<>();
				while (dbc.hasNext()) {
					DBObject dbObject = (DBObject) dbc.next();
					userlist.add(dbObject);
				}
				if(userlist.size()==0){
					System.out.println("------------------------------------------");
					System.out.println("need fetch users");
				}else if(userlist.size()==1){
					idlist.add(userlist.get(0).get("_id").toString());
				}else{
					for(int k=0;k<userlist.size();k++){
						DBObject ud = userlist.get(k);
						String info = ud.get("info").toString();
						JSONObject infoj = new JSONObject(info);
						int rank = infoj.getInt("api_rank");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				if(dbc!=null){
					dbc.close();
				}
			}
		}
	}
	
	public static void handleName(String name,DBObject senkaUser,int tid,int count,int server,String token){
		DBCollection cl_senka = Util.db.getCollection("cl_senka_"+server);
		DBCollection cl_n_senka = Util.db.getCollection("cl_n_senka_"+server);
		Date now = new Date();
		int month = now.getMonth();
		int date = now.getDate();
		DBCursor dbc = null;
		try {
			BasicDBObject projector = new BasicDBObject();
			projector.append("name", 1);
			projector.append("exp", 1);
			projector.append("info", 1);
			projector.append("_id", 1);
			BasicDBObject query = new BasicDBObject();
			query.append("name", name);
			BasicDBList rankquery = new BasicDBList();
			rankquery.add(new BasicDBObject("rank",1));
			rankquery.add(new BasicDBObject("rank",new BasicDBObject("$exists",false)));
			//query.append("$or", rankquery);
			dbc = cl_senka.find(query,projector);
			ArrayList<DBObject> userlist = new ArrayList<>();
			while (dbc.hasNext()) {
				DBObject dbObject = (DBObject) dbc.next();
				userlist.add(dbObject);
			}
			if(userlist.size()==0){
				System.out.println("------------------------------------------");
				System.out.println("need fetch users");
			}else if(userlist.size()==1){
				totalcount++;
				int id = Integer.valueOf(userlist.get(0).get("_id").toString());
				cl_n_senka.update(new BasicDBObject("_id",name),new BasicDBObject("$set",new BasicDBObject("id",""+id).append("c", 1)));
				Collector.collectById(id, token, server);
			}else{
				ArrayList<Integer> fetchidlist = new ArrayList<>();
				ArrayList<Integer> mayfetchidlist = new ArrayList<>();
				ArrayList<Integer> mayfetchranklist = new ArrayList<>();
				for(int i=0;i<userlist.size();i++){
					DBObject user = userlist.get(i);
					int id = Integer.valueOf(user.get("_id").toString());
					BasicDBList exp = (BasicDBList)user.get("exp");
					ArrayList<DBObject> explist = new ArrayList<>();
					boolean willupdate = false;
					Date last = new Date(0);
					for(int k=0;k<exp.size();k++){
						DBObject expdata = (DBObject)exp.get(k);
						int expn = Integer.valueOf(expdata.get("d").toString());
						Date then = (Date) expdata.get("ts");
						System.out.println(id+":"+k+":"+exp.size()+":"+then+":"+then.after(last)+":"+then.getYear()*12+then.getMonth()+":"+now.getYear()*12+month);
						System.out.println(then+":"+then.getYear()+":"+then.getMonth());
						System.out.println(now+":"+now.getYear()+":"+now.getMonth());

						if(then.getYear()*12+then.getMonth()==now.getYear()*12+month){
							explist.add(expdata);
							if(then.after(last)){
								last = then;
							}
						}else if(now.getTime()-then.getTime()>86400000L*30){
							exp.remove(k);
							willupdate = true;
						}else{
							
						}
					}
					String info = user.get("info").toString();
					JSONObject infoj = new JSONObject(info);
					int rank = infoj.getInt("api_rank");
					String tcmt = infoj.getString("api_cmt");
					if(now.getTime()-last.getTime()>86400000L*4*(rank-1)){
						fetchidlist.add(id);
					}else{
						if(now.getTime()-last.getTime()>86400000L*2*(rank-1)||rank==2){
							mayfetchidlist.add(id);
							mayfetchranklist.add(rank);
						}
					}
					System.out.println(id+":"+last);
				}
				
				if(fetchidlist.size()>=count){
					System.out.println("name:"+name+",count:"+count+",fetch:"+fetchidlist.size());
					totalcount+=fetchidlist.size();
					if(fetchidlist.size()==1){
						cl_n_senka.update(new BasicDBObject("_id",name),new BasicDBObject("$set",new BasicDBObject("id",""+fetchidlist.get(0)).append("c", count)));
						Collector.collectById(fetchidlist.get(0), token, server);
					}else{
						String saveidlist = "";
						for(int k=0;k<fetchidlist.size();k++){
							int id = fetchidlist.get(k);
							JSONObject userj = Collector.collectById(id, token, server);
							int rank = userj.getInt("api_rank");
							if(rank==1){
								if(saveidlist.equals("")){
									saveidlist = id+"";
								}else{
									saveidlist = saveidlist + "," + id;
								}
							}
						}
						cl_n_senka.update(new BasicDBObject("_id",name),new BasicDBObject("$set",new BasicDBObject("id",saveidlist).append("c", count)));
					}
				}else{
					System.out.println("(((((((((((((((((((((((((((((");
					System.out.println("no user matched,will fetch from mayidlist");
					System.out.println(name);
					String saveidlist = "";
					for(int k=0;k<fetchidlist.size();k++){
						int id = fetchidlist.get(k);
						JSONObject userj = Collector.collectById(id, token, server);
						int rank = userj.getInt("api_rank");
						if(rank==1){
							if(saveidlist.equals("")){
								saveidlist = id+"";
							}else{
								saveidlist = saveidlist + "," + id;
							}
						}
					}
					for(int k=0;k<mayfetchidlist.size();k++){
						int id = mayfetchidlist.get(k);
						JSONObject userj = Collector.collectById(id, token, server);
						int rank = userj.getInt("api_rank");
						if(rank==1){
							if(saveidlist.equals("")){
								saveidlist = id+"";
							}else{
								saveidlist = saveidlist + "," + id;
							}
						}
					}
					cl_n_senka.update(new BasicDBObject("_id",name),new BasicDBObject("$set",new BasicDBObject("id",saveidlist).append("c", count)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(dbc!=null){
				dbc.close();
			}
		}
	}
	

	
	
	public static void main(String[] args){
		Collector.main(args);
	}
}
