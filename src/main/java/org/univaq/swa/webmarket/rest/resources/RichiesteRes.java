package org.univaq.swa.webmarket.rest.resources;

import javax.sql.DataSource;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
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
import java.sql.Date;
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
     
     /*
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<RichiestaOrdine> richiesteInAttesa = new ArrayList<>();

        richiesteInAttesa = business.getAllRichieste();

        return Response.ok(richiesteInAttesa).build();
    }
     */

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




/*
@POST
@Logged
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public Response addItem(
        @Context ContainerRequestContext req,
        @Context UriInfo uriinfo,
        @Context SecurityContext sec,
        @FormParam("note") String note,
        @FormParam("data") Date data,
        @FormParam("stato") String stato,
        @FormParam("categoriaId") int categoriaId,
        @FormParam("idcaratteristica[]") List<Integer> idcaratteristica,  // Gestisce più ID
        @FormParam("valore[]") List<String> valore  // Gestisce più valori
) throws SQLException, NamingException {

        int utenteId = UserUtils.getLoggedId(sec);
        int richiestaId = business.inserisciNuovaRichiesta(utenteId, note, data, stato, categoriaId, idcaratteristica, valore);
        
        if (richiestaId > 0){
            URI uri = uriinfo.getAbsolutePathBuilder().path(String.valueOf(richiestaId)).build();
            return Response.created(uri).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Inserimento della richiesta fallito").build();
        }
    
}

*/
    
    @POST
    @Logged
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addItem(
            @Context ContainerRequestContext req,
            @Context UriInfo uriinfo,
            @Context SecurityContext sec,
            RichiestaOrdine richiesta
    ) throws SQLException, NamingException {

            int utenteId = UserUtils.getLoggedId(sec);
            if (utenteId == -1) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Utente non autenticato")
                        .build();
            }
            int richiestaId = business.inserisciNuovaRichiesta(richiesta, utenteId);

            if (richiestaId > 0){
                URI uri = uriinfo.getAbsolutePathBuilder().path(String.valueOf(richiestaId)).build();
                return Response.created(uri).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Inserimento della richiesta fallito").build();
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
    public Response getRichiesteNonRisolte(@Context SecurityContext sec) {
        List<RichiestaOrdine> richieste = new ArrayList<>();

        try {
            
            int utenteId = UserUtils.getLoggedId(sec);
            
            if (utenteId == -1) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Accesso non autorizzato").build();
            }

            richieste = business.getRichiesteInCorso(utenteId);
            } catch (Exception ex) {
                Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore interno del server").build();
            }

            return Response.ok(richieste).build();
    }
    
    /*
    @GET
    @Path("/non_assegnate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRichiesteNonAssegnate() {
        List<RichiestaOrdine> richiesteNonAssegnate = new ArrayList<>();

            try  {
               
                   richiesteNonAssegnate = business.getRichiesteNonAssegnate();
        } catch (Exception ex) {
            Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore interno del server").build();
        }

           return Response.ok(richiesteNonAssegnate).build();
    }
    */




         @GET
         @Logged
         @Path("/gestite_da_tecnico/{idtecnico}")
         @Produces(MediaType.APPLICATION_JSON)
         public Response getRichiesteGestiteDaTecnico(@PathParam("idtecnico") int idTecnico) {
             List<RichiestaOrdine> richiesteGestite = new ArrayList<>();
        
             try {
                 richiesteGestite = business.getRichiesteGestiteDa(idTecnico);
        
             } catch (Exception ex) {
                 Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, ex);
                 return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore interno del server").build();
             }
        
             return Response.ok(richiesteGestite).build();
         }
         

    }
