package com.tieto.webwicker.web;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.tieto.webwicker.api.conf.Configuration;
import com.tieto.webwicker.api.web.WebWickerPage;
import com.tieto.webwicker.api.web.WebWickerPageFactory;

public class StartPage extends WebWickerPage {
	private static final long serialVersionUID = -9107302898812653839L;

	public static final int ORDER = 0;

	public StartPage(final String id, final PageParameters parameters) {
		super(id);
    }
	
	public static class StartPageFactory extends WebWickerPageFactory {
		private static final long serialVersionUID = 7855045582303692430L;

		@Override
		public WebWickerPage create(String id, PageParameters pageParameters, Configuration configuration) {
			return new StartPage(id, pageParameters);
		}

		@Override
		public boolean isTopLevelPage() {
			return true;
		}

		@Override
		public String getPageTitle() {
			return "Home";
		}

		@Override
		public int getOrder() {
			return ORDER;
		}

		@Override
		public String getPageClassName() {
			return StartPage.class.getName();
		}
		
	}
}
