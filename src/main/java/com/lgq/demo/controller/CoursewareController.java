package com.lgq.demo.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lgq.demo.common.CommonUtils;
import com.lgq.demo.common.CoursewareExcelView;
import com.lgq.demo.common.PageLimit;
import com.lgq.demo.constant.TransformStatus;
import com.lgq.demo.model.Courseware;
import com.lgq.demo.service.CoursewareService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by liguoqing on 2016/1/11.
 */
@Controller
@RequestMapping(value = "/controller/courseware")
public class CoursewareController {

    @Autowired
    private CoursewareService coursewareService;


    @RequestMapping(value = "/search")
    public String list(@RequestParam(value = "coursewareCodeOrName", required = false, defaultValue = "") String coursewareCodeOrName,
                       @RequestParam(value = "fileType", required = false, defaultValue = "") String fileType,
                       @RequestParam(value = "deviceType", required = false, defaultValue = "") String deviceType,
                       @RequestParam(value = "open", required = false, defaultValue = "") String open,
                       @RequestParam(value = "transformStatus", required = false, defaultValue = "") String transformStatus,
                       @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                       @RequestParam(value = "collapse", required = false, defaultValue = "hide") String collapse,
                       ModelMap modelMap) {
        PageLimit<Courseware> pageLimit = new PageLimit<Courseware>();
        pageLimit.setPageNo(pageNo);
        pageLimit.setPageSize(pageSize);

        Map<String,String> conditionMap = Maps.newHashMap();
        conditionMap.put("coursewareCodeOrName",coursewareCodeOrName.trim());
        conditionMap.put("fileType",fileType);
        conditionMap.put("deviceType",deviceType);
        conditionMap.put("open",open);
        conditionMap.put("transformStatus",transformStatus);
        pageLimit = coursewareService.get(conditionMap, pageLimit);

        modelMap.put("pageLimit", pageLimit);
        modelMap.put("pageFirst", pageLimit.getFirst());
        modelMap.put("coursewares", pageLimit.getRows());
        modelMap.put("coursewareCodeOrName", coursewareCodeOrName);
        modelMap.put("fileType", fileType);
        modelMap.put("deviceType", deviceType);
        modelMap.put("open", open);
        modelMap.put("collapse", collapse);
        return "/courseware/list";
    }

    @RequestMapping(value = "/edit")
    public String edit(@RequestParam(value = "coursewareId", required = true, defaultValue = "") String coursewareId,
                       ModelMap modelMap) {
        Courseware courseware = coursewareService.get(coursewareId);
        modelMap.put("courseware", courseware);
        return "/courseware/form";
    }

    /**
     * 检查课件代码是否唯一
     * @param coursewareCode
     * @return
     */
    @RequestMapping(value = "/checkDuplicate")
    public @ResponseBody
    String checkDuplicate(@RequestParam(value = "coursewareCode", required = false, defaultValue = "") String coursewareCode) {
        boolean isDuplicate = coursewareService.checkDuplicate(coursewareCode);
        String msg = "";
        if(isDuplicate) {
            msg = "ERROR";
        } else {
            msg= "SUCCESS";
        }
        return msg;
    }


    @ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(@RequestParam(value = "coursewareId", required = false, defaultValue = "") String coursewareId) {
        String msg = coursewareService.delete(coursewareId);
        return msg;
    }

