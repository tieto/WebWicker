package com.tieto.ciweb.provider;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import com.google.gson.JsonObject;
import com.tieto.ciweb.Configuration;
import com.tieto.ciweb.api.persistence.PersistenceLayer;
import com.tieto.ciweb.api.provider.Provider;
import com.tieto.ciweb.model.Commit;

public class CommitProvider extends Provider<Commit> {
	private static final long serialVersionUID = -3567151657699169406L;
	
	private final transient PersistenceLayer persistanceLayer;
	
	public CommitProvider() {
		persistanceLayer = Configuration.getInstance().getPersistanceLayer();
	}

	@Override
	public Iterator<? extends Commit> iterator(long first, long count) {
		List<JsonObject> commits = persistanceLayer.fetchMany("commits","updated", true, "");
		List<Commit> result = new LinkedList<>();
		for(JsonObject json : commits.subList((int)first, (int)(first+count))) {
			Commit c = new Commit();
			c.fromJson(json);
			result.add(c);
		}
		return result.iterator();
	}

	@Override
	public long size() {
		return persistanceLayer.count("commits", "");
		//return commits.size();
	}

	@Override
	public IModel<Commit> model(Commit commit) {
        return new AbstractReadOnlyModel<Commit>() {
			private static final long serialVersionUID = 3054128343493812921L;

			@Override
            public Commit getObject() {
                return commit;
            }
        };
	}

	@Override
	public String getModelType() {
		return "commit";
	}

	@Override
	public Commit getInstance(String id) {
		JsonObject json = persistanceLayer.fetchOneWithId("commits", id);
		if(json != null) {
			final Commit result = new Commit();
			result.fromJson(persistanceLayer.fetchOneWithId("commits", id));
			return result;
		}
		return null;
	}

}
