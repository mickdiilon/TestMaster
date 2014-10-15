package grails.plugin.strutsmenu.adapter

import net.sf.navigator.displayer.MenuDisplayer
import net.sf.navigator.displayer.MenuDisplayerMapping
import net.sf.navigator.menu.MenuComponent
import javax.servlet.http.HttpServletRequest

/**
 *
 * Adapter interface which is a simple bridge between JSP TagLib Struts Menu and this Grails Impl.
 *
 * @author Colm Brady
 */
public interface MenuDisplayerAdapter extends MenuDisplayer {

    // this is a constant duplicated from StrutsMenu
    public static final String ROLES_ADAPTER = "rolesAdapter";

    /**
     * Simple init method interface allowing the TagLib renderer be created.
     *
     * @param id a unique menu id
     * @param out the print writer to output the menu to the GSP stream.
     * @param mapping the struts menu mapper
     * @param permissions the permission adapter to locate.
     * @param request the servlet request, which we can use in our menu to get attributes or parameters.
     */
    void init(String id, PrintWriter out, MenuDisplayerMapping mapping, String permissions, HttpServletRequest request )

    /**
     * @param topMenus the top level struts meuu components
     * @param menuName the menuName of the menu we are rendering.
     */
    void displayMenu ( MenuComponent[] topMenus, String menuName ) throws Exception

    /**
     * Return a string buffer of the CSS and Javascript that the menu impl needs.
     *
     * @return a string buffer of the CSS and Javascript that the menu impl needs.
     */
    StringBuffer renderCssAndJavascript ( HttpServletRequest request )
}