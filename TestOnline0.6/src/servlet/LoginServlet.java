package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import datatype.User;

/**
 * Servlet implementation class LoginServlet
 */ 
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String username="",password="";
		username=request.getParameter("username");password=request.getParameter("password");
		String tutor=request.getParameter("tutor");
		String type = request.getParameter("type");
		User u;
		System.out.println("da");
		if(type.equals("login"))
			u= dao.UserLoginDao.login(username, password,tutor);
		else
			u=dao.UserLoginDao.signup(username, password,tutor);
		if(u!=null)
		{
			if(u.getUid()==0){
				response.getWriter().append("existed username");
				return;
			}
			Gson gson = new Gson();
			String json = gson.toJson(u);
			response.getWriter().append(json);
		}
		else
		{
			response.getWriter().append("fail");
		}
		
		
	}

}
