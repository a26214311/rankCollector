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
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/api/*"}, loadOnStartup = 1)
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
	  
	  
	  
	  @Override 
	  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		  System.out.println(234);
	  }
		
	  public static String handleData(String path,Map<String, String[]> data,HttpServletRequest req, HttpServletResponse resp)throws Exception{
		String ret = "{\"r\":100}";
		System.out.println(path);
		System.out.println(data);
		if(path.equals("test")){
			ret = "{\"r\":110}";
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
			ret = Search.seekByName(data);
		}
		if(path.equals("ranktask")){
			TimerTask.rankTask();
			ret = "will run rank task";
		}
		if(path.equals("forcecollect")){
			TimerTask.collectorTask();
			ret = "will run rank task";
		}
		if(path.equals("calrank")){
			resp.setCharacterEncoding("utf-8");
			resp.setHeader("Access-Control-Allow-Origin", "*");
			ret = Calculator.calculator(data);
		}
		if(path.equals("calz")){
			int month = new Date().getMonth()-1;
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
