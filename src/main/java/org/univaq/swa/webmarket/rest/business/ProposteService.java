/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.business;

import java.util.List;
import org.univaq.swa.webmarket.rest.models.PropostaAcquisto;

/**
 *
 * @author jessviozzi
 */
public interface ProposteService {
    PropostaAcquisto getProposta(int id);
    List<PropostaAcquisto> getAll(int userId);
    List<PropostaAcquisto> getAllInAttesa(int userId);

    int inserisciProposta(PropostaAcquisto proposta, int techId);
    int approvaProposta(int id, int ordId);
    int rifiutaProposta(int id, String motivazione, int ordId);
    int modificaProposta(PropostaAcquisto prop, int idProposta, int techId);
    
}
