package com.douguo.dc.cookusertag.service;

import com.douguo.dc.cookusertag.dao.CookUserTagDao;
import com.douguo.dc.cookusertag.model.Categories;
import com.douguo.dc.cookusertag.model.CookTag;
import com.douguo.dc.cookusertag.model.CookUserTag;
import com.douguo.dc.cookusertag.model.Ingredients;
import com.douguo.dc.util.MyJSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository("cookUserTagService")
public class CookUserTagService {

    @Autowired
    private CookUserTagDao cookUserTagDao;

    /**
     * 获取所有用户标签
     */
    public List<CookUserTag> queryAllUserTagList() {
        return cookUserTagDao.queryAllUserTagList() ;
    }

    /**
     * 获取所有的菜谱标签
     * @return
     */
    public List<Categories> queryAllCategoriesList() {
        return cookUserTagDao.queryAllCategoriesList() ;
    }

    /**
     * 获取所有的菜谱食材
     * @return
     */
    public List<Ingredients> queryAllIngredientsList() {
        return cookUserTagDao.queryAllIngredientsList() ;
    }

    /**
     * 获取所有Categories食材名构建json树
     * @return
     * @throws Exception
     */
    public List queryAllCategoriesListToJson() throws Exception{
        List<Map<String,Object>> list= cookUserTagDao.queryAllDgCategories();
        return   buildJson(list);
    }

    /**
     * 获取所有Ingerdients菜谱食材构建json树
     * @return
     */
    public List queryAllIngerdientsListToJson() throws  Exception{
        List<Map<String,Object>> list= cookUserTagDao.queryAllDgIngredients();
        return  buildJson(list);

    }

    /**
     * 根据自增唯一ID获取某个tag
     * @param id
     * @return
     */
    public CookUserTag getCookUserTag(String id){
        List<CookUserTag> list = cookUserTagDao.getCookUserTag(id);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 获取所有的tag_id下所有的食材ID以及标签ID
     * @return
     */
    public List<CookTag> queryAllCookTag(String id,String flag) {
        return cookUserTagDao.queryAllCookTag(id,flag);
    }

    /**
     * 向标签-食材、标签表插入
     * @param cookTag
     * @return
     */
    public boolean insertCookTag(CookTag cookTag) {
        return cookUserTagDao.insertCookTag(cookTag);
    }

    /**
     * 向标签-食材、标签表插入
     * @param cookTag
     * @return
     */
    public boolean updateCookTag(CookTag cookTag) {
        return cookUserTagDao.insertCookTag(cookTag);
    }

    /**
     * 删除标签
     * @return
     */
    public boolean deleteCookTag(String id,String ingre_categ_id,String flag) {
        return cookUserTagDao.deleteCookTag(id,ingre_categ_id,flag);
    }


    /**
     * 构建json树
     * @param list
     * @return
     * @throws Exception
     */
    public List buildJson(List<Map<String,Object>> list) throws Exception{
        List<MyJSONObject>  oneList=new ArrayList<>();
        List<MyJSONObject>  twoList=new ArrayList<>();
        List<MyJSONObject> threeList=new ArrayList<>();
        List<MyJSONObject>  fourList=new ArrayList<>();

        for (Map<String,Object> map:list){
            String path=(String)map.get("path");
            String[] strArray  = path.split(","); //拆分字符为"," ,然后把结果交给数组strArray

            if (strArray.length==1){
                MyJSONObject oneMap=new MyJSONObject();
                oneMap.put("id",(int) map.get("id")+"");
                oneMap.put("name",(String) map.get("name"));
                oneMap.put("parentid", (int)map.get("parentid")+"");
                oneMap.put("flag",(boolean) map.get("flag")+"");
                oneMap.put("synonum","[]");
                oneList.add(oneMap);
            }if(strArray.length==2){
                for (int i=strArray.length-1;i<strArray.length;i++){
                    for (Map<String,Object> map1:list){
                        if (strArray[i].equals((int)map1.get("id")+"")) {
                            MyJSONObject twoMap=new MyJSONObject();
                            twoMap.put("parent",strArray[i]);
                            twoMap.put("id",(int) map.get("id")+"");
                            twoMap.put("name",(String) map.get("name"));
                            twoMap.put("parentid", (int)map.get("parentid")+"");
                            twoMap.put("flag",(boolean) map.get("flag")+"");
                            twoMap.put("synonum","[]");
                            twoList.add(twoMap);
                        }
                    }
                }
            }  if (strArray.length==3){
                for (int i=strArray.length-1;i<strArray.length;i++){
                    for (Map<String,Object> map1:list){
                        if (strArray[i].equals((int)map1.get("id")+"")) {
                            MyJSONObject threeMap=new MyJSONObject();
                            threeMap.put("parent",strArray[i]);
                            threeMap.put("id",(int) map.get("id")+"");
                            threeMap.put("name",(String) map.get("name"));
                            threeMap.put("parentid", (int)map.get("parentid")+"");
                            threeMap.put("flag",(boolean) map.get("flag")+"");
                            threeMap.put("synonum","[]");
                            threeList.add(threeMap);
                        }
                    }
                }
            }if(strArray.length==4){
                for (int i=strArray.length-1;i<strArray.length;i++){
                    for (Map<String,Object> map1:list){
                        if (strArray[i].equals((int)map1.get("id")+"")) {
                            MyJSONObject fourMap=new MyJSONObject();
                            fourMap.put("parent",strArray[i]);
                            fourMap.put("id",(int) map.get("id")+"");
                            fourMap.put("name",(String) map.get("name"));
                            fourMap.put("parentid", (int)map.get("parentid")+"");
                            fourMap.put("flag",(boolean) map.get("flag")+"");
                            fourMap.put("synonum","[]");
                            fourList.add(fourMap);
                        }
                    }
                }
            }
        }

        List   newthreeList=toJsons(threeList,fourList);
        List   newtwoList=toJsons(twoList,newthreeList);
        List   newoneList=toJsons(oneList,newtwoList);

        return newoneList;
    }

    /**
     * 父类与子类的关系
     * @param parentList
     * @param childList
     * @return
     */
    private List   toJsons(List<MyJSONObject> parentList, List<MyJSONObject> childList) throws Exception{
        for (MyJSONObject parentMap:parentList){
            List list=new ArrayList();
            for (MyJSONObject childMap:childList){
                if (parentMap.getKey("id").equals(childMap.getKey("parent"))){
                    list.add(childMap);
                }
            }
            if (!list.isEmpty()){
                parentMap.put("child",list);
            }
        }
        return  parentList;
    }
}