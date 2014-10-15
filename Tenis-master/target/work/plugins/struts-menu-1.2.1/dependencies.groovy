grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {

	inherits 'global'

	log 'warn'

    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

		mavenCentral()
    }
    dependencies {

        runtime 'struts-menu:struts-menu:2.4.3'
        runtime 'commons-digester:commons-digester:2.0'
        compile 'commons-beanutils:commons-beanutils:1.8.0'
    }

}
