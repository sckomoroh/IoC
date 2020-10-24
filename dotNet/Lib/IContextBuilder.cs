namespace Com.Company.Context {
    public interface IContextBuilder {
        IContext Build(object config);
    }
}