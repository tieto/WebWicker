package com.tieto.ciweb.lib.web;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class LinkPanel<T> extends Panel {
	private static final long serialVersionUID = -5323917488470420115L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public LinkPanel(String id, String label, String dataId, Class clazz) {
		super(id);
		PageParameters param = new PageParameters();
		param.add("id", dataId);
		BookmarkablePageLink link = new BookmarkablePageLink("link", clazz, param);
		link.add(new Label("label", label));
		add(link);
	}
}