namespace Com.Company.Context {
    public interface IContextFactory {
        IContext Create(object config);
    }
}