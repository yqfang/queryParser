package ast;

import ast.exp.E;

public class Query {
    ColumnList list;
    Table tbl;
    E exp;
    public Query(ColumnList list, Table tbl, E exp) {
        this.list = list;
        this.tbl = tbl;
        this.exp = exp;
    }
}
