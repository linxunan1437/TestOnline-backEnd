package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import datatype.ClassAnswer;
import datatype.Test;

/**
 * Servlet implementation class OpeningTest
 */
public class ReadTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReadTestServlet() {
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
		String testID=request.getParameter("testID"),operationtype=request.getParameter("type");
		int uid=Integer.parseInt(request.getParameter("uid"));
		ClassAnswer classanswer=new ClassAnswer();
		Test test=new Test();
		if(operationtype.equals("test"))
			test=dao.TestDao.findTest(testID);
		if(operationtype.equals("result"))
			test=dao.TestDao.findTestResult(uid,testID);
		if(operationtype.equals("marking"))
			test=dao.TestDao.findTestMarking(uid, testID);
		if(operationtype.equals("teacherResult"))
			classanswer=dao.TestDao.findClassAnswer(uid,testID);

		if((operationtype.equals("teacherResult"))&&classanswer!=null)
		{
			Gson gson=new Gson();
			String json = gson.toJson(classanswer);
			System.out.println(json);
			response.getWriter().append(json);
			return;
		}
		if(test!=null){
			Gson gson=new Gson();
			String json = gson.toJson(test);
			System.out.println(json);
			response.getWriter().append(json);
			return;
		}
		response.getWriter().append("no such test");
	}

}
