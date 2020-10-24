using System.Collections.Generic;
using System.Linq;

namespace Com.Company.Context {
    public class Context : IContext {
        private readonly IList<ConfigBeanItem> mBeanList;

        public Context(IList<ConfigBeanItem> beanList) {
            mBeanList = beanList;
        }

        public T Resolve<T>() {
            return Resolve<T>(null);
        }

        public T Resolve<T>(string id) {
            var beanList = mBeanList.Where(x => x.Interface.Equals(typeof(T)));

            if (id != null) {
                beanList = beanList.Where(x => x.Id.Equals(id));
            }

            if (!beanList.Any()) {
                throw new ContextException("Bean not found");
            }

            if (beanList.Count() > 1) {
                throw new ContextException("Ambigouod beans found");
            }

            return (T)beanList.First().Instance;
        }
    }
}