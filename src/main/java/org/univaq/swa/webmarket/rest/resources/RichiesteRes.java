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
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import org.univaq.swa.webmarket.rest.models.CaratteristicaRichiesta;
import org.univaq.swa.webmarket.rest.models.Categoria;
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;
import org.univaq.swa.webmarket.rest.models.StatoProposta;
import org.univaq.swa.webmarket.rest.models.StatoRichiesta;
import org.univaq.swa.webmarket.rest.models.TipologiaUtente;
import org.univaq.swa.webmarket.rest.models.Utente;
import org.univaq.swa.webmarket.rest.security.Logged;
import org.univaq.swa.webmarket.rest.models.RichiestaCompleta;
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


//recupero la caratteristica

private CaratteristicaRichiesta recuperaCaratteristicaRichiesta(Connection conn, int caratteristicaRicId) throws SQLException {
    PreparedStatement ps = conn.prepareStatement("SELECT * FROM caratteristica_richiesta WHERE id = ?");
    ps.setInt(1, caratteristicaRicId);
    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
        CaratteristicaRichiesta caratteristica = new CaratteristicaRichiesta();
        caratteristica.setId(rs.getInt("id"));
        caratteristica.setRichiestaOrdine(recuperaRichiestaOrdine(conn, rs.getInt("richiesta_id")));
        caratteristica.setCaratteristica(recuperaCaratteristica(conn, rs.getInt("caratteristica_id")));
        caratteristica.setValore(rs.getString("valore"));
        System.out.println("sono qui caratteristica");
        return caratteristica;
    }
    return null; 
}


private Caratteristica recuperaCaratteristica(Connection conn, int caratteristicaId) throws SQLException {
    PreparedStatement ps = conn.prepareStatement("SELECT * FROM caratteristica WHERE id = ?");
    ps.setInt(1, caratteristicaId);
    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
        Caratteristica caratteristica = new Caratteristica();
        caratteristica.setId(rs.getInt("id"));
        caratteristica.setNome(rs.getString("nome"));
        Categoria categoria = recuperaCategoria(conn, rs.getInt("categoria_id"));
        caratteristica.setCategoria(categoria);
        System.out.println("sono qui caratteristica");
        return caratteristica;
    }
    return null; 
}

private RichiestaOrdine recuperaRichiestaOrdine(Connection conn, int richiestaId) throws SQLException {
    PreparedStatement ps = conn.prepareStatement("SELECT * FROM richiesta_ordine WHERE id = ?");
    ps.setInt(1, richiestaId);
    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
        RichiestaOrdine richiesta = new RichiestaOrdine();
        richiesta.setId(rs.getInt("id"));
        richiesta.setCodiceRichiesta(rs.getString("codice_richiesta"));
        richiesta.setData(rs.getDate("data"));
        richiesta.setNote(rs.getString("note"));
        richiesta.setStato(StatoRichiesta.valueOf(rs.getString("stato")));
        System.out.println("sono qui richiesta");
        return richiesta;
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
    // @POST
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    // public Response inserisciRichiesta(RichiestaOrdine richiestaCompleta) {

    //public Response inserisciRichiesta(RichiestaCompleta richiestaCompleta) {
       // Map<Caratteristica, String> caratteristiche = new HashMap<>();

       // for (CaratteristicaRichiesta caratteristicaRichiesta : richiestaCompleta.getCaratteristiche()) {
       //     caratteristiche.put(caratteristicaRichiesta.getCaratteristica(), caratteristicaRichiesta.getValore());
        //}

    //     int newId;
    //     try {
    //         newId = business.inserisciProva(richiestaCompleta);

    //         //newId = business.inserisciNuovaRichiesta(richiestaCompleta.getRichiesta(), caratteristiche);
    //     } catch (Exception ex) {
    //         Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, ex);
    //         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore durante l'inserimento della richiesta").build();
    //     }

    //     return Response.status(Response.Status.CREATED).entity(newId).build();
    // }

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

    InitialContext ctx = new InitialContext();
    DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
    Connection conn = ds.getConnection();

    try {
        int utenteId = UserUtils.getLoggedId(sec);

        // Inserimento della richiesta
        String query = "INSERT INTO richiesta_ordine (note, data, stato, utente, categoria_id) VALUES(?, ?, ?, ?,?)";
        PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, note);
        ps.setDate(2, new java.sql.Date(data.getTime()));
        ps.setString(3, stato);
        ps.setInt(4,utenteId);
        ps.setInt(5, categoriaId);

        int rowsInserted = ps.executeUpdate();
        if (rowsInserted == 1) {
            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            int richiestaId = keys.getInt(1);

            // Inserimento delle caratteristiche associate
            String queryCaratteristica = "INSERT INTO caratteristica_richiesta (richiesta_id, caratteristica_id, valore) VALUES(?, ?, ?)";
            PreparedStatement psCaratteristica = conn.prepareStatement(queryCaratteristica);

            for (int i = 0; i < idcaratteristica.size(); i++) {
                psCaratteristica.setInt(1, richiestaId);
                psCaratteristica.setInt(2, idcaratteristica.get(i));
                psCaratteristica.setString(3, valore.get(i));
                psCaratteristica.addBatch();  // Usa batch per ottimizzare le prestazioni
            }

            psCaratteristica.executeBatch();  // Esegue il batch
            psCaratteristica.close();

            URI uri = uriinfo.getAbsolutePathBuilder().path(String.valueOf(richiestaId)).build();
            return Response.created(uri).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Inserimento della richiesta fallito").build();
        }
    } finally {
        if (conn != null) {
            conn.close();
        }
    }
}

