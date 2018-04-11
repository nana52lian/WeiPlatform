package com.yidatec.weixin.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.yidatec.weixin.entity.RegionEntity;

public class RegionDao extends DataBase {

	public List<RegionEntity> getRegion(int parentId, int type) throws Exception {		
		String sql = "select * from " + getTableName("region") + " where parent_id = ? and region_type = ?";		
		@SuppressWarnings("unchecked")
		List<RegionEntity> regions = jdbcTemplate.query(sql, new Object[]{ parentId, type }, new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				RegionEntity regionEntity = new RegionEntity();
				regionEntity.setRegion_id(rs.getInt("region_id"));
				regionEntity.setParent_id(rs.getInt("parent_id"));
				regionEntity.setRegion_name(rs.getString("region_name"));
				regionEntity.setRegion_type(rs.getInt("region_type"));
				regionEntity.setAgency_id(rs.getInt("agency_id"));
				return regionEntity;
			}
		});		
		return regions;
	}

}
