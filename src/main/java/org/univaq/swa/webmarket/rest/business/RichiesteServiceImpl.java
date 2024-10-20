/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.business;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.univaq.swa.webmarket.rest.exceptions.RESTWebApplicationException;
import org.univaq.swa.webmarket.rest.models.Caratteristica;
import org.univaq.swa.webmarket.rest.models.Categoria;
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;
import org.univaq.swa.webmarket.rest.models.StatoRichiesta;
import org.univaq.swa.webmarket.rest.models.TipologiaUtente;
import org.univaq.swa.webmarket.rest.models.Utente;
import org.univaq.swa.webmarket.rest.resources.RichiestaRes;
import org.univaq.swa.webmarket.rest.resources.RichiesteRes;
import org.univaq.swa.webmarket.rest.resources.UserUtils;

/**
 *
 * @author jessviozzi
 */
public class RichiesteServiceImpl implements RichiesteService{

    @Override
    public List<RichiestaOrdine> getAllRichieste() {
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
        }    
            return richiesteInAttesa;

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

    @Override
    public RichiestaOrdine getRichiesta(int id) {
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

        return richiesta;  
    }

    @Override
    public List<RichiestaOrdine> getRichiesteInAttesa() {
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
                    richiesta.setId(rs.getInt("ID"));
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
        }
            return richiesteInAttesa;
    }

    @Override
    public List<RichiestaOrdine> getRichiesteInCorso(int idUtente) {
        List<RichiestaOrdine> richieste = new ArrayList<>();
        InitialContext ctx;

        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");

            try (Connection conn = ds.getConnection()) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM richiesta_ordine WHERE stato != ? AND utente = ?");
                ps.setString(1, "RISOLTA");
                ps.setInt(2, idUtente);
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

                    if (idUtente > 0) {
                        Utente utente = recuperaUtente(conn, idUtente);
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
        }

        return richieste;
    }

    
    @Override
    public List<RichiestaOrdine> getRichiesteNonAssegnate() {
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
                 
             }

            return richiesteNonAssegnate;
            
         }

    @Override
    public List<RichiestaOrdine> getRichiesteGestiteDa(int idTecnico) {
            List<RichiestaOrdine> richiesteGestite = new ArrayList<>();
            InitialContext ctx;
        
             try {
                 ctx = new InitialContext();
                 DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
                 
                 try (Connection conn = ds.getConnection()) {
                     PreparedStatement ps = conn.prepareStatement("SELECT * FROM richiesta_ordine WHERE tecnico = ?");
                     ps.setInt(1, idTecnico);
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
                         int tecnicoId = rs.getInt("tecnico");
        
                         if (utenteId > 0) {
                             Utente utente = recuperaUtente(conn, utenteId);
                             richiesta.setUtente(utente);
                         }
        
                         if (categoriaId > 0) {
                             Categoria categoria = recuperaCategoria(conn, categoriaId);
                             richiesta.setCategoria(categoria);
                         }
        
                         if (tecnicoId > 0) {
                            Utente tecnico = recuperaTecnico(conn, tecnicoId);  // Recupera i dettagli del tecnico
                            richiesta.setTecnico(tecnico);
                        }

                        
                         richiesteGestite.add(richiesta);
                     }
                 }
        
             } catch (NamingException | SQLException ex) {
                 Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, ex);
             }
        
             return richiesteGestite;
        }

    @Override
    public int prendiInCarico(int idTecnico, RichiestaOrdine richiesta) {
        InitialContext ctx;
        Connection conn = null;
        PreparedStatement ps = null;
        int rowsUpdated = 0;

        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
            conn = ds.getConnection();
            

            String query = "UPDATE richiesta_ordine SET stato = ?, tecnico = ? WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, StatoRichiesta.PRESA_IN_CARICO.toString());
            ps.setInt(2, idTecnico);
            ps.setInt(3, richiesta.getId());
            
            rowsUpdated = ps.executeUpdate();
            
        } catch (SQLException | NamingException ex) {
            Logger.getLogger(RichiestaRes.class.getName()).log(Level.SEVERE, null, ex);
            
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(RichiesteServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
        return rowsUpdated;
    }

    @Override
    public JsonObjectBuilder getDettagliRichiesta(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            InitialContext ctx = new InitialContext();
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
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                JsonObjectBuilder richiestaDetails = Json.createObjectBuilder()
                    .add("richiesta_id", rs.getInt("richiesta_id"))
                    .add("codice_richiesta", rs.getString("codice_richiesta"))
                    .add("data_richiesta", rs.getDate("data_richiesta").toString())
                    .add("note_richiesta", rs.getString("note_richiesta"))
                    .add("stato_richiesta", rs.getString("stato_richiesta"))
                    .add("ordinante", rs.getString("ordinante"))
                    .add("categoria", rs.getString("categoria"));

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

                return richiestaDetails;
            }


        } catch (NamingException ex) {
            Logger.getLogger(RichiesteServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RichiesteServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(RichiesteServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }return null;
}

    @Override
    public int deleteRichiesta(int id) {
        InitialContext ctx;
         Connection conn = null;
         PreparedStatement ps = null;
         int rowsDeleted = 0;

         try {
             ctx = new InitialContext();
             DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
             conn = ds.getConnection();
             
             
             // Query SQL per eliminare la richiesta
             String query = "DELETE FROM richiesta_ordine WHERE id = ?";
             ps = conn.prepareStatement(query);
             ps.setInt(1, id);

             rowsDeleted = ps.executeUpdate();

             return rowsDeleted;

         } catch (SQLException e) {
             Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, e);
         } catch (NamingException e) {
             Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, e);
         } finally {
             // Chiusura delle risorse
             if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
             if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
         }
        return rowsDeleted;

    }
    
    @Override
    public int inserisciNuovaRichiesta(int userId, String note, Date data, String stato, int categoriaId, List<Integer> idcaratteristica, List<String> valore) throws SQLException, NamingException{
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
        Connection conn = ds.getConnection();

        try {

            String query = "INSERT INTO richiesta_ordine (note, data, stato, utente, categoria_id) VALUES(?, ?, ?, ?,?)";
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, note);
            ps.setDate(2, new java.sql.Date(data.getTime()));
            ps.setString(3, stato);
            ps.setInt(4,userId);
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

            
            return richiestaId;
        } else {
            return 0;
        }
    } finally {
        if (conn != null) {
            conn.close();
        }
    }
    }

    

    
}
