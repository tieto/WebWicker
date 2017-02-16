package com.tieto.webwicker.eiffel.web.commits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.time.Duration;

import ro.fortsoft.pf4j.Extension;

import com.tieto.webwicker.api.conf.Configuration;
import com.tieto.webwicker.api.web.WebWickerPage;
import com.tieto.webwicker.api.web.WebWickerPageFactory;
import com.tieto.webwicker.eiffel.model.Commit;
import com.tieto.webwicker.eiffel.model.PatchSet;
import com.tieto.webwicker.eiffel.model.WorkItem;
import com.tieto.webwicker.eiffel.provider.CommitProvider;

public class CommitPage extends WebWickerPage {
	
	private static final List<String> approved = Arrays.asList(new String[]{"SUCCESS"});
	private static final List<String> partlyApproved = Arrays.asList(new String[]{"INCONCLUSIVE"});
	private static final List<String> notApproved = Arrays.asList(new String[]{"FAILURE"});
	private static final List<String> verified = Arrays.asList(new String[]{"SUCCESS"});
	private static final List<String> notVerified = Arrays.asList(new String[]{"INCONCLUSIVE,FAILURE"});
	
	private final String commitId;
	private final CommitProvider commitProvider;
	private final ListView<WorkItem> workItemView;
	private final ListView<PatchSet> patchSetView;
	private final WebMarkupContainer verifiedNode;
	private final WebMarkupContainer codeReviewNode;
	private final WebMarkupContainer submitNode;
	
	private CommitModel model;
	
