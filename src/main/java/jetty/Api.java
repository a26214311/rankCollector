package jetty;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/*"}, loadOnStartup = 1)
public class Api extends HttpServlet 
{
	
	  static{
		  System.out.print("star1111111111111111111111111111111111111t");
	  }
	  @Override 
	  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		  System.out.println(123);
		  response.getOutputStream().print("Hello World");
	  }
	  
	  
	  
	  @Override 
	  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		  System.out.println(234);
	  }
}