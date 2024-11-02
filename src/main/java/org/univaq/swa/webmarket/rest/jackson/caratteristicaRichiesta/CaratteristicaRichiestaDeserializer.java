/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.jackson.caratteristicaRichiesta;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.univaq.swa.webmarket.rest.models.Caratteristica;
import org.univaq.swa.webmarket.rest.models.CaratteristicaRichiesta;
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;

/**
 *
 * @author jessviozzi
 */
public class CaratteristicaRichiestaDeserializer extends JsonDeserializer<CaratteristicaRichiesta>{
    @Override
    public CaratteristicaRichiesta deserialize (JsonParser jp, DeserializationContext ctxt)throws IOException, JsonProcessingException{
        CaratteristicaRichiesta c = new CaratteristicaRichiesta();
        
        JsonNode node = jp.getCodec().readTree(jp);
        if (node.has("valore")) {
            c.setValore(node.get("valore").asText());
        }
        
       
        if (node.has("caratteristica")) {
            JsonNode ne = node.get("caratteristica");
            Caratteristica caratteristica = jp.getCodec().treeToValue(ne, Caratteristica.class);
            c.setCaratteristica(caratteristica);
            } 
        
         if (node.has("richiestaOrdine")) {
            JsonNode ne = node.get("richiestaOrdine");
            RichiestaOrdine richiesta = jp.getCodec().treeToValue(ne, RichiestaOrdine.class);
            c.setRichiestaOrdine(richiesta);
            } 
        
        return c;     
        
    }
}
