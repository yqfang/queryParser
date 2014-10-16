import parser.Parser;
import util.IndexUtil;
import util.XMLUtil;
import bean.Query;

public class QueryTest {
    public static void main(String[] args) throws Exception {
        Query query = new Parser(
                "select b from table1 where age = 1 and sex = 2 or addr = 3")
                .parse();
        System.out.println(IndexUtil.getBestIndexes(
                XMLUtil.read(query.getTable()), query.getLeafLeft()));

    }
}
