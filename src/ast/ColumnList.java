package ast;

import java.util.LinkedList;
import java.util.List;

public class ColumnList {
    List<String> list;
    public ColumnList() {
        list = new LinkedList<String>();
    }
    public ColumnList(List<String> list){
        this.list = list;
    }
    public void addCol(String col)
    {
        this.list.add(col);
    }
}
