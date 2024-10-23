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
import org.univaq.swa.webmarket.rest.models.PropostaAcquisto;
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;
import org.univaq.swa.webmarket.rest.models.StatoProposta;
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
    public RichiestaOrdine getDettagliRichiesta(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RichiestaOrdine richiesta = null;
        List<PropostaAcquisto> proposte = new ArrayList<>();

        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
            conn = ds.getConnection();

            String query = "SELECT \n" +
            "    ro.ID AS richiesta_id,\n" +
            "    ro.note AS richiesta_note,\n" +
            "    ro.stato AS richiesta_stato,\n" +
            "    ro.data AS richiesta_data,\n" +
            "    ro.codice_richiesta,\n" +
            "    ro.utente AS richiesta_utente,\n" +
            "    ro.tecnico AS richiesta_tecnico,\n" +
            "    ro.categoria_id AS richiesta_categoria_id,\n" +
            "\n" +
            "    pa.ID AS proposta_id,\n" +
            "    pa.produttore,\n" +
            "    pa.prodotto,\n" +
            "    pa.codice,\n" +
            "    pa.codice_prodotto,\n" +
            "    pa.prezzo,\n" +
            "    pa.URL,\n" +
            "    pa.note AS proposta_note,\n" +
            "    pa.stato AS proposta_stato,\n" +
            "    pa.data AS proposta_data,\n" +
            "    pa.motivazione\n" +
            "FROM richiesta_ordine ro\n" +
            "LEFT JOIN proposta_acquisto pa ON ro.ID = pa.richiesta_id\n" +
            "WHERE ro.ID = ?;";

            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                if (richiesta == null) {
                    richiesta = new RichiestaOrdine();
                    richiesta.setId(rs.getInt("richiesta_id"));
                    richiesta.setNote(rs.getString("richiesta_note"));
                    richiesta.setStato(StatoRichiesta.valueOf(rs.getString("richiesta_stato")));
                    richiesta.setData(rs.getDate("richiesta_data"));
                    richiesta.setCodiceRichiesta(rs.getString("codice_richiesta"));

                         int utenteId = rs.getInt("richiesta_utente");
                         int categoriaId = rs.getInt("richiesta_categoria_id");
                         int tecnicoId = rs.getInt("richiesta_tecnico");
        
                         if (utenteId > 0) {
                             Utente utente = recuperaUtente(conn, utenteId);
                             richiesta.setUtente(utente);
                         }
        
                         if (categoriaId > 0) {
                             Categoria categoria = recuperaCategoria(conn, categoriaId);
                             richiesta.setCategoria(categoria);
                         }
        
                         if (tecnicoId > 0) {
                            Utente tecnico = recuperaTecnico(conn, tecnicoId);
                            richiesta.setTecnico(tecnico);
                        }
                }

                if (rs.getInt("proposta_id") != 0) {
                    PropostaAcquisto proposta = new PropostaAcquisto();
                    proposta.setId(rs.getInt("proposta_id"));
                    proposta.setProduttore(rs.getString("produttore"));
                    proposta.setProdotto(rs.getString("prodotto"));
                    proposta.setCodice(rs.getString("codice"));
                    proposta.setPrezzo(rs.getFloat("prezzo"));
                    proposta.setUrl(rs.getString("URL"));
                    proposta.setStatoProposta(StatoProposta.valueOf(rs.getString("proposta_stato")));
                    proposta.setData(rs.getDate("proposta_data"));
                    proposta.setNote(rs.getString("proposta_note"));
                    proposta.setMotivazione(rs.getString("motivazione"));

                    proposte.add(proposta); 
                }
            }

            if (richiesta != null) {
                richiesta.setProposte(proposte); 
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return richiesta;
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
