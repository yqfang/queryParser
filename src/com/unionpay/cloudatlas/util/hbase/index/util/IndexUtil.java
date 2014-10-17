package com.unionpay.cloudatlas.util.hbase.index.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.unionpay.cloudatlas.util.hbase.index.bean.Index;

public class IndexUtil {

    /**
     * f[i,S]表示前i个集合覆盖列表S,所需要的集合最小数目 则有[i,S] = min (f[i-1,S], f[i-1, S除去cS[i]])
     * 初值f[0,0] = 0，其余f[i,S]都是INF
     * 
     * @param pc
     *            the index list collection
     * @param a
     *            the condition collection
     * @return the best matched index collection or null if not matched
     * @throws Exception
     */
    public static Set<Index> getBestIndexes(Set<Index> indexes,
            List<String> a) throws Exception {
        int M = 10; // the max value for collection capacity;
        int N = 200; // the max amount of collection
        int[][] f = new int[N][1 << M];// f(i, S)
        String[][] s = new String[N][1 << M];// s(i, S)
        List<List<String>> c = new ArrayList<List<String>>(); // the string
        // collection
        Map<String, Integer> id = new HashMap<String, Integer>();// string map
        Map<String, List<String>> cmap = new HashMap<String, List<String>>();// collection
        Map<String, List<String>> mp = new HashMap<String, List<String>>();// a map for the indexes set
        List<List<String>> pc = new ArrayList<List<String>>();
        Set<Index> result = new HashSet<Index>();
        Map<List<String>, String> reverseMp = new HashMap<List<String>, String>();
        int cid = 0; // String id
        int[] bc;
        int ba = 0;// binary string collection
        int INF = (1 << 28);
        for(Index index : indexes){
            mp.put(index.getName(), index.getCols());
        }
        /*
         * reverse the mp map
         */
        for (String str : mp.keySet()) {
            reverseMp.put(mp.get(str), str);
        }
        for (String str : a) {
            if (!id.containsKey(str))
                id.put(str, cid++);
        }
        for (String str : mp.keySet()) {
            pc.add(mp.get(str));
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
        if (null == rss)
        {
            System.err.println("Go to hive !");
            System.exit(-1);
        }
        String[] ra = rss.split(",");
        for (String ss : ra) {
            List<String> ll = cmap.get(ss);
            if (null != ll) {
                result.add(new Index(reverseMp.get(ll), ll));
            }
        }
        return result;
    }
}