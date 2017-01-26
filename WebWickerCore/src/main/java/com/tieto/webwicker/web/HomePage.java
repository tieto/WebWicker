package com.tieto.webwicker.web;

import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.StyleAttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import com.tieto.webwicker.WebWickerApplication;
import com.tieto.webwicker.api.conf.Configuration;
import com.tieto.webwicker.api.web.WebWickerPageFactory;

import ro.fortsoft.pf4j.PluginManager;
import ro.fortsoft.wicket.plugin.PluginManagerInitializer;
import ro.fortsoft.pf4j.PluginWrapper;

public class HomePage extends WebPage {
	private static final long serialVersionUID = -7626368252793216083L;
	private final Configuration configuration;
	private List<WebWickerPageFactory> subPages;
	
	public static final int ORDER = 0;
	 private String passValue;
	 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HomePage(final PageParameters parameters) {
		super(parameters);
		
		configuration = WebWickerApplication.get().getConfiguration();
		
		RepeatingView listItems = new RepeatingView("listItems");
		subPages = configuration.getTopPageFactories();

		for(final WebWickerPageFactory subPage : subPages) {
			PageParameters params = new PageParameters();
			params.add("page", subPage.getPageClassName());
			WebMarkupContainer list = new WebMarkupContainer(listItems.newChildId());
			BookmarkablePageLink link = new BookmarkablePageLink("Link", getClass(), params);
			link.add(new Label("Text", subPage.getPageTitle()));
			if(pageMatchesLink(parameters.get("page").toString(), subPage.getPageClassName())) {
				link.add(StyleAttributeModifier.append("class", "active"));
			}
			list.add(link);
	        listItems.add(list);
		}
		
		add(listItems);
		add(configuration.getPageFactory(parameters.get("page").toString()).create("webwickerpanel", parameters, configuration));
	   	
		passValue = "value_modal_window.";

        // Display the current content of the passValue variable. The
        // PropertyModel must be used, as the value can be changed.
		PluginManager manager = null;
		
        final Label passValueLabel;
        add(passValueLabel = new Label(manager.getStartedPlugins().get(0).getPluginId(),
                new PropertyModel<String>(this, "passValue")));
        passValueLabel.setOutputMarkupId(true);

        // Create the modal window.
        final ModalWindow modal;
        add(modal = new ModalWindow("modal"));
        modal.setCookieName("modal-1");

        modal.setPageCreator(new ModalWindow.PageCreator() {
            public Page createPage() {
                // Use this constructor to pass a reference of this page.
                return new ModalContentPage(HomePage.this.getPageReference(),
                        modal);
            }
        });
        modal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            public void onClose(AjaxRequestTarget target) {
                // The variable passValue might be changed by the modal window.
                // We need this to update the view of this page.
                target.add(passValueLabel);
            }
        });
        modal.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
            public boolean onCloseButtonClicked(AjaxRequestTarget target) {
                // Change the passValue variable when modal window is closed.
                setPassValue("Modal window is closed by user.");
                return true;
            }
        });

        // Add the link that opens the modal window.
        add(new AjaxLink<Void>("showModalLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                modal.show(target);
            }
        });
		
	}
	
	
	public String getPassValue() {
        return passValue;
    }

    public void setPassValue(String passValue) {
        this.passValue = passValue;
    }
	
	private boolean pageMatchesLink(final String page, final String name) {
		if(page == null) {
			return StartPage.class.getName().equals(name);
		}
		return page.equals(name);
	}
	
	public PluginManager getPluginManager() {
		return getMetaData(PluginManagerInitializer.PLUGIN_MANAGER_KEY);
	}
	
	
}
