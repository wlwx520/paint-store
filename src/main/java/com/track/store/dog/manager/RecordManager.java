package com.track.store.dog.manager;

import java.util.ArrayList;
import java.util.List;

import com.track.paint.persistent.PersistentManager;
import com.track.store.dog.bean.Record;

public class RecordManager {
	public void create(Long time, Double count, Double univalent, Double freight, String inOrOut, String goods,
			String partner,Double other,String otherInfo) {
		Record record = new Record();
		record.setCount(Double.valueOf(count));
		record.setTime(Long.valueOf(time));
		record.setInOrOut(inOrOut);
		record.setUnivalent(Double.valueOf(univalent));
		record.setFreight(Double.valueOf(freight));
		record.setGoods(goods);
		record.setPartner(partner);
		record.setOther(other);
		record.setOtherInfo(otherInfo);
		PersistentManager.instance().save(record);
	}

	public List<Record> query(Long startTime, Long endTime, Double count, Double univalent, Double freight,
			String inOrOut, String goods, String partner, int offset, int limit) {
		Record template = new Record();
		List<String> condition = new ArrayList<>();

		if (count != null) {
			template.setCount(count);
			condition.add("count");
		}

		if (univalent != null) {
			template.setUnivalent(univalent);
			condition.add("univalent");
		}

		if (freight != null) {
			template.setFreight(freight);
			condition.add("freight");
		}

		if (inOrOut != null) {
			template.setInOrOut(inOrOut);
			condition.add("inOrOut");
		}

		if (goods != null) {
			template.setGoods(goods);
			condition.add("goods");
		}

		if (partner != null) {
			template.setPartner(partner);
			condition.add("partner");
		}

		// limit 10 offset 0

		StringBuilder extCondition = new StringBuilder();
		if (startTime != null && endTime != null) {
			extCondition.append(" time >= " + startTime + " and time <= " + endTime);
		}

		return PersistentManager.instance().queryExtPage(template, extCondition.toString(),
				condition.isEmpty() ? null : condition, offset, limit);
	}

	public long size(Long startTime, Long endTime, Double count, Double univalent, Double freight,
			String inOrOut, String goods, String partner) {
		Record template = new Record();
		List<String> condition = new ArrayList<>();

		if (count != null) {
			template.setCount(count);
			condition.add("count");
		}

		if (univalent != null) {
			template.setUnivalent(univalent);
			condition.add("univalent");
		}

		if (freight != null) {
			template.setFreight(freight);
			condition.add("freight");
		}

		if (inOrOut != null) {
			template.setInOrOut(inOrOut);
			condition.add("inOrOut");
		}

		if (goods != null) {
			template.setGoods(goods);
			condition.add("goods");
		}

		if (partner != null) {
			template.setPartner(partner);
			condition.add("partner");
		}

		// limit 10 offset 0

		StringBuilder extCondition = new StringBuilder();
		if (startTime != null && endTime != null) {
			extCondition.append(" time >= " + startTime + " and time <= " + endTime);
		}

		return PersistentManager.instance().queryCountExt(template, extCondition.toString(),
				condition.isEmpty() ? null : condition);
	}

	public boolean delete(Record record) {
		return PersistentManager.instance().delete(record);
	}

}
