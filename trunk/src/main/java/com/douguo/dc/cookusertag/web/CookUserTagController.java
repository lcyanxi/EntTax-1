package com.douguo.dc.cookusertag.web;

import com.douguo.dc.cookusertag.model.Categories;
import com.douguo.dc.cookusertag.model.CookTag;
import com.douguo.dc.cookusertag.model.CookUserTag;
import com.douguo.dc.cookusertag.model.Ingredients;
import com.douguo.dc.cookusertag.service.CookUserTagService;
import com.douguo.dc.user.utils.grid.GridPager;
import com.douguo.dc.user.utils.grid.JQGridUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cookUserTag")
public class CookUserTagController {
	
	@Autowired
	private CookUserTagService cookUserTagService;

    /**
     * 跳转至页面后请求数据
     * @param request
     * @param response
     * @param model
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/queryJson")
    public void queryJson(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException {

        request.setCharacterEncoding("utf-8");
        String rowsLimit = request.getParameter("rows"); // 取出每一页显示的行数
        String sidx = request.getParameter("sidx"); // 取出排序的项
        String sord = request.getParameter("sord"); // 取出排序方式：升序，降序

        if (rowsLimit == null) // 设置每一页显示行数的默认值
        {
            rowsLimit = "10";
        }
        JQGridUtil t = new JQGridUtil();
        int nPage = 1; // 当前显示的页数
        int total = 1; // 要显示的总的页数，初始值为1

        List<CookUserTag> list = cookUserTagService.queryAllUserTagList();

        int records = list.size();

        total = records / Integer.parseInt(rowsLimit) + 1; // 计算总的页数
        String[][] arryFun = new String[list.size()][];

        for (int i = 0; i < list.size(); i++) {
            CookUserTag fun = list.get(i);
            arryFun[i] = new String[] { String.valueOf(fun.getId()), String.valueOf(fun.getTagId()), fun.getTagName(),
                    String.valueOf(fun.getFlag()),
                    "<a href='/cookUserTag/preEdit.do?tagId=" + fun.getId() + "&component=1' title='选择标签' >选择标签</a>",
                    "<a href='/cookUserTag/preEdit.do?tagId=" + fun.getId() + "&component=2' title='选择食材' >选择食材</a>" };
        }

        // 行数据
        List<Map> rows = new ArrayList<Map>();
        for (String[] axx : arryFun) {
            Map map = new HashMap();
            map.put("id", "1");
            map.put("cell", axx);

            rows.add(map);
        }

        // rows
        GridPager<Map> gridPager = new GridPager<Map>(nPage, total, records, rows); // 将表格显示的配置初始化给GridPager
        t.toJson(gridPager, response); // 发送json数据

    }

    @RequestMapping(value = "/preList")
    public String preList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        return "/cookusertag/cook_user_tag";
    }

    /**
     * 修改页面
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/preEdit")
    public String preEdit(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        //或取指定tag
        String tagId = request.getParameter("tagId"); //标签/食材对应的ID号
        String component = request.getParameter("component"); //成分标识，1：标签，2：食材
        CookUserTag cookUserTag = cookUserTagService.getCookUserTag(tagId);

        //传输标识
        model.put("tagId",tagId);
        model.put("component",component);

        //传输tag对象
        model.put("cookUserTag", cookUserTag);

        //获取所有的Ingredients/categories
        List<Categories> categoriesList = cookUserTagService.queryAllCategoriesList();
        List<Ingredients> ingredientsList = cookUserTagService.queryAllIngredientsList();

        //传输ingredients对象
//        model.put("ingredietnsList",ingredientsList);

        //获取该Tag下已经选择的标签/食材
        List<CookTag> cookTagList = cookUserTagService.queryAllCookTag(tagId,component);


        //遍历cookTagList以获取该标签下已经选取的食材
        List cookTagChosenList = new ArrayList();
        for (CookTag cookTag : cookTagList){
            //getID : @param tagId  标识为tagID
            //getFlag : @param component 2代表食材 1代表标签
            if (String.valueOf(cookTag.getUser_tag_id()).equals(tagId) && String.valueOf(cookTag.getFlag()).equals(component)){
                if ("1".equals(component)){
                    Categories categeChosen = new Categories();
                    categeChosen.setId(Integer.parseInt(cookTag.getIngre_categ_id()));
                    //遍历所有的食材列表 获取食材名
                    for (Categories categListChosen : categoriesList){
                        String componentId = String.valueOf(cookTag.getIngre_categ_id());
                        String categoryId = String.valueOf(categListChosen.getId());
                        if (componentId.equals(categoryId)){
                            categeChosen.setName(categListChosen.getName());
                        }
                    }
                    //填入对象
                    cookTagChosenList.add(categeChosen);
                }

                if ("2".equals(component)){
                    Ingredients ingreChosen = new Ingredients();
                    ingreChosen.setId(Integer.valueOf(cookTag.getIngre_categ_id()));
                    //遍历所有的食材列表 获取食材名
                    for (Ingredients ingreListChosen : ingredientsList){
                        String componentId = String.valueOf(cookTag.getIngre_categ_id());
                        String ingredientId = String.valueOf(ingreListChosen.getId());
                        if (componentId.equals(ingredientId)){
                            ingreChosen.setName(ingreListChosen.getName());
                        }
                    }
                    //填入对象
                    cookTagChosenList.add(ingreChosen);
                }
            }
        }
        //传输cooktagList对象
        model.put("cookTagChosenList",cookTagChosenList);

        //根据数据list（所有食材、菜单）构建数据JSon树
        if ("1".equals(component)){
            try{
                //获得Categories Json树
                model.put("tree",(cookUserTagService.queryAllCategoriesListToJson()));
            }catch (Exception e){
                model.put("tree","");
                e.printStackTrace();
            }
        }
        if ("2".equals(component)){
            try{
                //获得Ingerdients Json树
                model.put("tree",(cookUserTagService.queryAllIngerdientsListToJson()));
            }catch (Exception e){
                model.put("tree","");
                e.printStackTrace();
            }
        }

        //跳转页面
        return "/cookusertag/cook_user_tag_ingredients_modify";
    }


    @RequestMapping(value = "/insert")
    public String insert(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        CookUserTag cookUserTag = new CookUserTag() ;

        Integer tagId = Integer.valueOf(request.getParameter("tagId")) ;
        String tagName = request.getParameter("tagName") ;
        Integer flag = Integer.valueOf(request.getParameter("flag")) ;

        cookUserTag.setTagId(tagId);
        cookUserTag.setTagName(tagName);
        cookUserTag.setFlag(flag);

        return "/cookusertag/cook_user_tag";
    }


    @RequestMapping(value = "/update")
    public String update(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws UnsupportedEncodingException {

        ArrayList<String> hadChonsenIdList = new ArrayList<>();//数据库里查出来的
        ArrayList<String> hadChonsenIdNewList = new ArrayList<>();

        request.setCharacterEncoding("utf-8");

        //获取已经选择的成分
        String[] newChosenId =  request.getParameterValues("category_id");
        //获取新选择的成分
        String[] oldChosenId = request.getParameterValues("category_id_chosen");

        //获取TagID
        String tagId = request.getParameter("tagId");
        //获取 标签flag/食材flag
        String component = request.getParameter("component");

        //处理不选择标签的情况，即chosenId或hadChosenId为NULL
        if (newChosenId == null){
            newChosenId = new String[0];
        }
        if (oldChosenId== null){
            oldChosenId = new String[0];
        }

        for (String ss:oldChosenId){
           hadChonsenIdNewList.add(ss);
        }

        // 获取原始数据，该标签下已选所有的数据
        List<CookTag> cookTagList = cookUserTagService.queryAllCookTag(tagId,component);

        // 把从数据库里查出来的Ingre_categ_id放在一个集合里
        for (CookTag c : cookTagList){
            hadChonsenIdList.add(c.getIngre_categ_id());//数据库里查出来的
        }

        //更新已经选的
        if (hadChonsenIdList.size() != oldChosenId.length){
            for (int i=0; i<hadChonsenIdList.size(); i++){
                boolean isContains =hadChonsenIdNewList.contains(hadChonsenIdList.get(i));
                if (!isContains){
                    cookUserTagService.deleteCookTag(tagId,hadChonsenIdList.get(i),component);
                }
            }
        }

        // 第一次插入，即查询所得为0
        if (cookTagList.size()==0){
            for (int i=0; i<newChosenId.length; i++){
                CookTag cookTag = new CookTag();
                cookTag.setUser_tag_id(tagId);
                cookTag.setIngre_categ_id(newChosenId[i]);
                cookTag.setFlag(Integer.valueOf(component));
                cookUserTagService.insertCookTag(cookTag);
            }
        }

        // 已经有数据，判断是否存在
        if (cookTagList.size()>0){
            for (int i=0; i<newChosenId.length; i++){
                if (!hadChonsenIdList.contains(newChosenId[i])){
                    CookTag cookTag = new CookTag();
                    cookTag.setUser_tag_id(tagId);
                    cookTag.setIngre_categ_id(newChosenId[i]);
                    cookTag.setFlag(Integer.valueOf(component));
                    cookUserTagService.insertCookTag(cookTag);
                }
            }
        }

        return "/cookusertag/cook_user_tag";
    }

}