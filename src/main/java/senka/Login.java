package senka;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.BSON;
import org.bson.BsonArray;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;



public class Login {
	public static void main(String[] args){
		System.out.println("start");
		try {
			//login("bot4@rewards.msharebox.com","987654321");
			MongoCollection<Document> cl_senka = Util.database.getCollection("cl_senka_"+20);
			long t1 = new Date().getTime();
			for(int i=0;i<1;i++){
				if(i%10==0){
					System.out.println(i);
				}
				Document ag = new Document("$sample", new Document("size",1));
				AggregateIterable<Document> output = cl_senka.aggregate(Arrays.asList(
						ag
					        ));
				Document doc = output.first();
				System.out.println(doc.toJson());
//				System.out.println(doc.get("name"));
				for (Document dbObject : output)
				{
				    //System.out.println(dbObject);
				}
			}

			long t2 = new Date().getTime();
			System.out.println("time cost:"+(t2-t1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String login(String userid,String pwd){
		try {
		        String url = "http://ooi.moe/";
		        String param = "login_id="+userid+"&password="+pwd+"&mode=1";
		        String cookie = getOoiCookie(url,param);
			String url2= "http://ooi.moe/kancolle";
		        String s = HttpGet2(url2,param,cookie);
			String str = "api_token=";
			int n = s.indexOf(str);
			if(n>0){
				String s1 = s.substring(n+str.length());
				int n2 = s1.indexOf("&");
				String token = s1.substring(0,n2);
				System.out.println(token);
				return token;
			}else{
				return "";
			}
		} catch (Exception e) {
			return "";
		}

	}
	

	

	 
	 public static String HttpGet2(String urlStr,String param,String cookie){
		if(param == null || param.trim().length()<1){
	            
	        }else{
	            urlStr +="?"+param;
	        }   
		try {
			URL url = new URL(urlStr);
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        conn.setConnectTimeout(10000);
	        conn.setReadTimeout(12000);
	        conn.setRequestProperty("Cookie", cookie);
	        conn.setDoOutput(true);
	        conn.setRequestMethod("GET");
	        if(conn.getResponseCode() ==200){
	        	BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
	            String line ;
	            String result ="";
	            while( (line =br.readLine()) != null ){
	                result += line + "\n";
	            }
	            return result;
	        }else{
	            System.out.println("failed"+conn.getResponseCode()+conn.getResponseMessage());
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	 
	 
	 
	 public static String  getOoiCookie(String urlStr,String param) throws Exception{
	         URL url = new URL(urlStr);
	         HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	         conn.setRequestMethod("POST");
	         conn.setConnectTimeout(10000);
	         conn.setReadTimeout(12000);
	         conn.setInstanceFollowRedirects(false);
	         conn.setDoInput(true);
	         conn.setDoOutput(true);
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
	         return conn.getHeaderField("Set-Cookie");
	 }
	 
	    
}
