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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.univaq.swa.webmarket.rest.exceptions.RESTWebApplicationException;
import org.univaq.swa.webmarket.rest.models.PropostaAcquisto;
import org.univaq.swa.webmarket.rest.models.StatoProposta;
import org.univaq.swa.webmarket.rest.resources.PropostaRes;
import org.univaq.swa.webmarket.rest.resources.ProposteRes;
import org.univaq.swa.webmarket.rest.resources.RichiesteRes;
import org.univaq.swa.webmarket.rest.resources.UserUtils;

/**
 *
 * @author jessviozzi
 */
public class ProposteServiceImpl implements ProposteService{

    @Override
    public PropostaAcquisto getProposta(int id) {
        PropostaAcquisto proposta = new PropostaAcquisto();
        
        InitialContext ctx;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
            try (Connection conn = ds.getConnection()) {
            
            PreparedStatement ps = conn.prepareStatement("Select * FROM proposta_acquisto WHERE id = ?");
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {                
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
                
                int richiestaId = rs.getInt("richiesta_id");

                if(richiestaId > 0){
                    RichiesteService richiesteService = RichiesteServiceFactory.getRichiesteService();
                    proposta.setRichiestaOrdine(richiesteService.getRichiesta(richiestaId));
                
                 } else {
                        throw new RESTWebApplicationException(Response.Status.NOT_FOUND.getStatusCode(), "Proposta non trovata");
                    }

                }
            }   
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, ex);
            throw new RESTWebApplicationException(ex);
        }
        
        return proposta;
    
    }

    @Override
    public List<PropostaAcquisto> getAllInAttesa(int userId) {
        List<PropostaAcquisto> proposte = new ArrayList<>();
        
        InitialContext ctx;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
            Connection conn = ds.getConnection();
            
            String sql = "SELECT p.* " +
                     "FROM proposta_acquisto p " +
                     "JOIN richiesta_ordine r ON p.richiesta_id = r.id " +
                     "WHERE (r.utente = ? OR r.tecnico = ?) AND p.stato = 'IN_ATTESA'";
        
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            
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
                proposta.setId(rs.getInt("id"));
               
                int richiestaId = rs.getInt("richiesta_id");

                if(richiestaId > 0){
                    RichiesteService richiesteService = RichiesteServiceFactory.getRichiesteService();
                    proposta.setRichiestaOrdine(richiesteService.getRichiesta(richiestaId));
                
                 } else {
                        throw new RESTWebApplicationException(Response.Status.NOT_FOUND.getStatusCode(), "Proposta non trovata");
                    }
                proposte.add(proposta);              
            }
            
        } catch (NamingException ex) {
            Logger.getLogger(ProposteRes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ProposteServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return proposte;
       }

     @Override
    public List<PropostaAcquisto> getAll(int userId) {
        List<PropostaAcquisto> proposte = new ArrayList<>();
        
        InitialContext ctx;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
            Connection conn = ds.getConnection();
            
            String sql = "SELECT p.* " +
                     "FROM proposta_acquisto p " +
                     "JOIN richiesta_ordine r ON p.richiesta_id = r.id " +
                     "WHERE (r.utente = ? OR r.tecnico = ?)";
        
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            
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
                proposta.setId(rs.getInt("id"));
               
                int richiestaId = rs.getInt("richiesta_id");

                if(richiestaId > 0){
                    RichiesteService richiesteService = RichiesteServiceFactory.getRichiesteService();
                    proposta.setRichiestaOrdine(richiesteService.getRichiesta(richiestaId));
                
                 } else {
                        throw new RESTWebApplicationException(Response.Status.NOT_FOUND.getStatusCode(), "Proposta non trovata");
                    }
                proposte.add(proposta);              
            }
            
        } catch (NamingException ex) {
            Logger.getLogger(ProposteRes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ProposteServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return proposte;
       }

    @Override
    public int inserisciProposta(PropostaAcquisto proposta, int techId) {
            InitialContext ctx;
            Connection conn = null;
            PreparedStatement ps = null;
            int rowsInserted = 0;
            int propostaId = 0;
            PreparedStatement ps2 = null;

         try {

             ctx = new InitialContext();
             DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
             conn = ds.getConnection();

             String query = "INSERT INTO proposta_acquisto (produttore, prodotto, codice_prodotto, prezzo, URL, note, stato, richiesta_id, data) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
             ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            if (proposta.getProduttore().equals("")) {
                throw new IllegalArgumentException("Produttore mancante.");
            }
            else{
             ps.setString(1, proposta.getProduttore());
            }
             if (proposta.getProdotto().equals("")) {
                throw new IllegalArgumentException("Prodotto mancante");
            }
            else{
             ps.setString(2, proposta.getProdotto());
             }
               if (proposta.getCodiceProdotto().equals("")) {
                throw new IllegalArgumentException("Codice Prodotto mancante");
            }
            else{
             ps.setString(3, proposta.getCodiceProdotto());
               }
                 if (proposta.getPrezzo() == 0) {
                throw new IllegalArgumentException("Prezzo mancante");
            }
            else{
             ps.setFloat(4, proposta.getPrezzo());
                 }
            if (proposta.getUrl().equals("")) {
                throw new IllegalArgumentException("Url mancante");
            }
            else{
             ps.setString(5, proposta.getUrl());
            }
             ps.setString(6, proposta.getNote());
             ps.setString(7,"IN_ATTESA");

             if (proposta.getRichiestaOrdine() == null) {
                throw new IllegalArgumentException("RichiestaOrdine mancante nella proposta.");
            }
            else{
             ps.setInt(8, proposta.getRichiestaOrdine().getId());
            }
             ps.setDate(9, new java.sql.Date(System.currentTimeMillis()));
             
             String query2 = "SELECT tecnico FROM richiesta_ordine WHERE ID = ?";
             ps2 = conn.prepareStatement(query2);
             ps2.setInt(1, proposta.getRichiestaOrdine().getId());
             
             ResultSet rs = ps2.executeQuery();
            
            if (rs.next()){
                if(rs.getInt("tecnico") == techId) {
                    rowsInserted = ps.executeUpdate();
                }
                else{
                propostaId = -1;
            }
                        }
             
             
             
            if (rowsInserted == 1) {
                // Recupera l'ID generato
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                propostaId = keys.getInt(1);  // Ottieni l'ID della proposta appena inserita
                }
            }

         } catch (SQLException | NamingException e) {
             Logger.getLogger(ProposteRes.class.getName()).log(Level.SEVERE, null, e);
         } finally {
             // Chiusura delle risorse
             if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
             if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
         }    
        return propostaId;

    }

    @Override
    public int approvaProposta(int id, int ordId) {
        InitialContext ctx;
        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;

        int rowsUpdated = 0;

        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
            conn = ds.getConnection();


            String query = "UPDATE proposta_acquisto SET stato = ? WHERE ID = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, StatoProposta.ACCETTATO.toString());
            ps.setInt(2, id);
            
            String query2 = "SELECT r.utente AS utente FROM richiesta_ordine r JOIN proposta_acquisto p ON r.ID = p.richiesta_id WHERE p.ID = ?";
                ps2 = conn.prepareStatement(query2);
                ps2.setInt(1, id);
                ps2.execute();

                ResultSet rs2 = ps2.executeQuery();

               if (rs2.next()){
                   System.out.println("sono dentro al primo if");
                   if(rs2.getInt("utente") == ordId) {
                        rowsUpdated = ps.executeUpdate();
                        System.out.println("sono dentro al secondo if");

                   }
                   else{
                    System.out.println("sono nell'else");

                   rowsUpdated = -1;
               }
            }
            
        } catch (SQLException | NamingException ex) {
            Logger.getLogger(PropostaRes.class.getName()).log(Level.SEVERE, null, ex);
           
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProposteServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        System.out.println("rows updated?? "+ rowsUpdated);
        return rowsUpdated;
    }

    @Override
    public int rifiutaProposta(int id, String motivazione) {
        InitialContext ctx;
        Connection conn = null;
        PreparedStatement ps = null;
        int rowsUpdated = 0;

        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
            conn = ds.getConnection();


            String query = "UPDATE proposta_acquisto SET stato = ?, motivazione = ? WHERE ID = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, StatoProposta.RIFIUTATO.toString());
            ps.setString(2, motivazione);

            ps.setInt(3, id);
            
            rowsUpdated = ps.executeUpdate();
            
        } catch (SQLException | NamingException ex) {
            Logger.getLogger(PropostaRes.class.getName()).log(Level.SEVERE, null, ex);
           
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProposteServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        return rowsUpdated;
    }

    @Override
    public int modificaProposta(PropostaAcquisto prop, int idProposta, int techId) {
             InitialContext ctx;
             Connection conn = null;
             PreparedStatement ps = null;
             PreparedStatement ps2 = null;

             int rowsUpdated = 0;
             ResultSet rs = null;


             try {
                 ctx = new InitialContext();
                 DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
                 conn = ds.getConnection();
                 
                String selectQuery = "SELECT produttore, prodotto, codice_prodotto, prezzo, URL, note FROM proposta_acquisto WHERE id = ?";
                ps = conn.prepareStatement(selectQuery);
                ps.setInt(1, idProposta);
                rs = ps.executeQuery();

                if (rs.next()) {
                    String produttore = rs.getString("produttore");
                    String prodotto = rs.getString("prodotto");
                    String codiceProdotto = rs.getString("codice_prodotto");
                    float prezzo = rs.getFloat("prezzo");
                    String url = rs.getString("URL");
                    String note = rs.getString("note");

                    if(prop.getUrl().equals("")) System.out.println("null");
                    else System.out.println("no null");

                    produttore = (!prop.getProduttore().equals("")) ? prop.getProduttore() : produttore;
                    prodotto = (!prop.getProdotto().equals("")) ? prop.getProdotto() : prodotto;
                    codiceProdotto = (!prop.getCodiceProdotto().equals("")) ? prop.getCodiceProdotto() : codiceProdotto;
                    prezzo = (prop.getPrezzo() != 0) ? prop.getPrezzo() : prezzo;
                    url = (!prop.getUrl().equals("")) ? prop.getUrl() : url;
                    note = (!prop.getNote().equals("")) ? prop.getNote() : note;
                

                 String query = "UPDATE proposta_acquisto SET produttore = ?, prodotto = ?, codice_prodotto = ?, prezzo = ?, URL = ?, note = ? WHERE id = ?";
                 ps = conn.prepareStatement(query);
                 ps.setString(1, produttore);
                 ps.setString(2, prodotto);
                 ps.setString(3, codiceProdotto);
                 ps.setFloat(4, prezzo);
                 ps.setString(5, url);
                 ps.setString(6, note);
                 ps.setInt(7, idProposta);
                 
                 String query2 = "SELECT r.tecnico AS tec FROM richiesta_ordine r JOIN proposta_acquisto p ON r.ID = p.richiesta_id WHERE p.ID = ?";
                ps2 = conn.prepareStatement(query2);
                ps2.setInt(1, idProposta);
                ps2.execute();

                ResultSet rs2 = ps2.executeQuery();

               if (rs2.next()){
                   if(rs2.getInt("tec") == techId) {
                        rowsUpdated = ps.executeUpdate();                   
                   }
                   else{
                   rowsUpdated = -1;
               }
                           }
                 
                 
                }

                 
             } catch (SQLException | NamingException e) {
                 Logger.getLogger(ProposteRes.class.getName()).log(Level.SEVERE, null, e);             } finally {
                 // Chiusura delle risorse
                 if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
                 if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
             }    
             return rowsUpdated;
    }
    
}
