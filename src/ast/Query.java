package ast;

import ast.exp.E;

public class Query {
    ColumnList columns;
    String table;
    E condition;
    public Query(ColumnList columns, String table, E condition) {
        this.columns = columns;
        this.table = table;
        this.condition = condition;
    }
}
