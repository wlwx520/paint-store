package com.track.store.dog.bean;

import com.track.paint.persistent.PersistentBean;
import com.track.paint.persistent.annotation.Column;
import com.track.paint.persistent.annotation.Persistent;

@Persistent(table = "configure")
public class Configure extends PersistentBean {
	@Column
	private String partners;
	@Column
	private String goods;

	public Configure() {
	}

	public String getPartners() {
		return partners;
	}

	public void setPartners(String partners) {
		this.partners = partners;
	}

	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}

}
