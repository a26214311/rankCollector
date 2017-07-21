package jetty;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import lib.TimerTask;
import senka.Calculator;
import senka.Search;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/api/*"}, loadOnStartup = 1)
public class Api extends HttpServlet 
{
	
	  static{
		  System.out.print("star1111111111111111111111111111111111111t");
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
				System.out.println(ret);
				System.out.println("==============");
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
		
	  public static String handleData(String path,Map<String, String[]> data,final HttpServletRequest req, final HttpServletResponse resp)throws Exception{
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
		return ret;
	  }
}