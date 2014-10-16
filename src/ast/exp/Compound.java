package ast.exp;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import ast.Operator;


public class Compound {
    protected List<Compound> children;
    public Operator operator;
    protected String left;
    protected String right;
    public Compound(){
        this.children = new LinkedList<Compound>();
    }
    public Compound(Operator opt){
        this.children = new LinkedList<Compound>();
        this.operator = opt;
    }
    public Compound(Compound... exp) {
        this.children = new LinkedList<Compound>(Arrays.asList(exp));
    }
    public Compound(Collection<Compound> exp) {
        this.children = new LinkedList<Compound>(exp);
    }

    public Compound add(Compound exp) {
        children.add(exp);
        return this;
    }
    public boolean hasChildren(){
        return !children.isEmpty();
    }
    public List<Compound> getChildren() {
        return children;
    }
    public Operator getOperator() {
        return operator;
    }
    public void setOperator(Operator operator) {
        this.operator = operator;
    }
    public String getLeft() {
        return left;
    }
    public void setLeft(String left) {
        this.left = left;
    }
    public String getRight() {
        return right;
    }
    public void setRight(String right) {
        this.right = right;
    }
    public void setChildren(List<Compound> children) {
        this.children = children;
    }
    


}