    /**
     * 批量删除对象
     * @param coursewareIds
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/batchDelete")
    public String batchDelete(@RequestParam(value = "coursewareIds", required = false, defaultValue = "") String coursewareIds) {
        String[] idsArray = StringUtils.split(coursewareIds, ",");
        String msg = coursewareService.delete(Lists.newArrayList(idsArray));
        return msg;
    }


    @RequestMapping(value = "/saveOrUpdate")
    public String saveOrUpdate(@RequestParam(value = "coursewareId", required = true, defaultValue = "") String coursewareId,
                               @RequestParam(value = "coursewareCode", required = true, defaultValue = "") String coursewareCode,
                               @RequestParam(value = "coursewareName", required = true, defaultValue = "") String coursewareName,
                               @RequestParam(value = "fileType", required = true, defaultValue = "") String fileType,
                               @RequestParam(value = "duration", required = true, defaultValue = "") int duration,
                               @RequestParam(value = "open", required = true, defaultValue = "") boolean open,
                               @RequestParam(value = "deviceType", required = true, defaultValue = "") String deviceType
                              ) {
        if(StringUtils.isEmpty(coursewareId)) {
            Courseware courseware = new Courseware();
            courseware.setId(CommonUtils.getUUID());
            courseware.setCode(coursewareCode);
            courseware.setName(coursewareName);
            courseware.setFileType(fileType);
            courseware.setDuration(duration);
            courseware.setOpen(open);
            courseware.setDeviceType(deviceType);
            courseware.setTransformStatus(TransformStatus.DOING);
            courseware.setCreateTime(CommonUtils.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss"));
            coursewareService.save(courseware);
        } else {
            Courseware courseware = coursewareService.get(coursewareId);
            courseware.setCode(coursewareCode);
            courseware.setName(coursewareName);
            courseware.setDuration(duration);
            courseware.setOpen(open);
            courseware.setDeviceType(deviceType);
            courseware.setUpdateTime(CommonUtils.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss"));
            coursewareService.update(courseware);
        }
        return "redirect:/controller/courseware/search";
    }


    /**
     * 权限设置公有或私有
     * 可批量
     * @param coursewareIds
     * @return
     */
    @RequestMapping(value = "/batchOpen")
    public String batchOpen(@RequestParam(value = "coursewareIds", required = false, defaultValue = "") String coursewareIds,
                            @RequestParam(value = "open", required = false, defaultValue = "") boolean open) {
        String[] idsArray = StringUtils.split(coursewareIds, ",");
        List<Courseware> coursewares = coursewareService.get(Lists.newArrayList(idsArray));
        for(Courseware courseware : coursewares) {
            courseware.setOpen(open);
            courseware.setUpdateTime(CommonUtils.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss"));
            coursewareService.update(courseware);
        }
        return "redirect:/controller/courseware/search";
    }


    /**
     * 导出excel，可批量
     * @param coursewareIds
     * @return
     */
    @RequestMapping(value = "/export")
    public ModelAndView export(@RequestParam(value = "coursewareIds", required = false, defaultValue = "") String coursewareIds,
                               @RequestParam(value = "coursewareCodeOrName", required = false, defaultValue = "") String coursewareCodeOrName,
                               @RequestParam(value = "fileType", required = false, defaultValue = "") String fileType,
                               @RequestParam(value = "deviceType", required = false, defaultValue = "") String deviceType,
                               @RequestParam(value = "isOpened", required = false, defaultValue = "") String isOpened,
                               ModelMap model) {
        List<Courseware> coursewares = Lists.newArrayList();
        if(StringUtils.isEmpty(coursewareIds)) {
            Map conditionMap = new HashMap();
            conditionMap.put("coursewareCodeOrName",coursewareCodeOrName);
            conditionMap.put("fileType",fileType);
            conditionMap.put("deviceType", deviceType);
            conditionMap.put("isOpened", isOpened);
            PageLimit<Courseware> pageLimit = coursewareService.get(conditionMap,new PageLimit<Courseware>(Integer.MAX_VALUE));
            coursewares = pageLimit.getRows();
        } else {
            List<String> ids = Lists.newArrayList(StringUtils.split(coursewareIds, ","));
            coursewares = coursewareService.get(ids);
        }

        // excel导出数据
        CoursewareExcelView coursewareExcelView = new CoursewareExcelView();
        String[] keys = {"code","name","fileType","duration","pcDeviceType","mobileDeviceType","isOpen","transformStatus"};
        String[] titles = {"课件编号","课件名称","课件类型","时长(分钟)","pc播放","移动播放","是否共享","转换状态"};
        String fileName = "课件信息";
        model.put("keys", keys);
        model.put("titles", titles);
        model.put("fileName", fileName);
        model.put("datas", coursewares);
        return new ModelAndView(coursewareExcelView, model);

    }
}
