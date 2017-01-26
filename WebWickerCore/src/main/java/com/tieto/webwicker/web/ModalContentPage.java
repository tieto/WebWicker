package com.tieto.webwicker.web;


import org.apache.wicket.PageReference;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

public class ModalContentPage extends WebPage {

    public ModalContentPage(final PageReference modalWindowPage,
            final ModalWindow window) {

        // Retrieve the passValue content for display.
        String passValue = ((HomePage) modalWindowPage.getPage())
                .getPassValue();
        add(new Label("passValueLabel", passValue));

        // You can use the
        // ((LaunchPage)modalWindowPage.getPage()).setPassValue() method to
        // change the passValue variable of the launch/caller page.
    }
}