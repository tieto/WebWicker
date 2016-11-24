package com.tieto.webwicker.eiffel.web.commits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ro.fortsoft.pf4j.Extension;

import com.tieto.webwicker.lib.web.LinkPanel;
import com.tieto.webwicker.api.conf.Configuration;
import com.tieto.webwicker.api.web.WebWickerPage;
import com.tieto.webwicker.api.web.WebWickerPageFactory;
import com.tieto.webwicker.eiffel.model.Commit;
import com.tieto.webwicker.eiffel.provider.CommitProvider;

public class CommitsPage extends WebWickerPage {
	private static final long serialVersionUID = 1386775624549749182L;
	private static final List<String> approved = Arrays.asList(new String[]{"+2"});
	private static final List<String> verified = Arrays.asList(new String[]{"+1"});

	public static final int ORDER = 100;

	public CommitsPage(final String id, final PageParameters parameters, final Configuration configuration) {
		super(id);

		final CommitProvider commitProvider = new CommitProvider(configuration);
        
        List<IColumn<Commit,String>> columns = new ArrayList<>();
        columns.add(new TextFilteredPropertyColumn<Commit,String,String>(new Model<String>("Change ID"), "changeId", "changeId"){
			private static final long serialVersionUID = -610011679744121473L;
			
			@SuppressWarnings("rawtypes")
			public void populateItem(Item cellItem, String componentId, IModel model) {
				final Commit commit = (Commit)model.getObject();
				cellItem.add(new LinkPanel<Commit>(componentId, commit.getChangeId(), commit.getChangeId(), CommitPage.class));
			}
        });
        columns.add(new PropertyColumn<Commit, String>(new Model<String>("Author"), "author", "author"));
        columns.add(new PropertyColumn<Commit, String>(new Model<String>("Project"), "project", "project"));
        columns.add(new PropertyColumn<Commit, String>(new Model<String>("Patch set"), "patchSet", "patchSet") {
			private static final long serialVersionUID = -8507527320731606189L;

			@SuppressWarnings("rawtypes")
			public void populateItem(Item cellItem, String componentId, IModel model) {
				final Commit commit = (Commit)model.getObject();
				cellItem.add(new Label(componentId, commit.getPatchSets().size()));
			}
        });
        columns.add(new PropertyColumn<Commit, String>(new Model<String>("Verified"), "verified", "latestPatchSet.verified"){
			private static final long serialVersionUID = 268541005046602019L;

			@SuppressWarnings("rawtypes")
			public void populateItem(Item cellItem, String componentId, IModel model) {
				final Commit commit = (Commit)model.getObject();
				final String v = commit.getLatestPatchSet().getVerified();
				cellItem.add(new Label(componentId, getVerifiedString(v)).add(getVerifiedStyle(v)));
			}
        });
        columns.add(new PropertyColumn<Commit, String>(new Model<String>("Code reviewed"), "codeReviewed", "latestPatchSet.codeReviewed"){
			private static final long serialVersionUID = -3648065452517023488L;

			@SuppressWarnings("rawtypes")
			public void populateItem(Item cellItem, String componentId, IModel model) {
				final Commit commit = (Commit)model.getObject();
				final String cr = commit.getLatestPatchSet().getCodeReviewed();
				cellItem.add(new Label(componentId, getCodeReviewString(cr)).add(getCodeReviewStyle(cr)));
			}
        });
        columns.add(new PropertyColumn<Commit, String>(new Model<String>("Merged"), "merged", "latestPatchSet.merged"){
			private static final long serialVersionUID = -5865134176934828462L;
        	
			@SuppressWarnings("rawtypes")
			public void populateItem(Item cellItem, String componentId, IModel model) {
				final Commit commit = (Commit)model.getObject();
				final String m = commit.getLatestPatchSet().getMerged();
				cellItem.add(new Label(componentId, m).add(getMergedStyle(m)));
			}
        });
        columns.add(new PropertyColumn<Commit, String>(new Model<String>("Created"), "created", "created"));
        columns.add(new PropertyColumn<Commit, String>(new Model<String>("Updated"), "updated", "latestPatchSet.updated"));
        
        DefaultDataTable<Commit,String> table = new DefaultDataTable<Commit,String>("datatable", columns, commitProvider, 20);
         
        add(table);
    }
	
	private String getCodeReviewString(String cr) {
		if(approved.contains(cr)) {
			return "Yes";
		}
		return "No";
	}

	private AttributeAppender getCodeReviewStyle(String cr) {
		if(approved.contains(cr)) {
			return new AttributeAppender("style", "color:green");
		}
		return new AttributeAppender("style", "color:red");
	}

	private String getVerifiedString(String v) {
		if(verified.contains(v)) {
			return "Yes";
		}
		return "No";
	}

	private AttributeAppender getVerifiedStyle(String v) {
		if(verified.contains(v)) {
			return new AttributeAppender("style", "color:green");
		}
		return new AttributeAppender("style", "color:red");
	}
	
	private AttributeAppender getMergedStyle(String m) {
		if("Yes".equalsIgnoreCase(m)) {
			return new AttributeAppender("style", "color:green");
		}
		return new AttributeAppender("style", "color:red");
	}
	
	@Extension
	public static class CommitsPageFactory extends WebWickerPageFactory {
		private static final long serialVersionUID = -1749951474362697919L;

		@Override
		public WebWickerPage create(String id, PageParameters pageParameters, Configuration configuration) {
			return new CommitsPage(id, pageParameters, configuration);
		}

		@Override
		public boolean isTopLevelPage() {
			return true;
		}

		@Override
		public String getPageTitle() {
			return "Commits";
		}

		@Override
		public int getOrder() {
			return ORDER;
		}

		@Override
		public String getPageClassName() {
			return CommitsPage.class.getName();
		}
	}
}
