package grails.plugin.strutsmenu.renderer

import javax.servlet.jsp.JspException
import net.sf.navigator.displayer.MenuDisplayerMapping
import net.sf.navigator.menu.MenuComponent

import grails.plugin.strutsmenu.adapter.AbstractMenuDisplayerAdapter
import javax.servlet.http.HttpServletRequest

/**
 * Example showing a YUI Menu
 * 
 * @author Colm Brady
 * 
 */
public class YUIMenuDisplayer extends AbstractMenuDisplayerAdapter {

    protected void start() {
        StringBuffer buffer = new StringBuffer()
        buffer.append( "<script type=\"text/javascript\">\n" )
        buffer.append( "YAHOO.util.Event.onContentReady(\""+id+"\", function () {\n" )
        buffer.append( "var oMenuBar = new YAHOO.widget.MenuBar(\""+id+"\", {\n" )
        buffer.append( "autosubmenudisplay: true,\n" )
        buffer.append( "hidedelay: 750,\n" )
        buffer.append( "lazyload: true });\n" )
        buffer.append( "oMenuBar.render();});\n" )
        buffer.append( "</script>" )
        out.println ( buffer.toString() )
        out.println( this.getMenuStartDiv( id ) );    
    }

    public void init(String id, PrintWriter out, MenuDisplayerMapping mapping, String permissions, HttpServletRequest request ) {
        super.init(id, out, mapping, permissions, request);
    }

    public void display(MenuComponent menu) {
        if (isAllowed(menu)) {
            out.println( "<li class=\"yuimenubaritem first-of-type\">" );
            displayComponents(menu, 0);
            out.println("</li>");
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

            boolean toplevel = true;

            if (level >= 1) {
                toplevel = false;
            }

            // if there is a location/page/action tag on base item use it
            if (menu.getUrl() != null ){
                out.println("<a href=\"#\" id=\""+domId+"\" href=\""+getMessage(menu.getUrl())+"\" class=\"yuimenuitemlabel\">"+getMessage(menu.getTitle())+"</a>");

                out.println("<div id=\""+domId+"Title\" class=\"yuimenu\">");
                out.println("<div class=\"bd\">");
                out.println("<ul>");
            } else {
                out.println("<a href=\"#\" id=\""+domId+"\" class=\"yuimenuitemlabel\">"+getMessage(menu.getTitle())+"</a>");
                out.println("<div id=\""+domId+"Title\" class=\"yuimenu\">");
                out.println("<div class=\"bd\">");
                //if ( toplevel ) {
                  out.println("<ul id=\""+domId+"Menu\" class=\"first-of-type\">");
                //}
                //else {
                  //out.println("<ul id=\""+domId+"Menu\">");
                //}
            }

            for (int i = 0; i < components.length; i++) {
                // check the permissions on this component
                if (isAllowed(components[i])) {
                    if (components[i].getMenuComponents().length > 0) {
                        out.println("<li class=\"yuimenuitem\">");
                        displayComponents(components[i], level + 1);
                        out.println("</ul></div></li>");
                    } else {

                        out.println( this.getListItem( components[i] ) );

                    }
                }
            }

            // close the </ul> for the top menu
            if (toplevel) {
                out.println("</ul></div></div>");
            }

        } else {
            if (menu.getParent() == null) {
                out.println( this.getHrefLink( menu ));
            } else {
                out.println( this.getListItem( menu ));
            }
        }
    }

    private String getHrefLink ( MenuComponent menu ) {
        
            return "<a href=\""+menu.getUrl()+"\" class=\"yuimenuitemlabel\" title=\""+super.getMenuToolTip(menu)+"\">"+getMessage(menu.getTitle())+"</a>"

    }

    private String getListItem ( MenuComponent menu ) {
      return "<li class=\"yuimenuitem\">" + getHrefLink( menu )+ "</li>";
    }

    private String getMenuStartDiv ( String divId ){
      //return "<div id=\"" + divId + "\" class=\"yuimenubar yuimenubarnav\"><div class=\"bd\"><ul class=\"first-of-type\">";
      return "<div id=\"" + divId + "\" class=\"yuimenubar yuimenubarnav\"><div class=\"bd\"><ul>";
    }

    /**
     * This will output the ending HTML code to close tags from the beginning
     *
     */
    public void end() {
        try {
            out.println( "</ul>" );
            out.println( "</div>" );
            out.println("</div>");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public String getExtra(MenuComponent menu) {
        return "";
    }

    public StringBuffer renderCssAndJavascript(HttpServletRequest request) {
        StringBuffer buffer = new StringBuffer();
        buffer.append('''
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/combo?2.9.0/build/reset-fonts-grids/reset-fonts-grids.css&2.9.0/build/menu/assets/skins/sam/menu.css">
<script type="text/javascript" src="http://yui.yahooapis.com/combo?2.9.0/build/yahoo-dom-event/yahoo-dom-event.js&2.9.0/build/container/container_core-min.js&2.9.0/build/menu/menu-min.js"></script>
        ''')
        return buffer;
    }
}