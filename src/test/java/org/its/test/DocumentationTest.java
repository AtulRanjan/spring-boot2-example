package org.its.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.Placement;
import org.asciidoctor.SafeMode;
import org.its.SpringRestApplication;
import org.its.config.SwaggerConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.swagger2markup.GroupBy;
import io.github.swagger2markup.Language;
import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.Swagger2MarkupProperties;
import io.github.swagger2markup.Swagger2MarkupConverter.Builder;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SpringRestApplication.class,
		SwaggerConfig.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DocumentationTest {

	private static final Logger LOG = LoggerFactory.getLogger(DocumentationTest.class);

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	public void generateApiAdoc() throws Exception {
		generateAsciDoc();
		generatePdf();
		generateHtml();
	}

	private void generateAsciDoc() throws Exception, UnsupportedEncodingException {
		ResponseEntity<String> apiDocResponse = restTemplate.getForEntity("/api-docs", String.class);

		String swaggerJson = apiDocResponse.getBody();
		Builder builder = Swagger2MarkupConverter.from(swaggerJson);
		Map<String, String> configMap = new HashMap<>();
		configMap.put(Swagger2MarkupProperties.MARKUP_LANGUAGE, MarkupLanguage.ASCIIDOC.toString());
		configMap.put(Swagger2MarkupProperties.OUTPUT_LANGUAGE, Language.EN.toString());
		configMap.put(Swagger2MarkupProperties.PATHS_GROUPED_BY, GroupBy.TAGS.toString());
		configMap.put(Swagger2MarkupProperties.INLINE_SCHEMA_ENABLED, Boolean.TRUE.toString());
		configMap.put(Swagger2MarkupProperties.INTER_DOCUMENT_CROSS_REFERENCES_ENABLED, Boolean.TRUE.toString());
		configMap.put(Swagger2MarkupProperties.GENERATED_EXAMPLES_ENABLED, Boolean.TRUE.toString());
		configMap.put(Swagger2MarkupProperties.OVERVIEW_DOCUMENT, Boolean.TRUE.toString());

		Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder(configMap).build();

		// Swagger2MarkupExtensionRegistry registry = new
		// Swagger2MarkupExtensionRegistryBuilder()
		// .withDefinitionsDocumentExtension(new
		// CtoolDefinitionExtension())
		// .withOverviewDocumentExtension(new
		// CToolDocOverviewExtension())
		// .withSwaggerModelExtension(new CtoolApiModelExtention())
		// .build();

		builder.withConfig(config);
		// builder.withExtensionRegistry(registry);

		Swagger2MarkupConverter converter = builder.build();
		converter.toFile(Paths.get("src/main/resources/asciidoc/api-doc"));
		Path path = Paths.get("src/main/resources/asciidoc/api-doc.adoc");
		Charset charset = StandardCharsets.UTF_8;

		String content = new String(Files.readAllBytes(path), charset);
		content = ":title-logo-image: images/cover.jpg\n\n" + content;
		content = content.replaceAll("__required__", "`__required__`").replaceAll("__Host__ :.*", "")
				.replaceAll("__BasePath__ :.*", "").replaceAll("===== Consumes", "")
				.replaceAll("\\* `application/json`", "").replaceAll("===== Produces", "")
				.replaceAll("\\* `\\*/\\*`", "");
		Files.write(path, content.getBytes(charset));
	}

	private void generatePdf() {
		Asciidoctor asciidoctor = Asciidoctor.Factory.create();
		OptionsBuilder optionsBuilder = OptionsBuilder.options();
		optionsBuilder.destinationDir(new File("src/main/resources/asciidoc/"));
		optionsBuilder.option(Attributes.BACKEND, "pdf");
		optionsBuilder.option(Attributes.SOURCE_HIGHLIGHTER, "coderay");
		optionsBuilder.option(Attributes.DOCTYPE, "book");
		optionsBuilder.option(Attributes.TOC, "left");
		optionsBuilder.option(Attributes.TOC_POSITION, Placement.RIGHT.getPosition());

		// optionsBuilder.eruby("eruby");
		optionsBuilder.compact(true);
		optionsBuilder.safe(SafeMode.SAFE);
		optionsBuilder.option("pagenums", "left");
		optionsBuilder.option("revnumber", "1.0");

		Map<String, Object> attributes = new HashMap<>();
		attributes.put(Attributes.ICONS, "font");
		attributes.put(Attributes.TOC, "left");
		attributes.put(Attributes.SET_ANCHORS, "true");
		String now = DateFormatUtils.format(new Date(), "dd MMM yyyy");
		attributes.put("revdate", now);
		attributes.put("numbered", "true");
		attributes.put("pagenums", "right");
		attributes.put("docinfo1", "true");
		attributes.put("idseparator", "true");
		attributes.put("idprefix", "true");
		optionsBuilder.attributes(attributes);
		LOG.info("Generating API Docs pdf");
		asciidoctor.convertFile(new File("src/main/resources/asciidoc/api-doc.adoc"), optionsBuilder);
	}

	private void generateHtml() {
		Asciidoctor asciidoctor = Asciidoctor.Factory.create();
		OptionsBuilder optionsBuilder = OptionsBuilder.options();
		optionsBuilder.destinationDir(new File("src/docs/output/"));
		optionsBuilder.option(Attributes.BACKEND, "html5");
		optionsBuilder.option(Attributes.SOURCE_HIGHLIGHTER, "coderay");
		optionsBuilder.option(Attributes.DOCTYPE, "book");
		optionsBuilder.option(Attributes.TOC, "left");
		optionsBuilder.option(Attributes.TOC_POSITION, Placement.RIGHT.getPosition());

		optionsBuilder.eruby("erubis");
		optionsBuilder.compact(true);
		optionsBuilder.safe(SafeMode.SAFE);

		optionsBuilder.option("pagenums", "left");
		optionsBuilder.option("revnumber", "1.0");

		Map<String, Object> attributes = new HashMap<>();
		attributes.put(Attributes.ICONS, "font");
		attributes.put(Attributes.TOC, "left");
		attributes.put(Attributes.SET_ANCHORS, "true");
		String now = DateFormatUtils.format(new Date(), "dd MMM yyyy");
		attributes.put("revdate", now);
		attributes.put("numbered", "true");
		attributes.put("pagenums", "left");
		attributes.put("docinfo1", "true");
		attributes.put("idseparator", "true");
		attributes.put("idprefix", "");
		optionsBuilder.attributes(attributes);
		LOG.info("Generating API Docs html");
		asciidoctor.convertFile(new File("src/main/resources/asciidoc/api-doc.adoc"), optionsBuilder);
	}

}
