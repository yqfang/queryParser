package ast.exp;
import java.util.Collection;

import ast.Operator;
public class Or extends Compound{
    
    public Or() {
        super();
    }
    public Or(Operator opt) {
        super(opt);
    }
    
    public Or(E... expressions) {
        super(expressions);
    }

    
    public Or(Collection<E> expressions) {
        super(expressions);
    }

   
    public Or or(E expression) {
        return (Or) super.add(expression);
    }
    
}
