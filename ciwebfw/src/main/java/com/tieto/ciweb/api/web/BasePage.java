package com.tieto.ciweb.api.web;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.StyleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.reflections.Reflections;

public abstract class BasePage extends WebPage {
	private static final long serialVersionUID = 1958619830536738136L;
	private final transient Reflections reflections = new Reflections("com.tieto.ciweb");
	private final List<Class<? extends TopLevelPage>> subPages;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BasePage(final PageParameters parameters) {
		super(parameters);
		RepeatingView listItems = new RepeatingView("listItems");
		subPages = initiateSubPages();

		for(final Class<? extends TopLevelPage> subPage : subPages) {
			WebMarkupContainer list = new WebMarkupContainer(listItems.newChildId());
			BookmarkablePageLink link = new BookmarkablePageLink("Link", subPage);
			link.add(new Label("Text", subPage.getSimpleName().replace("Page", "")));
			if(subPage.isInstance(this)) {
				link.add(StyleAttributeModifier.append("class", "active"));
			}
			list.add(link);
	        listItems.add(list);
		}
		
		add(listItems);
	}
	
	private final List<Class<? extends TopLevelPage>> initiateSubPages() {
		List<Class<? extends TopLevelPage>> subPages = new LinkedList<>();
		subPages.addAll(reflections.getSubTypesOf(TopLevelPage.class));
		subPages.sort(new Comparator<Class<? extends TopLevelPage>>() {
			@Override
			public int compare(Class<? extends TopLevelPage> o1,
					Class<? extends TopLevelPage> o2) {
				int o1value, o2value;
				try {
					o1value = o1.getDeclaredField("ORDER").getInt(null);
				} catch (IllegalArgumentException | IllegalAccessException
						| NoSuchFieldException | SecurityException e) {
					o1value = Integer.MAX_VALUE;
				}
				try {
					o2value = o2.getDeclaredField("ORDER").getInt(null);
				} catch (IllegalArgumentException | IllegalAccessException
						| NoSuchFieldException | SecurityException e) {
					o2value = Integer.MAX_VALUE;
				}
				if(o1value == o2value) {
					return o1.getSimpleName().compareTo(o2.getSimpleName());
				} else {
					return o1value - o2value;
				}
			}
		});
		return subPages;
	}
}
