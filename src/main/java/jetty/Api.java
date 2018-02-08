package jetty;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import lib.TimerTask;
import senka.Calculator;
import senka.Collector;
import senka.Search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/*"}, loadOnStartup = 1)
public class Api extends HttpServlet 
{
	
	  static{
		  System.out.print("芙兰baka233");
		  TimerTask.init();
	  }
	  
	  @Override 
	  public void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException{
			String pathinfo = req.getPathInfo();
			String path = pathinfo.substring(1);
			if(path.equals("rank.html")){
				System.out.println("rank");
				response.sendRedirect("http://flandrescarlet.gitee.io/tools/senka/");
				return;
			}else if(path.startsWith("api")){
				path=path.substring(4);
			}else{
				
			}
			String queryString = req.getQueryString();
			Map<String, String[]> data = new HashMap<>();
			if(queryString!=null){
				data = ApiUtil.getParamsMap(queryString, "utf-8");
			}
			try {
				
				System.out.println("==============");
				System.out.println(path);
				System.out.println(queryString);
				System.out.println(new Date());
				System.out.println(req.getRemoteAddr());
				Enumeration<String> header = req.getHeaderNames();
				while (header.hasMoreElements()) {
					String headername = (String) header.nextElement();
					System.out.println(headername+":"+req.getHeader(headername));
				}
				System.out.println("==============\n");
				String ret;
				if((path.equals("seek")&&req.getRemoteAddr().equals("113.251.37.232"))||path.equals("forbidtest")){
					response.setCharacterEncoding("utf-8");
					response.setContentType("text/plain");
					ret = "快告诉我你是谁！！";
				}else{
					ret = handleData(path, data, req, response);
				}
				OutputStream output = null;
				output = response.getOutputStream();
				IOUtils.write(ret.getBytes("utf-8"), output);
				output.flush();
			} catch (Exception e) {
				e.printStackTrace();
				response.getOutputStream().print("error");
			}

	  }
	  
	  
	  
	  @Override 
	  public void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException{
			String pathinfo = req.getPathInfo();
			String path = pathinfo.substring(1);
			String queryString = req.getQueryString();
			Map<String, String[]> data = new HashMap<>();
			if(queryString!=null){
				data = ApiUtil.getParamsMap(queryString, "utf-8");
			}
			try {
				String ret = handleData(path, data, req, response);
				System.out.println("==============");
				System.out.println(path);
				System.out.println(queryString);
				System.out.println(new Date());
				System.out.println(req.getRemoteAddr());
				Enumeration<String> header = req.getHeaderNames();
				while (header.hasMoreElements()) {
					String headername = (String) header.nextElement();
					System.out.println(headername+":"+req.getHeader(headername));
				}
				System.out.println("==============\n");
				OutputStream output = null;
				output = response.getOutputStream();
				IOUtils.write(ret.getBytes("utf-8"), output);
				output.flush();
			} catch (Exception e) {
				e.printStackTrace();
				response.getOutputStream().print("error");
			}
	  }
		
	  public static String handleData(String path,Map<String, String[]> data,HttpServletRequest req, HttpServletResponse resp)throws Exception{
		String ret = "{\"r\":100}";
		System.out.println(path);
		System.out.println(data);
		if(path.equals("test")){
			ret = "{\"r\":110}";
		}
		if(path.equals("testw")){
			ret = TimerTask.nowworking.toString();
		}
		if(path.equals("collect")){
			senka.Collector.runCollector(data);
			ret = "will run collector";
		}
		if(path.equals("rank")){
			senka.Rank.runRank(data);
			ret = "will run rank";
		}
		if(path.equals("seek")){
			resp.setCharacterEncoding("utf-8");
			resp.setContentType("text/plain");
			ret = "此地址关闭，新地址找我要";
		}
		if(path.equals("peek")){
			resp.setCharacterEncoding("utf-8");
			resp.setContentType("text/html");
			ret = Search.seekByName(data).replaceAll("\n", "<br>");
		}
		if(path.equals("ranktask")){
			TimerTask.rankTask();
			ret = "will run rank task";
		}
		if(path.equals("forcecollect")){
			TimerTask.collectorTask();
			ret = "will run collect task";
		}
		if(path.equals("collects")){
			Collector.collectByServer(data);
			ret = "will run server collect task";
		}
		if(path.equals("calrank")){
			resp.setCharacterEncoding("utf-8");
			resp.setHeader("Access-Control-Allow-Origin", "*");
			ret = Calculator.calculator(data);
			resp.setContentLength(ret.getBytes("utf-8").length);
		}
		if(path.equals("calz")){
			int month = new Date().getMonth()-1;
			if(month==-1){
				month=month+12;
			}
			Calculator.calculateZ(8,month);
			Calculator.calculateZ(15,month);
			Calculator.calculateZ(16,month);
			Calculator.calculateZ(18,month);
			Calculator.calculateZ(19,month);
			ret = "z ok";
		}
		if(path.equals("rank.html")){
			byte[] rk = toByteArray2("src/main/webapp/rank.html");
			resp.setContentType("text/html");
			ret = new String(rk);
		}
		if(path.equals("test2")){
			Collector.randomCollect("token", 8, 10);
			ret = "ok";
		}
		return ret;
	  }
	  
		public static byte[] toByteArray2(String filename) throws IOException {  
			  
		        File f = new File(filename);  
		        if (!f.exists()) {  
		            throw new FileNotFoundException(filename);  
		        }  
		  
		        FileChannel channel = null;  
		        FileInputStream fs = null;  
		        try {  
		            fs = new FileInputStream(f);  
		            channel = fs.getChannel();
		            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());  
		            while ((channel.read(byteBuffer)) > 0) {  
		                // do nothing  
		                // System.out.println("reading");  
		            }  
		            return byteBuffer.array();  
		        } catch (IOException e) {  
		            e.printStackTrace();  
		            throw e;  
		        } finally {  
		            try {  
		                channel.close();  
		            } catch (IOException e) {  
		                e.printStackTrace();  
		            }  
		            try {  
		                fs.close();  
		            } catch (IOException e) {  
		                e.printStackTrace();  
		            }  
		        }  
		    }  
	  
	  
	  
}
