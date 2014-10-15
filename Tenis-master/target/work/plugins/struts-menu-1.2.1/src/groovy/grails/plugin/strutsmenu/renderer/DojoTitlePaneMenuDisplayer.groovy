package grails.plugin.strutsmenu.renderer

import net.sf.navigator.menu.MenuComponent
import grails.plugin.strutsmenu.adapter.AbstractMenuDisplayerAdapter
import net.sf.navigator.displayer.MenuDisplayerMapping
import javax.servlet.jsp.JspException
import javax.servlet.http.HttpServletRequest

/**
 * Example showing a Dojo Menu
 * 
 * @author Colm Brady
 * 
 */

public class DojoTitlePaneMenuDisplayer extends AbstractMenuDisplayerAdapter {

    public void start() {
        // not really needed for this menu
    }

    public void end() {
        // not really needed for this menu
    }

    public void display(MenuComponent menu) {
        if (isAllowed(menu)) {
            out.println( "<div dojoType=\"dijit.TitlePane\" title=\""+menu.getName()+"\" open=\"false\">" );
            displayComponents(menu, 0);
            out.println("</div>");
        }
    }

    protected void displayComponents(MenuComponent menu, int level)
    throws JspException, IOException {

        MenuComponent[] components = menu.getMenuComponents();

        if (components.length > 0) {
            // eliminate spaces in string used for Id
            String domId = getMessage(menu.getName());
            // added to create a unique id everytime
            domId += ((int) (1000*Math.random()));

            // if there is a location/page/action tag on base item use it
            if (menu.getUrl() != null ){
                out.println("<a id=\""+domId+"\" href=\""+getMessage(menu.getUrl())+"\">"+getMessage(menu.getTitle())+"</a>");
            } else {
                out.println("<a href=\"#\" id=\""+domId+"\">"+getMessage(menu.getTitle())+"</a>");
            }

            for (int i = 0; i < components.length; i++) {
                // check the permissions on this component
                if (isAllowed(components[i])) {
                    out.println("<div>");
                    displayComponents(components[i], level + 1);
                    out.println("</div>");
                }
            }
        } else {

            String link = getHrefLink( menu )
            out.println( link )

        }
    }
    

    private String getHrefLink ( MenuComponent menu ) {
        return "<a href=\""+menu.getUrl()+"\" title=\""+super.getMenuToolTip(menu)+"\" >"+getMessage(menu.getTitle())+"</a>";
    }



    public StringBuffer renderCssAndJavascript(HttpServletRequest request) {
        return new StringBuffer(
                '''  <script src="http://ajax.googleapis.com/ajax/libs/dojo/1.6/dojo/dojo.xd.js" djConfig="parseOnLoad: true"></script>
        <script type="text/javascript">
            dojo.require("dijit.TitlePane");
        </script>
        <link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/dojo/1.6/dijit/themes/claro/claro.css"/>''');
    }


}