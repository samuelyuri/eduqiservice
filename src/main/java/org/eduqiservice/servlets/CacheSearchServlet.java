package org.eduqiservice.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eduqiservice.cache.CachedCollections;

/**
 * Servlet implementation class SearchCacheServlet
 */
public class CacheSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CacheSearchServlet() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		
		String jsonCallbackParam = request.getParameter("callback");
		String result = CachedCollections.getSearcheCacheObjects();
		
		if(jsonCallbackParam != null){
			result =jsonCallbackParam+"(" + result+")";
		}
		response.getWriter().write(result);
		
		
		response.flushBuffer();
		
	}

}