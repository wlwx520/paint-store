package com.track.store.dog.service;

import java.util.List;
import java.util.Map;

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
import com.track.store.dog.bean.Balance;
import com.track.store.dog.manager.BalanceManager;
import com.track.store.dog.util.AutoJsonUtil;

@Service("/balance")
public class BalanceService implements IService {

	@AutoLifeCycle
	private BalanceManager balanceManager;

	@Override
	public void init() {
	}

	@Handler(value = "/update", method = HttpMethod.POST)
	public Result updateBalance(Invocation invocation) {
		Map<String, String> data = invocation.getAttachment(Invocation.REQUEST);
		String goods = data.get("goods");
		String partner = data.get("partner");
		double update = Double.valueOf(data.get("update"));
		balanceManager.update(goods, partner, update);
		return ResultBuilder.buildResult(0);
	}

	@Handler(value = "/query", method = HttpMethod.POST)
	public Result queryBalance(Invocation invocation) {
		Map<String, String> data = invocation.getAttachment(Invocation.REQUEST);
		String goods = data.get("goods");
		String partner = data.get("partner");
		JSONObject resp = new JSONObject();
		JSONArray arr = new JSONArray();
		List<Balance> query = balanceManager.query(goods, partner);
		query.forEach(item -> {
			JSONObject json = AutoJsonUtil.instance().objToJson(item);
			arr.add(json);
		});
		resp.put("balance", arr);
		return ResultBuilder.buildResult(0, resp);
	}

}
