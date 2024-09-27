package org.univaq.swa.webmarket.rest.resources;

import javax.sql.DataSource;
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
import org.univaq.swa.webmarket.rest.models.Categoria;
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;
import org.univaq.swa.webmarket.rest.models.StatoProposta;
import org.univaq.swa.webmarket.rest.models.StatoRichiesta;
import org.univaq.swa.webmarket.rest.models.Utente;

/**
 * Servizio REST per la gestione delle richieste di acquisto
 */

/**
 * 
 * author samanta95
 */

@Path("/richieste")

public class RichiesteRes {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<RichiestaOrdine> richiesteInAttesa = new ArrayList<>();
        InitialContext ctx;

        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");

            try (Connection conn = ds.getConnection()) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM richiesta_ordine");
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    RichiestaOrdine richiesta = new RichiestaOrdine();
                    richiesta.setId(rs.getInt("id"));
                    richiesta.setCodiceRichiesta(rs.getString("codice_richiesta"));
                    richiesta.setData(rs.getDate("data"));
                    richiesta.setNote(rs.getString("note"));
                    richiesta.setStato(StatoRichiesta.valueOf(rs.getString("stato")));
                    
                    
                    int utenteId = rs.getInt("utente");
                    int tecnicoId = rs.getInt("tecnico");
                    int categoriaId = rs.getInt("categoria_id");
                    
                    System.out.println("utente: "+utenteId+", tecnico: "+tecnicoId+", categoria: "+categoriaId);
                    
                    if (!rs.wasNull() && utenteId > 0) {
                        Utente utente = recuperaUtente(conn, utenteId);
                        if (utente != null) {
                           richiesta.setUtente(utente);
                        }
                    }

                    if (!rs.wasNull() && tecnicoId > 0) {
                        Utente tecnico = recuperaUtente(conn, tecnicoId);
                        if (tecnico != null) {
                            richiesta.setTecnico(tecnico);
                        }
                    }

                    if (!rs.wasNull() && categoriaId > 0) {
                        Categoria categoria = recuperaCategoria(conn, categoriaId);
                        if (categoria != null) {
                            richiesta.setCategoria(categoria);
                        }
                    }
                    

