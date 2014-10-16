package bean;

import java.util.List;

public class Index {
    private String name;
    private List<String> cols;
    public Index(String name, List<String> cols) {
        this.name = name;
        this.cols = cols;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<String> getCols() {
        return cols;
    }
    public void setCols(List<String> cols) {
        this.cols = cols;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "IndexName: " + getName() + "\n Columns: " + getCols().toString();
    }
}
