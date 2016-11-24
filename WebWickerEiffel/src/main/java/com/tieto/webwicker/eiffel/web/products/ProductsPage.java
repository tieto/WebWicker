package com.tieto.webwicker.eiffel.web.products;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ro.fortsoft.pf4j.Extension;

import com.tieto.webwicker.api.conf.Configuration;
import com.tieto.webwicker.api.web.WebWickerPage;
import com.tieto.webwicker.api.web.WebWickerPageFactory;
import com.tieto.webwicker.eiffel.model.Product;
import com.tieto.webwicker.eiffel.provider.ProductProvider;

public class ProductsPage extends WebWickerPage {
	private static final long serialVersionUID = -2628622574983831269L;

	public static final int ORDER = 300;

	public ProductsPage(final String id, final PageParameters parameters, final Configuration configuration) {
		super(id);

		final ProductProvider productProvider = new ProductProvider();
        
        List<IColumn<Product,String>> columns = new ArrayList<>();
        columns.add(new PropertyColumn<Product, String>(new Model<String>("ArtifactId"), "artifactId", "artifactId"));
        columns.add(new PropertyColumn<Product, String>(new Model<String>("Latest release"), "latestRelease", "latestRelease"));
        columns.add(new PropertyColumn<Product, String>(new Model<String>("Latest snapshot"), "latestSnapshot", "latestSnapshot"));
        columns.add(new PropertyColumn<Product, String>(new Model<String>("Commits in review"), "commitsInReview", "commitsInReview"));
         
        DefaultDataTable<Product,String> table = new DefaultDataTable<Product,String>("datatable", columns, productProvider, 20);
         
        add(table);
    }
	
	@Extension
	public static class ProductsPageFactory extends WebWickerPageFactory {
		private static final long serialVersionUID = -7915844907031232853L;

		@Override
		public WebWickerPage create(String id, PageParameters pageParameters, Configuration configuration) {
			return new ProductsPage(id, pageParameters, configuration);
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
			return "Products";
		}

		@Override
		public String getPageClassName() {
			return ProductsPage.class.getName();
		}
		
	}
}
