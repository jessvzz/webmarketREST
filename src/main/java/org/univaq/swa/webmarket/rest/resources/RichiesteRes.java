package org.univaq.swa.webmarket.rest.resources;

import javax.sql.DataSource;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.container.ContainerRequestContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.univaq.swa.webmarket.rest.exceptions.RESTWebApplicationException;
import org.univaq.swa.webmarket.rest.models.Caratteristica;
import org.univaq.swa.webmarket.rest.models.Categoria;
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;
import org.univaq.swa.webmarket.rest.models.StatoProposta;
import org.univaq.swa.webmarket.rest.models.StatoRichiesta;
import org.univaq.swa.webmarket.rest.models.TipologiaUtente;
import org.univaq.swa.webmarket.rest.models.Utente;
import org.univaq.swa.webmarket.rest.security.Logged;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<RichiestaOrdine> richiesteInAttesa = new ArrayList<>();

        richiesteInAttesa = business.getAllRichieste();

        return Response.ok(richiesteInAttesa).build();
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



    //Inserimento di una nuova richiesta
    @POST
    @Logged 
    @Consumes(MediaType.APPLICATION_JSON)  
    
    public Response inserisciRichiesta(
            RichiestaOrdine richiesta,  // Oggetto RichiestaOrdine ricevuto dal client
            @Context UriInfo uriinfo,  // UriInfo per ottenere informazioni sulla richiesta
            @Context SecurityContext sec,  // Per gestire la sicurezza
            @Context ContainerRequestContext req) throws RESTWebApplicationException, SQLException, ClassNotFoundException, NamingException {
        

                System.out.println("Metodo inserisciRichiesta chiamato");

                
        InitialContext ctx;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            int utenteId = UserUtils.getLoggedId(sec);

            // Debug 1: Stampa l'oggetto RichiestaOrdine per vedere se è popolato correttamente
           System.out.println("DEBUG: RichiestaOrdine ricevuta: " + richiesta.toString());

            // Inizializzazione del contesto JNDI e recupero del DataSource
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
            conn = ds.getConnection();
            
            String query = "INSERT INTO richiesta_ordine (note, stato, data, utente, categoria_id) VALUES (?, ?, ?, ?, ?)";
            
            ps = conn.prepareStatement(query);

            System.out.println("DEBUG: Valori da inserire nella query:");
            System.out.println("Note: " + richiesta.getNote());
            System.out.println("Stato: " + richiesta.getStato().toString());
            System.out.println("Data: " + richiesta.getData());
            System.out.println("Categoria ID: " + (richiesta.getCategoria() != null ? richiesta.getCategoria().getId() : "NULL"));

            ps.setString(1, richiesta.getNote());
            ps.setString(2, richiesta.getStato().toString()); 
            ps.setDate(3, new java.sql.Date(richiesta.getData().getTime()));  // Conversione da java.util.Date a java.sql.Date
            ps.setInt(4, utenteId);  
            if (richiesta.getCategoria() != null) {
                ps.setInt(5, richiesta.getCategoria().getId());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            // ps.setInt(7, richiesta.getCategoria().getId()); 

            int rowsInserted = ps.executeUpdate();
            
            if (rowsInserted > 0) {

                System.out.println("DEBUG: Inserimento riuscito");

                return Response.status(Response.Status.CREATED).entity("Richiesta inserita con successo").build();
            } else {
                System.out.println("DEBUG: Inserimento non riuscito");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore durante l'inserimento della richiesta").build();
            }

        } catch (NamingException | SQLException e) {
            Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("!!Errore interno del server!!").build();
        } finally {

             if (ps != null) {
                 try {
                     ps.close();
                 } catch (SQLException e) {
                     Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, e);
                 }
             }
             if (conn != null) {
                 try {
                     conn.close();
                 } catch (SQLException e) {
                     Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, e);
                 }
             }
        }
    }
    
    //endpoint per recuperare le caratteristiche di una categoria
    @GET
    @Path("/categorie/{categoriaId}/caratteristiche")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCaratteristicheByCategoria(@PathParam("categoriaId") int categoriaId) throws SQLException, NamingException, JsonProcessingException {
    
        // Log del parametro categoriaId
        System.out.println("Categoria ID ricevuto: " + categoriaId);

            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

     try{

        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
        conn = ds.getConnection();
    
        ps = conn.prepareStatement("SELECT c.id, c.nome, c.categoria_id, cat.nome AS categoria_nome, cat.padre AS categoria_padre\n" + //
                        "FROM caratteristica c\n" + //
                        "JOIN categoria cat ON c.categoria_id = cat.id\n" + //
                        "WHERE c.categoria_id = ?");
                        
        ps.setInt(1, categoriaId);
    
        rs = ps.executeQuery();
    
        List<Caratteristica> caratteristiche = new ArrayList<>();
    
        while (rs.next()) {
            Caratteristica caratteristica = new Caratteristica();
            caratteristica.setId(rs.getInt("id"));
            caratteristica.setNome(rs.getString("nome"));
    
            // Popola l'oggetto Categoria
            Categoria categoria = new Categoria();
            categoria.setId(rs.getInt("categoria_id"));
            categoria.setNome(rs.getString("categoria_nome"));
            categoria.setPadre(rs.getInt("categoria_padre"));
            caratteristica.setCategoria(categoria);

            caratteristiche.add(caratteristica);
        }

        // Debug per vedere cosa c'è nella lista caratteristiche
        System.out.println("Lista delle caratteristiche trovate: " + caratteristiche);

        return Response.ok(caratteristiche).build();
    }

    catch (NamingException | SQLException e) {
        Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, e);          
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore durante l'ottenimento delle caratteristiche").build();          
    }

    finally {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
    


    //trovo richieste in attesa
    @GET
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

            richieste = business.getRichiesteInCorso(utenteId);
            } catch (Exception ex) {
                Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, ex);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore interno del server").build();
            }

            return Response.ok(richieste).build();
    }
    
    
        //per me si puo' togliere
         @GET
         @Path("/non_assegnate")
         @Produces(MediaType.APPLICATION_JSON)
         public Response getRichiesteNonAssegnate(
            @QueryParam("view") @DefaultValue("base") String view,
             @QueryParam("fields") List<String> fields ) {
             List<RichiestaOrdine> richiesteNonAssegnate = new ArrayList<>();
             InitialContext ctx;

             try {
                 ctx = new InitialContext();
                 DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");

                 try (Connection conn = ds.getConnection()) {
                     PreparedStatement ps = conn.prepareStatement("SELECT * FROM richiesta_ordine WHERE tecnico IS NULL OR tecnico = 0");
                     ResultSet rs = ps.executeQuery();

                     while (rs.next()) {
                         RichiestaOrdine richiesta = new RichiestaOrdine();
                         richiesta.setId(rs.getInt("ID"));
                         richiesta.setCodiceRichiesta(rs.getString("codice_richiesta"));
                         richiesta.setData(rs.getDate("data"));
                         richiesta.setNote(rs.getString("note"));
                         richiesta.setStato(StatoRichiesta.valueOf(rs.getString("stato")));

                         int utenteId = rs.getInt("utente");
                         int categoriaId = rs.getInt("categoria_id");

                         if (utenteId > 0) {
                             Utente utente = recuperaUtente(conn, utenteId);
                             richiesta.setUtente(utente);
                        }

                         if (categoriaId > 0) {
                             Categoria categoria = recuperaCategoria(conn, categoriaId);
                             richiesta.setCategoria(categoria);
                         }

                         richiesteNonAssegnate.add(richiesta);
                     }
                 }

             } catch (NamingException | SQLException ex) {
                 Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, ex);
                 return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore interno del server").build();
             }


            // Filtra i dati in base a 'view' e 'fields'
            List<Map<String, Object>> risultatiFiltrati = filtraRisultati(richiesteNonAssegnate, view, fields);
            return Response.ok(risultatiFiltrati).build();

            //  return Response.ok(richiesteNonAssegnate).build();
            
         }

            // Funzione per filtrare i dati in base a 'view' e 'fields'
            private List<Map<String, Object>> filtraRisultati(List<RichiestaOrdine> richieste, String view, List<String> fields) {
                List<Map<String, Object>> risultatiFiltrati = new ArrayList<>();

                for (RichiestaOrdine richiesta : richieste) {
                    Map<String, Object> risultato = new HashMap<>();

                    if (fields.isEmpty()) {
                        // Se nessun campo è specificato, usa la vista
                        switch (view) {
                            case "base":
                                risultato.put("id", richiesta.getId());
                                risultato.put("codiceRichiesta", richiesta.getCodiceRichiesta());
                                // risultato.put("data", richiesta.getData());
                                risultato.put("note", richiesta.getNote());
                                break;
                            case "dettagliata":
                                risultato.put("id", richiesta.getId());
                                risultato.put("codiceRichiesta", richiesta.getCodiceRichiesta());
                                risultato.put("data", richiesta.getData());
                                risultato.put("note", richiesta.getNote());
                                risultato.put("stato", richiesta.getStato());
                                risultato.put("utente", richiesta.getUtente());
                                risultato.put("categoria", richiesta.getCategoria());
                                break;
                            default:
                                throw new IllegalArgumentException("Vista non valida");
                        }
                    } else {
                        // Se sono specificati i campi, restituire solo quelli 
                        if (fields.contains("id")) risultato.put("id", richiesta.getId());
                        if (fields.contains("codiceRichiesta")) risultato.put("codiceRichiesta", richiesta.getCodiceRichiesta());
                        if (fields.contains("data")) risultato.put("data", richiesta.getData());
                        if (fields.contains("note")) risultato.put("note", richiesta.getNote());
                        if (fields.contains("stato")) risultato.put("stato", richiesta.getStato());
                        if (fields.contains("utente")) risultato.put("utente", richiesta.getUtente());
                        if (fields.contains("categoria")) risultato.put("categoria", richiesta.getCategoria());
                    }

                    risultatiFiltrati.add(risultato);
                    System.out.println("Risultato: " + risultato);
                }

                return risultatiFiltrati;
            }




         @GET
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
         
              //recuperaro l'Utente
