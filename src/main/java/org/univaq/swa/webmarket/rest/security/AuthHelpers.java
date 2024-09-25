package org.univaq.swa.webmarket.rest.security;

import jakarta.ws.rs.core.UriInfo;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class AuthHelpers {

    private static AuthHelpers instance = null;
    private static JWTHelpers jwtHelpers;

    private AuthHelpers(){
        jwtHelpers = JWTHelpers.getInstance();
    }
    
    //auth utente con db
    public boolean authenticateUser(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        InitialContext ctx;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
            
            try (Connection conn = ds.getConnection()) {
                String query = "SELECT password FROM utente WHERE username = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                     String passwordHash = rs.getString("password");
                
                    return SecurityHelpers.checkPasswordHashPBKDF2(password, passwordHash);
                }
            }
        } catch (NamingException | SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static String validateToken(String token)
    {
        return jwtHelpers.validateToken(token);
    }

    public String issueToken(String username, UriInfo uriInfo){
        return jwtHelpers.issueToken(username, uriInfo);
    }

    public void revokeToken(String token){
        jwtHelpers.revokeToken(token);
    }

    public static AuthHelpers getInstance()
    {
        if(instance == null){
            instance = new AuthHelpers();
        }
        return instance;
    }

}
