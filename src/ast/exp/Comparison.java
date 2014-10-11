package ast.exp;

public class Comparison extends E {
    public String left;
    public String right;
    public Operator operator;

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

    public enum Operator {
        /**
         * The equals operator.
         */
        EQ,
        /**
         * The greater than operator.
         */
        GT,
        /**
         * The greater than or equals operator.
         */
        GTE,
        /**
         * The less than operator.
         */
        LT,
        /**
         * The less than or equals operator.
         */
        LTE,
        /**
         * The not equals operator.
         */
        NEQ,
    }
}
