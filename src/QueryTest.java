import java.util.Scanner;

import com.unionpay.cloudatlas.util.hbase.index.bean.Query;
import com.unionpay.cloudatlas.util.hbase.index.parser.Parser;
import com.unionpay.cloudatlas.util.hbase.index.util.IndexUtil;
import com.unionpay.cloudatlas.util.hbase.index.util.XMLUtil;

public class QueryTest {
    public static void main(String[] args) throws Exception {
        /*case1:
         * select age, sex from table1 where age > 1 and sex = 2;
         * case2:
         * select age, sex from table1 where age > 1 and gender = 2;
         * case3:
         * select age, sex from table1 where age > 1 and sex = 2 or addr2 = 3;
         */
        Scanner scan = new Scanner(System.in);
        Query query;
        while(scan.hasNextLine()){
            query = new Parser(scan.nextLine()).parse();
            System.out.println(IndexUtil.getBestIndexes(query));
        }
        scan.close();
        

    }
}
