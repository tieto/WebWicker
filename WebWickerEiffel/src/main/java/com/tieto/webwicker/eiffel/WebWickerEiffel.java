package com.tieto.webwicker.eiffel;

import ro.fortsoft.pf4j.PluginWrapper;
import ro.fortsoft.wicket.plugin.WicketPlugin;

public class WebWickerEiffel extends WicketPlugin {

	public WebWickerEiffel(PluginWrapper wrapper) {
		super(wrapper);
	}

	@Override
	public void start() {
		System.err.println("Starting up the WebWickerEiffel plugin");
	}
	
	@Override
	public void stop() {
		System.err.println("Stopping the WebWickerEiffel plugin");
	}
}
