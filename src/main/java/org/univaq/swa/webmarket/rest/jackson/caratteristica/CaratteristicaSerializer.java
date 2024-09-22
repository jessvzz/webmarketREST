/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.jackson.caratteristica;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.univaq.swa.webmarket.rest.models.Caratteristica;

/**
 *
 * @author jessviozzi
 */
public class CaratteristicaSerializer extends JsonSerializer<Caratteristica> {
    @Override
    public void serialize(Caratteristica item, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject(); // {
        jgen.writeNumberField("id", item.getId());
        jgen.writeStringField("nome", item.getNome());
        jgen.writeObjectField("categoria", item.getCategoria());
        jgen.writeEndObject(); // }
    }
}
