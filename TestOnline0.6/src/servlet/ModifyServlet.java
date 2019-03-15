package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import datatype.Classes;
import datatype.User;
import dao.ModifyDao;

/**
 * Servlet implementation class LoginServlet
 */
public class ModifyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModifyServlet() {
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

		String type=request.getParameter("modifyType");
		String result="null";
		if(type==null){response.getWriter().append(result);return;}
		if(type.equals("password")){
			String newpass="",oldpass="";
			int uid=Integer.parseInt(request.getParameter("uid"));
			newpass=request.getParameter("newpass");
			oldpass=request.getParameter("oldpass");
			System.out.println("done");
			result= ModifyDao.modifyPass(uid,oldpass, newpass);
		}
		if(type.equals("class")){
			String operation=request.getParameter("operation");
			String classid=request.getParameter("classid");
			int uid=Integer.parseInt(request.getParameter("uid"));
			System.out.println("done");
			result= ModifyDao.modifyClass(uid, classid,operation);
		}
		if(type.equals("SearchClass")){
			String key=request.getParameter("key");
			System.out.println("done");
			List<Classes> list=ModifyDao.searchClass(key);
			Gson gson = new Gson();
			result = gson.toJson(list);
		}
		if(type.equals("SearchUserClass")){
			String key=request.getParameter("key");
			System.out.println("done");
			List<Classes> list=ModifyDao.searchForUserClass(key);
			Gson gson = new Gson();
			result = gson.toJson(list);
		}
		if(type.equals("createClass")){
			String key=request.getParameter("classes");
			String operation=request.getParameter("operation");
			Classes k=new Gson().fromJson(key, Classes.class);
			result=ModifyDao.createClass(k,operation);
		}
		response.getWriter().append(result);
	}
}
