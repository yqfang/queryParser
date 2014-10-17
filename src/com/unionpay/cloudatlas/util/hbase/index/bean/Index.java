package com.unionpay.cloudatlas.util.hbase.index.bean;

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
        return "IndexName: " + getName() + "\n Columns: "
                + getCols().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) // 先检查是否其自反性，后比较other是否为空。这样效率高
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Index))
            return false;

        final Index index = (Index) obj;

        if (this.getName().equals(index.getName())
                && this.getCols() == index.getCols())
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        return getName().hashCode() * 28 + getCols().hashCode();
    }
}
