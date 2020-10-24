namespace Com.Company.Context {
    public class ContextFactory : IContextFactory {
        private readonly IContextBuilder mBuilder;

        public ContextFactory(IContextBuilder builder) {
            mBuilder = builder;
        }

        public IContext Create(object config) {
            return mBuilder.Build(config);
        }
    }
}