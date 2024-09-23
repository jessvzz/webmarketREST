package org.univaq.swa.webmarket.rest.resources;

import javax.sql.DataSource;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.container.ContainerRequestContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.univaq.swa.webmarket.rest.exceptions.RESTWebApplicationException;
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;

/**
 * Servizio REST per la gestione delle richieste di acquisto
 */

/**
 * 
 * author samanta95
 */

@Path("/richieste")

public class RichiestaRes {

    @POST
    @Produces(MediaType.APPLICATION_JSON)  
    @Consumes(MediaType.APPLICATION_JSON)  
    public Response inserisciRichiesta(
            RichiestaOrdine richiesta,  // Oggetto RichiestaOrdine ricevuto dal client
            @Context UriInfo uriinfo,  // UriInfo per ottenere informazioni sulla richiesta
            @Context SecurityContext sec,  // Per gestire la sicurezza
            @Context ContainerRequestContext req) throws RESTWebApplicationException, SQLException, ClassNotFoundException, NamingException {
        
        InitialContext ctx;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            // Inizializzazione del contesto JNDI e recupero del DataSource
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
            conn = ds.getConnection();
            
            // Query SQL per l'inserimento della richiesta
            String query = "INSERT INTO richiesta_ordine (note, stato, data, codice_richiesta, utente_id, tecnico_id, categoria_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            ps = conn.prepareStatement(query);
            ps.setString(1, richiesta.getNote());
            ps.setString(2, richiesta.getStato().toString()); 
            ps.setDate(3, new java.sql.Date(richiesta.getData().getTime()));  // Conversione da java.util.Date a java.sql.Date
            ps.setString(4, richiesta.getCodiceRichiesta());
            ps.setInt(5, richiesta.getUtente().getId());  
            ps.setInt(6, richiesta.getTecnico() != null ? richiesta.getTecnico().getId() : null);  // Tecnico può essere null all'inizio
            ps.setInt(7, richiesta.getCategoria().getId()); 

            // Esecuzione della query di inserimento
            int rowsInserted = ps.executeUpdate();
            
            if (rowsInserted > 0) {
                // Se l'inserimento ha successo, restituiamo un HTTP 201 Created
                return Response.status(Response.Status.CREATED).entity("Richiesta inserita con successo").build();
            } else {
                // Se l'inserimento non va a buon fine
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore durante l'inserimento della richiesta").build();
            }

        } catch (NamingException | SQLException e) {
            // Gestione delle eccezioni
            Logger.getLogger(RichiestaRes.class.getName()).log(Level.SEVERE, null, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore interno del server").build();
        } finally {
            // Chiusura delle risorse
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }
}
