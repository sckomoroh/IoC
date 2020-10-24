using System.Collections.Generic;
using NUnit.Framework;

namespace Com.Company.Context {
    class ContextTests {
        private IList<ConfigBeanItem> mBeanItemList = new List<ConfigBeanItem> {
            new ConfigBeanItem("id1", typeof(string), "String1"),
            new ConfigBeanItem("id2", typeof(string), "String2"),
            new ConfigBeanItem("id3", typeof(int), 10),
            new ConfigBeanItem("", typeof(double), 0.5),
        };

        private Com.Company.Context.Context mContext;

        [SetUp]
        public void Setup() {
            mContext = new Com.Company.Context.Context(mBeanItemList);
        }

        [Test]
        public void ResolveByType_Test() {
            var instance = mContext.Resolve<double>();
            Assert.AreEqual(instance, 0,5);
        }

        [Test]
        public void ResolveByName_Test() {
            var instance = mContext.Resolve<string>("id1");
            Assert.AreEqual(instance, "String1");
        }

        [Test]
        public void ResolveAmbiguous_Test() {
            try {
                mContext.Resolve<string>();
                Assert.Fail("Ambiguous are not allowed");
            } catch (ContextException) {
            }
        }

        [Test]
        public void AbsentBean_Test() {
            try {
                mContext.Resolve<object>();
                Assert.Fail("Ambiguous are not allowed");
            } catch (ContextException) {
            }
        }
    }
}