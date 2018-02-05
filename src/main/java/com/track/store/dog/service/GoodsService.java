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
import com.track.store.dog.manager.GoodsManager;
import com.track.store.dog.util.AutoJsonHelper;
import com.track.store.dog.util.CheckUtil;

@Service("/goods")

public class GoodsService implements IService {
	@AutoLifeCycle
	private GoodsManager goodsManager;

	@Override
	public void init() {
		try {
			ResultBuilder.addErrorCode(0x2001, "货物名字为空");
			ResultBuilder.addErrorCode(0x2002, "货物名字已经存在");
			ResultBuilder.addErrorCode(0x2003, "货物名不唯一");
		} catch (ErrorCodeExcption e) {
			e.printStackTrace();
		}
	}

	@Handler(value = "/add", method = HttpMethod.POST)
	public Result addGoods(Invocation invocation) {
		Map<String, String> data = invocation.getAttachment(Invocation.REQUEST);
		String goodsName = data.get("name");

		if (CheckUtil.checkStrIsEmpty(goodsName)) {
			return ResultBuilder.buildResult(0x2001);
		}

		List<Goods> result = goodsManager.queryByName(goodsName);

		if (!CheckUtil.checkListZero(result)) {
			return ResultBuilder.buildResult(0x2002);
		}

		goodsManager.create(goodsName);

		return ResultBuilder.buildResult(0);
	}

	@Handler(value = "/list", method = HttpMethod.GET)
	public Result listGoods(Invocation invocation) {
		List<Goods> result = goodsManager.queryAll();

		JSONObject rep = new JSONObject();

		JSONArray arr = new JSONArray();
		result.forEach(item -> {
			arr.add(AutoJsonHelper.instance().objToJson(item));
		});

		rep.put("goods", arr);

		return ResultBuilder.buildResult(0, rep);
	}

}
