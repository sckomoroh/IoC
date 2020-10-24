package com.company.context;

import java.lang.reflect.InvocationTargetException;

public interface IContextFactory {
    IContext create(Object config) throws IllegalBeanException, IllegalConfigException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;
}