<html>
    <head>
        <title>Yui Sample</title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <sm:renderCssAndJavascript  name="YUIDisplayer"/>
    </head>
    <body class="yui-skin-sam">

        <div id="menu">
          <sm:displayMenu id="navigationDiv" name="YUIDisplayer" menuName="NavMenu"/>
          <!--<sm:displayMenu id="navigationDiv" name="YUIDisplayer" permissions="grails.plugin.strutsmenu.acl.SimpleRolePermissionsAdapter" menuName="NavMenu"/>-->
        </div>

    </body>
</html>