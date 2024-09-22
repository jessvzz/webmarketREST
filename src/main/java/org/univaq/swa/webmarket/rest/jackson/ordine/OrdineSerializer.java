/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.jackson.ordine;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.univaq.swa.webmarket.rest.models.CaratteristicaRichiesta;
import org.univaq.swa.webmarket.rest.models.Ordine;

/**
 *
 * @author jessviozzi
 */
public class OrdineSerializer extends JsonSerializer<Ordine> {
    @Override
    public void serialize(Ordine item, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject(); // {
        jgen.writeNumberField("id", item.getId());
        jgen.writeObjectField("propostaAcquisto", item.getProposta());
        jgen.writeObjectField("statoOrdine", item.getStato());
        java.sql.Date sqlDate = item.getData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //in caso questo poi lo cambiamo
        String formattedDate = sdf.format(sqlDate);
        jgen.writeStringField("data", formattedDate);
        jgen.writeEndObject(); // }
    }
    
}