/*
    //Inserimento di una nuova richiesta
    @POST
    @Logged 
    @Consumes(MediaType.APPLICATION_JSON)  
    
    public Response inserisciRichiesta(
            RichiestaCompleta richiestaCompleta,  // Oggetto Richiesta+Caratteristiche ricevuto dal client
            @Context UriInfo uriinfo,  // UriInfo per ottenere informazioni sulla richiesta
            @Context SecurityContext sec,  // Per gestire la sicurezza
            @Context ContainerRequestContext req) throws RESTWebApplicationException, SQLException, ClassNotFoundException, NamingException {
        

                System.out.println("Metodo inserisciRichiesta chiamato");

                
        InitialContext ctx;
        Connection conn = null;
        PreparedStatement psRichiesta = null;
        PreparedStatement psCaratteristica = null;

        try {
                    int utenteId = UserUtils.getLoggedId(sec);

                    // Estrai la richiesta e le caratteristiche dalla RichiestaCompleta
                    RichiestaOrdine richiesta = richiestaCompleta.getRichiesta();
                    List<CaratteristicaRichiesta> caratteristiche = richiestaCompleta.getCaratteristiche();

                    // Debug 1: Stampa l'oggetto RichiestaOrdine per vedere se è popolato correttamente
                    System.out.println("DEBUG: RichiestaOrdine ricevuta: " + richiesta.toString());

                    // Debug 2: Stampa la lista delle caratteristiche per vedere se è popolata correttamente
                    //  System.out.println("DEBUG: Caratteristiche ricevute: " + caratteristiche.toString());
                    

                    // Inizializzazione del contesto JNDI e recupero del DataSource
                    ctx = new InitialContext();
                    DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
                    conn = ds.getConnection();
                    
                    String query = "INSERT INTO richiesta_ordine (note, stato, data, utente, categoria_id) VALUES (?, ?, ?, ?, ?)";
                    
                    psRichiesta = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                    System.out.println("DEBUG: Valori da inserire nella query:");
                    System.out.println("Note: " + richiesta.getNote());
                    System.out.println("Stato: " + richiesta.getStato().toString());
                    System.out.println("Data: " + richiesta.getData());
                    System.out.println("Categoria ID: " + (richiesta.getCategoria() != null ? richiesta.getCategoria().getId() : "NULL"));

                    psRichiesta.setString(1, richiesta.getNote());
                    psRichiesta.setString(2, richiesta.getStato().toString()); 
                    psRichiesta.setDate(3, new java.sql.Date(richiesta.getData().getTime()));  // Conversione da java.util.Date a java.sql.Date
                    psRichiesta.setInt(4, utenteId);  
                    if (richiesta.getCategoria() != null) {
                        psRichiesta.setInt(5, richiesta.getCategoria().getId());
                    } else {
                        psRichiesta.setNull(5, java.sql.Types.INTEGER);
                    }

                    int rowsInserted = psRichiesta.executeUpdate();
                    
                    if (rowsInserted > 0) {

                        System.out.println("DEBUG: Inserimento riuscito della richiestaOrdine");

                    // Recupera l'ID generato per la richiesta appena inserita
                    ResultSet generatedKeys = psRichiesta.getGeneratedKeys();
                    int richiestaId = -1;
                    if (generatedKeys.next()) {
                        richiestaId = generatedKeys.getInt(1);
                    }
                    System.out.println("DEBUG: ID generato per la richiesta: " + richiestaId);

                    // Inserimento delle caratteristiche
                    String queryCaratteristica = "INSERT INTO caratteristica_richiesta (richiesta_id, caratteristica_id, valore) VALUES (?, ?, ?)";
                    psCaratteristica = conn.prepareStatement(queryCaratteristica);

                    for (CaratteristicaRichiesta caratteristica : caratteristiche) {
                        psCaratteristica.setInt(1, richiestaId);
                        if (caratteristica.getCaratteristica() != null) {
                            psCaratteristica.setInt(2, caratteristica.getCaratteristica().getId());
                        }
                        psCaratteristica.setString(3, caratteristica.getValore());

                        System.out.println("DEBUG: Inserimento riuscito della caratteristica: " + caratteristica.getCaratteristica().getId() + " - " + caratteristica.getValore());
                    }
                    
                    int rowsInsertedCaratteristiche = psCaratteristica.executeUpdate();
                    if (rowsInsertedCaratteristiche > 0) {
                        System.out.println("DEBUG: Inserimento caratteristiche riuscito");
                    } else {    
                        System.out.println("DEBUG: Inserimento caratteristiche non riuscito");
                    }
                    
                    
            return Response.status(Response.Status.CREATED).entity("Richiesta inserita con successo").build();

            } else {
                System.out.println("DEBUG: Inserimento non riuscito");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore durante l'inserimento della richiesta").build();
            }

        } catch (NamingException | SQLException e) {
            Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("!!Errore interno del server!!").build();
        } finally {
            if (psRichiesta != null) {
                try {
                    psRichiesta.close();
                } catch (SQLException e) {
                    Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            if (psCaratteristica != null) {
                try {
                    psCaratteristica.close();
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
    
*/

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


        

    }
