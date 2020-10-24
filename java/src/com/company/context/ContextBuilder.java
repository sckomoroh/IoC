package com.company.context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContextBuilder implements IContextBuilder {
    public IContext build(Object config) throws
        IllegalBeanException,
        IllegalConfigException,
        IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException {

        if (config == null) {
            throw new IllegalConfigException("Configuration is null");
        }

        List<MethodBeanItem> methodList = enumerateBeanGetters(config);
        if (methodList.isEmpty()) {
            throw new IllegalConfigException("Configuration is empty");
        }

        List<ConfigBeanItem> beanList = new ArrayList<>();
        for (MethodBeanItem methodItem : methodList) {
            createInstance(config, methodItem, methodList, beanList);
        }

        return new Context(beanList);
    }

    private List<MethodBeanItem> enumerateBeanGetters(Object config) throws
        IllegalConfigException {

        List<MethodBeanItem> result = new ArrayList<>();

        Class<?> clazz = config.getClass();
        Method[] methodArray = clazz.getMethods();
        for (Method methodItem : methodArray) {
            if (methodItem.getName().startsWith("get")) {
                Bean annotation = methodItem.getAnnotation(Bean.class);
                if (annotation != null) {
                    if (result.stream().anyMatch((item) -> {
                        return item.mId.equals(annotation.id())
                                && item.mMethod.getReturnType().equals(methodItem.getReturnType());
                    })) {
                        String msg = String.format(
                                "Bean are duplicated. Id: '%s', type: %s",
                                annotation.id(),
                                methodItem.getReturnType());

                        throw new IllegalConfigException(msg);
                    }

                    if (methodItem.getReturnType().equals(void.class)) {
                        String msg = String.format("The bean %s has void type",
                                                    methodItem.getName());

                        throw new IllegalConfigException(msg);
                    }

                    result.add(new MethodBeanItem(methodItem, annotation.id()));
                }
            }
        }

        return result;
    }

    private Object createWithoutParameters(Object config,
                                           MethodBeanItem methodItem,
                                           List<ConfigBeanItem> beanList) throws
        IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException {

        Object instance;
        Optional<ConfigBeanItem> optional = beanList.stream().filter(
                (item) ->
                        item.mId.equals(methodItem.mId) &&
                        item.mInterface.equals(methodItem.mMethod.getReturnType())).findFirst();

        if (!optional.isPresent()) {
            instance = methodItem.mMethod.invoke(config);
        } else {
            instance = optional.get();
        }

        return instance;
    }

    private Object createWithParameters(Object config,
                                        MethodBeanItem methodItem,
                                        List<MethodBeanItem> methodList,
                                        List<ConfigBeanItem> beanList) throws
            IllegalBeanException,
            InvocationTargetException,
            IllegalAccessException {

        Parameter[] paramArray = methodItem.mMethod.getParameters();
        Object[] params = new Object[paramArray.length];
        int index = 0;
        for (Parameter paramItem : paramArray) {
            String paramId = "";
            BeanParameter paramAnnotation = paramItem.getAnnotation(BeanParameter.class);
            if (paramAnnotation != null) {
                paramId = paramAnnotation.id();
            }

            Object paramObject = findInstance(paramId, paramItem.getType(), beanList);
            if (paramObject == null) {
                MethodBeanItem paramMethodItem = findMethodItem(paramId,
                        paramItem.getType(),
                        methodList);
                if (paramMethodItem == null) {
                    String msg = String.format("Could not find the Bean for %s of type %s",
                            methodItem.mMethod.getName(),
                            paramItem);

                    throw new IllegalBeanException(msg);
                }

                paramObject = createInstance(config, paramMethodItem, methodList, beanList);
            }

            params[index++] = paramObject;
        }

        return methodItem.mMethod.invoke(config, params);
    }

    private Object createInstance(Object config,
                                  MethodBeanItem methodItem,
                                  List<MethodBeanItem> methodList,
                                  List<ConfigBeanItem> beanList) throws
        IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException,
        IllegalBeanException {

        Object instance;
        if (methodItem.mMethod.getParameterTypes().length == 0) {
            instance = createWithoutParameters(config, methodItem, beanList);
        } else {
            instance = createWithParameters(config, methodItem, methodList, beanList);
        }

        beanList.add(new ConfigBeanItem(methodItem.mId,
                                        methodItem.mMethod.getReturnType(),
                                        instance));

        return instance;
    }

    private Object findInstance(String id, Class<?> clazz, List<ConfigBeanItem> beanList) {
        Optional<ConfigBeanItem> result = beanList.stream().filter(
            (item) -> item.mId.equals(id) && item.mInterface.equals(clazz)).findFirst();

        return result.map(configBeanItem -> configBeanItem.mInstance).orElse(null);
    }

    private MethodBeanItem findMethodItem(String id,
                                          Class<?> clazz,
                                          List<MethodBeanItem> methodList) {
        Optional<MethodBeanItem> result = methodList.stream().filter(
            (item) -> item.mId.equals(id) && item.mMethod.getReturnType().equals(clazz))
            .findFirst();

        return result.orElse(null);
    }
}