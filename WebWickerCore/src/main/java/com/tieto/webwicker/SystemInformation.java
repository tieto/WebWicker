package com.tieto.webwicker;

import java.util.ArrayList;
import java.util.List;

public class SystemInformation {

	private ArrayList<PluginInfo> plugins = new ArrayList<>();

	public List<PluginInfo> getPlugins() {
		return plugins;
	}
	
	public void addOnePlugin(PluginInfo plugin){
		plugins.add(plugin);
	}

	public void setPlugins(ArrayList<PluginInfo> plugins) {
		this.plugins = plugins;
	}

}
