package org.univaq.swa.webmarket.rest.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;
import org.univaq.swa.webmarket.rest.jackson.caratteristica.CaratteristicaDeserializer;
import org.univaq.swa.webmarket.rest.jackson.caratteristica.CaratteristicaSerializer;
import org.univaq.swa.webmarket.rest.jackson.caratteristicaRichiesta.CaratteristicaRichiestaDeserializer;
import org.univaq.swa.webmarket.rest.jackson.caratteristicaRichiesta.CaratteristicaRichiestaSerializer;
import org.univaq.swa.webmarket.rest.jackson.categoria.CategoriaDeserializer;
import org.univaq.swa.webmarket.rest.jackson.categoria.CategoriaSerializer;
import org.univaq.swa.webmarket.rest.jackson.ordine.OrdineDeserializer;
import org.univaq.swa.webmarket.rest.jackson.ordine.OrdineSerializer;
import org.univaq.swa.webmarket.rest.jackson.proposta.PropostaAcquistoDeserializer;
import org.univaq.swa.webmarket.rest.jackson.proposta.PropostaAcquistoSerializer;
import org.univaq.swa.webmarket.rest.jackson.richiesta.RichiestaOrdineDeserializer;
import org.univaq.swa.webmarket.rest.jackson.richiesta.RichiestaOrdineSerializer;
import org.univaq.swa.webmarket.rest.jackson.utente.UtenteDeserializer;
import org.univaq.swa.webmarket.rest.jackson.utente.UtenteSerializer;
import org.univaq.swa.webmarket.rest.models.Caratteristica;
import org.univaq.swa.webmarket.rest.models.CaratteristicaRichiesta;
import org.univaq.swa.webmarket.rest.models.Categoria;
import org.univaq.swa.webmarket.rest.models.Ordine;
import org.univaq.swa.webmarket.rest.models.PropostaAcquisto;
import org.univaq.swa.webmarket.rest.models.RichiestaOrdine;
import org.univaq.swa.webmarket.rest.models.Utente;


@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    public ObjectMapperContextResolver()
    {
        this.mapper = createObjectMapper();
    }

    @Override
    public ObjectMapper getContext(Class<?> type)
    {
        return mapper;
    }

    private ObjectMapper createObjectMapper()
    {
        ObjectMapper mapper = new ObjectMapper();

        //Enable indentation to best display JSON
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        //Module to register (de)serializers
        SimpleModule customSerializers = new SimpleModule("CustomSerializersModule");
        
        //Caratteristica
        customSerializers.addSerializer(Caratteristica.class, new CaratteristicaSerializer());
        customSerializers.addDeserializer(Caratteristica.class, new CaratteristicaDeserializer());

        //CaratteristicaRichiesta
        customSerializers.addSerializer(CaratteristicaRichiesta.class, new CaratteristicaRichiestaSerializer());
        customSerializers.addDeserializer(CaratteristicaRichiesta.class, new CaratteristicaRichiestaDeserializer());
        
        //Categoria
        customSerializers.addSerializer(Categoria.class, new CategoriaSerializer());
        customSerializers.addDeserializer(Categoria.class, new CategoriaDeserializer());
        
        //Ordine
        customSerializers.addSerializer(Ordine.class, new OrdineSerializer());
        customSerializers.addDeserializer(Ordine.class, new OrdineDeserializer());
        
        //Proposta
        customSerializers.addSerializer(PropostaAcquisto.class, new PropostaAcquistoSerializer());
        customSerializers.addDeserializer(PropostaAcquisto.class, new PropostaAcquistoDeserializer());
        
        //Richiesta
        customSerializers.addSerializer(RichiestaOrdine.class, new RichiestaOrdineSerializer());
        customSerializers.addDeserializer(RichiestaOrdine.class, new RichiestaOrdineDeserializer());
        
        //Utente
        customSerializers.addSerializer(Utente.class, new UtenteSerializer());
        customSerializers.addDeserializer(Utente.class, new UtenteDeserializer());
        
        
        mapper.registerModule(customSerializers);
        return mapper;
    }
}
