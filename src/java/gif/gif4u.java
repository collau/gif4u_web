/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gif;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author junyi
 */
@WebServlet(name = "gif4u", urlPatterns = {"/gif4u/*"})
public class gif4u extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    // Prepared Statement
    private static final String SQL = "select * from usergifs where username = ?";
    
    // Inject database
    @Resource(lookup = "jdbc/gif4u")
    private DataSource ds;
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet gif4u</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet gif4u at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getPathInfo().substring(1);
        
        JsonArrayBuilder gifBuilder = Json.createArrayBuilder();
        
        // standard JDBC functions
        try (Connection conn = ds.getConnection()){
           
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            /*if(!rs.next()){
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }*/
            
            while(rs.next()){
                JsonObjectBuilder gif = Json.createObjectBuilder();
                gif.add("username", rs.getString("username"))
                        .add("url", rs.getString("url"));
                gifBuilder.add(gif.build());
            }
            
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON);
            
            rs.close();
            conn.close();
            } catch (SQLException ex) {
            log(ex.getMessage());
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        
        JsonArray gifs = gifBuilder.build();
        
        try (PrintWriter pw = response.getWriter()) {
            pw.println(gifs.toString());
        }
    }
}

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */


    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */

// </editor-fold>

/*
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from usergifs");
            while (rs.next()) {
                JsonObjectBuilder gif = Json.createObjectBuilder();
                gif.add("username", rs.getString("username"))
                        .add("url", rs.getString("url"));
                gifBuilder.add(gif.build());
            }
            rs.close();
            conn.close();
            
        } catch (SQLException ex) {
            log(ex.getMessage());
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON);
*/

