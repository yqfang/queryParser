import parser.Parser;

import com.google.gson.Gson;

public class QueryTest {
    public static void main(String[] args) {
        String str = "select card_no,student_id from tbl where gender <> \"male\" or (school = \"ustc\" and no = '123')";
        System.out.println(str);
        Parser parser = new Parser(str);
        Gson js = new Gson();
        System.out.println(js.toJson(parser.parse()));
    }
}
