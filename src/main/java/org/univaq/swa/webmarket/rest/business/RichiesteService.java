/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.business;

import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import org.univaq.swa.webmarket.rest.models.Caratteristica;
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;

/**
 *
 * @author jessviozzi
 */
public interface RichiesteService {
    List<RichiestaOrdine> getAllRichieste();
    RichiestaOrdine getRichiesta(int id);
    List<RichiestaOrdine> getRichiesteInAttesa();
    List<RichiestaOrdine> getRichiesteInCorso(int idUtente);
    List<RichiestaOrdine> getRichiesteNonAssegnate();
    List<RichiestaOrdine> getRichiesteGestiteDa(int idTecnico);
    int prendiInCarico(int idTecnico, RichiestaOrdine richiesta);
    RichiestaOrdine getDettagliRichiesta(int id);
    int deleteRichiesta(int id);
    int inserisciNuovaRichiesta(int userId, String note, Date data, String stato, int categoriaId, List<Integer> idcaratteristica, List<String> valore) throws SQLException, NamingException;
}
