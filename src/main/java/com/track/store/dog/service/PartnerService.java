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
import com.track.paint.core.interfaces1.IService;
import com.track.store.dog.bean.Partner;
import com.track.store.dog.manager.PartnerManager;
import com.track.store.dog.util.AutoJsonHelper;
import com.track.store.dog.util.CheckUtil;

@Service("/partner")
public class PartnerService implements IService {

	@AutoLifeCycle
	private PartnerManager partnerManager;

	@Override
	public void init() {
		try {
			ResultBuilder.addErrorCode(0x3001, "合作人名字为空");
			ResultBuilder.addErrorCode(0x3002, "合作人字已经存在");
			ResultBuilder.addErrorCode(0x3003, "合作人不唯一");
		} catch (ErrorCodeExcption e) {
			e.printStackTrace();
		}
	}

	@Handler(value = "/add", method = HttpMethod.POST)
	public Result addGoods(Invocation invocation) {
		Map<String, String> data = invocation.getAttachment(Invocation.REQUEST);
		String partnerName = data.get("name");
		if (CheckUtil.checkStrIsEmpty(partnerName)) {
			return ResultBuilder.buildResult(0x3001);
		}

		List<Partner> result = partnerManager.queryByName(partnerName);
		if (!CheckUtil.checkListZero(result)) {
			return ResultBuilder.buildResult(0x3002);
		}

		partnerManager.create(partnerName);

		return ResultBuilder.buildResult(0);
	}

	@Handler(value = "/list", method = HttpMethod.GET)
	public Result listGoods(Invocation invocation) {
		List<Partner> result = partnerManager.queryAll();

		JSONObject rep = new JSONObject();

		JSONArray arr = new JSONArray();
		result.forEach(item -> {
			arr.add(AutoJsonHelper.instance().objToJson(item));
		});

		rep.put("partners", arr);

		return ResultBuilder.buildResult(0, rep);
	}

}
