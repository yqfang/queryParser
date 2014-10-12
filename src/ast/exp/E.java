package ast.exp;

import ast.Operator;

public abstract class E {
    public abstract E add(E e);
    public abstract boolean hasChildren();
    public Operator operator;
}
