package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import datatype.Test;

/**
 * Servlet implementation class OpeningTest
 */
public class OpeningTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OpeningTest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		List<Test> all=new  ArrayList<Test>();
		String type=request.getParameter("type");
		int uid = Integer.parseInt(request.getParameter("uid"));
		if(type.equals("test"))
			all=dao.TestDao.SeekOpeningTest(uid);
		else if(type.equals("result"))
			all=dao.TestDao.SeekTestResult(uid);
		else if(type.equals("marking"))
			all=dao.TestDao.SeekTestMarking(uid);
		else if(type.equals("teacherResult"))
			all=dao.TestDao.SeekTeacherTestResult(uid);
		else{
			all=null;
		}
		if(all!=null){
			Gson gson=new Gson();
			String json = gson.toJson(all);
			System.out.println(json);
			response.getWriter().append(json);
		}
		else{
			response.getWriter().append("no test");
		}
	}

}
