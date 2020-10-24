using System;

namespace Com.Company.Context {
    public class ConfigBeanItem {
        public ConfigBeanItem(string id, Type interfaze, object instance) {
            Id = id;
            Interface = interfaze;
            Instance = instance;
        }

        public string Id { get; private set; }

        public object Instance { get; private set; }

        public Type Interface { get; private set; }
    }
}