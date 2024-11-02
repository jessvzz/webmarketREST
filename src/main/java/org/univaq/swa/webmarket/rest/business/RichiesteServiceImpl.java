/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.business;

import jakarta.ws.rs.core.Response;
import java.sql.Connection;
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
import javax.sql.DataSource;
import org.univaq.swa.webmarket.rest.exceptions.RESTWebApplicationException;
import org.univaq.swa.webmarket.rest.models.Categoria;
import org.univaq.swa.webmarket.rest.models.PropostaAcquisto;
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;
import org.univaq.swa.webmarket.rest.models.StatoProposta;
import org.univaq.swa.webmarket.rest.models.StatoRichiesta;
import org.univaq.swa.webmarket.rest.models.TipologiaUtente;
import org.univaq.swa.webmarket.rest.models.Utente;
import org.univaq.swa.webmarket.rest.resources.RichiestaRes;
import org.univaq.swa.webmarket.rest.resources.RichiesteRes;

/**
 *
 * @author jessviozzi
 */
public class RichiesteServiceImpl implements RichiesteService{

    @Override
    public List<RichiestaOrdine> getAllRichieste(int userId) {
        List<RichiestaOrdine> richieste = new ArrayList<>();
        InitialContext ctx;

        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");

            try (Connection conn = ds.getConnection()) {

                String sql = "SELECT r.* " +
                "FROM richiesta_ordine r " +
                "WHERE (r.utente = ? OR r.tecnico = ?)";
   
            PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, userId);
                ps.setInt(2, userId);
              
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
                    

                    richieste.add(richiesta);
                }
            }

        } catch (NamingException | SQLException ex) {
            Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, ex);
        }    
            return richieste;
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
        return tecnico;
    }
    return null;    

}

    @Override
    public RichiestaOrdine getRichiesta(int id) {
        RichiestaOrdine richiesta = new RichiestaOrdine();
        InitialContext ctx;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        Map<String, String> caratteristiche = new HashMap<>();

        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");

            try (Connection conn = ds.getConnection()) {
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
                    
                    String query2 = "SELECT c.nome AS caratteristica, cr.valore AS valore FROM caratteristica_richiesta cr RIGHT JOIN caratteristica c ON cr.caratteristica_id=c.ID WHERE cr.richiesta_id = ? ";
                    ps2 = conn.prepareStatement(query2);
                    ps2.setInt(1, id);
                    rs2 = ps2.executeQuery();

                    while (rs2.next()) {

                            String caratteristica = rs2.getString("caratteristica");
                            String valore = rs2.getString("valore");

                            if (caratteristica != null && valore != null) {
                                caratteristiche.put(caratteristica, valore);
                            }

                        }

                    
                    richiesta.setCaratteristiche(caratteristiche);
                   

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
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        RichiestaOrdine richiesta = null;
        List<PropostaAcquisto> proposte = new ArrayList<>();
        Map<String, String> caratteristiche = new HashMap<>();


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
            // "    ,pa.richiesta_id AS proposta_richiesta_id\n" +
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
            String query2 = "SELECT c.nome AS caratteristica, cr.valore AS valore FROM caratteristica_richiesta cr RIGHT JOIN caratteristica c ON cr.caratteristica_id=c.ID WHERE cr.richiesta_id = ? ";
            ps2 = conn.prepareStatement(query2);
            ps2.setInt(1, id);
            rs2 = ps2.executeQuery();
            
            while (rs2.next()) {
                
                    String caratteristica = rs2.getString("caratteristica");
                    String valore = rs2.getString("valore");

                    if (caratteristica != null && valore != null) {
                        caratteristiche.put(caratteristica, valore);
                    }
                
                }
            
            if (richiesta != null) {
                richiesta.setProposte(proposte);
                richiesta.setCaratteristiche(caratteristiche);
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
             if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
             if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
         }
        return rowsDeleted;

    }
    
    
    @Override
    public int inserisciNuovaRichiesta(RichiestaOrdine richiesta) throws SQLException, NamingException{
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
        Connection conn = ds.getConnection();

        try {

            String query = "INSERT INTO richiesta_ordine (note, data, stato, utente, categoria_id) VALUES(?, ?, ?, ?,?)";
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, richiesta.getNote());
            ps.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            ps.setString(3, "IN_ATTESA");
            ps.setInt(4,richiesta.getUtente().getId());
            ps.setInt(5, richiesta.getCategoria().getId());


            int rowsInserted = ps.executeUpdate();
            if (rowsInserted == 1) {
            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            int richiestaId = keys.getInt(1);
            
            String queryCaratteristicaId = "SELECT ID FROM caratteristica WHERE nome = ?";
            PreparedStatement psCaratteristicaId = conn.prepareStatement(queryCaratteristicaId);

            String queryCaratteristica = "INSERT INTO caratteristica_richiesta (richiesta_id, caratteristica_id, valore) VALUES(?, ?, ?)";
            PreparedStatement psCaratteristica = conn.prepareStatement(queryCaratteristica);
            
            Map<String, String> caratteristiche = richiesta.getCaratteristiche();
            if (caratteristiche != null) {

            for (Map.Entry<String, String> entry : richiesta.getCaratteristiche().entrySet()) 
            {
                String nomeCaratteristica = entry.getKey();
                String valoreCaratteristica = entry.getValue();
                
                psCaratteristicaId.setString(1, nomeCaratteristica);
                ResultSet rsCaratteristica = psCaratteristicaId.executeQuery();

                if (rsCaratteristica.next()) {
                    int caratteristicaId = rsCaratteristica.getInt("ID");

                psCaratteristica.setInt(1, richiestaId);
                psCaratteristica.setInt(2, caratteristicaId);
                psCaratteristica.setString(3,valoreCaratteristica);
                psCaratteristica.addBatch();  
            }
                 rsCaratteristica.close();
            }
            }

            psCaratteristica.executeBatch();
            psCaratteristicaId.close();
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