                    richiesteInAttesa.add(richiesta);
                }
            }

        } catch (NamingException | SQLException ex) {
            Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore interno del server").build();
        }

        return Response.ok(richiesteInAttesa).build();
    }

    @Path("/{idrichiesta: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
 
    public RichiestaRes getItem(
        @PathParam("idrichiesta") int id,
        @Context UriInfo uriinfo,
        @Context SecurityContext sec,
        @Context ContainerRequestContext req) throws RESTWebApplicationException {
    
    RichiestaOrdine richiesta = new RichiestaOrdine();
    InitialContext ctx;

    try {
        ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");

        try (Connection conn = ds.getConnection()) {
            // Query per recuperare i dettagli della richiesta
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM richiesta_ordine WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                richiesta.setId(rs.getInt("id"));
                richiesta.setCodiceRichiesta(rs.getString("codice_richiesta"));
                richiesta.setData(rs.getDate("data"));
                richiesta.setNote(rs.getString("note"));
                richiesta.setStato(StatoRichiesta.valueOf(rs.getString("stato")));

                int utenteId = rs.getInt("utente");
                int tecnicoId = rs.getInt("tecnico");
                int categoriaId = rs.getInt("categoria_id");
                
                
                //trovo utete
                if (utenteId > 0) {
                    Utente utente = recuperaUtente(conn, utenteId);
                    richiesta.setUtente(utente);
                }

                //trovo tecnico
                if (tecnicoId > 0) {
                    Utente tecnico = recuperaUtente(conn, tecnicoId);
                    richiesta.setTecnico(tecnico);
                }

                //trovo cat
                if (categoriaId > 0) {
                    Categoria categoria = recuperaCategoria(conn, categoriaId);
                    richiesta.setCategoria(categoria);
                }
                

            } else {
                throw new RESTWebApplicationException(Response.Status.NOT_FOUND.getStatusCode(), "Richiesta non trovata");
            }
        }
    } catch (NamingException | SQLException ex) {
        Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, ex);
        throw new RESTWebApplicationException(ex);
    }

    return new RichiestaRes(richiesta);
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
    //Inserimento di una nuova richiesta
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
            String query = "INSERT INTO richiesta_ordine (note, stato, data, codice_richiesta, utente, tecnico, categoria_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            
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
            Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore interno del server").build();
        } finally {
            // Chiusura delle risorse
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }
    
    //trovo richieste in attesa
    @GET
    @Path("/in_attesa")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRichiesteInAttesa() {
        List<RichiestaOrdine> richiesteInAttesa = new ArrayList<>();
        InitialContext ctx;

        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");

            try (Connection conn = ds.getConnection()) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM richiesta_ordine WHERE stato = ?");
                ps.setString(1, StatoRichiesta.IN_ATTESA.toString());
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    RichiestaOrdine richiesta = new RichiestaOrdine();
                    richiesta.setCodiceRichiesta(rs.getString("codice_richiesta"));
                    richiesta.setData(rs.getDate("data"));
                    richiesta.setNote(rs.getString("note"));
                    richiesta.setStato(StatoRichiesta.valueOf(rs.getString("stato")));

                    int utenteId = rs.getInt("utente");
                    int tecnicoId = rs.getInt("tecnico");
                    int categoriaId = rs.getInt("categoria_id");

                    if (utenteId > 0) {
                        Utente utente = recuperaUtente(conn, utenteId);
                        richiesta.setUtente(utente);
                    }

                    if (tecnicoId > 0) {
                        Utente tecnico = recuperaUtente(conn, tecnicoId);
                        richiesta.setTecnico(tecnico);
                    }

                    if (categoriaId > 0) {
                        Categoria categoria = recuperaCategoria(conn, categoriaId);
                        richiesta.setCategoria(categoria);
                    }

                    richiesteInAttesa.add(richiesta);
                }
            }

        } catch (NamingException | SQLException ex) {
            Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore interno del server").build();
        }

        return Response.ok(richiesteInAttesa).build();
    }
    
    @GET
    @Path("/non_risolte")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRichiesteNonRisolte(@QueryParam("ordId") int utenteId) {
        List<RichiestaOrdine> richieste = new ArrayList<>();
        InitialContext ctx;

        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");

            try (Connection conn = ds.getConnection()) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM richiesta_ordine WHERE stato != ? AND utente = ?");
                ps.setString(1, "RISOLTA");
                ps.setInt(2, utenteId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    RichiestaOrdine richiesta = new RichiestaOrdine();
                    richiesta.setId(rs.getInt("ID"));
                    richiesta.setCodiceRichiesta(rs.getString("codice_richiesta"));
                    richiesta.setData(rs.getDate("data"));
                    richiesta.setNote(rs.getString("note"));
                    richiesta.setStato(StatoRichiesta.valueOf(rs.getString("stato")));

                    int tecnicoId = rs.getInt("tecnico");
                    int categoriaId = rs.getInt("categoria_id");

                    if (utenteId > 0) {
                        Utente utente = recuperaUtente(conn, utenteId);
                        richiesta.setUtente(utente);
                    }

                    if (tecnicoId > 0) {
                        Utente tecnico = recuperaUtente(conn, tecnicoId);
                        richiesta.setTecnico(tecnico);
                    }

                    if (categoriaId > 0) {
                        Categoria categoria = recuperaCategoria(conn, categoriaId);
                        richiesta.setCategoria(categoria);
                    }

                    richieste.add(richiesta);
                }
            }

        } catch (NamingException | SQLException ex) {
            Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore interno del server").build();
        }

        return Response.ok(richieste).build();
    }
    
    
        //     @GET
        // @Path("/non_assegnate")
        // @Produces(MediaType.APPLICATION_JSON)
        // public Response getRichiesteNonAssegnate() {
        //     List<RichiestaOrdine> richiesteNonAssegnate = new ArrayList<>();
        //     InitialContext ctx;

        //     try {
        //         ctx = new InitialContext();
        //         DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");

        //         try (Connection conn = ds.getConnection()) {
        //             PreparedStatement ps = conn.prepareStatement("SELECT * FROM richiesta_ordine WHERE tecnico IS NULL OR tecnico = 0");
        //             ResultSet rs = ps.executeQuery();

        //             while (rs.next()) {
        //                 RichiestaOrdine richiesta = new RichiestaOrdine();
        //                 richiesta.setId(rs.getInt("ID"));
        //                 richiesta.setCodiceRichiesta(rs.getString("codice_richiesta"));
        //                 richiesta.setData(rs.getDate("data"));
        //                 richiesta.setNote(rs.getString("note"));
        //                 richiesta.setStato(StatoRichiesta.valueOf(rs.getString("stato")));

        //                 int utenteId = rs.getInt("utente");
        //                 int categoriaId = rs.getInt("categoria_id");

        //                 if (utenteId > 0) {
        //                     Utente utente = recuperaUtente(conn, utenteId);
        //                     richiesta.setUtente(utente);
        //                 }

        //                 if (categoriaId > 0) {
        //                     Categoria categoria = recuperaCategoria(conn, categoriaId);
        //                     richiesta.setCategoria(categoria);
        //                 }

        //                 richiesteNonAssegnate.add(richiesta);
        //             }
        //         }

        //     } catch (NamingException | SQLException ex) {
        //         Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, ex);
        //         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore interno del server").build();
        //     }

        //     return Response.ok(richiesteNonAssegnate).build();
        // }

        // @GET
        // @Path("/gestite_da_tecnico/{idtecnico}")
        // @Produces(MediaType.APPLICATION_JSON)
        // public Response getRichiesteGestiteDaTecnico(@PathParam("idtecnico") int idTecnico) {
        //     List<RichiestaOrdine> richiesteGestite = new ArrayList<>();
        //     InitialContext ctx;
        
        //     try {
        //         ctx = new InitialContext();
        //         DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
        
        //         try (Connection conn = ds.getConnection()) {
        //             PreparedStatement ps = conn.prepareStatement("SELECT * FROM richiesta_ordine WHERE tecnico = ?");
        //             ps.setInt(1, idTecnico);
        //             ResultSet rs = ps.executeQuery();
        
        //             while (rs.next()) {
        //                 RichiestaOrdine richiesta = new RichiestaOrdine();
        //                 richiesta.setId(rs.getInt("ID"));
        //                 richiesta.setCodiceRichiesta(rs.getString("codice_richiesta"));
        //                 richiesta.setData(rs.getDate("data"));
        //                 richiesta.setNote(rs.getString("note"));
        //                 richiesta.setStato(StatoRichiesta.valueOf(rs.getString("stato")));
        
        //                 int utenteId = rs.getInt("utente");
        //                 int categoriaId = rs.getInt("categoria_id");
        
        //                 if (utenteId > 0) {
        //                     Utente utente = recuperaUtente(conn, utenteId);
        //                     richiesta.setUtente(utente);
        //                 }
        
        //                 if (categoriaId > 0) {
        //                     Categoria categoria = recuperaCategoria(conn, categoriaId);
        //                     richiesta.setCategoria(categoria);
        //                 }
        
        //                 richiesteGestite.add(richiesta);
        //             }
        //         }
        
        //     } catch (NamingException | SQLException ex) {
        //         Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, ex);
        //         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore interno del server").build();
        //     }
        
        //     return Response.ok(richiesteGestite).build();
        // }
        

    }
