/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
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
import org.univaq.swa.webmarket.rest.business.ProposteService;
import org.univaq.swa.webmarket.rest.business.ProposteServiceFactory;
import org.univaq.swa.webmarket.rest.exceptions.RESTWebApplicationException;
import org.univaq.swa.webmarket.rest.models.PropostaAcquisto;
import org.univaq.swa.webmarket.rest.models.StatoProposta;
import org.univaq.swa.webmarket.rest.models.StatoRichiesta;
import org.univaq.swa.webmarket.rest.security.Logged;

/**
 *
 * @author jessviozzi
 */
public class PropostaRes {
    private final PropostaAcquisto proposta;
    private final ProposteService business;


    PropostaRes(PropostaAcquisto proposta) {
        this.proposta = proposta;
        this.business = ProposteServiceFactory.getProposteService();

    }
    
    @GET
    @Logged
    @Produces("application/json")
    public Response getItem() {
        if (proposta.getId() == 0) {
            throw new RESTWebApplicationException(Response.Status.NOT_FOUND.getStatusCode(), "Proposta non trovata.");
        }
        return Response.ok(proposta).build();
    }
    
    @PATCH
    @Logged
    @Path("/approva") 
    @Produces(MediaType.APPLICATION_JSON)
    public Response approve(@Context SecurityContext sec) throws SQLException {
        int rowsUpdated = 0;
        try{
            rowsUpdated = business.approvaProposta(proposta.getId());
            if (rowsUpdated > 0) {
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Proposta non trovata.")
                               .build();
            }
        } catch (Exception ex) {
            Logger.getLogger(PropostaRes.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore interno del server.")
                           .build();
        } 
    }
    
    @PATCH
    @Logged
    @Path("/rifiuta") 
    @Produces(MediaType.APPLICATION_JSON)
    public Response reject(@FormParam("motivazione") String motivazione, @Context SecurityContext sec) throws SQLException {
        int rowsUpdated = 0;
        try{
            rowsUpdated = business.rifiutaProposta(proposta.getId(), motivazione);
            if (rowsUpdated > 0) {
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Proposta non trovata.")
                               .build();
            }
        } catch (Exception ex) {
            Logger.getLogger(PropostaRes.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore interno del server.")
                           .build();
        } 
    }

         @PUT
         @Logged
         @Consumes(MediaType.APPLICATION_JSON)
         @Produces(MediaType.APPLICATION_JSON)
         public Response modificaProposta(PropostaAcquisto prop,         
                 @Context SecurityContext sec) {
             int rowsUpdated = 0;

             try {
                 int techId = UserUtils.getLoggedId(sec);
                 rowsUpdated = business.modificaProposta(prop, proposta.getId(), techId);

                 if (rowsUpdated > 0) {
                     return Response.noContent().build();
                 } else if (rowsUpdated == -1) { 
                    return Response.status(Response.Status.UNAUTHORIZED)
                                   .entity("ID utente loggato non corrisponde al tecnico incaricato della richiesta")
                                   .build();
                }else {
                     return Response.status(Response.Status.NOT_FOUND).entity("Proposta non trovata").build();
                 }

             } catch (Exception e) {
                 Logger.getLogger(ProposteRes.class.getName()).log(Level.SEVERE, null, e);
                 return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore interno del server").build();
             } 
         }
    
}
