package org.caph.parser.tests;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.caph.parser.CaphParserHelper;
import org.caph.parser.CaphParserHelper.CaphParseResult;
import org.eclipse.xtext.diagnostics.Severity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class AllCaphFilesParsingTest {

	final File sourceFile;

	public AllCaphFilesParsingTest(final File sourceFile) {
		this.sourceFile = sourceFile;
	}

	/**
	 * @throws IOException
	 */
	@Parameters(name = "{0}")
	public static Collection<Object[]> data() throws IOException {
		final URL resource = AllCaphFilesParsingTest.class.getResource("/");

		if (resource != null) {
			File rootFolder = new File(resource.getPath());
			final List<Object[]> caphSourceFiles = new ArrayList<>();
			Files.walkFileTree(rootFolder.toPath(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
					if (path.toString().endsWith(".cph")) {
						caphSourceFiles.add(new Object[] { path.toFile() });
					}
					return FileVisitResult.CONTINUE;
				}

			});
			return caphSourceFiles;
		} else {
			return new ArrayList<Object[]>(0);
		}
	}

	@Test
	public void testParse() throws IOException {
		CaphParseResult parse = CaphParserHelper.parse(sourceFile);
		if (parse.error) {
			System.out.println(sourceFile.toString());
			parse.issues.stream().filter(i -> i.getSeverity() == Severity.ERROR).forEach(i -> System.out.println(i));
		}
		Assert.assertFalse(parse.error);
	}
}
