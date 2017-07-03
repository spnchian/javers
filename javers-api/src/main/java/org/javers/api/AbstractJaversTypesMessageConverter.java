package org.javers.api;

import org.javers.core.Javers;
import org.javers.core.json.JsonConverter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author pawel szymczyk
 */
abstract class AbstractJaversTypesMessageConverter<T extends JaversResponse> extends AbstractHttpMessageConverter<T> {

    private final JsonConverter jsonConverter;

    AbstractJaversTypesMessageConverter(Javers javers) {
        this(javers.getJsonConverter());
    }

    private AbstractJaversTypesMessageConverter(JsonConverter jsonConverter) {
        super(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8);
        this.jsonConverter = jsonConverter;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return JaversResponse.class.isAssignableFrom(clazz);
    }

    @Override
    protected T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return jsonConverter.fromJson(new InputStreamReader(inputMessage.getBody()), clazz);
    }

    @Override
    protected void writeInternal(T javersResponse, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        String snapshotsResponseJson = jsonConverter.toJson(javersResponse);
        outputMessage.getBody().write(snapshotsResponseJson.getBytes());
    }
}
