import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class LoginServlet extends HttpServlet {

   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      response.setContentType("text/html;charset=UTF-8");
      PrintWriter out = response.getWriter();

      Connection conn = null;
      Statement  stmt = null;
      try {
         out.println("<html><head><title>Login</title></head><body>");
         out.println("<h2>hei</h2>");
         Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/SGTravel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", "myuser", "xxxx");   // Get a connection from the pool
         stmt = conn.createStatement();

         // Retrieve and process request parameters: username and password
         String userName = request.getParameter("username");
         //debug out.println("<h2>heihei</h2>");
         String password = request.getParameter("password");
         //debug out.println("<h2>heiheihei</h2>");
         // boolean hasUserName = userName != null && ((userName = userName.trim()).length() > 0);
         // boolean hasPassword = password != null && ((password = password.trim()).length() > 0);
         boolean hasUserName = (userName != null);
         //debug out.println("<h2>32</h2>");
         boolean hasPassword = (password != null);
         //debug out.println("<h2>33</h2>");

         // Validate input request parameters
         if (!hasUserName) {

            out.println("<h3>Please Enter Your username!</h3>");
         } else if (!hasPassword) {
            out.println("<h3>Please Enter Your password!</h3>");
         } else {
            // Verify the username/password and retrieve the role(s)
            StringBuilder sqlStr = new StringBuilder();
            sqlStr.append("SELECT * FROM User WHERE ");
            sqlStr.append("STRCMP(user.username, '"+userName+"')");
            sqlStr.append("AND STRCMP(user.password, '"+password+"')");
            //System.out.println(sqlStr);  // for debugging

            ResultSet rset = stmt.executeQuery(sqlStr.toString());

            // Check if username/password are correct
            boolean added = true;
            while (!rset.next()) {  // empty ResultSet
               out.println("<h3>Wrong username/password!</h3>");
               out.println("<p><a href='login.html'>Back to Login Menu</a></p>");
               added = false;
            } 
            if(added = true){

               HttpSession session = request.getSession(false);
               if (session != null) {
                  session.invalidate();
               }
               session = request.getSession(true);
               synchronized (session) {
                  session.setAttribute("username", userName);
               }

               out.println("<p>Hello, <a href='account'>" + userName + "</a>!</p>");
               out.println("<p><a href='index'>Back to main</a></p>");
            }
         }
         out.println("</body></html>");

      } catch (SQLException ex) {
         ex.printStackTrace();
     } catch (ClassNotFoundException ex) {
        ex.printStackTrace();
     } finally {
         out.close();
         try {
            // Step 5: Close the Statement and Connection
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
         } catch (SQLException ex) {
            ex.printStackTrace();
         }
      }
   }

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      doGet(request, response);
   }
}