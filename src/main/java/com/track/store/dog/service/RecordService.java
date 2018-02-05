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
import com.track.paint.core.exception.ErrorCodeExcption;
import com.track.paint.core.http.ResultBuilder;
import com.track.paint.core.interfaces.IService;
import com.track.store.dog.bean.Goods;
import com.track.store.dog.bean.Partner;
import com.track.store.dog.bean.Record;
import com.track.store.dog.manager.GoodsManager;
import com.track.store.dog.manager.PartnerManager;
import com.track.store.dog.manager.RecordManager;
import com.track.store.dog.util.AutoJsonHelper;

@Service("/record")
public class RecordService implements IService {

	@AutoLifeCycle
	private GoodsManager goodsManager;
	@AutoLifeCycle
	private PartnerManager partnerManager;
	@AutoLifeCycle
	private RecordManager recordManager;

	@Override
	public void init() {
		try {
			ResultBuilder.addErrorCode(0x4001, "数据类型错误");
			ResultBuilder.addErrorCode(0x4002, "货物名错误");
			ResultBuilder.addErrorCode(0x4003, "合作人错误");
			ResultBuilder.addErrorCode(0x4004, "出入账类型错误");
		} catch (ErrorCodeExcption e) {
			e.printStackTrace();
		}
	}

	@Handler(value = "/add", method = HttpMethod.POST)
	public Result addRecord(Invocation invocation) {
		Map<String, String> data = invocation.getAttachment(Invocation.REQUEST);
		String time = data.get("time");
		String count = data.get("count");
		String univalent = data.get("univalent");
		String freight = data.get("freight");
		String inOrOut = data.get("inOrOut");
		String goods = data.get("goods");
		String partner = data.get("partner");

		if (inOrOut == null || !(inOrOut.equals("in") || inOrOut.equals("out"))) {
			return ResultBuilder.buildResult(0x4004);
		}

		List<Goods> queryGoods = goodsManager.queryByName(goods);
		if (queryGoods == null || queryGoods.size() != 1) {
			return ResultBuilder.buildResult(0x4002);
		}

		List<Partner> queryPartner = partnerManager.queryByName(partner);
		if (queryPartner == null || queryPartner.size() != 1) {
			return ResultBuilder.buildResult(0x4003);
		}

		try {
			recordManager.create(Long.valueOf(time), Long.valueOf(count), Double.valueOf(univalent),
					Double.valueOf(freight), inOrOut, queryGoods.get(0), queryPartner.get(0));
		} catch (Exception e) {
			return ResultBuilder.buildResult(0x4001);
		}

		return ResultBuilder.buildResult(0);
	}

	@Handler(value = "/query", method = HttpMethod.POST)
	public Result queryRecord(Invocation invocation) {
		Map<String, String> data = invocation.getAttachment(Invocation.REQUEST);
		String startTime = data.get("start-time");
		String endTime = data.get("end-time");
		String count = data.get("count");
		String univalent = data.get("univalent");
		String freight = data.get("freight");
		String inOrOut = data.get("inOrOut");
		String goods = data.get("goods");
		String partner = data.get("partner");

		Goods tempGoods = null;
		Partner tempPartner = null;
		List<Goods> queryGoods = goodsManager.queryByName(goods);
		if (queryGoods != null && queryGoods.size() == 1) {
			tempGoods = queryGoods.get(0);
		}

		List<Partner> queryPartner = partnerManager.queryByName(partner);
		if (queryPartner != null && queryPartner.size() == 1) {
			tempPartner = queryPartner.get(0);
		}

		List<Record> result = recordManager.query(startTime == null ? null : Long.valueOf(startTime),
				endTime == null ? null : Long.valueOf(endTime), count == null ? null : Long.valueOf(count),
				univalent == null ? null : Double.valueOf(univalent), freight == null ? null : Double.valueOf(freight),
				inOrOut, tempGoods, tempPartner);

		JSONObject rep = new JSONObject();

		JSONArray arr = new JSONArray();
		result.forEach(item -> {
			arr.add(AutoJsonHelper.instance().objToJson(item));
		});

		rep.put("records", arr);

		return ResultBuilder.buildResult(0, rep);
	}

}
