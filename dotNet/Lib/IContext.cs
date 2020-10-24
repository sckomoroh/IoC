namespace Com.Company.Context {
    public interface IContext {
        T Resolve<T>();

        T Resolve<T>(string id);
    }
}