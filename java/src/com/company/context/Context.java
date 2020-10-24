package com.company.context;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

class Context implements IContext {
    private final List<ConfigBeanItem> mBeanList;

    Context(List<ConfigBeanItem> beanList) {
        mBeanList = beanList;
    }

    @Override
    public <T> T resolve(Class<?> type) throws ContextException {
        return resolve(type, null);
    }

    @Override
    public <T> T resolve(Class<?> type, String id) throws ContextException {
        Supplier<Stream<ConfigBeanItem>> beanStream = () -> mBeanList.stream().filter((item) -> {
            boolean result = item.mInterface.equals(type);
            if (id != null) {
                result &= id.equals(item.mId);
            }

            return result;
        });

        if (beanStream.get().count() > 1) {
            throw new ContextException("Multiple beans are found");
        }

        Optional<ConfigBeanItem> result = beanStream.get().findFirst();
        if (!result.isPresent()) {
            throw new ContextException("Bean not found");
        }

        return (T)result.get().mInstance;
    }
}