package com.tieto.webwicker;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

import ro.fortsoft.pf4j.PluginManager;
import ro.fortsoft.pf4j.PluginWrapper;
import ro.fortsoft.wicket.plugin.PluginManagerInitializer;
import com.tieto.webwicker.api.conf.Configuration;
import com.tieto.webwicker.api.source.SourceFactory;
import com.tieto.webwicker.api.web.WebWickerPageFactory;
import com.tieto.webwicker.conf.ConfigurationImpl;
import com.tieto.webwicker.persistence.InMemoryStorage.InMemoryStorageFactory;
import com.tieto.webwicker.web.ErrorPage.ErrorPageFactory;
import com.tieto.webwicker.web.HomePage;
import com.tieto.webwicker.web.StartPage.StartPageFactory;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see com.tieto.webwicker.Start#main(String[])
 */
public class WebWickerApplication extends WebApplication {
	
	private final ConfigurationImpl configuration = new ConfigurationImpl();
	private final SystemInformation sysinfo = new SystemInformation();

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage() {
		return HomePage.class;
	}

	public static WebWickerApplication get() {
		return (WebWickerApplication) WebApplication.get();
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init() {

		super.init();

		configuration.setPersistanceLayer(new InMemoryStorageFactory().create(configuration));
		configuration.setMainPageClass(getHomePage());
		configuration.setHomePageFactory(new StartPageFactory());
		configuration.setErrorPageFactory(new ErrorPageFactory());

		PluginManager manager = getPluginManager();

		List<WebWickerPageFactory> pageFactories = manager.getExtensions(WebWickerPageFactory.class);
		for (WebWickerPageFactory factory : pageFactories) {
			configuration.setPageFactory(factory.getPageClassName(), factory);
		}

		List<SourceFactory> sourceFactories = manager.getExtensions(SourceFactory.class);
		for (SourceFactory factory : sourceFactories) {
			Thread sourceThread = new Thread(factory.create(configuration));
			sourceThread.start();
		}

		fetchPluginInformation(manager);
	}

	private void fetchPluginInformation(PluginManager manager) {

		List<PluginWrapper> startedPlugins = manager.getStartedPlugins();
		List<PluginWrapper> unresolvedPlugins = manager.getUnresolvedPlugins();
		
		for (PluginWrapper startedplugin : startedPlugins) {
			sysinfo.addOnePlugin(new PluginInfo(startedplugin.getDescriptor().getPluginId(),
					startedplugin.getDescriptor().getVersion().toString(), "started"));
			System.err.println(startedplugin.getDescriptor().getPluginId() + " "
					+ startedplugin.getDescriptor().getVersion() + " started.");
			
		}

		for (PluginWrapper unresolvedplugin : unresolvedPlugins) {
			
			sysinfo.addOnePlugin(new PluginInfo(unresolvedplugin.getDescriptor().getPluginId(),
					unresolvedplugin.getDescriptor().getVersion().toString(), "unresolved"));
			System.err.println(unresolvedplugin.getDescriptor().getPluginId() + " "
					+ unresolvedplugin.getDescriptor().getVersion() + " unresolved.");
			
		}

	}

	public SystemInformation getSystemInformation() {

		return sysinfo;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public PluginManager getPluginManager() {
		return getMetaData(PluginManagerInitializer.PLUGIN_MANAGER_KEY);
	}
}
