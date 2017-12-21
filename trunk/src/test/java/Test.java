import com.douguo.dc.util.DateUtil;

import java.util.*;

public class Test {
    public static void main(String args []){
        Map<Integer, String> map = new TreeMap<Integer, String>();
        map.put(576, "ddddd");
        map.put(342, "bbbbb");
        map.put(876, "aaaaa");
        map.put(444, "ccccc");
        System.out.println(map);

        /*//这里将map.entrySet()转换成list
        List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String,String>>(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list,new Comparator<Map.Entry<String,String>>() {
            //升序排序
            public int compare(Map.Entry<String, String> o1,
                               Map.Entry<String, String> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }

        });

        for(Map.Entry<String,String> mapping:list){
            System.out.println(mapping.getKey()+":"+mapping.getValue());
        }*/
    }
}
