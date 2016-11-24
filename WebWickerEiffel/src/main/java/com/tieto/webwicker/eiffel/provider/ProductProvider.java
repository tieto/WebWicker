package com.tieto.webwicker.eiffel.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import ro.fortsoft.pf4j.Extension;

import com.tieto.webwicker.api.conf.Configuration;
import com.tieto.webwicker.api.model.Model;
import com.tieto.webwicker.api.provider.Provider;
import com.tieto.webwicker.api.provider.ProviderFactory;
import com.tieto.webwicker.eiffel.model.Product;

public class ProductProvider extends Provider<Product> {
	private static final long serialVersionUID = 6839101608020231857L;
	private final List<Product> products;
	
	public ProductProvider() {
		products = new ArrayList<Product>();
		products.add(new Product("Artifact1","10.0","11.0.5","-"));
		products.add(new Product("Artifact2","12.0","13.0.16","2f4413ef3a320813646a939f5a672c57338b314c6"));
		products.add(new Product("Artifact3","8.1","9.0.3","-"));
		products.add(new Product("Artifact4","1.0","1.1.4","45cc780126f7bf019c27d1b4e6a251c0c5b3298fd"));
		products.add(new Product("Artifact5","3.0","3.1.1","-"));
		products.add(new Product("Artifact6","1.0","2.0.4","ee2bdc22b9936161c3e9bf5000f324517542a6f01"));
		products.add(new Product("Artifact7","5.0","6.0.2","-"));
		products.add(new Product("Artifact8","7.0","7.0","ee2bdc22b9936161c3e9bf5000f324517542a6f01"));
		products.add(new Product("Artifact9","3.0","4.0.2","-"));
	}
	

	@Override
	public IModel<Product> model(final Product product) {
        return new AbstractReadOnlyModel<Product>() {
			private static final long serialVersionUID = -1556368745452135950L;

			@Override
            public Product getObject() {
                return (Product) product;
            }
        };
    }

	@Override
	public long size() {
		// TODO Auto-generated method stub
		return products.size();
	}

	@Override
	public Iterator<? extends Product> iterator(long arg0, long arg1) {
		final SortParam<String> sort = getSort();
		
		Collections.sort(products, new Comparator<Product>() {
			@Override
			public int compare(final Product o1, final Product o2) {
				final String value1 = extractValueFromProduct(o1, sort != null ? sort.getProperty() : "");
				final String value2 = extractValueFromProduct(o2, sort != null ? sort.getProperty() : "");
				return sort != null && sort.isAscending() ? value2.compareTo(value1) : value1.compareTo(value2);
			}
			
			private String extractValueFromProduct(final Product p, final String k) {
				if("latestSnapshot".equals(k)) {
					return p.getLatestSnapshot();
				} else if("latestRelease".equals(k)) {
					return p.getLatestRelease();
				} else if("commitsInReview".equals(k)) {
					return p.getCommitsInReview();
				}
				return p.getArtifactId();
			}
		});
		return products.subList((int)arg0, (int)(arg0+arg1)).iterator();
	}

	@Override
	public String getModelType() {
		return "product";
	}


	@Override
	public Product getInstance(String id) {
		return null;
	}
	
	@Extension
	public static class ProductProviderFactory extends ProviderFactory<Product> {
		public Provider<Product> create(Configuration configuration) {
			return new ProductProvider();
		}

		@Override
		public Class<? extends Model> getModelClass() {
			return Product.class;
		}
		
	}
}
