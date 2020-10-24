package com.company.context;

public interface IContext {
    <T> T resolve(Class<?> type) throws ContextException;

    <T> T resolve(Class<?> type, String id) throws ContextException;
}