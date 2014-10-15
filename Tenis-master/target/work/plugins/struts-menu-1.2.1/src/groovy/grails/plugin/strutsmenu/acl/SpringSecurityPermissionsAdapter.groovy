package grails.plugin.strutsmenu.acl

//import org.springframework.security.core.context.SecurityContextHolder as SCH

import org.apache.juli.logging.Log
import org.apache.juli.logging.LogFactory
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest
import net.sf.navigator.menu.MenuComponent

/**
 *
 * Example of how you might write a Spring Security ACL adapter..
 * 
 * @author Colm Brady
 *
 */

public class SpringSecurityPermissionsAdapter implements PluginPermissionsAdapter {

    private Pattern delimiters = Pattern.compile("(?<!\\\\),");

    private static Log log = LogFactory.getLog(SpringSecurityPermissionsAdapter.name)

    public SpringSecurityPermissionsAdapter(  ) {
    }


    public void setHttpRequest ( HttpServletRequest request ) {
        // note needed for this impl
    }

    /**
     * If the menu is allowed, this should return true.
     *
     * @return whether or not the menu is allowed.
     */
    public boolean isAllowed( final MenuComponent menu) {
        if (menu.getRoles() == null) {
            return true; // no roles define, allow everyone
        } else {
            // todo: make ths more efficent
            // Get the list of roles this menu allows
            List<String> allowedRoles = new ArrayList<String>( Arrays.asList(  delimiters.split(menu.getRoles()) ))
            String role
            try {
                role = null//SCH.context?.authentication?.getAuthorities()

                if (role && role.contains(allowedRoles[i])) {
                    return true;
                }

            }
            catch (Exception e) {
                log.error ( 'Error checking permission', e )
            }
        }
        return false;
    }
}