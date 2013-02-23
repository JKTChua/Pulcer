package com.me.pulcer.entity;

import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="transaction")
public class Transaction {

	/*@Column(autoincrement=true)
	public long tId;*/
	
	public static final String TABLE_NAME="transaction";
	
	@Column(primary=true,name="tran_date")
	public String tDate;
	
	@Column(primary=true,name="reminder_id")
	public long reminderId;
	
	@Column(name="user_id")
	public long userId;
	
	@Column(name="status")
	public int status;
	
	@Column(name="lat")
	public long lat;
	
	@Column(name="lng")
	public long lng;
	
	
	
	
}
