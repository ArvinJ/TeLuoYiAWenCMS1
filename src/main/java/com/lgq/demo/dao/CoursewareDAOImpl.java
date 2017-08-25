package com.lgq.demo.dao;

import com.google.common.collect.Lists;
import com.lgq.demo.common.BaseDAO;
import com.lgq.demo.common.PageLimit;
import com.lgq.demo.dao.CoursewareDAO;
import com.lgq.demo.model.Courseware;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.deser.ValueInstantiators;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by liguoqing on 2016/1/11.
 */
@Repository("coursewareDAO")
public class CoursewareDAOImpl extends BaseDAO implements CoursewareDAO {

	public PageLimit<Courseware> get(Map<String, String> conditionMap, PageLimit<Courseware> pageLimit) {
		String coursewareCodeOrName = conditionMap.get("coursewareCodeOrName");
		String fileType = conditionMap.get("fileType");
		String deviceType = conditionMap.get("deviceType");
		String open = conditionMap.get("open");
		String transformStatus = conditionMap.get("transformStatus");
		StringBuilder sql = new StringBuilder(
				"select id,code,name,duration,device_type,file_type,open,transform_status,create_time,update_time ");
		sql.append(" from t_cms_coursewares ");
		sql.append(" where 1=1 ");
		List<Object> params = Lists.newArrayList();
		if (StringUtils.isNotEmpty(coursewareCodeOrName)) {
			sql.append(" and ( code like ? or name like ? ) ");
			params.add("%" + coursewareCodeOrName + "%");
			params.add("%" + coursewareCodeOrName + "%");
		}
		if (StringUtils.isNotEmpty(fileType)) {
			sql.append(" and file_type = ? ");
			params.add(fileType);
		}
		if (StringUtils.isNotEmpty(deviceType)) {
			sql.append(" and device_type like ? ");
			params.add("%" + deviceType + "%");
		}
		if (StringUtils.isNotEmpty(open)) {
			sql.append(" and open = ? ");
			params.add(open);
		}
		if (StringUtils.isNotEmpty(transformStatus)) {
			sql.append(" and transform_status = ? ");
			params.add(transformStatus);
		}
		List<Courseware> coursewaresForCondition = getJdbcTemplate().query(sql.toString(), params.toArray(),
				new Courseware());
		sql.append(" order by create_time desc ");
		sql.append(" limit ?,?");
		params.add(pageLimit.getFirst() - 1);
		params.add(pageLimit.getPageSize());
		List<Courseware> coursewares = getJdbcTemplate().query(sql.toString(), params.toArray(), new Courseware());
		pageLimit.setRows(coursewares);
		pageLimit.setTotal(coursewaresForCondition.size());
		return pageLimit;
	}

	public Courseware get(String id) {
		StringBuilder sql = new StringBuilder(
				"select id,code,name,duration,device_type,file_type,open,transform_status,create_time,update_time from t_cms_coursewares where id = ?");
		List<Courseware> coursewares = getJdbcTemplate().query(sql.toString(), new Courseware(), id);
		if (CollectionUtils.isEmpty(coursewares)) {
			return null;
		} else {
			return coursewares.get(0);
		}
	}

	public List<Courseware> get(List<String> ids) {
		String SELECT_SQL = "select id,code,name,duration,device_type,file_type,open,transform_status,create_time,update_time from t_cms_coursewares ";
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", ids);
		List<Courseware> coursewares = namedParameterJdbcTemplate.query(SELECT_SQL + "where id in (:ids)", parameters,
				new Courseware());
		return coursewares;
	}

	public String update(final Courseware courseware) {
		StringBuilder sql = new StringBuilder(
				"update t_cms_coursewares set code=?,name=?,duration=?,device_type=?,file_type=?,open=?,transform_status=?,create_time=?,update_time=?");
		sql.append(" where id = ?");
		int status = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				int i = 0;
				ps.setString(++i, courseware.getCode());
				ps.setString(++i, courseware.getName());
				ps.setInt(++i, courseware.getDuration());
				ps.setString(++i, courseware.getDeviceType());
				ps.setString(++i, courseware.getFileType());
				ps.setBoolean(++i, courseware.isOpen());
				ps.setString(++i, courseware.getTransformStatus());
				ps.setString(++i, courseware.getCreateTime());
				ps.setString(++i, courseware.getUpdateTime());
				ps.setString(++i, courseware.getId());
			}
		});
		if (status > 0) {
			return courseware.getId();
		} else {
			return "FAILED";
		}
	}

	public String save(final Courseware courseware) {
		StringBuilder sql = new StringBuilder(
				"insert into t_cms_coursewares (id,code,name,duration,device_type,file_type,open,transform_status,create_time,update_time) ");
		sql.append(" values (?,?,?,?,?,?,?,?,?,?) ");
		int status = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				int i = 0;
				ps.setString(++i, courseware.getId());
				ps.setString(++i, courseware.getCode());
				ps.setString(++i, courseware.getName());
				ps.setInt(++i, courseware.getDuration());
				ps.setString(++i, courseware.getDeviceType());
				ps.setString(++i, courseware.getFileType());
				ps.setBoolean(++i, courseware.isOpen());
				ps.setString(++i, courseware.getTransformStatus());
				ps.setString(++i, courseware.getCreateTime());
				ps.setString(++i, courseware.getUpdateTime());
			}
		});
		if (status > 0) {
			return courseware.getId();
		} else {
			return "FAILED";
		}
	}

	public String delete(String id) {
		String sql = "delete from t_cms_coursewares where id = ?";
		int status = getJdbcTemplate().update(sql, id);
		if (status > 0) {
			return "SUCCESS";
		} else {
			return "FAILED";
		}
	}

	public String delete(final List<String> ids) {
		String sql = "delete from t_cms_coursewares where id = ?";
		int[] deleteCounts = getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, ids.get(i));
			}

			public int getBatchSize() {
				return ids.size();
			}
		});
		if (deleteCounts.length > 0) {
			return "SUCCESS";
		} else {
			return "FAILED";
		}
	}

	public List<Courseware> getByCode(String code) {
		StringBuilder sql = new StringBuilder(
				"select id,code,name,duration,device_type,file_type,open,transform_status,create_time,update_time from t_cms_coursewares ");
		sql.append(" where code = ?");
		List<Courseware> coursewares = getJdbcTemplate().query(sql.toString(), new Courseware(), code);
		return coursewares;
	}

}
