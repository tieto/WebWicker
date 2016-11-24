package com.tieto.webwicker.api.conf;

import java.util.List;
import org.apache.wicket.markup.html.WebPage;

import com.tieto.webwicker.api.persistence.PersistenceLayer;
import com.tieto.webwicker.api.web.WebWickerPageFactory;

public interface Configuration {
	PersistenceLayer getPersistenceLayer();

	WebWickerPageFactory getPageFactory(final String page);
	
	public void setMainPageClass(Class<? extends WebPage> mainPageClass);
	
	public Class<? extends WebPage> getMainPageClass();
	
	public List<WebWickerPageFactory> getTopPageFactories();
}
