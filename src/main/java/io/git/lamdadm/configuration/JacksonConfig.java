package io.git.lamdadm.configuration;

import com.fasterxml.jackson.databind.SerializationFeature;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Primary;
import io.micronaut.jackson.JacksonConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@ConfigurationProperties("jackson")
@Primary
public class JacksonConfig extends JacksonConfiguration {
    @Override
    public TimeZone getTimeZone() { return TimeZone.getTimeZone("UTC"); }

    @Override
    public Map<SerializationFeature, Boolean> getSerializationSettings() {
        var opts = new HashMap<SerializationFeature, Boolean>();

        opts.put(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        opts.put(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        return opts;
    }
}
