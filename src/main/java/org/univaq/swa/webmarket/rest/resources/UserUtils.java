/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.resources;

import jakarta.ws.rs.core.SecurityContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author jessviozzi
 */
public class UserUtils {
    public static int getLoggedId(SecurityContext sec) {
        if (sec.getUserPrincipal() == null) {
            System.out.println("User Principal is null");
            return -1; //se l'utente non è autenticato
        }

        String username = sec.getUserPrincipal().getName();
        System.out.println("Logged username: " + username);

        int id = findIdByUsername(username);
       

        return id;
    }

    public static int findIdByUsername(String username) {
        InitialContext ctx;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
            conn = ds.getConnection();

            String query = "SELECT id FROM utente WHERE username = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException | NamingException ex) {
            Logger.getLogger(UserUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(UserUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return -1;
    }
}
