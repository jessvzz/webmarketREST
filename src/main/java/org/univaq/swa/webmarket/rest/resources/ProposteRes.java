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
    
    @Path("/{idproposta: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
 
    public PropostaRes getItem(
            @PathParam("idproposta") int idproposta,
            @Context UriInfo uriinfo,
            //iniettiamo elementi di contesto utili per la verifica d'accesso
            @Context SecurityContext sec,
            @Context ContainerRequestContext req)
            throws RESTWebApplicationException, SQLException, ClassNotFoundException, NamingException {
        
        PropostaAcquisto proposta = new PropostaAcquisto();
        
        InitialContext ctx;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
            Connection conn = ds.getConnection();
            
            PreparedStatement ps = conn.prepareStatement("Select * FROM proposta_acquisto WHERE id = ?");
            ps.setInt(1, idproposta);
            
            ResultSet rs = ps.executeQuery();
            
            rs.next();
                
            proposta.setProduttore(rs.getString("produttore"));
            proposta.setProdotto(rs.getString("prodotto"));
            proposta.setCodice(rs.getString("codice"));
            proposta.setCodiceProdotto(rs.getString("codice_prodotto"));
            proposta.setPrezzo(rs.getFloat("prezzo")); 
            proposta.setUrl(rs.getString("URL"));
            proposta.setNote(rs.getString("note"));
            proposta.setStatoProposta(StatoProposta.valueOf(rs.getString("stato")));
            proposta.setData(rs.getDate("data"));
            proposta.setMotivazione(rs.getString("motivazione"));
            proposta.setId(rs.getInt("id")); 
            
            ps.close();

        
            
        } catch (NamingException ex) {
            Logger.getLogger(ProposteRes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new PropostaRes(proposta);
    }
    
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProposals(
            @Context UriInfo uriinfo,
            //iniettiamo elementi di contesto utili per la verifica d'accesso
            @Context SecurityContext sec,
            @Context ContainerRequestContext req)
            throws RESTWebApplicationException, SQLException, ClassNotFoundException, NamingException {
        
        List<PropostaAcquisto> proposte = new ArrayList<>();
        
        InitialContext ctx;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
            Connection conn = ds.getConnection();
            
            PreparedStatement ps = conn.prepareStatement("Select * FROM proposta_acquisto");
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                
                PropostaAcquisto proposta = new PropostaAcquisto();
                proposta.setProduttore(rs.getString("produttore"));
                proposta.setProdotto(rs.getString("prodotto"));
                proposta.setCodice(rs.getString("codice"));
                proposta.setCodiceProdotto(rs.getString("codice_prodotto"));
                proposta.setPrezzo(rs.getFloat("prezzo"));
                proposta.setUrl(rs.getString("URL"));
                proposta.setNote(rs.getString("note"));
                proposta.setStatoProposta(StatoProposta.valueOf(rs.getString("stato")));
                proposta.setData(rs.getDate("data"));
                proposta.setMotivazione(rs.getString("motivazione"));
                proposta.setId(rs.getInt("richiesta_id"));
                proposte.add(proposta);              
            }
            
        } catch (NamingException ex) {
            Logger.getLogger(ProposteRes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.ok(proposte).build();
    }

    //inserimento
     @POST
     @Logged
     @Consumes(MediaType.APPLICATION_JSON)
     public Response inserisciProposta(
        PropostaAcquisto proposta, //oggetto Proposta ricevuto in formato JSON dal client
        @Context UriInfo uriinfo,  // UriInfo per ottenere informazioni sulla richiesta
        @Context SecurityContext sec,  // Per gestire la sicurezza
        @Context ContainerRequestContext req) throws RESTWebApplicationException, SQLException, ClassNotFoundException, NamingException {
        
            System.out.println("Metodo inserisciProposta chiamato");

            InitialContext ctx;
            Connection conn = null;
            PreparedStatement ps = null;

         try {
            int utenteId = UserUtils.getLoggedId(sec);

            // Debug 1: Stampa l'oggetto PropostaAcquisto per vedere se è popolato correttamente
            System.out.println("DEBUG: Proposta ricevuta: " + proposta.toString());
            // Controllo se richiestaOrdine è null
       
            if (proposta.getRichiestaOrdine() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Richiesta non valida").build();
          }


            //Connessione al database e inserimento della proposta
             ctx = new InitialContext();
             DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
             conn = ds.getConnection();

             // Query SQL per inserire una nuova proposta
             String query = "INSERT INTO proposta_acquisto (produttore, prodotto, codice, codice_prodotto, prezzo, URL, note, stato, richiesta_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
             ps = conn.prepareStatement(query);

             // Debug 2: Stampa i valori prima di assegnarli ai parametri SQL
             System.out.println("DEBUG: Valori da inserire nella query:");
             System.out.println("produttore: " + proposta.getProduttore());
             System.out.println("prodotto: " + proposta.getProdotto());
             System.out.println("codice: " + proposta.getCodice());
             System.out.println("Codice prodotto: " + proposta.getCodiceProdotto());
             System.out.println("prezzo: " + proposta.getPrezzo());
             System.out.println("url" + proposta.getUrl());
             System.out.println("note: " + proposta.getNote());
             System.out.println("stato" + proposta.getStatoProposta().toString());
             System.out.println("richiesta: " + proposta.getRichiestaOrdine().getId());
             
             ps.setString(1, proposta.getProduttore());
             ps.setString(2, proposta.getProdotto());
             ps.setString(3, proposta.getCodice());
             ps.setString(4, proposta.getCodiceProdotto());
             ps.setFloat(5, proposta.getPrezzo());
             ps.setString(6, proposta.getUrl());
             ps.setString(7, proposta.getNote());
             ps.setString(8, proposta.getStatoProposta().toString());
             ps.setInt(9, proposta.getRichiestaOrdine().getId()); // ID della richiesta associata

             int rowsInserted = ps.executeUpdate();

             if (rowsInserted > 0) {
                System.out.println("DEBUG: Inserimento riuscito");

                 return Response.status(Response.Status.CREATED).entity("Proposta inserita con successo").build();
             } else {
                System.out.println("DEBUG: Inserimento non riuscito");
                 return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore durante l'inserimento della proposta").build();
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