package com.track.store.dog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.track.paint.core.HttpMethod;
import com.track.paint.core.Invocation;
import com.track.paint.core.Result;
import com.track.paint.core.annotation.AutoLifeCycle;
import com.track.paint.core.annotation.Handler;
import com.track.paint.core.annotation.Service;
import com.track.paint.core.http.ResultBuilder;
import com.track.paint.core.interfaces.IService;
import com.track.paint.persistent.PersistentManager;
import com.track.store.dog.bean.Balance;
import com.track.store.dog.bean.Configure;
import com.track.store.dog.manager.BalanceManager;
import com.track.store.dog.manager.ConfigureManager;

@Service("/configure")
public class ConfigureService implements IService {

	@AutoLifeCycle
	private ConfigureManager configureManager;

	@AutoLifeCycle
	private BalanceManager balanceManager;

	@Override
	public void init() {
	}

	@Handler(value = "/update", method = HttpMethod.POST)
	public Result updateConfigure(Invocation invocation) {
		Map<String, String> data = invocation.getAttachment(Invocation.REQUEST);
		String goods = data.get("goods");
		String partner = data.get("partner");

		Configure configure = configureManager.get();
		if (goods != null) {
			configure.setGoods(goods);
		}
		if (partner != null) {
			configure.setPartners(partner);
		}
		configureManager.save(configure);

		List<String> goodsList = convert(configure.getGoods());
		List<String> partnerList = convert(configure.getPartners());

		List<Balance> allBalance = balanceManager.queryAll();
		allBalance.forEach(balance -> {
			if (!goodsList.contains(balance.getGoods())) {
				PersistentManager.instance().delete(balance);
			}
			if (!partnerList.contains(balance.getPartner())) {
				PersistentManager.instance().delete(balance);
			}
		});

		for (String goodsItem : goodsList) {
			for (String partnerItem : partnerList) {
				Balance tempBalance = new Balance();
				tempBalance.setBalance(0);
				tempBalance.setGoods(goodsItem);
				tempBalance.setPartner(partnerItem);
				if (!allBalance.contains(tempBalance)) {
					PersistentManager.instance().save(tempBalance);
				}
			}
		}

		return ResultBuilder.buildResult(0);
	}

	@Handler(value = "/get", method = HttpMethod.GET)
	public Result getConfigure(Invocation invocation) {
		Configure configure = configureManager.get();
		String goods = configure.getGoods();
		String partners = configure.getPartners();
		JSONObject resp = new JSONObject();
		JSONArray goodsArr = JSONArray.parseArray(goods);
		JSONArray partnerArr = JSONArray.parseArray(partners);
		resp.put("goods", goodsArr);
		resp.put("partner", partnerArr);
		return ResultBuilder.buildResult(0, resp);
	}

	private List<String> convert(String origin) {
		JSONArray goodsArr = JSONArray.parseArray(origin);
		List<String> goodsList;
		if (goodsArr == null || goodsArr.isEmpty()) {
			goodsList = new ArrayList<>();
		} else {
			goodsList = goodsArr.stream().map(item -> {
				return ((JSONObject) item).getString("name");
			}).collect(Collectors.toList());
		}
		return goodsList;
	}

}
