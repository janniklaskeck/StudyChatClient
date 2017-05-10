package stud.mi.gui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@Title("Student Chat")
@Theme("valo")
public class Main extends UI {

    private static final long serialVersionUID = -720922173696713289L;

    @Override
    protected void init(VaadinRequest request) {
        initContent();
    }

    private void initContent() {
        setContent(new MainLayout(2, 4));
    }

    @WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = Main.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
        private static final long serialVersionUID = -5921182411818280724L;
    }

}