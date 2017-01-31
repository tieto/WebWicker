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
		
		add(new Label("title", configuration.getSettings().getSetting("WebWicker", "title").orElse("WebWicker")));
		add(listItems);
		add(configuration.getPageFactory(parameters.get("page").toString()).create("webwickerpanel", parameters, configuration));
	   	
		passValue = "";

        // Display the current content of the passValue variable. The
        // PropertyModel must be used, as the value can be changed.
		
        final Label passValueLabel;
        add(passValueLabel = new Label("passValueLabel",new PropertyModel<String>(this, "passValue")));
        passValueLabel.setOutputMarkupId(true);
        passValueLabel.setEscapeModelStrings(false);

        final ModalWindow modal = createSystemPopUpWindow(passValueLabel);
        modal.setEscapeModelStrings(false);
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
	private ModalWindow createSystemPopUpWindow(final Label passValueLabel) {
		// Create the modal window.
        final ModalWindow systemPopup;
        add(systemPopup = new ModalWindow("modal"));
        systemPopup.setCookieName("modal-1");

        systemPopup.setPageCreator(new ModalWindow.PageCreator() {
            public Page createPage() {
                // Use this constructor to pass a reference of this page.
                return new ModalContentPage(HomePage.this.getPageReference(),
                        systemPopup);
            }
        });
        systemPopup.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            public void onClose(AjaxRequestTarget target) {
                // The variable passValue might be changed by the modal window.
                // We need this to update the view of this page.
                target.add(passValueLabel);
            }
        });
        systemPopup.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
            public boolean onCloseButtonClicked(AjaxRequestTarget target) {
                 return true;
  
            }
        });
		return systemPopup;
	}
	

	private boolean pageMatchesLink(final String page, final String name) {
		if(page == null) {
			return StartPage.class.getName().equals(name);
		}
		return page.equals(name);
	}
		
}
