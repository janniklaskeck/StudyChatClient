package stud.mi.gui;

import org.vaadin.cdiviewmenu.ViewMenuUI;

import com.vaadin.annotations.Title;
import com.vaadin.cdi.CDIUI;
import com.vaadin.ui.UI;

@CDIUI("")
@Title("Student Chat Server")
public class Main extends ViewMenuUI {

    private static final long serialVersionUID = -720922173696713289L;

    public static Main get() {
        return (Main) UI.getCurrent();
    }
}