package com.company.context;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ContextTests {
    private ConfigBeanItem[] mBeanItemArrays = new ConfigBeanItem[] {
            new ConfigBeanItem("id1", String.class, "String1"),
            new ConfigBeanItem("id2", String.class, "String2"),
            new ConfigBeanItem("id3", int.class, 10),
            new ConfigBeanItem("", double.class, 0.5),
    };

    private Context mContext = new Context(Arrays.asList(mBeanItemArrays));

    @Test
    void resolveByType_Test() {
        try {
            double instance = mContext.resolve(double.class);
            assertEquals(instance, 0,5);
        } catch (Exception ex) {
            fail("Exception occurs: " + ex.getMessage());
        }
    }

    @Test
    void resolveByName_Test() {
        try {
            String instance = mContext.resolve(String.class, "id1");
            assertEquals(instance, "String1");
        } catch (Exception ex) {
            fail("Exception occurs: " + ex.getMessage());
        }
    }

    @Test
    void resolveAmbiguous_Test() {
        try {
            mContext.resolve(String.class);
            fail("Ambiguous are not allowed");
        } catch (Exception ex) {
            assertEquals(ex.getClass(), ContextException.class);
        }
    }

    @Test
    void absentBean_Test() {
        try {
            mContext.resolve(Object.class);
            fail("Ambiguous are not allowed");
        } catch (Exception ex) {
            assertEquals(ex.getClass(), ContextException.class);
        }
    }
}
