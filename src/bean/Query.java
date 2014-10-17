package bean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ast.exp.Comparison;
import ast.exp.Compound;

public class Query {
    private List<String> columns;
    private String table;
    private Compound condition;

    public Query(List<String> columns, String table, Compound condition) {
        this.columns = columns;
        this.table = table;
        this.condition = condition;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Compound getCondition() {
        return condition;
    }

    public void setCondition(Compound condition) {
        this.condition = condition;
    }

    public List<String> getConditionCols() {
        List<String> l = new ArrayList<String>();
        LinkedList<Compound> ll = new LinkedList<Compound>();
        Compound p = (Compound) condition;
        if (!p.hasChildren()) {
            l.add(((Comparison) (p)).getLeft());
            return l;
        }
        ll.addLast(p);
        while (!ll.isEmpty()) {
            Compound pp = ll.removeFirst();
            List<Compound> list = pp.getChildren();
            for(Compound c : list){
                if(c.hasChildren())
                    ll.add(c);
                else
                    l.add(c.getLeft());
            }

        }
        return l;

    }

}
