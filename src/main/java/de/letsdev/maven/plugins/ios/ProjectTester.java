/**
 * maven-ios-plugin
 * <p/>
 * User: fkoebel
 * Date: 2016-06-23
 * <p/>
 * This code is copyright (c) 2016 let's dev.
 * URL: http://www.letsdev.de
 * e-Mail: contact@letsdev.de
 */

package de.letsdev.maven.plugins.ios;

import org.apache.maven.project.MavenProject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProjectTester {
    public static void test(final Map<String, String> properties, MavenProject mavenProject) throws IOSException, IOException {
        String projectName = Utils.buildProjectName(properties, mavenProject);
        File workDirectory = Utils.getWorkDirectory(properties, mavenProject, projectName);

        String scheme = properties.get(Utils.PLUGIN_PROPERTIES.XCTEST_SCHEME.toString());
        String configuration = properties.get(Utils.PLUGIN_PROPERTIES.CONFIGURATION.toString());
        String sdk = Utils.SDK_IPHONE_SIMULATOR;

        final String scriptName = "run-xctests.sh";

        File tempFile = File.createTempFile(scriptName, "sh");
        InputStream inputStream = ProjectBuilder.class.getResourceAsStream("/META-INF/" + scriptName);
        OutputStream outputStream = new FileOutputStream(tempFile);

        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();

        ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", tempFile.getAbsoluteFile().toString(),
                scheme,
                configuration,
                sdk);

        processBuilder.directory(workDirectory);
        CommandHelper.performCommand(processBuilder);
    }
}