private Utente recuperaUtente(Connection conn, int utenteId) throws SQLException {
    PreparedStatement ps = conn.prepareStatement("SELECT * FROM utente WHERE id = ?");
    ps.setInt(1, utenteId);
    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
        Utente utente = new Utente();
        utente.setId(rs.getInt("id"));
        utente.setUsername(rs.getString("username"));
        utente.setEmail(rs.getString("email"));
        utente.setPassword(rs.getString("password"));
        
        System.out.println("sono qui utente");
        return utente;
    }
    return null; 
}


//recupero la categoria
private Categoria recuperaCategoria(Connection conn, int categoriaId) throws SQLException {
    PreparedStatement ps = conn.prepareStatement("SELECT * FROM categoria WHERE id = ?");
    ps.setInt(1, categoriaId);
    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
        Categoria categoria = new Categoria();
        categoria.setId(rs.getInt("id"));
        categoria.setNome(rs.getString("nome"));
        categoria.setPadre(rs.getInt("padre"));

        
        System.out.println("sono qui categoria");

        return categoria;
    }
    return null; 
}

//recupero il tecnico   
private Utente recuperaTecnico(Connection conn, int tecnicoId) throws SQLException {
    PreparedStatement ps = conn.prepareStatement("SELECT * FROM utente WHERE id = ?");
    ps.setInt(1, tecnicoId);        
    ResultSet rs = ps.executeQuery();   

    if(rs.next()) {
        Utente tecnico = new Utente();
        tecnico.setId(rs.getInt("id"));
        tecnico.setUsername(rs.getString("username"));
        tecnico.setEmail(rs.getString("email"));
        tecnico.setPassword(rs.getString("password"));
        tecnico.setTipologiaUtente(TipologiaUtente.valueOf(rs.getString("tipologia_utente")));
        System.out.println("sono qui tecnico");
        return tecnico;
    }
    return null;    

}
        

    }
