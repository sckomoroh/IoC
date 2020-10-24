package com.company.context;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

class ContextFactoryTests {
    @Mock
    IContextBuilder mBuilder = mock(IContextBuilder.class);

    private IContext mContext = new IContext() {
        @Override
        public <T> T resolve(Class<?> type) throws ContextException {
            return null;
        }

        @Override
        public <T> T resolve(Class<?> type, String id) throws ContextException {
            return null;
        }
    };

    @Test
    void create_Test() throws InvocationTargetException, IllegalAccessException, IllegalConfigException, IllegalBeanException {
        when(mBuilder.build(any())).thenReturn(mContext);

        try {
            ContextFactory factory = new ContextFactory(mBuilder);
            IContext context = factory.create(new Object());
            assertEquals(context, mContext);

            verify(mBuilder, times(1)).build(any());
        } catch (Exception ex) {
            fail("Exception occurs: " + ex.getMessage());
        }
    }
}
