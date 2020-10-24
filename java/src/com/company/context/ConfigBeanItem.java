package com.company.context;

class ConfigBeanItem {
    public final String mId;
    public final Class<?> mInterface;
    public final Object mInstance;

    ConfigBeanItem(String id, Class<?> interfaze, Object instance) {
        mId = id;
        mInterface = interfaze;
        mInstance = instance;
    }
}