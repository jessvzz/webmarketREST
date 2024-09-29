/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.univaq.swa.webmarket.rest.exceptions.RESTWebApplicationException;
import org.univaq.swa.webmarket.rest.models.PropostaAcquisto;
import org.univaq.swa.webmarket.rest.models.StatoProposta;
import org.univaq.swa.webmarket.rest.models.StatoRichiesta;

/**
 *
 * @author jessviozzi
 */
public class PropostaRes {
    private final PropostaAcquisto proposta;

    PropostaRes(PropostaAcquisto proposta) {
        this.proposta = proposta;
    }
    
    @GET
    @Produces("application/json")
    public Response getItem() {
        if (proposta == null) {
            throw new RESTWebApplicationException(Response.Status.NOT_FOUND.getStatusCode(), "Proposta non trovata.");
        }
        return Response.ok(proposta).build();
    }
    
    @PUT
    @Path("/approva") 
    @Produces(MediaType.APPLICATION_JSON)
    public Response approve(@Context SecurityContext sec) throws SQLException {
        InitialContext ctx;
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
            conn = ds.getConnection();


            String query = "UPDATE proposta_acquisto SET stato = ? WHERE ID = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, StatoProposta.ACCETTATO.toString());
            ps.setInt(2, proposta.getId());
            
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                return Response.ok("Proposta aggiornata con successo.").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Proposta non trovata.")
                               .build();
            }
        } catch (SQLException | NamingException ex) {
            Logger.getLogger(PropostaRes.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore interno del server.")
                           .build();
        } finally {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }

// //modifica
     @PUT
         @Consumes(MediaType.APPLICATION_JSON)
         @Produces(MediaType.APPLICATION_JSON)
         public Response modificaProposta(@PathParam("idproposta") int idProposta, PropostaAcquisto proposta) {
             InitialContext ctx;
             Connection conn = null;
             PreparedStatement ps = null;

             try {
                 ctx = new InitialContext();
                 DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
                 conn = ds.getConnection();

                 // Query SQL per aggiornare una proposta esistente
                 String query = "UPDATE proposta_acquisto SET produttore = ?, prodotto = ?, codice = ?, codice_prodotto = ?, prezzo = ?, URL = ?, note = ?, stato = ? WHERE id = ?";
                 ps = conn.prepareStatement(query);
                 ps.setString(1, proposta.getProduttore());
                 ps.setString(2, proposta.getProdotto());
                 ps.setString(3, proposta.getCodice());
                 ps.setString(4, proposta.getCodiceProdotto());
                 ps.setFloat(5, proposta.getPrezzo());
                 ps.setString(6, proposta.getUrl());
                 ps.setString(7, proposta.getNote());
                 ps.setString(8, proposta.getStatoProposta().toString());
                 ps.setInt(9, idProposta); // ID della proposta da aggiornare

                 int rowsUpdated = ps.executeUpdate();

                 if (rowsUpdated > 0) {
                     return Response.ok("Proposta modificata con successo").build();
                 } else {
                     return Response.status(Response.Status.NOT_FOUND).entity("Proposta non trovata").build();
                 }

             } catch (SQLException | NamingException e) {
                 Logger.getLogger(ProposteRes.class.getName()).log(Level.SEVERE, null, e);
                 return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore interno del server").build();
             } finally {
                 // Chiusura delle risorse
                 if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
                 if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
             }
         }
    
}
