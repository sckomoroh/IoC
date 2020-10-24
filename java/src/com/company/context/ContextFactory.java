package com.company.context;

import java.lang.reflect.InvocationTargetException;

public class ContextFactory implements IContextFactory {
    private final IContextBuilder mBuilder;

    ContextFactory(IContextBuilder builder) {
        mBuilder = builder;
    }

    public IContext create(Object config) throws IllegalBeanException, IllegalConfigException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        return mBuilder.build(config);
    }
}