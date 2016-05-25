package tr.com.turktelecom.lighthouse.domain.util;

/**
 * Created by 010235 on 17.05.2016.
 */

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import tr.com.turktelecom.lighthouse.web.rest.dto.search.PredicateObject;
import tr.com.turktelecom.lighthouse.web.rest.dto.search.Search;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CustomSearchDeserializer extends JsonDeserializer<Search> {

    @Override
    public Search deserialize(JsonParser jp, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
        JsonNode jsonNode = jp.getCodec().readTree(jp);
        JsonNode predicateObjectNode = jsonNode.get("predicateObject");
        Map<String, String> predicates = new HashMap<String, String>();
        if(predicateObjectNode == null){
            return new Search(new PredicateObject(new HashMap<String, String>()));
        }
        if (!predicateObjectNode.elements().hasNext()) {
            return new Search(new PredicateObject(new HashMap<String, String>()));
        }

        Iterator<String> fieldNames = predicateObjectNode.fieldNames();
        while (fieldNames.hasNext()) {
            String field = fieldNames.next();
            predicates.put(field, predicateObjectNode.get(field).textValue());
        }

        PredicateObject predicateObject = new PredicateObject();
        predicateObject.setPredicates(predicates);

        Search search = new Search();
        search.setPredicateObject(predicateObject);
        return search;
    }

}
