package grails.plugin.strutsmenu.taglibs

import net.sf.navigator.displayer.MenuDisplayerMapping
import net.sf.navigator.menu.MenuRepository
import net.sf.navigator.displayer.MenuDisplayer
import grails.plugin.strutsmenu.adapter.MenuDisplayerAdapter

/**
 * Menu Display Tag. This tag libray renders a Struts Menu by instantiating a Renderer instance.
 */
class MenuDisplayTagLib {

    static namespace = 'sm'

    // this is a constant duplicated from StrutsMenu
    public static final String PRIVATE_REPOSITORY = "net.sf.navigator.repositoryKey";
    // this is a constant duplicated from StrutsMenu
    public static final String DISPLAYER_KEY = "net.sf.navigator.taglib.DISPLAYER";
    // this is a constant duplicated from StrutsMenu
    public static final String MENU_ID = "net.sf.navigator.MENU_ID";

    // inject in the message source
    def messageSource

    def permissionAdapter

    public MenuDisplayTagLib() {}

    private MenuRepository getMenuRepository ( def servletContext, def repository ) {
        // get the menu repository
        MenuRepository rep = (MenuRepository) servletContext.getAttribute(repository)

        if (rep == null) {
            String error = "Can find menu repository [ " + repository + "]"
            log.error( error, new Exception() )
            out.println( error )
            return null
        }
        return rep
    }

    private MenuDisplayerAdapter getDisplayerInstance ( def name, def repository ){
        //get an instance of the menu displayer
        MenuDisplayerAdapter displayerInstance

        try {
            final String className = repository.getDisplayers().get( name ).getType();
            displayerInstance = (MenuDisplayerAdapter) Thread.currentThread().contextClassLoader.loadClass( className ).newInstance();
        } catch (Exception e) {
            log.error("Cant create menu displayer", e)
            out.println("Cant create menu!")
            return null
        }

        return displayerInstance
    }

    /**
     * Render the CSS and Javascript for this menu
     */
    def renderCssAndJavascript = {attrs, body ->
        def name = attrs.name
        def repository = MenuRepository.MENU_REPOSITORY_KEY
        if (attrs.repository) repository = attrs.repository

        // get the menu repository
        MenuRepository rep = getMenuRepository ( servletContext, repository );

        if (rep == null) {
            return
        }

        // get the displayer mapping instance
        MenuDisplayerMapping displayerMapping = rep.getMenuDisplayerMapping(name)

        if (displayerMapping == null) {
            log.error( "Displayer mapping is null", new Exception() )
            return
        }

        //get an instance of the menu displayer
        MenuDisplayerAdapter displayerInstance = this.getDisplayerInstance( name, rep )

        out << displayerInstance.renderCssAndJavascript( request ).toString()
    }
    // This is the tag library entry point
    def displayMenu = {attrs, body ->

        def bundleKey

        def id = attrs.id
        def name = attrs.name
        def menuName = attrs.menuName
        def localeKey = attrs.localeKey
        def permissions = (attrs.permissions) ? attrs.permissions : MenuDisplayerAdapter.ROLES_ADAPTER
        def repository = (attrs.repository) ? attrs.repository : MenuRepository.MENU_REPOSITORY_KEY

        out << body() << renderMenu( id, name, repository, permissions, localeKey, menuName )
    }

    // method renders the Menu Taglibrary
    def renderMenu( def id, def name, def repository, def permissions, def localeKey, def menuName ) {

        def config = MenuDisplayer.DEFAULT_CONFIG

        if (log.isDebugEnabled()) {
            log.debug("Looking for menu repository named '" + repository + "'")
        }

        // get the menu repository
        MenuRepository rep = getMenuRepository ( servletContext, repository );

        if (rep == null) {
            return
        }
        else {
            request.setAttribute(PRIVATE_REPOSITORY, rep)
        }

        // get the displayer mapping instance
        MenuDisplayerMapping displayerMapping = rep.getMenuDisplayerMapping(name)

        if (displayerMapping == null) {
            log.error( "Displayer mapping is null", new Exception() )
            return
        }

        //get an instance of the menu displayer
        MenuDisplayerAdapter displayerInstance = this.getDisplayerInstance( name, rep )

        if (displayerInstance == null) {
            return
        }

        // default to use the config on the mapping
        if (displayerMapping.getConfig() != null) {
            // this value (config) is set on the displayer below
            config = displayerMapping.getConfig();
        }


        Locale locale;

        // backward compatible with old struts menu code,
        // must be needed in the core menu taglib
        if (localeKey == null) {
            locale = Locale.getDefault()
        }
        else {
            locale = (Locale) request.getAttribute(localeKey);
        }

        // initialise the displayer...
        displayerInstance.setLocale(locale);
        displayerInstance.setMessageSource(messageSource)
        // Not sure what the config setting was used for
        // in Struts Menu, maybe this should be removed.
        displayerInstance.setConfig(config);

        // init the component
        displayerInstance.init(id, out, displayerMapping, permissions, request);
        // render the method
        displayerInstance.displayMenu ( rep.getTopMenusAsArray(), menuName )

    }


}

