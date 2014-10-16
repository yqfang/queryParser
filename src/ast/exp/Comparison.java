package ast.exp;

import ast.Operator;

public class Comparison extends Compound {
//    protected String left;
//    protected String right;

    public Comparison(String left, String right, Operator operator) {
        this.left = left;
        this.right = right;
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

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    @Override
    public boolean hasChildren() {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public Compound add(Compound e) {
        // TODO Auto-generated method stub
        return null;
    }
}
