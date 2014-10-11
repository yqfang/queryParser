package ast;

import java.util.LinkedList;
import java.util.List;

public class ColumnList {
    List<String> list;
    public ColumnList() {
        list = new LinkedList<String>();
    }
    public void addCol(String col)
    {
        this.list.add(col);
    }
}
