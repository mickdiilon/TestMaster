<html>
    <head>
        <title>Dojo Sample</title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <sm:renderCssAndJavascript  name="DojoDisplayer"/>
    </head>
    <body class="claro">

        <div id="menu">
          <sm:displayMenu id="navigationDiv" name="DojoDisplayer" menuName="NavMenu"/>
          <!--<sm:displayMenu id="navigationDiv" name="DojoDisplayer" permissions="grails.plugin.strutsmenu.acl.SimpleRolePermissionsAdapter" menuName="NavMenu"/>-->
        </div>

    </body>
</html>