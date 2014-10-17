package com.unionpay.cloudatlas.util.hbase.index.exp;

import java.util.Collection;

    public class And extends Compound {

        public And() {
            super();
        }
        public And(Operator opt) {
            super(opt);
        }

        public And(Compound... expressions) {
            super(expressions);
        }

        public And(Collection<Compound> expressions) {
            super(expressions);
        }

        public And and(Compound expression) {
            return (And) super.add(expression);
        }
}
