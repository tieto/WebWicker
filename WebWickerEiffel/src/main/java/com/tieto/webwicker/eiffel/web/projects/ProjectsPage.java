package com.tieto.webwicker.eiffel.web.projects;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import ro.fortsoft.pf4j.Extension;

import com.tieto.webwicker.api.conf.Configuration;
import com.tieto.webwicker.api.web.WebWickerPage;
import com.tieto.webwicker.api.web.WebWickerPageFactory;

public class ProjectsPage extends WebWickerPage {
	private static final long serialVersionUID = -9107302898812653839L;

	public static final int ORDER = 200;

	public ProjectsPage(final String id, final PageParameters parameters, final Configuration configuration) {
		super(id);
    }
	
	@Extension
	public static class ProjectsPageFactory extends WebWickerPageFactory {
		private static final long serialVersionUID = 3168892865361642500L;

		@Override
		public WebWickerPage create(String id, PageParameters pageParameters, Configuration configuration) {
			return new ProjectsPage(id, pageParameters, configuration);
		}

		@Override
		public boolean isTopLevelPage() {
			return false;
		}

		@Override
		public int getOrder() {
			return ORDER;
		}

		@Override
		public String getPageTitle() {
			return "Projects";
		}

		@Override
		public String getPageClassName() {
			return ProjectsPage.class.getName();
		}
	}
}
