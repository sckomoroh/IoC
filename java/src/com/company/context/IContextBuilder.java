package com.company.context;

import java.lang.reflect.InvocationTargetException;

public interface IContextBuilder {
    IContext build(Object config) throws IllegalBeanException, IllegalConfigException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;
}