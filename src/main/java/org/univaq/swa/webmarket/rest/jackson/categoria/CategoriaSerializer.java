/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.jackson.categoria;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.univaq.swa.webmarket.rest.models.Categoria;

/**
 *
 * @author jessviozzi
 */
public class CategoriaSerializer extends JsonSerializer<Categoria> {
    @Override
    public void serialize(Categoria item, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject(); 
        jgen.writeNumberField("id", item.getId());
        jgen.writeStringField("nome", item.getNome());
        jgen.writeEndObject(); 
    }
}
