package com.me.pulcer.entity;

import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="test_data_table")
public class Test {

	@Column(name="Name")
	public String name;
}
