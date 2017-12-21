package com.douguo.dc.serverlog.web;

import com.douguo.dc.serverlog.model.ServerLogSrctypeDict;
import com.douguo.dc.serverlog.service.ServerLogSrctypeDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("slog/srctype")
public class ServerLogSrctypeDictController {
    @Autowired
    private ServerLogSrctypeDictService serverLogSrctypeDictService;

    @RequestMapping(value = "/queryJson",method = RequestMethod.GET)
    public String queryJson(ModelMap model){

        List list=serverLogSrctypeDictService.querySrctypeList();
        model.addAttribute("data",list);
        return "/serverlog/serverLogSrctypeDictList";
    }

    @RequestMapping(value = "/addSrctype",method = RequestMethod.POST)
    @ResponseBody
    public Map addSrctype(HttpServletRequest request, @RequestParam(value = "srctype") String srctype, @RequestParam(value = "name") String name,
                          @RequestParam(value = "service") String service,
                          @RequestParam(value = "sdesc") String sdesc){

        ServerLogSrctypeDict srctypeDict=new ServerLogSrctypeDict();
        srctypeDict.setSrctype(srctype);
        srctypeDict.setSrctypeName(name);
        srctypeDict.setSdesc(sdesc);
        srctypeDict.setService(service);

        int result = serverLogSrctypeDictService.addSrctype(srctypeDict);
        String message = "";
        int status = 0;
        if (result > 0) {
            message = "添加成功";
            status = 1;
        } else {
            message = "添加失败";
        }
        Map map = new HashMap();

        map.put("message", message);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = "/updateSrctype",method = RequestMethod.POST)
    @ResponseBody
    public Map updateSrctype(HttpServletRequest request,@RequestParam(value = "id") int id, @RequestParam(value = "srctype") String srctype,
                             @RequestParam(value = "name") String name, @RequestParam(value = "service") String service,
                             @RequestParam(value = "sdesc") String sdesc){

        ServerLogSrctypeDict srctypeDict=new ServerLogSrctypeDict();
        srctypeDict.setId(id);
        srctypeDict.setSrctype(srctype);
        srctypeDict.setSrctypeName(name);
        srctypeDict.setSdesc(sdesc);
        srctypeDict.setService(service);

        int result = serverLogSrctypeDictService.updateSrctype(srctypeDict);
        String message = "";
        int status = 0;
        if (result > 0) {
            message = "添加成功";
            status = 1;
        } else {
            message = "添加失败";
        }
        Map map = new HashMap();

        map.put("message", message);
        map.put("status", status);
        return map;
    }

    @RequestMapping(value = "/deleteSrctype", method = RequestMethod.GET)
    @ResponseBody
    public Map deleteSrctype(HttpServletRequest request, @RequestParam(value = "id") int id) {
        Map map = new HashMap();

        int result = serverLogSrctypeDictService.deleteSrctype(id);
        String message = "";
        int status = 0;
        if (result > 0) {
            message = "删除成功";
            status = 1;
        } else {
            message = "删除失败";
        }
        map.put("message", message);
        map.put("status", status);
        return map;
    }

}
