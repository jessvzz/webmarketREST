/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.jackson.proposta;

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
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;
import org.univaq.swa.webmarket.rest.models.StatoOrdine;
import org.univaq.swa.webmarket.rest.models.StatoProposta;

/**
 *
 * @author jessviozzi
 */
public class PropostaAcquistoDeserializer extends JsonDeserializer<PropostaAcquisto>{
    @Override
    public PropostaAcquisto deserialize (JsonParser jp, DeserializationContext ctxt)throws IOException, JsonProcessingException{
        PropostaAcquisto proposta = new PropostaAcquisto();
        
        JsonNode node = jp.getCodec().readTree(jp);
        if (node.has("data")) {
        String dataStr = node.get("data").asText();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = sdf.parse(dataStr);  

            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());

            proposta.setData(sqlDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        }
       
        if (node.has("stato")) {
            JsonNode ne = node.get("stato");
            StatoProposta stato = jp.getCodec().treeToValue(ne, StatoProposta.class);
            proposta.setStatoProposta(stato);
            } 
        
         if (node.has("richiestaOrdine")) {
            JsonNode ne = node.get("richiestaOrdine");
            RichiestaOrdine richiesta = jp.getCodec().treeToValue(ne, RichiestaOrdine.class);
            proposta.setRichiestaOrdine(richiesta);
         } 
         
        if (node.has("codice")) {
            proposta.setCodice(node.get("codice").asText());
        }
        
        if (node.has("codiceProdotto")) {
            proposta.setCodiceProdotto(node.get("codiceProdotto").asText());
        }
        
        if (node.has("motivazione")) {
            proposta.setMotivazione(node.get("motivazione").asText());
        }
        
        if (node.has("note")) {
            proposta.setNote(node.get("note").asText());
        }
        
        if (node.has("prodotto")) {
            proposta.setProdotto(node.get("prodotto").asText());
        }
        
        if (node.has("produttore")) {
            proposta.setProduttore(node.get("produttore").asText());
        }
        
        if (node.has("url")) {
            proposta.setUrl(node.get("url").asText());
        }
        
        if (node.has("prezzo")) {
            proposta.setPrezzo(node.get("prezzo").floatValue());
        }
        
        return proposta;     
        
    }
}
