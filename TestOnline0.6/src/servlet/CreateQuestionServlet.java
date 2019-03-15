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

import datatype.Classes;
import datatype.Question;
import datatype.Test;
import datatype.User;
import dao.ModifyDao;
import dao.TestDao;

/**
 * Servlet implementation class LoginServlet
 */
public class CreateQuestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateQuestionServlet() {
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

		String type=request.getParameter("operation");
		String result="null";
		if(type==null){response.getWriter().append(result);return;}
		if(type.equals("question")){
			Question q = new Question();
			q=new Gson().fromJson(request.getParameter("question"), Question.class);
			System.out.println("done");
			result=TestDao.CreateNewQuestion(q.getQuestion(), q.getAnswer(), "subjective",false);
		}
		if(type.equals("test")){
			Test test=new Gson().fromJson(request.getParameter("test"),Test.class);
			result=TestDao.CreateNewTest(test);
		}
		if(type.equals("searchQuestion")){
			List<Question> q=new ArrayList<Question>();
			q=ModifyDao.searchQuestion(request.getParameter("key"));
			result=new Gson().toJson(q);
		}
		if(type.equals("initQuestion")){
			List<Question> q=new ArrayList<Question>();
			q=ModifyDao.searchInitQuestion();
			result=new Gson().toJson(q);
		}
		if(type.equals("deleteQuestion")){
			Question q=new Gson().fromJson(request.getParameter("question"), Question.class);
			result=TestDao.deleteQuestion(q);
		}
		response.getWriter().append(result);
		System.out.println(result);
	}
}
