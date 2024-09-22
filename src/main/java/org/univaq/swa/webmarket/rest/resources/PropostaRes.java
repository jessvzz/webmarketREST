/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.resources;

import javax.sql.DataSource;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.univaq.swa.webmarket.rest.exceptions.RESTWebApplicationException;

/**
 *
 * @author jessviozzi
 */

@Path("proposte")

public class PropostaRes {
    
    //rivedere
 @GET
    @Path("{idproposta: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPOG(
            @PathParam("idproposta") int idproposta,
            @Context UriInfo uriinfo,
            //iniettiamo elementi di contesto utili per la verifica d'accesso
            @Context SecurityContext sec,
            @Context ContainerRequestContext req)
            throws RESTWebApplicationException, SQLException, ClassNotFoundException, NamingException {
        
        List<String> l = new ArrayList();
        
        InitialContext ctx;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
            Connection conn = ds.getConnection();
            
            PreparedStatement ps = conn.prepareStatement("Select * FROM proposta WHERE id = ?");
            ps.setInt(1, idproposta);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                
                    l.add(rs.getString("produttore"));
                    l.add(rs.getString("prodotto"));
                    l.add(rs.getString("codice"));
                    l.add(rs.getString("codice_prodotto"));
                    l.add(String.valueOf(rs.getFloat("prezzo"))); 
                    l.add(rs.getString("URL"));
                    l.add(rs.getString("note"));
                    l.add(rs.getString("stato"));
                    l.add(rs.getString("data")); 
                    l.add(rs.getString("motivazione"));
                    l.add(String.valueOf(rs.getInt("richiesta_id")));               
            }
            
        } catch (NamingException ex) {
            Logger.getLogger(PropostaRes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.ok(l).build();
    }
    
}