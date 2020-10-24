package com.company.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

class ContextBuilderTests {
    @Test
    void createSimple_Test() throws InvocationTargetException, IllegalAccessException, IllegalConfigException, IllegalBeanException {
        Object config = new Object() {
            @Bean
            public String getObject() { return "Instance"; }

            @Bean(id = "bean3")
            public String getObject1(@BeanParameter(id = "bean1") String input) {
                return "Instance";
            }

            @Bean(id = "bean1")
            public String getObject2() { return "Instance"; }

            @Bean(id = "bean2")
            public String getObject3(@BeanParameter(id = "bean1") String input) {
                return "Instance";
            }
        };

        IContextBuilder builder = new ContextBuilder();

        IContext context = builder.build(config);
        assertNotNull(context, "Context is null");
    }

    @Test
    void voidBean_Test() throws IllegalAccessException, IllegalBeanException, InvocationTargetException {
        Object config = new Object() {
            @Bean
            public String getObject1() { return "Instance 1"; }

            @Bean
            public void getObject2() { }
        };

        IContextBuilder builder = new ContextBuilder();

        try {
            builder.build(config);
            fail();
        } catch (IllegalConfigException ex) {
        }
    }

    @Test
    void multipleType_Test() throws IllegalAccessException, IllegalBeanException, InvocationTargetException {
        Object config = new Object() {
            @Bean
            public String getObject1() { return "Instance 1"; }

            @Bean
            public String getObject2() { return "Instance 2"; }
        };

        IContextBuilder builder = new ContextBuilder();

        try {
            builder.build(config);
            fail("Multiple unnamed instances with same type are not allowed");
        } catch (IllegalConfigException ex) {
        }
    }

    @Test
    void multipleNamedDiffType_Test() throws IllegalAccessException, IllegalBeanException, InvocationTargetException, IllegalConfigException {
        Object config = new Object() {
            @Bean(id = "object")
            public String getObject1() { return "Instance 1"; }

            @Bean(id = "object")
            public Object getObject2() { return new Object(); }
        };

        IContextBuilder builder = new ContextBuilder();

        builder.build(config);
    }

    @Test
    void nullConfig_Test() throws IllegalAccessException, IllegalBeanException, InvocationTargetException {
        IContextBuilder builder = new ContextBuilder();

        try {
            builder.build(null);
            fail("Null config are not allowed");
        } catch (IllegalConfigException ex) {
        }
    }

    @Test
    void emptyConfig_Test() throws IllegalAccessException, IllegalBeanException, InvocationTargetException {
        IContextBuilder builder = new ContextBuilder();

        try {
            Object config = new Object();
            builder.build(config);
            fail("Empty config are not allowed");
        } catch (IllegalConfigException ex) {
        }
    }

    @Test
    void absentParamBean_Test() {
        Object config = new Object() {
            @Bean(id = "inst")
            public String getObject1(@BeanParameter(id = "val3")int obj) { return "Instance " + obj; }

            @Bean(id = "val1")
            public int getVal1() { return 10; }

            @Bean(id = "val2")
            public int getVal2() { return 20; }
        };

        IContextBuilder builder = new ContextBuilder();

        try {
            builder.build(config);
            fail("Absent bean param is not allowed");
        } catch (Exception ex) {
            assertEquals(ex.getClass(), IllegalBeanException.class);
        }
    }
}