package com.track.store.dog.service;

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
import com.track.store.dog.bean.Configure;
import com.track.store.dog.manager.ConfigureManager;

@Service("/configure")
public class ConfigureService implements IService {

	@AutoLifeCycle
	private ConfigureManager configureManager;

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
	

}
