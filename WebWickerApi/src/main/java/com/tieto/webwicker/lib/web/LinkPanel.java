package com.tieto.webwicker.lib.web;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.tieto.webwicker.api.conf.Configuration;

public class LinkPanel<T> extends Panel {
	private static final long serialVersionUID = -5323917488470420115L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public LinkPanel(Configuration configuration, String id, String label, String dataId, Class clazz) {
		super(id);
		PageParameters param = new PageParameters();
		param.add("page", clazz.getName());
		param.add("id", dataId);
		BookmarkablePageLink link = new BookmarkablePageLink("link", configuration.getMainPageClass(), param);
		link.add(new Label("label", label));
		add(link);
	}
}