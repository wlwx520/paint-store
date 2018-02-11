package com.track.store.dog.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
import com.track.paint.core.exception.SystemException;
import com.track.paint.core.http.ResultBuilder;
import com.track.paint.core.interfaces.IService;
import com.track.paint.persistent.PersistentBean;
import com.track.paint.persistent.PersistentManager;
import com.track.paint.util.AutoJsonUtil;
import com.track.store.dog.bean.Record;
import com.track.store.dog.manager.BalanceManager;
import com.track.store.dog.manager.RecordManager;
import com.track.store.dog.util.CheckUtil;
import com.track.store.dog.util.ExcelExportUtil;

@Service("/record")
public class RecordService implements IService {

	private static SimpleDateFormat DATAFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat DATAFORMAT2 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");

	@AutoLifeCycle
	private RecordManager recordManager;

	@AutoLifeCycle
	private BalanceManager balanceManager;

	@Override
	public void init() {
		try {
			ResultBuilder.addErrorCode(0x4001, "数据类型错误");
			ResultBuilder.addErrorCode(0x4002, "货物名错误");
			ResultBuilder.addErrorCode(0x4003, "合作人错误");
			ResultBuilder.addErrorCode(0x4004, "出入账类型错误");
			ResultBuilder.addErrorCode(0x4005, "记录删除错误");
			ResultBuilder.addErrorCode(0x4006, "导出无记录");
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

		if (inOrOut == null || !(inOrOut.equals("进货") || inOrOut.equals("出货"))) {
			return ResultBuilder.buildResult(0x4004);
		}

		Double univalentValue = Double.valueOf(univalent);
		Double freightValue = Double.valueOf(freight);
		Double countValue = Double.valueOf(count);
		try {
			Date parseTime = DATAFORMAT.parse(time);
			recordManager.create(parseTime.getTime(), countValue, univalentValue, freightValue, inOrOut, goods,
					partner);
		} catch (Exception e) {
			return ResultBuilder.buildResult(0x4001);
		}

		if (freightValue > 0) {
			freightValue = -freightValue;
		}
		double x = univalentValue * countValue;
		if (inOrOut.equals("出货") && x < 0) {
			x = -x;
		}
		if (inOrOut.equals("进货") && x > 0) {
			x = -x;
		}

		balanceManager.update(goods, partner, x + freightValue);
		return ResultBuilder.buildResult(0);
	}

	@Handler(value = "/query", method = HttpMethod.POST)
	public Result queryRecord(Invocation invocation) {
		Map<String, String> data = invocation.getAttachment(Invocation.REQUEST);
		String startTime = data.get("startTime");
		String endTime = data.get("endTime");
		String count = data.get("count");
		String univalent = data.get("univalent");
		String freight = data.get("freight");
		String inOrOut = data.get("inOrOut");
		String goods = data.get("goods");
		String partner = data.get("partner");
		int currrent = Integer.valueOf(data.get("current"));
		int size = Integer.valueOf(data.get("size"));

		if (univalent != null) {
			univalent = null;
		}
		if (count != null) {
			count = null;
		}
		if (freight != null) {
			freight = null;
		}

		if (goods != null && goods.equals("任意")) {
			goods = null;
		}
		if (partner != null && partner.equals("任意")) {
			partner = null;
		}
		if (inOrOut != null && inOrOut.equals("任意")) {
			inOrOut = null;
		}

		try {
			List<Record> result = recordManager.query(
					CheckUtil.checkStrIsEmpty(startTime) ? null : DATAFORMAT.parse(startTime).getTime(),
					CheckUtil.checkStrIsEmpty(endTime) ? null : DATAFORMAT.parse(endTime).getTime(),
					CheckUtil.checkStrIsEmpty(count) ? null : Double.valueOf(count),
					CheckUtil.checkStrIsEmpty(univalent) ? null : Double.valueOf(univalent),
					CheckUtil.checkStrIsEmpty(freight) ? null : Double.valueOf(freight), inOrOut, goods, partner,
					(currrent - 1) * size, size);

			List<Record> result2 = recordManager.query(
					CheckUtil.checkStrIsEmpty(startTime) ? null : DATAFORMAT.parse(startTime).getTime(),
					CheckUtil.checkStrIsEmpty(endTime) ? null : DATAFORMAT.parse(endTime).getTime(),
					CheckUtil.checkStrIsEmpty(count) ? null : Double.valueOf(count),
					CheckUtil.checkStrIsEmpty(univalent) ? null : Double.valueOf(univalent),
					CheckUtil.checkStrIsEmpty(freight) ? null : Double.valueOf(freight), inOrOut, goods, partner, 0,
					Integer.MAX_VALUE);

			JSONObject rep = new JSONObject();
			JSONArray arr = new JSONArray();
			double freightSum = 0;
			double coastSum = 0;

			if (result != null) {
				result.stream().sorted((a, b) -> {
					return (int) (a.getTime() - b.getTime());
				}).forEach(item -> {
					JSONObject json = AutoJsonUtil.instance().objToJson(item);
					json.put("time", DATAFORMAT.format(json.getLong("time")));
					json.put("id", item.cat_static_table_primary_key);
					arr.add(json);
				});

				for (Record r : result2) {
					double y = r.getFreight();
					if (y > 0) {
						y = -y;
					}
					freightSum += y;
					double x = r.getUnivalent() * r.getCount();
					if (r.getInOrOut().equals("出货") && x < 0) {
						x = -x;
					}
					if (r.getInOrOut().equals("进货") && x > 0) {
						x = -x;
					}
					coastSum += x;
				}
			}

			rep.put("records", arr);
			rep.put("freightSum", freightSum);
			rep.put("coastSum", coastSum);

			long total = recordManager.size(
					CheckUtil.checkStrIsEmpty(startTime) ? null : DATAFORMAT.parse(startTime).getTime(),
					CheckUtil.checkStrIsEmpty(endTime) ? null : DATAFORMAT.parse(endTime).getTime(),
					CheckUtil.checkStrIsEmpty(count) ? null : Double.valueOf(count),
					CheckUtil.checkStrIsEmpty(univalent) ? null : Double.valueOf(univalent),
					CheckUtil.checkStrIsEmpty(freight) ? null : Double.valueOf(freight), inOrOut, goods, partner);
			rep.put("total", total);

			return ResultBuilder.buildResult(0, rep);
		} catch (Exception e) {
			return ResultBuilder.buildResult(0x4001);
		}
	}

	@Handler(value = "/delete", method = HttpMethod.POST)
	public Result deleteRecord(Invocation invocation) {
		Map<String, String> data = invocation.getAttachment(Invocation.REQUEST);
		String id = data.get("id");
		Record template = new Record();
		template.cat_static_table_primary_key = Integer.valueOf(id);

		List<Record> query = PersistentManager.instance().query(template,
				Arrays.asList(new String[] { PersistentBean.ID }));
		assert (CheckUtil.checkListOne(query));
		Record delete = query.get(0);
		double y = delete.getFreight();
		if (y > 0) {
			y = -y;
		}
		double x = delete.getUnivalent() * delete.getCount();
		if (delete.getInOrOut().equals("出货") && x < 0) {
			x = -x;
		}
		if (delete.getInOrOut().equals("进货") && x > 0) {
			x = -x;
		}

		balanceManager.update(delete.getGoods(), delete.getPartner(), -x - y);

		if (PersistentManager.instance().delete(template)) {
			return ResultBuilder.buildResult(0);
		} else {
			return ResultBuilder.buildResult(0x4005);
		}
	}

	@Handler(value = "/export", method = HttpMethod.DOWNLOAD)
	public Result exportRecord(Invocation invocation) {
		Map<String, String> data = invocation.getAttachment(Invocation.REQUEST);
		String startTime = data.get("startTime");
		String endTime = data.get("endTime");
		String count = data.get("count");
		String univalent = data.get("univalent");
		String freight = data.get("freight");
		String inOrOut = data.get("inOrOut");
		String goods = data.get("goods");
		String partner = data.get("partner");

		if (univalent != null) {
			univalent = null;
		}
		if (count != null) {
			count = null;
		}
		if (freight != null) {
			freight = null;
		}

		if (goods != null && goods.equals("任意")) {
			goods = null;
		}
		if (partner != null && partner.equals("任意")) {
			partner = null;
		}
		if (inOrOut != null && inOrOut.equals("任意")) {
			inOrOut = null;
		}

		try {
			List<Record> result = recordManager.query(
					CheckUtil.checkStrIsEmpty(startTime) ? null : DATAFORMAT.parse(startTime).getTime(),
					CheckUtil.checkStrIsEmpty(endTime) ? null : DATAFORMAT.parse(endTime).getTime(),
					CheckUtil.checkStrIsEmpty(count) ? null : Double.valueOf(count),
					CheckUtil.checkStrIsEmpty(univalent) ? null : Double.valueOf(univalent),
					CheckUtil.checkStrIsEmpty(freight) ? null : Double.valueOf(freight), inOrOut, goods, partner, 0,
					Integer.MAX_VALUE);

			if (CheckUtil.checkListZero(result)) {
				return ResultBuilder.buildResult(0x4006);
			} else {
				ExcelExportUtil xlsx = ExcelExportUtil.createXlsx();
				result.forEach(record -> {
					xlsx.writeRecord(record);
				});

				StringBuilder name = new StringBuilder();
				if (partner == null) {
					name.append("全部伙伴");
				} else {
					name.append(partner);
				}
				name.append("-");

				if (goods == null) {
					name.append("全部货物");
				} else {
					name.append(goods);
				}
				name.append("-");

				if (inOrOut == null) {
					name.append("进出货");
				} else {
					name.append(inOrOut);
				}
				name.append("-");

				if (CheckUtil.checkStrIsEmpty(startTime)) {
					name.append("全部时间");
				} else {
					name.append(DATAFORMAT2.format(DATAFORMAT.parse(startTime).getTime()));
					name.append("至");
					name.append(DATAFORMAT2.format(DATAFORMAT.parse(endTime).getTime()));
				}
				name.append("-交易记录");
				String filePath = xlsx.toFile(name.toString());

				return ResultBuilder.buildResult(0, filePath,name.toString());
			}

		} catch (NumberFormatException | ParseException e) {
			throw new SystemException(e);
		}
	}

}
