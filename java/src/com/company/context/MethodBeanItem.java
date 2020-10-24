package com.company.context;

import java.lang.reflect.Method;

class MethodBeanItem {
    public final Method mMethod;
    public final String mId;
    
    public MethodBeanItem(Method method, String id) {
        mMethod = method;
        mId = id;
    }
}