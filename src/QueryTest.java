import java.util.Scanner;

import com.unionpay.cloudatlas.util.hbase.index.bean.Query;
import com.unionpay.cloudatlas.util.hbase.index.parser.Parser;
import com.unionpay.cloudatlas.util.hbase.index.util.IndexUtil;
import com.unionpay.cloudatlas.util.hbase.index.util.XMLUtil;

public class QueryTest {
    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        Query query;
        while(scan.hasNextLine()){
            query = new Parser(scan.nextLine()).parse();
            System.out.println(IndexUtil.getBestIndexes(
                    XMLUtil.read(query.getTable()), query.getConditionCols()));
        }
        scan.close();
        

    }
}
