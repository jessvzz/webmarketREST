/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.jackson.caratteristica;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.univaq.swa.webmarket.rest.models.Caratteristica;
import org.univaq.swa.webmarket.rest.models.Categoria;

/**
 *
 * @author jessviozzi
 */
public class CaratteristicaDeserializer extends JsonDeserializer<Caratteristica>{
    @Override
    public Caratteristica deserialize (JsonParser jp, DeserializationContext ctxt)throws IOException, JsonProcessingException{
        Caratteristica c = new Caratteristica();
        
        JsonNode node = jp.getCodec().readTree(jp);
        if (node.has("nome")) {
            c.setNome(node.get("nome").asText());
        }
        
       
        if (node.has("categoria")) {
            JsonNode ne = node.get("categoria");
            Categoria categoria = jp.getCodec().treeToValue(ne, Categoria.class);
            c.setCategoria(categoria);
            }   
        
        return c;     
        
    }
}
