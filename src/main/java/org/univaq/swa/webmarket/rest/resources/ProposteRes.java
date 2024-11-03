/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import org.univaq.swa.webmarket.rest.business.ProposteService;
import org.univaq.swa.webmarket.rest.business.ProposteServiceFactory;
import org.univaq.swa.webmarket.rest.exceptions.RESTWebApplicationException;
import org.univaq.swa.webmarket.rest.models.PropostaAcquisto;
import org.univaq.swa.webmarket.rest.security.Logged;

/**
 *
 * @author jessviozzi
 */

@Path("/proposte")

public class ProposteRes {
    private final ProposteService business;
    
     public ProposteRes() {
        this.business = ProposteServiceFactory.getProposteService();

    }
     
    @Path("/{idproposta: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public PropostaRes getItem(
            @PathParam("idproposta") int idproposta,
            @Context UriInfo uriinfo,
            @Context SecurityContext sec,
            @Context ContainerRequestContext req)
            throws RESTWebApplicationException, SQLException, ClassNotFoundException, NamingException {
        
        PropostaAcquisto proposta = business.getProposta(idproposta);
        
        return new PropostaRes(proposta);
    }
    
    @GET
    @Path("/in_attesa")
    @Logged
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProposals(
            @QueryParam("userId") int userId,
            @Context UriInfo uriinfo,
            //iniettiamo elementi di contesto utili per la verifica d'accesso
            @Context SecurityContext sec,
            @Context ContainerRequestContext req)
            throws RESTWebApplicationException, SQLException, ClassNotFoundException, NamingException {
        List<PropostaAcquisto> proposte = new ArrayList<>();
        
        proposte = business.getAllInAttesa(userId);
        
        if (proposte.isEmpty()) return Response.status(Response.Status.NOT_FOUND).entity("Nessuna proposta in attesa associata all'utente selezionato").build();

        
        return Response.ok(proposte).build();
    }
    
    @GET
    @Logged
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProposals(
            @Context UriInfo uriinfo,
            @QueryParam("userId") int userId,
            @Context SecurityContext sec,
            @Context ContainerRequestContext req)
            throws RESTWebApplicationException, SQLException, ClassNotFoundException, NamingException {
        List<PropostaAcquisto> proposte = new ArrayList<>();

        proposte = business.getAll(userId);
        if(proposte.isEmpty()) return Response.status(Response.Status.NOT_FOUND).entity("Nessuna proposta associata all'utente")
                           .build();
        
        return Response.ok(proposte).build();
    }

    
     @POST
     @Logged
     @Consumes(MediaType.APPLICATION_JSON)
     public Response inserisciProposta(
        PropostaAcquisto proposta, 
        @Context UriInfo uriinfo, 
        @Context SecurityContext sec, 
        @Context ContainerRequestContext req) throws RESTWebApplicationException, SQLException, ClassNotFoundException, NamingException {
        

            try{
             int techId = UserUtils.getLoggedId(sec);
             
             int newProp = business.inserisciProposta(proposta, techId);

             if (newProp > 0) {

                URI uri = uriinfo.getAbsolutePathBuilder().path(String.valueOf(newProp)).build();
                //  return Response.status(Response.Status.CREATED).entity("Proposta inserita con successo").build();
                return Response.created(uri).build();
             } 
             else if (newProp == -1) { 
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("ID utente loggato non corrisponde al tecnico incaricato della richiesta")
                           .build();
        }
             else {
                 return Response.status(Response.Status.BAD_REQUEST).entity("Errore durante l'inserimento della proposta").build();
             }

         } catch (Exception e) {
             Logger.getLogger(ProposteRes.class.getName()).log(Level.SEVERE, null, e);
             return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore interno del server").build();
         } 
     }

    
}