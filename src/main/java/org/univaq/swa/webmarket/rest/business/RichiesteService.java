/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.business;

import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;

/**
 *
 * @author jessviozzi
 */
public interface RichiesteService {
    RichiestaOrdine getRichiesta(int id);
    List<RichiestaOrdine> getRichiesteInAttesa();
    List<RichiestaOrdine> getRichiesteInCorso(int idUtente);
    List<RichiestaOrdine> getRichiesteNonAssegnate();
    List<RichiestaOrdine> getRichiesteGestiteDa(int idTecnico);
    int prendiInCarico(int idTecnico, RichiestaOrdine richiesta);
    RichiestaOrdine getDettagliRichiesta(int id);
    int deleteRichiesta(int id);
    int inserisciNuovaRichiesta(RichiestaOrdine richiesta) throws SQLException, NamingException;
}
