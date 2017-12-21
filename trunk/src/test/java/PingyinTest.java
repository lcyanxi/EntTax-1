import com.douguo.dc.util.DateUtil;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.junit.Test;

import java.io.*;
import java.util.*;

import static com.douguo.dc.util.DataFillZeroUtil.getNextDay;

public class PingyinTest {
   @Test
    public  void hbaseTest(){
        List hourList=new ArrayList();
        for(int i=0;i<24;i++){
            hourList.add(i);

        }
        System.out.println(hourList);
        System.out.println(hourList.contains(24));
    }


    @Test
    public void pingyinTest() {
        File file = new File("D:/srctypename.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String date=DateUtil.dateToString(new Date(),"yyyy-MM-dd HH:mm:ss");
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行

                String[] strArray = s.split(","); //拆分字符为"," ,然后把结果交给数组strArray
                String name=strArray[0].trim();
                String srctype=strArray[1];
                if (strArray.length==3){
                    String desc=strArray[2];
                    System.out.println("insert into dd_server_log_srctype_dict(srctype,srctype_name,service,create_time,update_time,sdesc)" +
                            " values(\""+srctype+"\",\""+name+"\",\"caipu\",\""+date+"\",\""+date+"\",\""+desc+"\");");
                }else {
                    System.out.println("insert into dd_server_log_srctype_dict(srctype,srctype_name,service,create_time,update_time,sdesc)" +
                            " values(\""+srctype+"\",\""+name+"\",\"caipu\",\""+date+"\",\""+date+"\",\"暂无\");");
                }



            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("------------执行成功");
    }

    public static void converKeyword(List<Map> tempList){
        File file = new File("D:/result_keyword.txt");
        FileWriter fw = null;
        BufferedWriter writer = null;
        try {
            fw = new FileWriter(file);
            writer = new BufferedWriter(fw);

            for (Map map:tempList){
                Iterator iter = map.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key =(String) entry.getKey();
                    String value=(String) entry.getValue();
                    System.out.println(key);
                    String newStr = new String(key.getBytes(), "UTF-8");
                    String pinying=ToPinyin(newStr );
                    String firstChar=ToFirstChar(newStr);

                    writer.write(key+","+value+","+pinying+","+firstChar);
                    writer.newLine();//换行
                }
            }


            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                writer.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public static void writeToTxt(List<Pinying> list){

        File file = new File("D:/new_categories.txt");
        FileWriter fw = null;
        BufferedWriter writer = null;
        try {
            fw = new FileWriter(file);
            writer = new BufferedWriter(fw);
            for (Pinying p:list){
                writer.write(p.getKeyword()+","+p.getNum()+","+p.getPinying()+","+p.getFirstChar());
                writer.newLine();//换行
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                writer.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取字符串拼音的第一个字母
     *
     * @param chinese
     * @return
     */
    public static String ToFirstChar(String chinese) {
        String pinyinStr = "";
        char[] newChar = chinese.toCharArray();  //转为单个字符
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < newChar.length; i++) {
            if (newChar[i] > 128) {
                try {
                    pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0].charAt(0);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinStr += newChar[i];
            }
        }
        return pinyinStr;
    }

    /**
     * 汉字转为拼音
     *
     * @param chinese
     * @return
     */
    public static String ToPinyin(String chinese) {
        String pinyinStr = "";
        char[] newChar = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < newChar.length; i++) {
            if (newChar[i] > 128) {
                try {
                    pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0];
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinStr += newChar[i];
            }
        }
        return pinyinStr;
    }


    @Test
    public void testDate(){
        int day=8;
        String[] columnKeysCooks = new String[day];
        int indexdst=0;
        for (int i=day;i>0;i--){
            columnKeysCooks[indexdst]=getNextDay(i);
            System.out.println(getNextDay(i));
            indexdst++;
        }
    }


}
