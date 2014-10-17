package com.unionpay.cloudatlas.util.hbase.index.ast;

public enum Operator {
    AND,
    OR,
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
