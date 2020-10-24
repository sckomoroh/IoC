using System;
using NUnit.Framework;

namespace Com.Company.Context {
    public class TestConfig {
        [Bean]
        public string GetInstance() {
            return "Instance1";
        }

        [Bean("bean3")]
        public string GetInstance3([BeanParameter("bean1")]string input) {
            return "Instance1";
        }

        [Bean("bean1")]
        public string GetInstance1() {
            return "Instance1";
        }

        [Bean("bean2")]
        public string GetInstance2([BeanParameter("bean1")]string input) {
            return "Instance1";
        }
    }

    public class EmptyConfig {
        public string GetInstance() {
            return "Instance";
        }
    }

    public class VoidBeanType {
        [Bean("bean2")]
        public void GetVoid() {}
    }

    public class IncompleteConfig {
        [Bean("bean2")]
        public string GetInstance2([BeanParameter("bean1")]string input) {
            return "Instance1";
        }
    }

    public class NullBeanId {
        [Bean(null)]
        public string GetInstance2([BeanParameter("bean1")]string input) {
            return "Instance1";
        }
    }

    public class NullBeanParamId {
        [Bean]
        public string GetInstance2([BeanParameter(null)]string input) {
            return "Instance1";
        }
    }

    public class MultipleTypes {
        [Bean]
        public object GetInstance1() { return new object(); }

        [Bean]
        public object GetInstance2() { return new object(); }
    }

    public class MultipleNamesDiffTypes {
        [Bean("bean1")]
        public object GetInstance1() { return new object(); }

        [Bean("Bena2")]
        public object GetInstance2() { return new object(); }
    }

    public class ContextBuilderTests {
        [Test]
        public void MultipleNamesDiffTypes_Test() {
            try {
                var builder = new ContextBuilder();
                var context = builder.Build(new MultipleNamesDiffTypes());
                Assert.NotNull(context);
            } catch (IllegalBeanException) {
            }
        }

        [Test]
        public void MultipleTypes_Test() {
            try {
                var builder = new ContextBuilder();
                builder.Build(new MultipleTypes());
                Assert.Fail();
            } catch (IllegalConfigException) {
            }
        }

        [Test]
        public void CreateSimple_Test() {
            var builder = new ContextBuilder();
            var context = builder.Build(new TestConfig());
            Assert.NotNull(context);
        }

        [Test]
        public void VoidBeanType_Test() {
            try {
                var builder = new ContextBuilder();
                builder.Build(new VoidBeanType());
                Assert.Fail();
            } catch (IllegalBeanException) {
            }
        }

        [Test]
        public void NullConfig_Test() {
            try {
                var builder = new ContextBuilder();
                builder.Build(null);
                Assert.Fail();
            } catch (IllegalConfigException) {
            }
        }

        [Test]
        public void EmptyConfig_Test() {
            try {
                var builder = new ContextBuilder();
                builder.Build(new EmptyConfig());
                Assert.Fail();
            } catch (IllegalConfigException) {
            }
        }

        [Test]
        public void AbsentParamBean_Test() {
            try {
                var builder = new ContextBuilder();
                builder.Build(new IncompleteConfig());
                Assert.Fail();
            } catch (IllegalBeanException) {
            }
        }

        [Test]
        public void NullBeanId_Test() {
            try {
                var builder = new ContextBuilder();
                builder.Build(new NullBeanId());
                Assert.Fail();
            } catch (NullReferenceException) {
            }
        }

        [Test]
        public void NullBeanParamId_Test() {
            try {
                var builder = new ContextBuilder();
                builder.Build(new NullBeanParamId());
                Assert.Fail();
            } catch (NullReferenceException) {
            }
        }
    }
}
