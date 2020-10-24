using System;

namespace Com.Company.Context {
    public class IllegalConfigException : Exception {
        public IllegalConfigException(string msg) : base (msg) {
        }
    }
}