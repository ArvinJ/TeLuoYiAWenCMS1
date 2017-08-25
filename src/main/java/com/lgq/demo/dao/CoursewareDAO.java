package com.lgq.demo.dao;

import com.lgq.demo.common.PageLimit;
import com.lgq.demo.model.Courseware;

import java.util.List;
import java.util.Map;

/**
 * Created by liguoqing on 2016/1/11.
 */
public interface CoursewareDAO {

    public PageLimit<Courseware> get(Map<String,String> conditionMap,PageLimit<Courseware> pageLimit);

    public Courseware get(String id);

    public List<Courseware> get(List<String> ids);

    public String update(Courseware courseware);

    public String save(Courseware courseware);

    public String delete(String id);

    public String delete(List<String> ids);

    public List<Courseware> getByCode(String code);

}
