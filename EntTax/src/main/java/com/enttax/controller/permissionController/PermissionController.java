package com.enttax.controller.permissionController;

import com.enttax.model.Staff;
import com.enttax.service.permissionService.PermissService;
import com.enttax.util.constant.ConstantException;
import com.enttax.util.constant.ConstantStr;
import com.enttax.util.tools.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 *  权限模块  包括登录、注册
 * Created by lcyanxi on 17-3-13.
 */
@Controller
@RequestMapping(value = "/user")
public class PermissionController extends BaseController{
    private PermissService permissService;
    @Autowired
    public void setPermissService(PermissService permissService) {
        this.permissService = permissService;
    }

    /**
     * 登录功能
     * @param
     * @return
     */
   @RequestMapping(value = "/login")
   @ResponseBody
    public Map<String ,String> login(
           @RequestParam("sname")String sname,
           @RequestParam("spassword")String spassword,
           @RequestParam("kcode")String kcode) {

       Map<String,String> map=new HashMap<String, String>();

       if (sname==null||spassword==null){
           map.put("status",ConstantException.args_error_code);
           map.put("message",ConstantException.args_error_message);
           return map;
       }
       if (!kcode.equals(request.getSession().getAttribute("sRand"))){
           map.put("status",ConstantException.image_error_code);
           map.put("message",ConstantException.image_error_message);
           return map;
       }

        Staff staff=  permissService.login(sname,Encodes.encodeBase64(spassword));

       if (staff==null){
           map.put("status",ConstantException.no_data_code);
           map.put("message",ConstantException.no_data_message);
           return map;
       }

       map.put("status",ConstantException.sucess_code);
       map.put("message",ConstantException.sucess_message);

       session.setAttribute("staff",staff);
       session.setAttribute("sid",staff.getSid());
       return map;
    }


    /**
     * 注册功能  用户名和密码为必须字段0
     * @param staff
     * @return
     */
    @RequestMapping(value = "/register" ,method = RequestMethod.POST)
    public String register(@RequestParam("rid") String rid,
            @ModelAttribute Staff staff , Model model){
        boolean result= permissService.register(staff,rid);
        if (result){
            model.addAttribute("state", ConstantStr.str_one);
//            model.addAttribute("sid",sid);
            return "successful";
        }else {
            model.addAttribute("state",ConstantStr.str_zero);
            return "error";
        }
    }


    /**
     * 更新个人信息
     * @param staff
     * @return
     */
    @RequestMapping(value = "/updateStaffInfo",method = RequestMethod.POST)
    public String updateStaffInfo(@ModelAttribute Staff staff){
        if(staff.getSid()==null){
            return "staffInfo";
        }
        staff.setSpassword(Encodes.encodeBase64(staff.getSpassword()));
        permissService.updateStaffInfo(staff);
        return "staffInfo";
    }



    /**
     * 头像上传
     * @param x
     * @param y
     * @param h
     * @param w
     * @param imageFile
     * @return
     */
    @RequestMapping(value = "/uploadHeadImage",method = RequestMethod.POST)
    public String uploadHeadImage(
            @RequestParam(value = "x") String x,
            @RequestParam(value = "y") String y,
            @RequestParam(value = "h") String h,
            @RequestParam(value = "w") String w,
            @RequestParam(value = "imgFile") MultipartFile imageFile,
            Model model) {
        String realPath = session.getServletContext().getRealPath("/");
        try {
           String savator= FileUploadUtil.uploadHeadImage(realPath,x,y,h,w,imageFile);
           if (savator==null){
               return "index";
           }

            Staff staff=(Staff) model.asMap().get("staff");
            staff.setSavator(savator);
            permissService.updateStaffInfo(staff);
        }catch (IOException e){

        }
        return "image";
    }


    /**
     * 重置密码
     * @param password
     */
    @RequestMapping(value = "/updatepassword",method = RequestMethod.GET)
    @ResponseBody
    public  Map<String ,String >  updateToPassword(@RequestParam(value = "password") String password){
       String sid = (String) request.getSession().getAttribute("sid");
        Map<String,String> map=new HashMap<String, String>();
        if (permissService.updateToPassword(sid,Encodes.encodeBase64(password))){
            map.put("status",ConstantStr.str_one);
        }else {
            map.put("status",ConstantStr.str_zero);
        }
        map.put("status",ConstantStr.str_one);
          return map;
    }

    /**
     * 查找电话号码是否存在
     * @param phone
     */
    @RequestMapping(value = "/findphone",method = RequestMethod.GET)
    @ResponseBody
    public Map selectByPhone(@RequestParam(value = "phone") String phone){
        Map<String ,String> map=new HashMap<String, String>();
        if (permissService.selectByPhone(phone,request)){
            map.put("status",ConstantStr.str_one);
        }else {
            map.put("status",ConstantStr.str_zero);
        }
        return map;

    }


}