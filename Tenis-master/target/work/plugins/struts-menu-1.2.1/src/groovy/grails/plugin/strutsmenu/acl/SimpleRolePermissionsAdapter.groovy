package grails.plugin.strutsmenu.acl

import javax.servlet.http.HttpServletRequest
import net.sf.navigator.menu.MenuComponent
import java.util.regex.Pattern
import org.apache.juli.logging.Log
import org.apache.juli.logging.LogFactory

/**
 * 
 * @author Colm Brady
 */

public class SimpleRolePermissionsAdapter implements PluginPermissionsAdapter {
    private Pattern delimiters = Pattern.compile("(?<!\\\\),");
    private static Log log = LogFactory.getLog(SimpleRolePermissionsAdapter.name)

    private HttpServletRequest request = null;

    public SimpleRolePermissionsAdapter(  ) {
    }


    public void setHttpRequest ( HttpServletRequest request ) {
        this.request = request;
    }

    /**
     * If the menu is allowed, this should return true.
     *
     * @return whether or not the menu is allowed.
     */
    public boolean isAllowed( final MenuComponent menu) {
        if (menu.getRoles() == null || menu.getRoles().equals( "" ) ) {
            return true; // no roles define, allow everyone
        } else {
            if ( request.getUserPrincipal() == null ) {
                return false; // not authenticated - dont show menu
            }

            String roleName = request.getUserPrincipal().getName()
            List<String> allowedRoles = new ArrayList<String>( Arrays.asList( delimiters.split(menu.getRoles()) ))
            try {

                for ( String allowedRole : allowedRoles ) {
                    if ( allowedRole.equals( roleName ) ) {
                        return true;
                    }
                }
                return false;
            }
            catch (Exception e) {
                log.error ( 'Error checking permission', e )
            }
        }
        return false;
    }

}