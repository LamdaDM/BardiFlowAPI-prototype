package io.git.lamdadm.caching;

import io.micronaut.cache.interceptor.CacheKeyGenerator;
import io.micronaut.core.annotation.AnnotationMetadata;

public class RedisCacheKeyGenerator implements CacheKeyGenerator {
    @Override
    public Object generateKey(AnnotationMetadata annotationMetadata, Object... params) {
        return null;
    }
}
