package com.tieto.webwicker.eiffel.web.commits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ro.fortsoft.pf4j.Extension;

import com.tieto.webwicker.api.conf.Configuration;
import com.tieto.webwicker.api.web.WebWickerPage;
import com.tieto.webwicker.api.web.WebWickerPageFactory;
import com.tieto.webwicker.eiffel.model.Commit;
import com.tieto.webwicker.eiffel.model.PatchSet;
import com.tieto.webwicker.eiffel.model.WorkItem;
import com.tieto.webwicker.eiffel.provider.CommitProvider;

public class CommitPage extends WebWickerPage {
	private static final long serialVersionUID = -1879929547602037385L;
	private static final List<String> approved = Arrays.asList(new String[]{"+2"});
	private static final List<String> partlyApproved = Arrays.asList(new String[]{"+1"});
	private static final List<String> notApproved = Arrays.asList(new String[]{"-1","-2"});
	private static final List<String> verified = Arrays.asList(new String[]{"+1"});
	private static final List<String> notVerified = Arrays.asList(new String[]{"-1"});

	@SuppressWarnings({"serial", "rawtypes"})
	public CommitPage(String id, PageParameters parameters, Configuration configuration) {
		super(id);
		
		final String commitId = parameters.get("id").toString("");
		final CommitProvider commitProvider = new CommitProvider(configuration);
		final Commit commit = commitProvider.getInstance(commitId);
		
		final String codeReview = commit.getLatestPatchSet().getCodeReviewed();
		final String verified = commit.getLatestPatchSet().getVerified();
		final String merged = commit.getLatestPatchSet().getMerged();
		
		add(new Label("author", commit == null ? "-" : commit.getAuthor()));
		add(new Label("team", commit == null ? "-" : commit.getTeam()));
		add(new Label("changeId", commit == null ? "-" : commit.getChangeId()));
		add(new Label("project", commit == null ? "-" : commit.getProject()));
		add(new Label("branch", commit == null ? "-" : commit.getBranch()));
		add(new Label("created", commit == null ? "-" : commit.getCreated()));
		add(new Label("updated", commit == null ? "-" : commit.getLatestPatchSet().getUpdated()));
		add(new Label("currentPatchset", commit == null ? "-" : commit.getPatchSets().size()));
		add(new Label("verified", commit == null ? "-" : getVerifiedString(verified)).add(getVerifiedStyle(verified)));
		add(new Label("codeReview", commit == null ? "-" : getCodeReviewString(codeReview)).add(getCodeReviewStyle(codeReview)));
		add(new Label("merged", commit == null ? "-" : merged).add(getMergedStyle(merged)));

	   	add(new ListView<WorkItem>("workItems", commit.getWorkItems()) {
			@Override
			protected void populateItem(ListItem<WorkItem> item) {
		   		item.add(new Label("workItem", new PropertyModel(item.getModel(), "itemId")));
			}			
		});

	   	final List<PatchSet> patchSets = new ArrayList<>(commit.getPatchSets());
	   	Collections.reverse(patchSets);
	   	patchSets.remove(0);
	   	
	   	add(new ListView<PatchSet>("patchSets", patchSets) {
			@Override
			protected void populateItem(ListItem<PatchSet> item) {
				int ps = patchSets.size() - patchSets.indexOf(item.getModel().getObject());
				String patchSet = "Patch set "+ps;
				String verified = item.getModel().getObject().getVerified();
				String codeReviewed = item.getModel().getObject().getCodeReviewed();
		   		item.add(new Label("psId", patchSet));
		   		item.add(new Label("psVerified", getVerifiedString(verified)).add(getVerifiedStyle(verified)));
		   		item.add(new Label("psCodeReview", getCodeReviewString(codeReviewed)).add(getCodeReviewStyle(codeReviewed)));
			}
	   	});
	}
	
	private String getCodeReviewString(String cr) {
		if(approved.contains(cr)) {
			return "Approved";
		} else if(partlyApproved.contains(cr)) {
			return "Checked, not yet approved";
		} else if(notApproved.contains(cr)) {
			return "Not approved";
		}
		return "Not reviewed";
	}

	private AttributeAppender getCodeReviewStyle(String cr) {
		if(approved.contains(cr)) {
			return new AttributeAppender("style", "color:green");
		} else if(notApproved.contains(cr)) {
			return new AttributeAppender("style", "color:red");
		}
		return new AttributeAppender("style", "color:orange");
	}

	private String getVerifiedString(String v) {
		if(verified.contains(v)) {
			return "Verification passed";
		} else if(notVerified.contains(v)) {
			return "Verification failed";
		}
		return "Not verified";
	}
	
	private AttributeAppender getVerifiedStyle(String v) {
		if(verified.contains(v)) {
			return new AttributeAppender("style", "color:green");
		} else if(notVerified.contains(v)) {
			return new AttributeAppender("style", "color:red");
		}
		return new AttributeAppender("style", "color:orange");
	}
	
	private AttributeAppender getMergedStyle(String m) {
		if("Yes".equalsIgnoreCase(m)) {
			return new AttributeAppender("style", "color:green");
		}
		return new AttributeAppender("style", "color:red");
	}
	
	@Extension
	public static class CommitPageFactory extends WebWickerPageFactory {
		private static final long serialVersionUID = -6004897366175631499L;

		@Override
		public WebWickerPage create(String id, PageParameters pageParameters, Configuration configuration) {
			return new CommitPage(id, pageParameters, configuration);
		}

		@Override
		public boolean isTopLevelPage() {
			return false;
		}

		@Override
		public String getPageTitle() {
			return "Commit";
		}

		@Override
		public int getOrder() {
			return 0;
		}

		@Override
		public String getPageClassName() {
			return CommitPage.class.getName();
		}
	}
}
