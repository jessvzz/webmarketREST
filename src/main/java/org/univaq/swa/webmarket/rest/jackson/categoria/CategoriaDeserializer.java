/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.jackson.categoria;

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
public class CategoriaDeserializer extends JsonDeserializer<Categoria> {
    
    @Override
    public Categoria deserialize (JsonParser jp, DeserializationContext ctxt)throws IOException, JsonProcessingException{
        Categoria c = new Categoria();
        
        JsonNode node = jp.getCodec().readTree(jp);

        if (node.has("id")) {
            c.setId(node.get("id").asInt());
        }

        if (node.has("nome")) {
            c.setNome(node.get("nome").asText());
        }
        
        // da capire quale usare
        // if (node.has("padre")) {
        //     c.setPadre(node.get("padre").asInt());
        // }
        /*
        if (node.has("padre")) {
            JsonNode ne = node.get("padre");
            Categoria padre = jp.getCodec().treeToValue(ne, Categoria.class);
            c.setPadre(padre);
            }   
        */
        return c;     
        
    }
        
    
}
