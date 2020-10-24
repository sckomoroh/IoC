using System.Reflection;

namespace Com.Company.Context {
    internal class MethodBeanItem {
        public MethodBeanItem(MethodInfo method, string id) {
            Method = method;
            Id = id;
        }

        public string Id { get; private set; }

        public MethodInfo Method { get; private set; }
    }
}