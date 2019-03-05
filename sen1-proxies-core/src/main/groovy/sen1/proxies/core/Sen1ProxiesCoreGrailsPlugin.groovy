package sen1.proxies.core

import grails.plugins.*

class Sen1ProxiesCoreGrailsPlugin extends Plugin {

	// the version or versions of Grails the plugin is designed for
	def grailsVersion = "3.3.8 > *"
	// resources that are excluded from plugin packaging
	def pluginExcludes = [
		"grails-app/views/error.gsp"
	]

	def title = "Sen1 Proxies Core" // Headline display name of the plugin
	def author = "Consometers"
	def authorEmail = ""
	def description = '''\
Core Module For SEN1 Applications
'''
	def profiles = ['web']

	// URL to the plugin's documentation
	def documentation = "http://grails.org/plugin/sen1-proxies-core"

	// Extra (optional) plugin metadata

	// License: one of 'APACHE', 'GPL2', 'GPL3'
	def license = "EUPL"

	// Details of company behind the plugin (if there is one)
	def organization = [ name: "Consometers", url: "https://consometers.frama.wiki/" ]

	// Any additional developers beyond the author specified above.
	def developers = [
		[ name: "Gregory Elleouet", email: "gregory.elleouet@gmail.com" ]
		// insert here others developers
	]

	// Location of the plugin's issue tracker.
	def issueManagement = [ system: "GITHUB", url: "https://github.com/consometers/sen1-poc-proxies/issues" ]

	// Online location of the plugin's browseable source code.
	def scm = [ url: "https://github.com/consometers/sen1-poc-proxies" ]

	Closure doWithSpring() { {
			->
			// TODO Implement runtime spring config (optional)
		}
	}

	void doWithDynamicMethods() {
		// TODO Implement registering dynamic methods to classes (optional)
	}

	void doWithApplicationContext() {
		// TODO Implement post initialization spring config (optional)
	}

	void onChange(Map<String, Object> event) {
		// TODO Implement code that is executed when any artefact that this plugin is
		// watching is modified and reloaded. The event contains: event.source,
		// event.application, event.manager, event.ctx, and event.plugin.
	}

	void onConfigChange(Map<String, Object> event) {
		// TODO Implement code that is executed when the project configuration changes.
		// The event is the same as for 'onChange'.
	}

	void onShutdown(Map<String, Object> event) {
		// TODO Implement code that is executed when the application shuts down (optional)
	}
}
