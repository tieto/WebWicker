package com.tieto.webwicker.api.web;

import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.time.Duration;

public abstract class WebWickerPage extends Panel {
	private static final long serialVersionUID = 1958619830536738136L;

	public WebWickerPage(final String id) {
		super(id);
	}
}
