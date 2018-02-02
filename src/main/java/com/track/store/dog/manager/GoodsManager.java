package com.track.store.dog.manager;

import java.util.List;

import com.track.paint.persistent.PersistentManager;
import com.track.store.dog.bean.Goods;

public class GoodsManager {
	public List<Goods> queryByName(String goodsName) {
		Goods temple = new Goods();
		temple.setName(goodsName);
		List<Goods> result = PersistentManager.instance().query(temple, "name");
		return result;
	}

	public void create(String goodsName) {
		Goods temple = new Goods();
		temple.setName(goodsName);
		PersistentManager.instance().save(temple);
	}

	public List<Goods> queryAll() {
		Goods temple = new Goods();
		List<Goods> result = PersistentManager.instance().query(temple);
		return result;
	}

	public boolean delete(Goods goods) {
		return PersistentManager.instance().delete(goods);
	}

	public void update(Goods goods) {
		PersistentManager.instance().save(goods);
	}
}
