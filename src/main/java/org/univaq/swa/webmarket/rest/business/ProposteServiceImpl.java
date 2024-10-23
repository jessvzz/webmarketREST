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
            
            } else {
                    throw new RESTWebApplicationException(Response.Status.NOT_FOUND.getStatusCode(), "Proposta non trovata");
                }

        
            }   
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(RichiesteRes.class.getName()).log(Level.SEVERE, null, ex);
            throw new RESTWebApplicationException(ex);
        }
        
        return proposta;
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
                proposta.setId(rs.getInt("richiesta_id"));
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
    public int inserisciProposta(PropostaAcquisto proposta) {
            InitialContext ctx;
            Connection conn = null;
            PreparedStatement ps = null;
            int rowsInserted = 0;

         try {

             ctx = new InitialContext();
             DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
             conn = ds.getConnection();

             String query = "INSERT INTO proposta_acquisto (produttore, prodotto, codice_prodotto, prezzo, URL, note, stato, richiesta_id, data) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
             ps = conn.prepareStatement(query);

             
             ps.setString(1, proposta.getProduttore());
             ps.setString(2, proposta.getProdotto());
             ps.setString(3, proposta.getCodiceProdotto());
             ps.setFloat(4, proposta.getPrezzo());
             ps.setString(5, proposta.getUrl());
             ps.setString(6, proposta.getNote());
             ps.setString(7,"IN_ATTESA");
             ps.setInt(8, proposta.getRichiestaOrdine().getId());
             ps.setDate(9, new java.sql.Date(System.currentTimeMillis()));
             rowsInserted = ps.executeUpdate();

         } catch (SQLException | NamingException e) {
             Logger.getLogger(ProposteRes.class.getName()).log(Level.SEVERE, null, e);
         } finally {
             // Chiusura delle risorse
             if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
             if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
         }    
        return rowsInserted;

    }

    @Override
    public int approvaProposta(int id) {
        InitialContext ctx;
        Connection conn = null;
        PreparedStatement ps = null;
        int rowsUpdated = 0;

        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
            conn = ds.getConnection();


            String query = "UPDATE proposta_acquisto SET stato = ? WHERE ID = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, StatoProposta.ACCETTATO.toString());
            ps.setInt(2, id);
            
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
    public int modificaProposta(PropostaAcquisto prop, int idProposta) {
             InitialContext ctx;
             Connection conn = null;
             PreparedStatement ps = null;
             int rowsUpdated = 0;

             try {
                 ctx = new InitialContext();
                 DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/webdb2");
                 conn = ds.getConnection();

                 // Query SQL per aggiornare una proposta esistente
                 String query = "UPDATE proposta_acquisto SET produttore = ?, prodotto = ?, codice_prodotto = ?, prezzo = ?, URL = ?, note = ? WHERE id = ?";
                 ps = conn.prepareStatement(query);
                 ps.setString(1, prop.getProduttore());
                 ps.setString(2, prop.getProdotto());
                 ps.setString(3, prop.getCodiceProdotto());
                 ps.setFloat(4, prop.getPrezzo());
                 ps.setString(5, prop.getUrl());
                 ps.setString(6, prop.getNote());
                 ps.setInt(7, idProposta);
                 
                 rowsUpdated = ps.executeUpdate();

                 
             } catch (SQLException | NamingException e) {
                 Logger.getLogger(ProposteRes.class.getName()).log(Level.SEVERE, null, e);             } finally {
                 // Chiusura delle risorse
                 if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
                 if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
             }    
             return rowsUpdated;
    }
    
}
