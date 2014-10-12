package ast.exp;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class Compound extends E {
    protected List<E> children;
    public Compound(){
        this.children = new LinkedList<E>();
    }
    public Compound(E... exp) {
        this.children = new LinkedList<E>(Arrays.asList(exp));
    }
    public Compound(Collection<E> exp) {
        this.children = new LinkedList<E>(exp);
    }

    public Compound add(E exp) {
        children.add(exp);
        return this;
    }
    public boolean hasChildren(){
        return !children.isEmpty();
    }

}
