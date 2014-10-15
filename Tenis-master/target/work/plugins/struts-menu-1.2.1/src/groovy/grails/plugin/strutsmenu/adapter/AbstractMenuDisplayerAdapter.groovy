package grails.plugin.strutsmenu.adapter

import net.sf.navigator.displayer.AbstractMenuDisplayer
import net.sf.navigator.displayer.MenuDisplayerMapping
import net.sf.navigator.menu.MenuComponent
import org.springframework.context.MessageSource
import javax.servlet.http.HttpServletRequest
import net.sf.navigator.menu.PermissionsAdapter
import net.sf.navigator.menu.RolesPermissionsAdapter
import grails.plugin.strutsmenu.acl.PluginPermissionsAdapter

/**
 *
 * Simple abstract class providing most of the original methods and functionality from Struts Menu tab lib.
 *
 * All Menu Renderer impls should extend this class, specifically they should implement method "start", "display" and "end".
 *
 * @author Colm Brady
 */

public abstract class AbstractMenuDisplayerAdapter extends AbstractMenuDisplayer implements MenuDisplayerAdapter {

    //~ Instance fields ========================================================

    protected MessageSource messageSource = null;
    protected Locale locale = null;
    protected PrintWriter out;
    protected String id;
    protected PermissionsAdapter permissionsAdapter = null;
    protected HttpServletRequest request;

    /**
     * {@inheritDoc}
     */
     @Override
    public void init(String id, PrintWriter out, MenuDisplayerMapping mapping, String permissions, HttpServletRequest request ) {
        this.out = out;
        this.id = id;
        super.mapping = mapping;
        this.request =request;
        this.permissionsAdapter = this.getPermissionsAdapter( permissions )
    }

    /**
     * Returns <code>true</code> if the specified component is usable.
     * If <code>permissionsAdapter</code> is not defined, this method will
     * return <code>true</code>.  Otherwise, the adapter will be used to check
     * permissions on the menu.
     *
     * @return <code>true</code> if the menu component is usable.
     * @param menu The menu component.
     */
    public boolean isAllowed(MenuComponent menu) {
        return permissionsAdapter == null || permissionsAdapter.isAllowed(menu);
    }

    /**
     * Return a permissions adapter, this method is somewhat backward compatible with struts menu. It will return a
     * Struts Menu RolesPermissionsAdapter if String "rolesAdapter" if used as the permissions param.
     *
     * Otherwise, it will assume that the permissions param is a class and try to load it dynamically. this is a little
     * crude mechanism, but it seems overkill to map a class name to a string identifier.
     */
    protected PermissionsAdapter getPermissionsAdapter( String permissions ) {
        PermissionsAdapter adapter = null;

        if (permissions != null) {
            // If set to "rolesAdapter", then create legacy struts menu permission adapter automatically
            if (permissions.equalsIgnoreCase(ROLES_ADAPTER)) {
                adapter =
                    new RolesPermissionsAdapter((HttpServletRequest) request);
            }
            else {
                try {
                    adapter = (PluginPermissionsAdapter) Thread.currentThread().contextClassLoader.loadClass( permissions ).newInstance();
                    adapter.setHttpRequest ( request ) 
                }
                catch ( Exception e) {
                    log.warn("Tried to dynamically load adapter class, falling back to request scope" )
                    adapter = (PermissionsAdapter) request.getAttribute(permissions);
                }

                if (adapter == null) {
                    log.warn("Adapter is null")
                }
            }
        }

        return adapter;
    }

    /**
     * {@inheritDoc}
     */
     @Override
    public void displayMenu( MenuComponent[] topMenus, String menuName ) throws Exception {

         // Call Start to render begining of menu
        start()

        for (int i = 0; i < topMenus.length; i++) {
            MenuComponent menu = topMenus[i];

            // code loosely migrated from struts menu display tag lib
            if (menuName && menu.getName().toLowerCase().startsWith(menuName.toLowerCase())) {
                if (isAllowed(menu)) {
                    try {
                        // set the location value to use
                        // the context relative page attribute
                        // if specified in the menu
                        try {
                            setPageLocation(menu, request);
                        } catch (MalformedURLException m) {
                            log.error("Incorrect action or forward: " + m.getMessage())
                            log.warn("Menu '" + menu.getName() + "' location set to #")
                            menu.setLocation("#");
                        }

                        display(menu);
                        setTarget(null);
                    } catch (Exception e) {
                        throw e;
                    }
                }
            }

        }

        // Call end to render end of menu
        end()
    }

    /**
     * Implementations of this method should render the meat and bones of a menu, I.E recursivly iterate over the menu
     * components until the menu is completely rendered.
     *
     * @param menu the menu component to render
     */
    public abstract void display ( MenuComponent menu );

    /**
     * Implementations should close of the HTML menu, or leave empty if not needed
     */
    protected abstract void end ();

    /**
     * Implementations should start the HTML menu, or leave empty if not needed
     */
    protected abstract void start ();

    //~ Methods ================================================================

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public MessageSource getMessageSource() {
        return this.mesageSource;
    }

    public void setMessageSource(MessageSource messages) {
        this.messageSource = messages;
    }

