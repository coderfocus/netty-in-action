package com.coderfocus.source.code;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public abstract class TypeParameterMatcher {
    private static final TypeParameterMatcher NOOP = new TypeParameterMatcher() {
        @Override
        public boolean match(Object msg) {
            return true;
        }
    };

    public static TypeParameterMatcher get(Class<?> parameterType) {
        // 先从ThreadLocal 缓存中查询，如果没有，则构造 TypeParameterMatcher 并更新到缓存  Map<Class<?>, TypeParameterMatcher>  中
        Map<Class<?>, TypeParameterMatcher> getCache = InternalThreadLocalMap.get().typeParameterMatcherGetCache();
        TypeParameterMatcher matcher = (TypeParameterMatcher)getCache.get(parameterType);
        if (matcher == null) {
            if (parameterType == Object.class) { //如果是 Object 类，则一直返回 true
                matcher = NOOP;
            } else { //如果不是 Object 类，则Class<?>.isInstance()
                matcher = new TypeParameterMatcher.ReflectiveMatcher(parameterType);
            }

            getCache.put(parameterType, matcher);
        }

        return (TypeParameterMatcher)matcher;
    }

    public static TypeParameterMatcher find(Object object, Class<?> parametrizedSuperclass, String typeParamName) {
        // 先从ThreadLocal 缓存中查询，如果没有，则构造Map<String, TypeParameterMatcher> 更新到缓存中
        Map<Class<?>, Map<String, TypeParameterMatcher>> findCache = InternalThreadLocalMap.get().typeParameterMatcherFindCache();
        Class<?> thisClass = object.getClass();
        Map<String, TypeParameterMatcher> map = (Map)findCache.get(thisClass);
        if (map == null) {
            map = new HashMap();
            findCache.put(thisClass, map);
        }

        // 从 Map<String, TypeParameterMatcher> 中匹配类型参数匹配器 TypeParameterMatcher
        // 如果没有匹配到，则进行构建
        // 1. find0 方法构建 Class<?>
        // 2. get 方法匹配 TypeParameterMatcher
        TypeParameterMatcher matcher = (TypeParameterMatcher)((Map)map).get(typeParamName);
        if (matcher == null) {
            matcher = get(find0(object, parametrizedSuperclass, typeParamName));
            ((Map)map).put(typeParamName, matcher);
        }

        return matcher;
    }


    /**
    * @Description:
     *  object 泛型类实例
     *  parametrizedSuperclass 泛型包装类
     *  typeParamName 泛型类型参数名称
    * @param: [object, parametrizedSuperclass, typeParamName]
    * @return: java.lang.Class<?>
    */
    private static Class<?> find0(Object object, Class<?> parametrizedSuperclass, String typeParamName) {
        // 泛型类实例 Class
        Class<?> thisClass = object.getClass();
        Class currentClass = thisClass;

        do {
            //-----------------------------------
            // 判断泛型类实例是不是泛型包装类parametrizedSuperclass 的子类
            while(currentClass.getSuperclass() != parametrizedSuperclass) {
                currentClass = currentClass.getSuperclass();
                if (currentClass == null) {
                    return fail(thisClass, typeParamName);
                }
            }
            //-----------------------------------

            //-----------------------------------
            // 判断泛型类实例typeParamName参数 是不是泛型包装类parametrizedSuperclass的参数相同
            int typeParamIndex = -1;
            TypeVariable<?>[] typeParams = currentClass.getSuperclass().getTypeParameters();

            for(int i = 0; i < typeParams.length; ++i) {
                if (typeParamName.equals(typeParams[i].getName())) {
                    typeParamIndex = i;
                    break;
                }
            }

            if (typeParamIndex < 0) {
                throw new IllegalStateException("unknown type parameter '" + typeParamName + "': " + parametrizedSuperclass);
            }
            //-----------------------------------

            //-----------------------------------
            // 获取泛型类实例实际的类型
            Type genericSuperType = currentClass.getGenericSuperclass();
            if (!(genericSuperType instanceof ParameterizedType)) {
                return Object.class;
            }

            // 获取泛型类实例实际的参数类型
            Type[] actualTypeParams = ((ParameterizedType)genericSuperType).getActualTypeArguments();
            Type actualTypeParam = actualTypeParams[typeParamIndex];
            if (actualTypeParam instanceof ParameterizedType) {
                actualTypeParam = ((ParameterizedType)actualTypeParam).getRawType();
            }

            if (actualTypeParam instanceof Class) {
                return (Class)actualTypeParam;
            }

            if (actualTypeParam instanceof GenericArrayType) {
                Type componentType = ((GenericArrayType)actualTypeParam).getGenericComponentType();
                if (componentType instanceof ParameterizedType) {
                    componentType = ((ParameterizedType)componentType).getRawType();
                }

                if (componentType instanceof Class) {
                    return Array.newInstance((Class)componentType, 0).getClass();
                }
            }

            if (!(actualTypeParam instanceof TypeVariable)) {
                return fail(thisClass, typeParamName);
            }

            TypeVariable<?> v = (TypeVariable)actualTypeParam;
            currentClass = thisClass;
            if (!(v.getGenericDeclaration() instanceof Class)) {
                return Object.class;
            }

            parametrizedSuperclass = (Class)v.getGenericDeclaration();
            typeParamName = v.getName();
        } while(parametrizedSuperclass.isAssignableFrom(thisClass));

        return Object.class;
    }

    private static Class<?> fail(Class<?> type, String typeParamName) {
        throw new IllegalStateException("cannot determine the type of the type parameter '" + typeParamName + "': " + type);
    }

    public abstract boolean match(Object var1);

    TypeParameterMatcher() {
    }

    private static final class ReflectiveMatcher extends TypeParameterMatcher {
        private final Class<?> type;

        ReflectiveMatcher(Class<?> type) {
            this.type = type;
        }

        @Override
        public boolean match(Object msg) {
            return this.type.isInstance(msg);
        }
    }
}

