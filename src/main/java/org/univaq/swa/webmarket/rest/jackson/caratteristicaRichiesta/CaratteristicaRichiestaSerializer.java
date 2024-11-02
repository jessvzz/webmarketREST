/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.jackson.caratteristicaRichiesta;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.univaq.swa.webmarket.rest.models.CaratteristicaRichiesta;

/**
 *
 * @author jessviozzi
 */
public class CaratteristicaRichiestaSerializer extends JsonSerializer<CaratteristicaRichiesta> {
    @Override
    public void serialize(CaratteristicaRichiesta item, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject(); 
        jgen.writeNumberField("id", item.getId());
        jgen.writeObjectField("richiestaOrdine", item.getRichiestaOrdine());
        jgen.writeObjectField("caratteristica", item.getCaratteristica());
        jgen.writeStringField("valore", item.getValore());

        jgen.writeEndObject(); 
    }
    
}
