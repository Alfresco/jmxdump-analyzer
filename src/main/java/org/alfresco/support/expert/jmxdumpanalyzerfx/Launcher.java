package org.alfresco.support.expert.jmxdumpanalyzerfx;

/**
 * Plain entry point for the shaded (jar-with-dependencies) distribution.
 *
 * <p>Launching a fat jar via {@code java -jar} whose main class extends
 * {@link javafx.application.Application} fails on JavaFX 11+ with
 * "JavaFX runtime components are missing", because the JavaFX modules sit on the
 * classpath rather than the module path. Delegating through a class that does
 * not extend {@code Application} sidesteps that launcher check.
 */
public class Launcher {

    public static void main(String[] args) {
        JMXFX.main(args);
    }
}
