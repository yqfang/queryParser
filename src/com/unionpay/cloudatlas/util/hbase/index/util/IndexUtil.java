package com.unionpay.cloudatlas.util.hbase.index.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.unionpay.cloudatlas.util.hbase.index.bean.Index;
import com.unionpay.cloudatlas.util.hbase.index.bean.Query;

public class IndexUtil {

    /**
     * @param indexes
     *            the indexes set ready for match
     * @param cols
     *            the condition column list
     * @return the best matched indexes Set
     */
    public static Set<Index> getBestIndexes(Query query) throws Exception {
        Set<Index> indexes = XMLUtil.readIndexFromTable(query.getTable());
        List<String> cols = query.getConditionCols();
        int M = 10; // the max value for collection capacity;
        int N = 200; // the max amount of collection
        int[][] f = new int[N][1 << M];// 构造最优解f(i, S)
        String[][] s = new String[N][1 << M];// 构造最优解值s(i, S)
        List<List<String>> c = new ArrayList<List<String>>();// 存放过滤后的String列表
        Map<String, Integer> id = new HashMap<String, Integer>();// cols 中
                                                                 // 的cols映射表
        Map<String, List<String>> cmap = new HashMap<String, List<String>>();// collection
        Map<String, List<String>> mp = new HashMap<String, List<String>>();// index名和index
                                                                           // cols的映射表
        List<List<String>> pc = new ArrayList<List<String>>();// 存放过滤前的String列表
        // 结果集合
        Set<Index> result = new HashSet<Index>();
        // index名和index cols的映射表的反转（因为是一一对应）
        Map<List<String>, String> reverseMp = new HashMap<List<String>, String>();
        int cid = 0; // cols映射表中每个String对应的自增id
        int[] bc; // 存放待查索引每个集合的压缩状态
        int ba = 0;// 存放待匹配列的压缩状态
        int INF = (1 << 28);
        /*
         * 构造mp
         */
        for (Index index : indexes) {
            mp.put(index.getName(), index.getCols());
        }
        /*
         * 通过反转mp构造reverseMp
         */
        for (String str : mp.keySet()) {
            reverseMp.put(mp.get(str), str);
        }
        /*
         * 把待匹配列存入id字典
         */
        for (String str : cols) {
            if (!id.containsKey(str))
                id.put(str, cid++);
        }
        /*
         * 构造pc
         */
        for (String str : mp.keySet()) {
            pc.add(mp.get(str));
        }
        /*
         * 通过过滤pc构造c
         */
        for (List<String> pl : pc) {
            List<String> l = new ArrayList<String>();
            for (String str : pl)
                if (id.containsKey(str))
                    l.add(str);
            if (l.size() == pl.size())
                c.add(l);
        }
        /*
         * 确定压缩状态数组bc的大小
         */
        bc = new int[c.size() + 1];
        /*
         * construct binary a collection to a int value;
         */
        int iid = 0;
        for (String str : cols) {
            iid = id.get(str);
            ba = ba | (1 << iid);
        }
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
        /*
         * f[i,S]表示前i个集合覆盖列表S,所需要的集合最小数目 则有 f[i,S] = min(f[i-1,S], f[i-1,
         * S除去cS[i]] + 1) 初值f[0,0] = 0，其余f[i,S]都是INF
         */
        for (int S = 0; S < (1 << cid); S++)
            f[0][S] = INF;
        f[0][0] = 0;
        for (int i = 1; i <= c.size(); i++) {
            for (int S = 0; S < (1 << cid); S++) {
                int zS = (S ^ (bc[i] & S));// zS是集合S和集合bc[i]的差集
                if (f[i - 1][S] < f[i - 1][zS] + 1) {
                    f[i][S] = f[i - 1][S];
                    s[i][S] = s[i - 1][S];
                } else {
                    f[i][S] = f[i - 1][zS] + 1;
                    s[i][S] += s[i - 1][zS] + "," + bc[i];
                }
            }
        }
        /*
         * 解析最优解值
         */
        String rss = s[c.size()][(1 << cid) - 1];
        if (null == rss) {
            System.err.println("Go to hive !");
            return result;
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