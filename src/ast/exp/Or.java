package ast.exp;
import java.util.Collection;
public class Or extends Compound{
    
    public Or() {
        super();
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
