package com.tieto.webwicker.eiffel.web.products;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import ro.fortsoft.pf4j.Extension;

import com.tieto.webwicker.api.conf.Configuration;
import com.tieto.webwicker.api.web.WebWickerPage;
import com.tieto.webwicker.api.web.WebWickerPageFactory;

public class ProductPage extends WebWickerPage {
	private static final long serialVersionUID = 2813026533242517627L;

	public ProductPage(String id, PageParameters parameters, Configuration configuration) {
		super(id);
	}
	
	@Extension
	public static class ProductPageFactory extends WebWickerPageFactory {
		private static final long serialVersionUID = 8557556866287865441L;

		@Override
		public WebWickerPage create(String id, PageParameters pageParameters, Configuration configuration) {
			return new ProductPage(id, pageParameters, configuration);
		}

		@Override
		public boolean isTopLevelPage() {
			return false;
		}

		@Override
		public int getOrder() {
			return 0;
		}

		@Override
		public String getPageTitle() {
			return "Product";
		}

		@Override
		public String getPageClassName() {
			return ProductPage.class.getName();
		}
		
	}
}
