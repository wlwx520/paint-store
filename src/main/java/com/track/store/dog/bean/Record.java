package com.track.store.dog.bean;

import com.track.paint.persistent.PersistentBean;
import com.track.paint.persistent.annotation.Column;
import com.track.paint.persistent.annotation.Persistent;
import com.track.store.dog.util.AutoJsonHelper.AutoJsonObject;

@Persistent(table = "record")
public class Record extends PersistentBean {
	@Column
	private long time;
	@Column
	@AutoJsonObject
	private Goods goods;
	@Column
	@AutoJsonObject
	private Partner partner;
	@Column
	private long count;
	@Column
	private double univalent;
	@Column
	private String inOrOut;
	@Column
	private double freight;

	public Record() {
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public double getUnivalent() {
		return univalent;
	}

	public void setUnivalent(double univalent) {
		this.univalent = univalent;
	}

	public String getInOrOut() {
		return inOrOut;
	}

	public void setInOrOut(String inOrOut) {
		this.inOrOut = inOrOut;
	}

	public double getFreight() {
		return freight;
	}

	public void setFreight(double freight) {
		this.freight = freight;
	}

}
