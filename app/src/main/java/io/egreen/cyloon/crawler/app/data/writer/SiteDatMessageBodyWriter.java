package io.egreen.cyloon.crawler.app.data.writer;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by dewmal on 10/13/15.
 */
public class SiteDatMessageBodyWriter implements MessageBodyWriter {
    @Override
    public boolean isWriteable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return false;
    }

    @Override
    public long getSize(Object o, Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return 0;
    }

    @Override
    public void writeTo(Object o, Class type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {

    }
}
