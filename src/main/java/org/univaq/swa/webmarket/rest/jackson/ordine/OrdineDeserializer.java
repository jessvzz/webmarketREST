/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.jackson.ordine;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.univaq.swa.webmarket.rest.models.Ordine;
import org.univaq.swa.webmarket.rest.models.PropostaAcquisto;
import org.univaq.swa.webmarket.rest.models.StatoOrdine;

/**
 *
 * @author jessviozzi
 */
public class OrdineDeserializer extends JsonDeserializer<Ordine>{
    @Override
    public Ordine deserialize (JsonParser jp, DeserializationContext ctxt)throws IOException, JsonProcessingException{
        Ordine ordine = new Ordine();
        
        JsonNode node = jp.getCodec().readTree(jp);
        if (node.has("data")) {
        String dataStr = node.get("data").asText();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = sdf.parse(dataStr);  

            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());

            ordine.setData(sqlDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        }
       
        if (node.has("stato")) {
            JsonNode ne = node.get("stato");
            StatoOrdine stato = jp.getCodec().treeToValue(ne, StatoOrdine.class);
            ordine.setStato(stato);
            } 
        
         if (node.has("propostaAcquista")) {
            JsonNode ne = node.get("propostaAcquisto");
            PropostaAcquisto proposta = jp.getCodec().treeToValue(ne, PropostaAcquisto.class);
            ordine.setProposta(proposta);
            } 
        
        return ordine;     
        
    }
    
}
