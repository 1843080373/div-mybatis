package com.batis.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.batis.bean.MybatisCfg;
import com.batis.bean.MybatisDataSource;

/**
 * 根据配置文件，管理连接对象
 * 
 * @author 紫马
 *
 */
public class DBMananger {

	private static MybatisDataSource conf = null;
	
	public static Connection getConnection(MybatisCfg mybatisCfg) {
		try {
			conf=mybatisCfg.getEnvironments().get(mybatisCfg.getDefaultEnv()).getMybatisDataSource();
			Class.forName(conf.getDriver());
			return DriverManager.getConnection(conf.getUrl(), conf.getUsername(), conf.getPassword());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void closeConnection(Connection conn, PreparedStatement ps) {
		try {
			if (ps != null) {
				if (conn != null) {
					conn.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static MybatisDataSource getConfiguration() {
		return conf;
	}
}
