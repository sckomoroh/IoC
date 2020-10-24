using System;

namespace Com.Company.Context {
    public class IllegalBeanException : Exception {
        public IllegalBeanException(string msg) : base (msg) {
        }
    }
}