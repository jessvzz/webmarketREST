package org.univaq.swa.webmarket.rest.resources;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.container.ContainerRequestContext;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import org.univaq.swa.webmarket.rest.exceptions.RESTWebApplicationException;
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;
import org.univaq.swa.webmarket.rest.security.Logged;
import org.univaq.swa.webmarket.rest.business.RichiesteService;
import org.univaq.swa.webmarket.rest.business.RichiesteServiceFactory;

/**
 * Servizio REST per la gestione delle richieste di acquisto
 */

/**
 * 
 * author samanta95
 */

@Path("/richieste")

public class RichiesteRes {
    
     private final RichiesteService business;
    
     public RichiesteRes() {
        this.business = RichiesteServiceFactory.getRichiesteService();

    }

     
    @GET
    @Logged
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(
    @Context UriInfo uriinfo,
    @QueryParam("userId") int userId,
    @Context SecurityContext sec,
    @Context ContainerRequestContext req)
    throws RESTWebApplicationException, SQLException, ClassNotFoundException, NamingException  {
        List<RichiestaOrdine> richieste = new ArrayList<>();

        richieste = business.getAllRichieste(userId);

        return Response.ok(richieste).build();
    }
     

    @Path("/{idrichiesta: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
 
    public RichiestaRes getItem(
        @PathParam("idrichiesta") int id,
        @Context UriInfo uriinfo,
        @Context SecurityContext sec,
        @Context ContainerRequestContext req) throws RESTWebApplicationException {
        
        RichiestaOrdine richiesta = business.getRichiesta(id);

        return new RichiestaRes(richiesta);
}
    


    @POST
    @Logged
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addItem(
            @Context ContainerRequestContext req,
            @Context UriInfo uriinfo,
            @Context SecurityContext sec,
            RichiestaOrdine richiesta
    ) throws SQLException, NamingException {

        try {
            
            int richiestaId = business.inserisciNuovaRichiesta(richiesta);

            if (richiestaId > 0){
                URI uri = uriinfo.getAbsolutePathBuilder().path(String.valueOf(richiestaId)).build();
                return Response.created(uri).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Inserimento della richiesta fallito").build();
            }
        }catch (SQLException | NamingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity("Errore del server durante l'elaborazione della richiesta")
            .build();
        }
   }

    //trovo richieste in attesa
    @GET
    @Logged
    @Path("/in_attesa")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRichiesteInAttesa() {
        List<RichiestaOrdine> richiesteInAttesa;

        try {
            richiesteInAttesa = business.getRichiesteInAttesa();
        } catch (Exception ex) {
            Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore interno del server").build();
        }

        return Response.ok(richiesteInAttesa).build();
    }
    
    @GET
    @Logged
    @Path("/in_corso")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRichiesteNonRisolte(@QueryParam("ordId") int userId, @Context SecurityContext sec) {
        List<RichiestaOrdine> richieste = new ArrayList<>();

        try {
            
            
            richieste = business.getRichiesteInCorso(userId);
            } catch (Exception ex) {
                Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore interno del server").build();
            }

            return Response.ok(richieste).build();
    }
    
         @GET
         @Logged
         @Path("/gestite_da_tecnico")
         @Produces(MediaType.APPLICATION_JSON)
         public Response getRichiesteGestiteDaTecnico(@QueryParam("idtecnico") int idTecnico) {
             List<RichiestaOrdine> richiesteGestite = new ArrayList<>();
        
             try {
                 richiesteGestite = business.getRichiesteGestiteDa(idTecnico);
                    // Controllo se la lista è vuota e restituzione del codice 404 se necessario
                if (richiesteGestite.isEmpty()) {
                    return Response.status(Response.Status.NOT_FOUND)
                                .entity("Nessuna richiesta trovata per il tecnico con ID: " + idTecnico)
                                .build();
                }
        
             } catch (Exception ex) {
                 Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, ex);
                 return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore interno del server").build();
             }
        
             return Response.ok(richiesteGestite).build();
         }
         

    }
