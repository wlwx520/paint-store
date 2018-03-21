package com.track.store.dog.bean;

import com.track.paint.persistent.PersistentBean;
import com.track.paint.persistent.annotation.Column;
import com.track.paint.persistent.annotation.Persistent;

@Persistent(table = "record")
public class Record extends PersistentBean {
	@Column
	private long time;
	@Column
	private String goods;
	@Column
	private String partner;
	@Column
	private double count;
	@Column
	private double univalent;
	@Column
	private String inOrOut;
	@Column
	private double freight;
	@Column
	private double other;
	@Column
	private String otherInfo;

	public Record() {
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
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

	public double getOther() {
		return other;
	}

	public void setOther(double other) {
		this.other = other;
	}

	public String getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}

	@Override
	public String toString() {
		return "Record [time=" + time + ", goods=" + goods + ", partner=" + partner + ", count=" + count
				+ ", univalent=" + univalent + ", inOrOut=" + inOrOut + ", freight=" + freight + ", other=" + other
				+ ", otherInfo=" + otherInfo + "]";
	}

}
