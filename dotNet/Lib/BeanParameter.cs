using System;

namespace Com.Company.Context {
    [System.AttributeUsage(System.AttributeTargets.Parameter)]
    public class BeanParameter : Attribute {
        public BeanParameter(string id) {
            if (id == null) {
                throw new NullReferenceException("Bean id cannot be null");
            }

            Id = id;
        }

        public string Id { get; private set; }
    }
}