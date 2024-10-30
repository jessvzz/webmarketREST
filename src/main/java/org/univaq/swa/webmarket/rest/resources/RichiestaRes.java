/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.resources;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.websocket.server.PathParam;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
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
import org.univaq.swa.webmarket.rest.business.RichiesteService;
import org.univaq.swa.webmarket.rest.business.RichiesteServiceFactory;
import org.univaq.swa.webmarket.rest.exceptions.RESTWebApplicationException;
import org.univaq.swa.webmarket.rest.models.PropostaAcquisto;
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;
import org.univaq.swa.webmarket.rest.models.StatoRichiesta;
import org.univaq.swa.webmarket.rest.security.Logged;

/**
 *
 * @author jessviozzi
 */
public class RichiestaRes {
    private final RichiestaOrdine richiesta;
    private final RichiesteService business;
    
    RichiestaRes(RichiestaOrdine richiesta) {
        this.richiesta = richiesta;
        this.business = RichiesteServiceFactory.getRichiesteService();

    }
    
    @GET
    @Logged
    @Produces("application/json")
    public Response getItem() {
        if (richiesta == null) {
            throw new RESTWebApplicationException(Response.Status.NOT_FOUND.getStatusCode(), "Richiesta non trovata.");
        }
        return Response.ok(richiesta).build();
    }

    @PATCH
    @Logged
    @Path("/presa_in_carico") 
    @Produces(MediaType.APPLICATION_JSON)
    public Response presaInCarico(@QueryParam("idtecnico") int techId, @Context SecurityContext sec) throws SQLException {
        int rowsUpdated = 0;

        try {
            
            
            
            rowsUpdated = business.prendiInCarico(techId, richiesta);
            if (rowsUpdated > 0) {
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Richiesta non trovata.")
                               .build();
            }
        } catch (Exception ex) {
            Logger.getLogger(RichiestaRes.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore interno del server.")
                           .build();
        }
    }
    
    
    @GET
    @Logged
    @Path("/dettagli")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDettagliRichiesta() throws SQLException {
        try {
            RichiestaOrdine richiestaDetails = business.getDettagliRichiesta(richiesta.getId());

            if (richiestaDetails != null) {
                return Response.ok(richiestaDetails).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Dettagli della richiesta non trovati.")
                               .build();
            }

        } catch (Exception ex) {
            Logger.getLogger(RichiestaRes.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore interno del server.")
                           .build();
        }
    }


    
    //cancellazione
     @DELETE
     @Logged
     @Produces(MediaType.APPLICATION_JSON)
     public Response eliminaRichiesta() {

         try {
             
            int rowsDeleted = business.deleteRichiesta(richiesta.getId());

            if (rowsDeleted > 0) {
                
                return Response.noContent().build();
            } else {
                
                return Response.status(Response.Status.NOT_FOUND).entity("Richiesta non trovata").build();
            }

         } catch (Exception e) {
             Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, e);
             Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, e);
             return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore interno del server").build();
         }
     }

}
