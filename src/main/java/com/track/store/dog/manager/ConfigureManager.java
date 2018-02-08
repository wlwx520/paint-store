package com.track.store.dog.manager;

import java.util.List;

import com.track.paint.persistent.PersistentManager;
import com.track.store.dog.bean.Configure;

public class ConfigureManager {
	public ConfigureManager() {
	}

	public Configure get() {
		List<Configure> query = PersistentManager.instance().query(new Configure());
		if (query == null || query.isEmpty()) {
			Configure configure = new Configure();
			configure.setGoods("[]");
			configure.setPartners("[]");
			save(configure);
			return configure;
		} else {
			return query.get(0);
		}
	}

	public void save(Configure configure) {
		PersistentManager.instance().save(configure);
	}
}
