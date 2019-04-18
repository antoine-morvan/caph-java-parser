package org.caph.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.caph.parser.caph.Model;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import com.google.inject.Injector;

public class CaphParserHelper {

	// forbid instantiation
	private CaphParserHelper() {
	}

	public static final class CaphParseResult {
		public final List<Issue> issues;
		public final Model ast;
		public final boolean error;

		public CaphParseResult(final Resource resource, final List<Issue> issues) {
			this.issues = issues;
			this.error = issues.stream().anyMatch(i -> i.getSeverity() == Severity.ERROR);
			if (this.error) {
				this.ast = null;
			} else {
				this.ast = (Model) resource.getContents().get(0);
			}
		}
	}

	public static final CaphParseResult parse(final File sourceFile) {
		return parse(URI.createURI(sourceFile.toURI().toString()));
	}

	public static final CaphParseResult parse(final URI sourceFileUri) {
		Injector injector = new CaphStandaloneSetup().createInjectorAndDoEMFRegistration();
		ResourceSet rs = injector.getInstance(ResourceSet.class);
		Resource resource = rs.getResource(sourceFileUri, true);
		try {
			resource.load(null);
		} catch (IOException e) {
			throw new CaphParserException("Could not load file.", e);
		}
		IResourceValidator validator = injector.getInstance(IResourceValidator.class);
		List<Issue> issues = validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl);
		return new CaphParseResult(resource, issues);
	}
}
