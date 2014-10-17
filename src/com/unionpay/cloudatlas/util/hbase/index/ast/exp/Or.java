package com.unionpay.cloudatlas.util.hbase.index.ast.exp;
import java.util.Collection;

import com.unionpay.cloudatlas.util.hbase.index.ast.Operator;
public class Or extends Compound{
    
    public Or() {
        super();
    }
    public Or(Operator opt) {
        super(opt);
    }
    
    public Or(Compound... expressions) {
        super(expressions);
    }

    
    public Or(Collection<Compound> expressions) {
        super(expressions);
    }

   
    public Or or(Compound expression) {
        return (Or) super.add(expression);
    }
    
}
