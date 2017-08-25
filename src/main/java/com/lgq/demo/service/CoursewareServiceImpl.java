package com.lgq.demo.service;

import com.lgq.demo.common.PageLimit;
import com.lgq.demo.dao.CoursewareDAO;
import com.lgq.demo.model.Courseware;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by liguoqing on 2016/1/11.
 */
@Service("coursewareService")
public class CoursewareServiceImpl implements CoursewareService {

    @Autowired
    private CoursewareDAO coursewareDAO;

    public PageLimit<Courseware> get(Map<String, String> conditionMap, PageLimit<Courseware> pageLimit) {
        return coursewareDAO.get(conditionMap, pageLimit);
    }

    public Courseware get(String id) {
        return coursewareDAO.get(id);
    }

    public List<Courseware> get(List<String> ids) {
        return coursewareDAO.get(ids);
    }

    public String update(Courseware courseware) {
        return coursewareDAO.update(courseware);
    }

    public String save(Courseware courseware) {
        return coursewareDAO.save(courseware);
    }

    public String delete(String id) {
        return coursewareDAO.delete(id);
    }

    public String delete(List<String> ids) {
        return coursewareDAO.delete(ids);
    }

    public boolean checkDuplicate(String code) {
        List<Courseware> coursewares = coursewareDAO.getByCode(code);
        if(CollectionUtils.isNotEmpty(coursewares)) {
            return true;
        }
        return false;
    }

}
