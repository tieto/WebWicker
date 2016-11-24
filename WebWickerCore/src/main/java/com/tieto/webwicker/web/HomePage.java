package com.tieto.webwicker.web;

import java.util.List;

import org.apache.wicket.StyleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.tieto.webwicker.WebWickerApplication;
import com.tieto.webwicker.api.conf.Configuration;
import com.tieto.webwicker.api.web.WebWickerPageFactory;

public class HomePage extends WebPage {
	private static final long serialVersionUID = -7626368252793216083L;
	private List<WebWickerPageFactory> subPages;
	
	public static final int ORDER = 0;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HomePage(final PageParameters parameters) {
		super(parameters);
		
		Configuration configuration = WebWickerApplication.get().getConfiguration();
		
		RepeatingView listItems = new RepeatingView("listItems");
		subPages = configuration.getTopPageFactories();

		for(final WebWickerPageFactory subPage : subPages) {
			PageParameters params = new PageParameters();
			params.add("page", subPage.getPageClassName());
			WebMarkupContainer list = new WebMarkupContainer(listItems.newChildId());
			BookmarkablePageLink link = new BookmarkablePageLink("Link", getClass(), params);
			link.add(new Label("Text", subPage.getPageTitle()));
			if(pageMatchesLink(parameters.get("page").toString(), subPage.getPageClassName())) {
				link.add(StyleAttributeModifier.append("class", "active"));
			}
			list.add(link);
	        listItems.add(list);
		}
		
		add(listItems);
		add(configuration.getPageFactory(parameters.get("page").toString()).create("webwickerpanel", parameters, configuration));
	}
	
	private boolean pageMatchesLink(final String page, final String name) {
		if(page == null) {
			return "Home".equals(name);
		}
		return page.equals(name);
	}
}
