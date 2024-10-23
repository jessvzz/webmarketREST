package org.univaq.swa.webmarket.rest.jackson.richiesta;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import org.univaq.swa.webmarket.rest.models.PropostaAcquisto;
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;

public class RichiestaOrdineSerializer extends JsonSerializer<RichiestaOrdine> {
    @Override
    public void serialize(RichiestaOrdine item, JsonGenerator jgen, SerializerProvider provider) 
            throws IOException, JsonProcessingException {
        jgen.writeStartObject(); // {
        jgen.writeNumberField("id", item.getId());
        jgen.writeStringField("note", item.getNote());
        jgen.writeObjectField("stato", item.getStato());
        jgen.writeStringField("codiceRichiesta", item.getCodiceRichiesta());
        
        java.sql.Date sqlDate = (Date) item.getData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
        String formattedDate = sdf.format(sqlDate);
        jgen.writeStringField("data", formattedDate); 
        
        // if (item.getUtente() != null) {
        jgen.writeObjectField("utente", item.getUtente());
        // }
        // if (item.getTecnico() != null) {
            jgen.writeObjectField("tecnico", item.getTecnico()); 
        // }
        // if (item.getUtente() != null) {
        jgen.writeObjectField("categoria", item.getCategoria());
        // }
        
        List<PropostaAcquisto> proposte = item.getProposte();
        if (proposte != null && !proposte.isEmpty()) {
            jgen.writeArrayFieldStart("proposte");
            for (PropostaAcquisto proposta : proposte) {
                jgen.writeObject(proposta);
            }
            jgen.writeEndArray();
        } else {
            jgen.writeNullField("proposte");
        }
        jgen.writeEndObject(); // }
    }
}
