package com.tieto.webwicker.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.PageReference;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.protocol.http.WebApplication;

import com.tieto.webwicker.PluginInfo;
import com.tieto.webwicker.WebWickerApplication;

public class ModalContentPage extends WebPage {

	private static final long serialVersionUID = 1L;

	public ModalContentPage(final PageReference modalWindowPage, final ModalWindow window) {

		List<PluginInfo> plugins = new ArrayList<>();

		for (PluginInfo plugin : ((WebWickerApplication) WebApplication.get()).getSystemInformation().getPlugins()) {
			plugins.add(plugin);
		}

		ListDataProvider<PluginInfo> listDataProvider = new ListDataProvider<PluginInfo>(plugins);
		DataView<PluginInfo> dataView = new DataView<PluginInfo>("rows", listDataProvider) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<PluginInfo> item) {
				
				PluginInfo plugin = item.getModelObject();
				RepeatingView repeatingView = new RepeatingView("dataRow");

				repeatingView.add(new Label(repeatingView.newChildId(), plugin.getName()));
				repeatingView.add(new Label(repeatingView.newChildId(), plugin.getVersion()));
				repeatingView.add(new Label(repeatingView.newChildId(), plugin.getStatus()));

				item.add(repeatingView);
			}
		};
		add(dataView);
	
	}

}