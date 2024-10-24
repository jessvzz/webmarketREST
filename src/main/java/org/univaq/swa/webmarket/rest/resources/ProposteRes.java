/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.resources;

import javax.sql.DataSource;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
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
import org.univaq.swa.webmarket.rest.business.ProposteService;
import org.univaq.swa.webmarket.rest.business.ProposteServiceFactory;
import org.univaq.swa.webmarket.rest.business.ProposteServiceImpl;
import org.univaq.swa.webmarket.rest.business.RichiesteService;
import org.univaq.swa.webmarket.rest.business.RichiesteServiceFactory;
import org.univaq.swa.webmarket.rest.exceptions.RESTWebApplicationException;
import org.univaq.swa.webmarket.rest.models.PropostaAcquisto;
import org.univaq.swa.webmarket.rest.models.StatoProposta;
import org.univaq.swa.webmarket.rest.models.StatoRichiesta;
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
            @Context UriInfo uriinfo,
            //iniettiamo elementi di contesto utili per la verifica d'accesso
            @Context SecurityContext sec,
            @Context ContainerRequestContext req)
            throws RESTWebApplicationException, SQLException, ClassNotFoundException, NamingException {
        int userId = UserUtils.getLoggedId(sec);
        List<PropostaAcquisto> proposte = new ArrayList<>();
        
        InitialContext ctx;
        proposte = business.getAll(userId);
        
        return Response.ok(proposte).build();
    }

    //inserimento
     @POST
     @Logged
     @Consumes(MediaType.APPLICATION_JSON)
     public Response inserisciProposta(
        PropostaAcquisto proposta, 
        @Context UriInfo uriinfo, 
        @Context SecurityContext sec, 
        @Context ContainerRequestContext req) throws RESTWebApplicationException, SQLException, ClassNotFoundException, NamingException {
        

            try{ 
             int rowsInserted = business.inserisciProposta(proposta);

             if (rowsInserted > 0) {
                System.out.println("DEBUG: Inserimento riuscito");

                URI uri = uriinfo.getAbsolutePathBuilder().path(String.valueOf(rowsInserted)).build();
                //  return Response.status(Response.Status.CREATED).entity("Proposta inserita con successo").build();
                return Response.created(uri).build();
             } else {
                System.out.println("DEBUG: Inserimento non riuscito");
                 return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore durante l'inserimento della proposta").build();
             }

         } catch (Exception e) {
             Logger.getLogger(ProposteRes.class.getName()).log(Level.SEVERE, null, e);
             return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore interno del server").build();
         } 
     }

    
}