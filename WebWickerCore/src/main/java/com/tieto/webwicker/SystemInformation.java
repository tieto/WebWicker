package com.tieto.webwicker;

import java.util.ArrayList;
import java.util.List;

public class SystemInformation {

	private ArrayList<PluginInfo> plugins = new ArrayList<>();
	private List<String> startedPlugins = new ArrayList<>();
	private List<String> unresolvedPlugins = new ArrayList<>();
	

	public List<String> getStartedPlugins() {
		return startedPlugins;
	}

	public void setStartedPlugins(List<String> startedPlugins) {
		this.startedPlugins = startedPlugins;
	}

	public List<String> getUnresolvedPlugins() {
		return unresolvedPlugins;
	}

	public void setUnresolvedPlugins(List<String> unresolvedPlugins) {
		this.unresolvedPlugins = unresolvedPlugins;
	}

	public void setOnestartedPlugin(String name) {

		this.startedPlugins.add(name);

	}

	public void setOneunresolvedPlugin(String name) {
		this.unresolvedPlugins.add(name);

	}

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
