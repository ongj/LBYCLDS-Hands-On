package Servlet;

import Bean.AlchemyConnector;
import Servlet.MongoDBClient;
import java.io.*;
import java.net.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.*;

@WebServlet(name = "IServlet", urlPatterns = {"/IServlet"})
public class IServlet extends HttpServlet {

	private String FACE_ENDPOINT_URL = "http://gateway-a.watsonplatform.net/calls/url/URLGetRankedImageFaceTags";

 @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
	
		AlchemyConnector connector = new AlchemyConnector();
		//AlchemyVision service = new AlchemyVision();
		//service.setApiKey(connector.getAPIKey());

		String input_url = (String) request.getParameter("gurl");
		StringBuilder sb = new StringBuilder();
		String line;
		
		URL face_url = new URL(FACE_ENDPOINT_URL+"?url="+input_url+"&apikey="+connector.getAPIKey()+"&outputMode=json");
		BufferedReader reader = new BufferedReader(new InputStreamReader(face_url.openStream()));
		while ((line = reader.readLine()) != null){
			sb.append(line);
		}
		
		String finaljson = sb.toString();
		String finalAge = "";
		String finalGender = "";
		System.out.println(finaljson);
		try{
			MongoDBClient mongo = new MongoDBClient();
			mongo.addEntry(finaljson);
		
			request.setAttribute("result", mongo.getEntry());
			
			JSONParser parser = new JSONParser();
			Object image = parser.parse(finaljson);
			JSONObject object = (JSONObject) image;
			System.out.println("!!!!!");
			JSONArray imagefaces = (JSONArray) object.get("imageFaces");
			System.out.println("!!!!!");
			for(int a=0; a < imagefaces.size(); a++){
				JSONObject object2 = (JSONObject) imagefaces.get(a);
				JSONObject age = (JSONObject) object2.get("age");
				JSONObject gender = (JSONObject) object2.get("gender");
				finalAge = age.get("ageRange").toString();
				finalGender = gender.get("gender").toString();
				System.out.println("Age: " + finalAge + " Gender: " + finalGender);
			}
			request.setAttribute("age", finalAge);
			request.setAttribute("gender", finalGender);
		}
		catch(Exception e){
			System.out.println("error = " + e);
		}
		//request.setAttribute("face",sb.toString());

		//ImageFaces image_faces = service.recognizeFaces(input_url,false);
		//request.setAttribute("image_faces",image_faces);
	
		response.setContentType("text/html");
        response.setStatus(200);
        request.getRequestDispatcher("viewinfo.jsp").forward(request, response);

	}

}

