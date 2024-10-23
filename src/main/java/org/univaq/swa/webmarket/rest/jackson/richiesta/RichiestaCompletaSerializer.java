package org.univaq.swa.webmarket.rest.jackson.richiesta;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.List;
import org.univaq.swa.webmarket.rest.models.CaratteristicaRichiesta;
import org.univaq.swa.webmarket.rest.models.PropostaAcquisto;
import org.univaq.swa.webmarket.rest.models.RichiestaCompleta;

public class RichiestaCompletaSerializer extends JsonSerializer<RichiestaCompleta> {
    @Override
    public void serialize(RichiestaCompleta richiestaCompleta, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        
        if (richiestaCompleta.getRichiesta() != null) {
            jgen.writeObjectField("richiestaOrdine", richiestaCompleta.getRichiesta());
        }
        
        if (richiestaCompleta.getCaratteristiche() != null) {
            jgen.writeFieldName("caratteristiche");
            jgen.writeStartArray();
            for (CaratteristicaRichiesta caratteristica : richiestaCompleta.getCaratteristiche()) {
                jgen.writeObject(caratteristica);
            }
            jgen.writeEndArray();
        }
        
        
        jgen.writeEndObject(); 
    }
}
