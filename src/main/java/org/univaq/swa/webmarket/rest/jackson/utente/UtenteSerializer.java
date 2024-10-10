package org.univaq.swa.webmarket.rest.jackson.utente;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.univaq.swa.webmarket.rest.models.Utente;

public class UtenteSerializer extends JsonSerializer<Utente> {

    @Override
    public void serialize(Utente utente, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject(); // {
        
        // jgen.writeNumberField("id", utente.getId());
        jgen.writeStringField("username", utente.getUsername());
        jgen.writeStringField("email", utente.getEmail());

        // jgen.writeStringField("password", utente.getPassword());

        // jgen.writeObjectField("tipologiaUtente", utente.getTipologiaUtente());

        jgen.writeEndObject(); // }
    }
}
