package senka;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class Lib {
	
	private static ArrayList<String> ips = new ArrayList<>();
	static{
		ips.add("");
		
		   
		ips.add(	"203.104.209.71");
		ips.add(        "203.104.209.87");
		ips.add(        "125.6.184.16");
		ips.add(        "125.6.187.205");
		ips.add(        "125.6.187.229");
		ips.add(        "125.6.187.253");
		ips.add(        "125.6.188.25");
		ips.add(        "203.104.209.199");
		ips.add(        "125.6.189.7");
		ips.add(        "125.6.189.39");
		ips.add(        "125.6.189.71");
		ips.add(        "125.6.189.103");
		ips.add(        "125.6.189.135");
		ips.add(        "125.6.189.167");
		ips.add(        "125.6.189.215");
		ips.add(        "125.6.189.247");
		ips.add(        "203.104.209.23");
		ips.add(        "203.104.209.39");
		ips.add(        "203.104.209.55");
		ips.add(        "203.104.209.102");
	}
	
	
	 public static String  ApiPost(String path,String param,String token,int server) throws Exception{
		 try {
			 String urlStr ="http://"+ips.get(server)+path;
			 System.out.println(urlStr);
			 System.out.println(param);
		         URL url = new URL(urlStr);
				
		         HttpURLConnection conn;
		         if(server==18){
		        	 conn = (HttpURLConnection)url.openConnection();
		         }else{
		        	 conn = (HttpURLConnection)url.openConnection();
		         }
		         conn.setRequestMethod("POST");
		         conn.setConnectTimeout(5000);
		         conn.setReadTimeout(6000);
		         conn.setDoInput(true);
		         conn.setDoOutput(true);
		         if(server==18){
		        	 
		         }
		         conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		         conn.setRequestProperty("X-Powered-By","PHP/5.3.3");
		         conn.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8");
		         conn.setRequestProperty("Referer","http://"+ips.get(server)+"/kcs/mainD2.swf?api_token="+token+"&api_starttime="+new Date().getTime()+"/[[DYNAMIC]]/1");
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}

     }
	 public static void main(String[] args){
		 System.out.println(123);
		 try {
			String param = "api%5Fhighspeed=1&api%5Fndock%5Fid=3&api%5Fship%5Fid=3455&api%5Fverno=1&api%5Ftoken=95927c5d691316a6d69b0368763aca6404b41bfe";
			String path = "/kcsapi/api_req_nyukyo/start";
			String token = "95927c5d691316a6d69b0368763aca6404b41bfe";
			int server = 8;
			String rString = ApiPost(path, param, token, server);
			System.out.println(rString);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	 
	 
	 
}
