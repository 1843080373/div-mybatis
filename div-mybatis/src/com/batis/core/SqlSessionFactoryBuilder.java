package com.batis.core;

import java.io.InputStream;

public class SqlSessionFactoryBuilder {

	public SqlSessionFactory build(InputStream inputStream) {
		return SqlSessionFactory.build(inputStream);
	}
}
