package grails.plugin.strutsmenu.acl

import net.sf.navigator.menu.PermissionsAdapter
import javax.servlet.http.HttpServletRequest

/**
 * Simple adapter interface to plug in ACL impls for struts menu.
 * 
 */
public interface PluginPermissionsAdapter extends PermissionsAdapter {
    
    public void setHttpRequest( HttpServletRequest request )

}