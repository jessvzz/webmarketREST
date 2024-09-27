/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.resources;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.GET;
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
    public Response presaInCarico(@QueryParam("techid") int techId, @Context SecurityContext sec) throws SQLException {
        InitialContext ctx;
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            System.out.println("id tecnico: "+techId);
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
            conn = ds.getConnection();

            
            if (techId < 0) {
                return Response.status(Response.Status.FORBIDDEN)
                               .entity("Tecnico non autenticato.")
                               .build();
            }

            String query = "UPDATE richiesta_ordine SET stato = ?, tecnico = ? WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, StatoRichiesta.PRESA_IN_CARICO.toString());
            ps.setInt(2, techId);
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
    
    
    @GET
    @Path("/dettagli")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDettagliRichiesta() throws SQLException {
        InitialContext ctx;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
            conn = ds.getConnection();

            String query = "SELECT r.id AS richiesta_id, r.codice_richiesta, r.data AS data_richiesta, " +
                           "r.note AS note_richiesta, r.stato AS stato_richiesta, u.username AS ordinante, " +
                           "cat.nome AS categoria, p.produttore, p.prodotto, p.codice_prodotto, p.prezzo, " +
                           "p.stato AS stato_proposta, p.motivazione AS motivazione_proposta, p.URL AS url_prodotto, " +
                           "p.note AS note_proposta, o.stato AS stato_ordine, o.data AS data_ordine " +
                           "FROM richiesta_ordine r " +
                           "LEFT JOIN utente u ON r.utente = u.ID " +
                           "LEFT JOIN categoria cat ON r.categoria_id = cat.ID " +
                           "LEFT JOIN proposta_acquisto p ON r.id = p.richiesta_id " +
                           "LEFT JOIN ordine o ON p.id = o.proposta_id " +
                           "WHERE r.id = ?";

            ps = conn.prepareStatement(query);
            ps.setInt(1, richiesta.getId());
            rs = ps.executeQuery();

            if (rs.next()) {
                // uso una struttura fatta con jsonObjBuilder
                JsonObjectBuilder richiestaDetails = Json.createObjectBuilder()
                    .add("richiesta_id", rs.getInt("richiesta_id"))
                    .add("codice_richiesta", rs.getString("codice_richiesta"))
                    .add("data_richiesta", rs.getDate("data_richiesta").toString())
                    .add("note_richiesta", rs.getString("note_richiesta"))
                    .add("stato_richiesta", rs.getString("stato_richiesta"))
                    .add("ordinante", rs.getString("ordinante"))
                    .add("categoria", rs.getString("categoria"));

                //se prodotto è nullo non ci sta proposta
                if (rs.getString("prodotto") != null) {
                    JsonObjectBuilder propostaBuilder = Json.createObjectBuilder()
                        .add("produttore", rs.getString("produttore"))
                        .add("prodotto", rs.getString("prodotto"))
                        .add("codice_prodotto", rs.getString("codice_prodotto"))
                        .add("prezzo", rs.getDouble("prezzo"))
                        .add("stato_proposta", rs.getString("stato_proposta"));

                    String motivazione = rs.getString("motivazione_proposta");
                    if (motivazione != null) {
                        propostaBuilder.add("motivazione_proposta", motivazione);
                    }

                    String note = rs.getString("note_proposta");
                    if (note != null) {
                        propostaBuilder.add("note_proposta", note);
                    }

                    propostaBuilder.add("url_prodotto", rs.getString("url_prodotto"));

                    richiestaDetails.add("proposta", propostaBuilder);
                }


                

                return Response.ok(richiestaDetails.build()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Dettagli della richiesta non trovati.")
                               .build();
            }
        } catch (SQLException | NamingException ex) {
            Logger.getLogger(RichiestaRes.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Errore interno del server.")
                           .build();
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }


    /*
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
*/
}
