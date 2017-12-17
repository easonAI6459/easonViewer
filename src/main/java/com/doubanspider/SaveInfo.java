package com.doubanspider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaveInfo {
	public static final String url = "jdbc:mysql://localhost:3306/doubanspider";

	public static final String name = "com.mysql.jdbc.Driver";

	public static final String user = "root";

	public static final String password = "root";



	public Connection conn = null;

	public PreparedStatement pst = null;



	public void saveBookInfo(String sql) {

		try {

			Class.forName(name);// 指定连接类型

			conn = DriverManager.getConnection(url, user, password);// 获取连接

			pst = conn.prepareStatement(sql);// 准备执行语句

			pst.execute();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {

				this.conn.close();

				this.pst.close();

			} catch (SQLException e) {

				e.printStackTrace();

			}

		}

	}

}
