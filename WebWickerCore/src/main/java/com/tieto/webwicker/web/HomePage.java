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
import org.apache.wicket.request.mapper.parameter.PageParameters;
import com.tieto.webwicker.WebWickerApplication;
import com.tieto.webwicker.api.conf.Configuration;
import com.tieto.webwicker.api.web.WebWickerPageFactory;

public class HomePage extends WebPage {

	private static final long serialVersionUID = -7626368252793216083L;
	private final Configuration configuration;
	private List<WebWickerPageFactory> subPages;

	public static final int ORDER = 0;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HomePage(final PageParameters parameters) {
		super(parameters);

		configuration = WebWickerApplication.get().getConfiguration();

		RepeatingView listItems = new RepeatingView("listItems");
		subPages = configuration.getTopPageFactories();

		for (final WebWickerPageFactory subPage : subPages) {
			PageParameters params = new PageParameters();
			params.add("page", subPage.getPageClassName());
			WebMarkupContainer list = new WebMarkupContainer(listItems.newChildId());
			BookmarkablePageLink link = new BookmarkablePageLink("Link", getClass(), params);
			link.add(new Label("Text", subPage.getPageTitle()));
			if (pageMatchesLink(parameters.get("page").toString(), subPage.getPageClassName())) {
				link.add(StyleAttributeModifier.append("class", "active"));
			}
			list.add(link);
			listItems.add(list);
		}

		add(new Label("title", configuration.getSettings().getSetting("WebWicker", "title").orElse("WebWicker")));
		add(listItems);
		add(configuration.getPageFactory(parameters.get("page").toString()).create("webwickerpanel", parameters,
				configuration));

		final ModalWindow modal = createSystemPopUpWindow();
		modal.setEscapeModelStrings(false);
		// Add the link that opens the modal window.
		add(new AjaxLink<Void>("showModalLink") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				modal.show(target);
			}
		});
	}

	private ModalWindow createSystemPopUpWindow() {

		final ModalWindow systemPopup =  new ModalWindow("modal");
		
		add(systemPopup);
		systemPopup.setCookieName("modal-1");

		systemPopup.setPageCreator(new ModalWindow.PageCreator() {

			private static final long serialVersionUID = 1L;

			public Page createPage() {

				return new PluginPopUpPage(HomePage.this.getPageReference(), systemPopup);
			}
		});
		systemPopup.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {

			private static final long serialVersionUID = 1L;

			public void onClose(AjaxRequestTarget target) {
			}
		});
		systemPopup.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {

			private static final long serialVersionUID = 1L;

			public boolean onCloseButtonClicked(AjaxRequestTarget target) {
				return true;
			}
		});
		return systemPopup;
	}

	private boolean pageMatchesLink(final String page, final String name) {
		if (page == null) {
			return StartPage.class.getName().equals(name);
		}
		return page.equals(name);
	}

}
