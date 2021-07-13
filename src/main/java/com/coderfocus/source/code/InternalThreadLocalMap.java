package com.coderfocus.source.code;

import java.util.IdentityHashMap;
import java.util.Map;

public class InternalThreadLocalMap {

    private Map<Class<?>, TypeParameterMatcher> typeParameterMatcherGetCache;
    private Map<Class<?>, Map<String, TypeParameterMatcher>> typeParameterMatcherFindCache;

    private static final ThreadLocal<InternalThreadLocalMap> slowThreadLocalMap =
            new ThreadLocal<InternalThreadLocalMap>();

    private static InternalThreadLocalMap slowGet() {
        InternalThreadLocalMap ret = slowThreadLocalMap.get();
        if (ret == null) {
            ret = new InternalThreadLocalMap();
            slowThreadLocalMap.set(ret);
        }
        return ret;
    }

    public static InternalThreadLocalMap get() {
        return slowGet();
    }

    public Map<Class<?>, TypeParameterMatcher> typeParameterMatcherGetCache() {
        Map<Class<?>, TypeParameterMatcher> cache = typeParameterMatcherGetCache;
        if (cache == null) {
            typeParameterMatcherGetCache = cache = new IdentityHashMap<Class<?>, TypeParameterMatcher>();
        }
        return cache;
    }


    public Map<Class<?>, Map<String, TypeParameterMatcher>> typeParameterMatcherFindCache() {
        Map<Class<?>, Map<String, TypeParameterMatcher>> cache = typeParameterMatcherFindCache;
        if (cache == null) {
            typeParameterMatcherFindCache = cache = new IdentityHashMap<Class<?>, Map<String, TypeParameterMatcher>>();
        }
        return cache;
    }
}
