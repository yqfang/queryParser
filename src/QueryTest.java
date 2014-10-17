import java.util.Scanner;

import parser.Parser;
import util.IndexUtil;
import util.XMLUtil;
import bean.Query;

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
