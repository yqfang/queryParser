import parser.Parser;

public class QueryTest {
    public static void main(String[] args) {
        // String str1 =
        // "select a, b, c from b where (a > 1) or (a < 2) and (b > 3)";
        // String str4 = "select a, b, c from b where a > 1 or a < 2 and b > 3";
        // String str2 =
        // "select a, b, c from b where ((a > 1) or (a < 2)) and (b > 3)";
        // String str3 =
        // "select a, b, c from b where (((a > 1) or (a < 2))) and (b > 3)";
        // String str5 =
        // "select a, b, c from b where (((a > 1) or (a < 2))) or (b > 3)";
        String str7 = "select card_no, age, mobile from tbl where card_no = 1 and age >= 2 and mobile= 13223";
        // String str7 =
        // "select card_no, age, mobile from tbl where card_no = 1";
        // System.out.println(new Gson().toJson(new Parser(str1).parse()));
        // System.out.println(new Gson().toJson(new Parser(str4).parse()));
        // System.out.println(new Gson().toJson(new Parser(str2).parse()));
        // System.out.println(new Gson().toJson(new Parser(str3).parse()));
        // System.out.println(new Gson().toJson(new Parser(str5).parse()));
        System.out.println((new Parser(str7).parse().getLeafLeft()));
        // System.out.println(new Gson().toJson(new Parser(str7).parse()));
    }
}
