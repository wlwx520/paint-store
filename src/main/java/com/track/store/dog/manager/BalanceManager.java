package com.track.store.dog.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.track.paint.persistent.PersistentManager;
import com.track.store.dog.bean.Balance;
import com.track.store.dog.util.CheckUtil;

public class BalanceManager {
	public void update(String goods, String partner, double update) {
		Balance template = new Balance();
		template.setGoods(goods);
		template.setPartner(partner);
		List<Balance> query = PersistentManager.instance().query(template,
				Arrays.asList(new String[] { "goods", "partner" }));
		if (CheckUtil.checkListOne(query)) {
			Balance balance = query.get(0);
			balance.setBalance(balance.getBalance() + update);
			PersistentManager.instance().save(balance);
		}
	}

	public void createBalance(String goods, String partner) {
		Balance balance = new Balance();
		balance.setGoods(goods);
		balance.setPartner(partner);
		balance.setBalance(0);
		PersistentManager.instance().save(balance);
	}

	public List<Balance> queryAll() {
		return PersistentManager.instance().query(new Balance(), null);
	}

	public List<Balance> query(String goods, String partner) {
		ArrayList<String> condition = new ArrayList<>();
		if (goods != null && goods.equals("任意")) {
			goods = null;
		} else {
			condition.add("goods");
		}
		if (partner != null && partner.equals("任意")) {
			partner = null;
		} else {
			condition.add("partner");
		}
		Balance balance = new Balance();
		balance.setGoods(goods);
		balance.setPartner(partner);
		return PersistentManager.instance().query(balance, condition);
	}
}
