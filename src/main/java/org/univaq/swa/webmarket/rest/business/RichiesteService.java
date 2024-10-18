/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.business;

import jakarta.json.JsonObjectBuilder;
import java.util.List;
import java.util.Map;
import org.univaq.swa.webmarket.rest.models.Caratteristica;
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;

/**
 *
 * @author jessviozzi
 */
public interface RichiesteService {
    List<RichiestaOrdine> getAllRichieste();
    RichiestaOrdine getRichiesta(int id);
    //String addRichiesta(RichiestaOrdine richiesta);
    List<RichiestaOrdine> getRichiesteInAttesa();
    List<RichiestaOrdine> getRichiesteInCorso(int idUtente);
    List<RichiestaOrdine> getRichiesteNonAssegnate();
    List<RichiestaOrdine> getRichiesteGestiteDa(int idTecnico);
    int prendiInCarico(int idTecnico, RichiestaOrdine richiesta);
    JsonObjectBuilder getDettagliRichiesta(int id);
    int deleteRichiesta(int id);
    int inserisciNuovaRichiesta(RichiestaOrdine nuovaRichiesta, Map<Caratteristica, String> caratteristiche);
    int inserisciProva(RichiestaOrdine nuovaRichiesta);
}