	@SuppressWarnings({"serial", "rawtypes"})
	public CommitPage(String id, PageParameters parameters, Configuration configuration) {
		super(id);
		
		commitProvider = new CommitProvider(configuration);
		commitId = parameters.get("id").toString("");

		final Commit commit = commitProvider.getInstance(commitId);
		
		model = new CommitModel();
		model.setObject(commit);
		
		add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)));
		
		verifiedNode = new WebMarkupContainer("verifiedNode");
		codeReviewNode = new WebMarkupContainer("codeReviewNode");
		submitNode = new WebMarkupContainer("submitNode");
		
		add(verifiedNode);
		add(codeReviewNode);
		add(submitNode);

        Map<String, IModel<Commit>> modelMap = new LinkedHashMap<>();
        modelMap.put("author", new PropertyModel<Commit>(model, "author"));
        modelMap.put("team", new PropertyModel<Commit>(model, "team"));
        modelMap.put("changeId", new PropertyModel<Commit>(model, "changeId"));
        modelMap.put("project", new PropertyModel<Commit>(model, "project"));
        modelMap.put("branch", new PropertyModel<Commit>(model, "branch"));
		modelMap.put("created", new PropertyModel<Commit>(model, "created"));
		modelMap.put("updated", new PropertyModel<Commit>(model, "latestPatchSet.updated"));
		modelMap.put("currentPatchset", new PropertyModel<Commit>(model, "numberOfPatchSets"));
		modelMap.put("verified", new PropertyModel<Commit>(model, "latestPatchSet.verified"));
		modelMap.put("codeReview", new PropertyModel<Commit>(model, "latestPatchSet.codeReviewed"));
		modelMap.put("merged", new PropertyModel<Commit>(model, "latestPatchSet.merged"));
		modelMap.put("currentPatchsetGraph", modelMap.get("currentPatchset"));
		modelMap.put("verifiedGraph", modelMap.get("verified"));
		modelMap.put("codeReviewGraph", modelMap.get("codeReview"));
        
		for(String key : modelMap.keySet()) {
			if(key.startsWith("verified")) {
				add(new Label(key, modelMap.get(key)) {
					@Override
				    public <C> IConverter<C> getConverter(Class<C> type) {
						return new IConverter<C>() {
							public String convertToString(C value, Locale locale) {
								if(null == value) return null;
								return getVerifiedString(value.toString());
							}
							public C convertToObject(String value, Locale locale)
							{
								throw new UnsupportedOperationException();
							}
						};
					}			
				});
			} else if(key.startsWith("codeReview")) {
				add(new Label(key, modelMap.get(key)) {
					@Override
				    public <C> IConverter<C> getConverter(Class<C> type) {
						return new IConverter<C>() {
							public String convertToString(C value, Locale locale) {
								if(null == value) return null;
								return getCodeReviewString(value.toString());
							}
							public C convertToObject(String value, Locale locale)
							{
								throw new UnsupportedOperationException();
							}
						};
					}			
				});
			} else if(key.equals("currentPatchsetGraph")) {
				add(new Label(key, modelMap.get(key)) {
					@Override
				    public <C> IConverter<C> getConverter(Class<C> type) {
						return new IConverter<C>() {
							public String convertToString(C value, Locale locale) {
								if(null == value) return null;
								return "Patchset "+value.toString();
							}
							public C convertToObject(String value, Locale locale)
							{
								throw new UnsupportedOperationException();
							}
						};
					}			
				});
			} else {
				add(new Label(key, modelMap.get(key)));
			}
		}
		
		workItemView = new ListView<WorkItem>("workItems", model.getObject().getWorkItems()) {
			@Override
			protected void populateItem(ListItem<WorkItem> item) {
		   		item.add(new Label("workItem", new PropertyModel(item.getModel(), "itemId")));
			}			
		};
		
		add(workItemView);

	   	final List<PatchSet> patchSets = new ArrayList<>(model.getObject().getPatchSets());
	   	Collections.reverse(patchSets);
	   	patchSets.remove(0);
	   	
	   	patchSetView = new ListView<PatchSet>("patchSets", patchSets) {
			@Override
			protected void populateItem(ListItem<PatchSet> item) {
				int ps = getList().size() - getList().indexOf(item.getModel().getObject());
				String patchSet = "Patch set "+ps;
				String verified = item.getModel().getObject().getVerified();
				String codeReviewed = item.getModel().getObject().getCodeReviewed();
		   		item.add(new Label("psId", patchSet));
		   		item.add(new Label("psVerified", getVerifiedString(verified)).add(getVerifiedStyle(verified)));
		   		item.add(new Label("psCodeReview", getCodeReviewString(codeReviewed)).add(getCodeReviewStyle(codeReviewed)));
			}
	   	};
	   	
	   	updateNodeColors();
	   	
	   	add(patchSetView);
	}
	
	private void updateNodeColors() {
	
		final String tmpverified = model.getObject().getLatestPatchSet().getVerified();
		final String codeReview = model.getObject().getLatestPatchSet().getCodeReviewed();
		final String merged = model.getObject().getLatestPatchSet().getMerged();
        //test
		
		
		if(tmpverified.equals("FAILURE")) {
			verifiedNode.add(new AttributeModifier("fill", "#FFBBBB"));
		} else if(tmpverified.equals("SUCCESS")) {
			verifiedNode.add(new AttributeModifier("fill", "#BBFFBB"));
		} else {
			verifiedNode.add(new AttributeModifier("fill", "#BBBBBB"));
		}
		
		if(codeReview.equals("FAILURE")) {
			codeReviewNode.add(new AttributeModifier("fill", "#FFBBBB"));
		} else if(codeReview.equals("SUCCESS")) {
			codeReviewNode.add(new AttributeModifier("fill", "#BBFFBB"));
		} else {
			codeReviewNode.add(new AttributeModifier("fill", "#BBBBBB"));
		}

		if(merged.equals("Yes")) {
			submitNode.add(new AttributeModifier("fill", "#BBFFBB"));
		} else {
			submitNode.add(new AttributeModifier("fill", "#BBBBBB"));
		}
	}

	@Override
	public void onEvent(IEvent<?> event) {
		final Commit commit = commitProvider.getInstance(commitId);
		model.setObject(commit);

		workItemView.setList(model.getObject().getWorkItems());
		
	   	final List<PatchSet> patchSets = new ArrayList<>(model.getObject().getPatchSets());
	   	Collections.reverse(patchSets);
	   	patchSets.remove(0);
	   	
	   	patchSetView.setList(patchSets);
	   	
	   	updateNodeColors();
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
	
	private static class CommitModel implements IModel<Commit> {
		private static final long serialVersionUID = -2580980548620301203L;
		private Commit object = null;
		
		@Override
		public void detach() {
		}

		@Override
		public Commit getObject() {
			return object;
		}

		@Override
		public void setObject(Commit object) {
			this.object = object;
		}
	}
}
