/*
 * generated by Xtext 2.17.1
 */
package org.caph.parser.ide

import com.google.inject.Guice
import org.caph.parser.CaphRuntimeModule
import org.caph.parser.CaphStandaloneSetup
import org.eclipse.xtext.util.Modules2

/**
 * Initialization support for running Xtext languages as language servers.
 */
class CaphIdeSetup extends CaphStandaloneSetup {

	override createInjector() {
		Guice.createInjector(Modules2.mixin(new CaphRuntimeModule, new CaphIdeModule))
	}
	
}
