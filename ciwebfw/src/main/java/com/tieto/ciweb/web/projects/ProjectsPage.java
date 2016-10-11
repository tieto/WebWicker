package com.tieto.ciweb.web.projects;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.tieto.ciweb.api.web.BasePage;
import com.tieto.ciweb.api.web.TopLevelPage;

public class ProjectsPage extends BasePage implements TopLevelPage {
	private static final long serialVersionUID = -9107302898812653839L;

	public static final int ORDER = 200;

	public ProjectsPage(final PageParameters parameters) {
		super(parameters);

		// TODO Add your page's components here
    }
}
