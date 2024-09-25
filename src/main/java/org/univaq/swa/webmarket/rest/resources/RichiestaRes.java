/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
import org.univaq.swa.webmarket.rest.exceptions.RESTWebApplicationException;
import org.univaq.swa.webmarket.rest.models.PropostaAcquisto;
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;
import org.univaq.swa.webmarket.rest.models.StatoRichiesta;

/**
 *
 * @author jessviozzi
 */
public class RichiestaRes {
    private final RichiestaOrdine richiesta;

    RichiestaRes(RichiestaOrdine richiesta) {
        this.richiesta = richiesta;
    }
    
    @GET
    @Produces("application/json")
    public Response getItem() {
        if (richiesta == null) {
            throw new RESTWebApplicationException(Response.Status.NOT_FOUND.getStatusCode(), "Richiesta non trovata.");
        }
        return Response.ok(richiesta).build();
    }

    @PUT
    @Path("/presa_in_carico") 
    @Produces(MediaType.APPLICATION_JSON)
    public Response presaInCarico(@Context SecurityContext sec) throws SQLException {
        InitialContext ctx;
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
            conn = ds.getConnection();

            int tecnicoId = getLoggedTechnicianId(sec);
            if (tecnicoId == -1) {
                return Response.status(Response.Status.FORBIDDEN)
                               .entity("Tecnico non autenticato.")
                               .build();
            }

            String query = "UPDATE richiesta_ordine SET stato = ?, tecnico = ? WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, StatoRichiesta.PRESA_IN_CARICO.toString());
            ps.setInt(2, tecnicoId);
            ps.setInt(3, richiesta.getId());
            
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                return Response.ok("Richiesta aggiornata con successo.").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Richiesta non trovata.")
                               .build();
            }
        } catch (SQLException | NamingException ex) {
            Logger.getLogger(RichiestaRes.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore interno del server.")
                           .build();
        } finally {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }

    //ANCORA DA IMPLEMENTARE
    //per trovare id tecnico loggato
    private int getLoggedTechnicianId(SecurityContext sec) {
         if (sec.getUserPrincipal() == null) {
             System.out.println("User Principal is null");
            return -1; //se l'utente non è autenticato
        }

        String username = sec.getUserPrincipal().getName();
        System.out.println("Logged username: " + username);

        int tecnicoId = findTechnicianIdByUsername(username);
        System.out.println("Tecnico ID: " + tecnicoId);

        return tecnicoId;
    }


    private int findTechnicianIdByUsername(String username) {
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
            Logger.getLogger(RichiestaRes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(RichiestaRes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return -1;
    }
}
