using Moq;
using NUnit.Framework;

namespace Com.Company.Context {
    public class ContextFactoryTests {
        Mock<IContextBuilder> mMockBuilder = new Mock<IContextBuilder>();

        Mock<IContext> mMockContext = new Mock<IContext>();

        [Test]
        public void Create_Test() {
            mMockBuilder.Setup(x => x.Build(It.IsAny<object>())).Returns(mMockContext.Object);
            ContextFactory factory = new ContextFactory(mMockBuilder.Object);
            var context = factory.Create(new object());
            Assert.AreEqual(context, mMockContext.Object);
        }
    }
}