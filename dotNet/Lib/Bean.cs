using System;

namespace Com.Company.Context {
    [System.AttributeUsage(System.AttributeTargets.Method)]
    public class Bean : Attribute {
        public Bean() {
            Id = string.Empty;
        }

        public Bean(string id) {
            if (id == null) {
                throw new NullReferenceException("Bean id cannot be null");
            }

            Id = id;
        }

        public string Id { get; private set; }
    }
}