    /**
     * Get the title key from the bundle (if it exists).  This method
     * is public to expose it to Velocity.
     *
     * @param key the key
     */
    public String getMessage(String key) {
        String message = null;

        if (messageSource != null) {
            try {
                message = messageSource.getMessage(key, null, key, this.locale);
            } catch (MissingResourceException mre) {
                message = null;
            }
        }

        if (message == null) {
            message = key;
        }

        return message;
    }

    /**
     * Get the menu's tooltip (if it exists).
     *
     * @param menu the menu
     */
    public String getMenuToolTip(MenuComponent menu) {
        String tooltip;

        if (menu.getToolTip() != null) {
            tooltip = this.getMessage(menu.getToolTip());
        } else {
            tooltip = this.getMessage(menu.getTitle());
        }

        return tooltip;
    }

    /**
     * Get the menu on click (if it exists).
     *
     * @param menu the menu
     */
    public String getMenuOnClick(MenuComponent menu) {
        if (menu.getOnclick() != null) {
            return " onclick=\"" + menu.getOnclick() + "\"";
        }
        return "";
    }

    /**
     * Migration from Struts Menu, adaptered for Grails.
     *
     * Sets the value for the menu location to the
     * appropriate value if location is null.  If location
     * is null, and the page attribute exists, it's value
     * will be set to the the value for page prepended with
     * the context path of the application.
     *
     * If the page is null, and the forward attribute exists,
     * it's value will be looked up in struts-config.xml.
     *
     * FIXME - ssayles - 121102
     * Ideally, this should happen at menu initialization but
     * I was unable to find a reliable way to get the context path
     * outside of a request.  The performance impact is probably
     * negligable, but it would be better to check for this only once.
     *
     * @param menu The menu component to set the location for.
     * @param request the http servlet request
     */
    protected void setPageLocation(MenuComponent menu, HttpServletRequest request) throws MalformedURLException {
        setLocation(menu, request);
        setImageSrc(menu)
        String url = menu.getLocation();

        // Check if there are parameters on the value
        if ((url != null) /*&& (url.indexOf("${") > -1)*/) {
            String queryString = null;

            if (url.indexOf("?") > -1) {
                queryString = url.substring(url.indexOf("?") + 1);
                url = url.substring(0, url.indexOf(queryString));
            }

            // variable is in the URL
            if (queryString != null) {
                menu.setUrl(url + parseString(queryString, request));
            } else {
                // parse the URL, rather than the queryString
                menu.setUrl(parseString(url, request).toString() + getParameterString(request.params).toString());
            }
        } else {
            menu.setUrl(url);
        }

        if (menu.getUrl() != null) {
            // todo: should encode url -> response.encodeURL(menu.getUrl())
            menu.setUrl(menu.getUrl());
        }

        // do all contained menus using recursion..
        MenuComponent[] subMenus = menu.getMenuComponents();

        if (subMenus.length > 0) {
            for (int i = 0; i < subMenus.length; i++) {
                this.setPageLocation(subMenus[i], request);
            }
        }
    }

    protected void setLocation(MenuComponent menu, HttpServletRequest request) throws MalformedURLException {
        // if the location attribute is null, then set it with a context relative page
        // attribute if it exists
        if (menu.getLocation() == null && menu.getPage() != null) {

            menu.setLocation(request.getContextPath() + getPage(menu.getPage()));
            // todo: encode url -> response.encodeURL
            menu.setLocation(menu.getLocation());

        }
    }

    protected void setImageSrc(MenuComponent menu) throws MalformedURLException {
        // if the location attribute is null, then set it with a context relative page
        // attribute if it exists
        if (menu.getImage() != null && !menu.getImage().startsWith(request.getContextPath())) {

            menu.setImage(request.getContextPath() + menu.getImage());

        }
    }

    /**
     * Returns the value with page prepended with a "/"
     * if it is not already.
     *
     * @param page The value for the page.
     */
    protected String getPage(String page) {
        if (page.startsWith("/")) {
            return page;
        } else {
            page = "/" + page;
        }

        return page;
    }

    // Util to help build URL, based on Struts Menu code.
    private StringBuffer parseString(String str, HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();

        while (str.indexOf("{") >= 0) {
            sb.append(str.substring(0, str.indexOf("{")))

            String variable = str.substring(str.indexOf("{") + 2, str.indexOf("}"))

            String value = String.valueOf(request.getAttribute(variable))

            if (value == null) {
                // look for it as a request parameter
                value = request.getParameter(variable);
            }

            // is value still null?!
            if (value == null) {
                log.warn("Value for '" + variable +
                        "' not found in pageContext or as a request parameter");
            }

            sb.append(value);
            str = str.substring(str.indexOf("}") + 1, str.length());
        }

        return sb.append(str)
    }

    // Util method to add request params to menu if needed.
    private StringBuffer getParameterString(Map parameters) {
        StringBuffer buffer = new StringBuffer()

        if (!parameters || parameters.isEmpty()) {
            return buffer
        }

        Iterator elements = parameters.keySet().iterator()

        if (elements.hasNext()) {
            buffer.append("?")
        }
        // We could use HttpUtil.isHttpParameterAvailable, but it probably would require more
        // passes over the request parameters then doing it manually.
        while (elements.hasNext()) {
            final String paramName = (String) elements.next()
            final String value = (String) parameters.get(paramName)

            buffer.append(paramName + "=" + value)

            if (elements.hasNext()) {
                buffer.append("&")
            }
        }

        return buffer
    }



}