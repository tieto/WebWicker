package com.tieto.webwicker.eiffel.provider;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import ro.fortsoft.pf4j.Extension;

import com.google.gson.JsonObject;
import com.tieto.webwicker.api.conf.Configuration;
import com.tieto.webwicker.api.model.Model;
import com.tieto.webwicker.api.persistence.PersistenceLayer;
import com.tieto.webwicker.api.provider.Provider;
import com.tieto.webwicker.api.provider.ProviderFactory;
import com.tieto.webwicker.eiffel.model.Commit;

public class CommitProvider extends Provider<Commit> {
	private static final long serialVersionUID = -3567151657699169406L;
	
	private final PersistenceLayer persistanceLayer;
	
	public CommitProvider(Configuration configuration) {
		persistanceLayer = configuration.getPersistenceLayer();
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

	@Extension
	public static class CommitProviderFactory extends ProviderFactory<Commit> {
		@Override
		public Provider<Commit> create(Configuration configuration) {
			return new CommitProvider(configuration);
		}

		@Override
		public Class<? extends Model> getModelClass() {
			return Commit.class;
		}
	}
}
