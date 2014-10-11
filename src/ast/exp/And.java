package ast.exp;

import java.util.Collection;

    public class And extends Compound {

        public And() {
            super();
        }

        public And(E... expressions) {
            super(expressions);
        }

        public And(Collection<E> expressions) {
            super(expressions);
        }

        public And and(E expression) {
            return (And) super.add(expression);
        }
}
