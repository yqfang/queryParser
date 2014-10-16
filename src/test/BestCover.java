package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BestCover {

    public static void main(String[] args) {
        /*
         * given condition: a, b, c, d search in index collection:
         * {a},{b},{c},{d},{a,b,c} return {a,b,c},{d};
         */

        List<String> a = new ArrayList<String>();
        a.add("age");
        a.add("sex");
        List<List<String>> c = new ArrayList<List<String>>();
        List<String> c1 = new ArrayList<String>();
        c1.add("age");
        List<String> c2 = new ArrayList<String>();
        c2.add("sex");
        c2.add("gg");
        List<String> c3 = new ArrayList<String>();
        c3.add("sex");
        List<String> c4 = new ArrayList<String>();
        c4.add("d");

        c.add(c1);
        c.add(c2);
        c.add(c3);
        c.add(c4);
        if(null == getBestCover(c, a))
        System.out.println("no match");
        else
            System.out.println(getBestCover(c, a));

    }
    /** 
     * f[i,S]表示前i个集合覆盖列表S,所需要的集合最小数目
     * 则有[i,S] = min (f[i-1,S], f[i-1, S除去cS[i]])
     * 初值f[0,0] = 0，其余f[i,S]都是INF
     * @param pc the index list collection
     * @param a the condition collection
     * @return the best matched index collection or null if not matched
     */
    public static List<List<String>> getBestCover(List<List<String>> pc, List<String> a) {
        int M = 10; // the max value for collection capacity;
        int N = 200; // the max amount of collection
        int[][] f = new int[N][1 << M];// f(i, S)
        String[][] s = new String[N][1 << M];// s(i, S)
        List<List<String>> c = new ArrayList<List<String>>(); // the string
                                                              // collection
                                                              // after fliter
        Map<String, Integer> id = new HashMap<String, Integer>();// string map
        Map<String, List<String>> cmap = new HashMap<String, List<String>>();// collection
                                                                             // dic
        List<List<String>> rs = new ArrayList<List<String>>();

        int cid = 0; // String id
        int[] bc;
        int ba = 0;// binary string collection

        int INF = (1 << 28);

        for (String str : a) {
            if (!id.containsKey(str)) 
                id.put(str, cid++);
        }
        for (List<String> pl : pc) {
            List<String> l = new ArrayList<String>();
            for (String str : pl) 
                if (id.containsKey(str))
                    l.add(str);
            if (l.size() == pl.size())
                c.add(l);
        }
        bc = new int[c.size() + 1];
        /*
         * construct binary a collection to a int value;
         */
        int iid = 0;
        for (String str : a) {
            iid = id.get(str);
            ba = ba | (1 << iid);
        }
        // System.out.println(ba);
        /*
         * construct binary c collection
         */
        for (int i = 1; i <= c.size(); i++) {
            for (String str : c.get(i - 1)) {
                iid = id.get(str);
                bc[i] = bc[i] | (1 << iid);
            }
            cmap.put(String.valueOf(bc[i]), c.get(i - 1));
        }
        for (int S = 0; S < (1 << cid); S++)
            f[0][S] = INF;
        f[0][0] = 0;
        for (int i = 1; i <= c.size(); i++) {
            for (int S = 0; S < (1 << cid); S++) {
                int zS = (S ^ (bc[i] & S));
                if (f[i - 1][S] < f[i - 1][zS] + 1) {
                    f[i][S] = f[i - 1][S];
                    s[i][S] = s[i - 1][S];
                } else {
                    f[i][S] = f[i - 1][zS] + 1;
                    s[i][S] += s[i - 1][zS] + "," + bc[i];
                }
            }
        }
        String rss = s[c.size()][(1 << cid) - 1];
        if(null == rss)
            return null;
        String[] ra = rss.split(",");
        for (String ss : ra) {
            List<String> ll = cmap.get(ss);
            if (null != ll) 
                rs.add(ll);
        }
        return rs;
    }
}
