package com.track.store.dog.manager;

import java.util.List;

import com.track.paint.persistent.PersistentManager;
import com.track.store.dog.bean.Partner;

public class PartnerManager {

	public List<Partner> queryByName(String partnerName) {
		Partner temple = new Partner();
		temple.setName(partnerName);
		List<Partner> result = PersistentManager.instance().query(temple, "name");
		return result;
	}

	public void create(String partnerName) {
		Partner temple = new Partner();
		temple.setName(partnerName);
		PersistentManager.instance().save(temple);
	}

	public List<Partner> queryAll() {
		Partner temple = new Partner();
		List<Partner> result = PersistentManager.instance().query(temple);
		return result;
	}

	public boolean delete(Partner partner) {
		return PersistentManager.instance().delete(partner);
	}

	public void update(Partner partner) {
		PersistentManager.instance().save(partner);
	}
}
