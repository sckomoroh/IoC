using System.Collections.Generic;
using System.Linq;

namespace Com.Company.Context {
    public class ContextBuilder : IContextBuilder {
        public IContext Build(object config) {
            if (config == null) {
                throw new IllegalConfigException("Configuration is null");
            }

            var methodList = EnumerateBeanGetters(config);
            if (!methodList.Any()) {
                throw new IllegalConfigException("Configuration does not contains beans");
            }

            var beanList = new List<ConfigBeanItem>();
            foreach(var methodItem in methodList) {
                CreateInstance(config, methodItem, methodList, beanList);
            }

            return new Context(beanList);
        }

        private IList<MethodBeanItem> EnumerateBeanGetters(object config) {
            var result = new List<MethodBeanItem> ();

            var configType = config.GetType();
            var methodArray = configType.GetMethods();
            foreach (var methodItem in methodArray) {
                if (methodItem.Name.StartsWith("Get")) {
                    var attrs = methodItem.GetCustomAttributes(typeof(Bean), false);
                    if (attrs != null && attrs.Any()) {
                        if (methodItem.ReturnType == typeof(void)) {
                            var msg = string.Format(
                                "Bean {0} has void return type",
                                methodItem.Name);

                            throw new IllegalBeanException(msg);
                        }

                        var beanAttr = attrs[0] as Bean;
                        if (result.Any(x => x.Id.Equals(beanAttr.Id) && x.Method.ReturnType.Equals(methodItem.ReturnType))) {
                            string msg = string.Format(
                                "Bean are duplicated. Id: '{0}', type: {1}",
                                beanAttr.Id,
                                methodItem.ReturnType);

                        throw new IllegalConfigException(msg);                        }

                        result.Add(new MethodBeanItem(methodItem, beanAttr.Id));
                    }
                }
            }

            return result;
        }

        private object CreateInstance(object config,
                                      MethodBeanItem methodItem,
                                      IList<MethodBeanItem> methodList,
                                      IList<ConfigBeanItem> beanList) {

            object instance;
            if (!methodItem.Method.GetParameters().Any()) {
                instance = CreateWithoutParameters(config, methodItem, beanList);
            } else {
                instance = CreateWithParameters(config, methodItem, methodList, beanList);
            }

            beanList.Add(new ConfigBeanItem(methodItem.Id, methodItem.Method.ReturnType, instance));

            return instance;
        }

        private object CreateWithoutParameters(object config,
                                               MethodBeanItem methodItem,
                                               IList<ConfigBeanItem> beanList) {

            var beanItem = beanList.FirstOrDefault(x => x.Id.Equals(methodItem.Id) &&
                x.Interface.Equals(methodItem.Method.ReturnType));

            object instance;
            if (beanItem == null) {
                instance = methodItem.Method.Invoke(config, new object[] {});
            } else {
                instance = beanItem.Instance;
            }

            return instance;
        }

        private object CreateWithParameters(object config,
                                            MethodBeanItem methodItem,
                                            IList<MethodBeanItem> methodList,
                                            IList<ConfigBeanItem> beanList) {

            var paramsArray = methodItem.Method.GetParameters();
            object[] prms = new object[paramsArray.Length];
            var index = 0;
            foreach(var paramItem in paramsArray) {
                var paramId = string.Empty;
                var paramAnnotation = paramItem.GetCustomAttributes(typeof(BeanParameter), false);
                if (paramAnnotation != null && paramAnnotation.Any()) {
                    var annotation = paramAnnotation[0] as BeanParameter;
                    paramId = annotation.Id;
                }

                object instance;
                var paramObjectItem = beanList.FirstOrDefault(x => x.Id.Equals(paramId) &&
                    x.Interface.Equals(paramItem.ParameterType));

                if (paramObjectItem == null) {
                    var paramMethodItem = methodList.FirstOrDefault(x => x.Id.Equals(paramId) &&
                        x.Method.ReturnType.Equals(paramItem.ParameterType));
                    if (paramMethodItem == null) {
                        var msg = string.Format("Could not find the Bean for {0} of type {1}",
                                                methodItem.Method.Name,
                                                paramItem);

                        throw new IllegalBeanException(msg);
                    }

                    instance = CreateInstance(config, paramMethodItem, methodList, beanList);
                } else {
                    instance = paramObjectItem.Instance;
                }

                prms[index++] = instance;
            }

            return methodItem.Method.Invoke(config, prms);
        }
    }
}