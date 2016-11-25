package com.tieto.webwicker.conf;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.WebPage;

import com.tieto.webwicker.api.conf.Configuration;
import com.tieto.webwicker.api.persistence.PersistenceLayer;
import com.tieto.webwicker.api.web.WebWickerPageFactory;

public class ConfigurationImpl implements Configuration {
	private static final long serialVersionUID = -9145343387139913935L;

	private transient PersistenceLayer persistenceLayer = null;
	private final Map<String, WebWickerPageFactory> pageFactories;
	private WebWickerPageFactory homePageFactory;
	private WebWickerPageFactory errorPageFactory;
	
	private Class<? extends WebPage> mainPageClass;
	
	public ConfigurationImpl() {
		pageFactories = new HashMap<>();
	}
	
	public void setPersistanceLayer(final PersistenceLayer persistanceLayer) {
		if(this.persistenceLayer == null) {
			this.persistenceLayer = persistanceLayer;
		}
	}
	
	public PersistenceLayer getPersistenceLayer() {
		return persistenceLayer;
	}

	public WebWickerPageFactory getPageFactory(final String page) {
		if(page == null) {
			return homePageFactory;
		}
		if(!pageFactories.containsKey(page)) {
			return errorPageFactory;
		}
		return pageFactories.get(page);
	}
	
	public void setPageFactory(final String page, final WebWickerPageFactory factory) {
		pageFactories.put(page, factory);
	}
	
	public void setHomePageFactory(final WebWickerPageFactory factory) {
		homePageFactory = factory;
		pageFactories.put(factory.getPageClassName(), factory);
	}

	public void setErrorPageFactory(final WebWickerPageFactory factory) {
		errorPageFactory = factory;
	}

	public void setMainPageClass(Class<? extends WebPage> mainPageClass) {
		this.mainPageClass = mainPageClass;
	}
	
	public Class<? extends WebPage> getMainPageClass() {
		return mainPageClass;
	}
	
	public List<WebWickerPageFactory> getTopPageFactories() {
		List<WebWickerPageFactory> topPages = new LinkedList<>();
		for(final WebWickerPageFactory factory : pageFactories.values()) {
			if(factory.isTopLevelPage()) {
				topPages.add(factory);
			}
		}
		topPages.sort(new Comparator<WebWickerPageFactory>() {
			@Override
			public int compare(WebWickerPageFactory o1, WebWickerPageFactory o2) {
				int o1value = o1.getOrder();
				int o2value = o2.getOrder();
				if(o1value == o2value) {
					return o1.getPageTitle().compareTo(o2.getPageTitle());
				} else {
					return o1value - o2value;
				}
			}
		});
		return topPages;
	}

	@Override
	public WebWickerPageFactory getHomePageFactory() {
		return homePageFactory;
	}
}
