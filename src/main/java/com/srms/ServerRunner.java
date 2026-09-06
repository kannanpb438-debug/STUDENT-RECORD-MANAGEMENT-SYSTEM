package com.srms;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;

/**
 * ServerRunner - Embedded Jetty Launcher.
 * Enables zero-setup 1-click execution via 'mvn compile exec:java' or Java main.
 */
public class ServerRunner {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        String portProp = System.getProperty("port");
        if (portProp != null && !portProp.isEmpty()) {
            try {
                port = Integer.parseInt(portProp);
            } catch (NumberFormatException e) {
                // fallback to 8080
            }
        }

        Server server = new Server(port);

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/srms");
        webapp.setResourceBase(new File("src/main/webapp").getAbsolutePath());
        webapp.setParentLoaderPriority(true);

        File classesDir = new File("target/classes");
        if (classesDir.exists()) {
            webapp.setExtraClasspath(classesDir.getAbsolutePath());
        }

        webapp.setClassLoader(Thread.currentThread().getContextClassLoader());
        webapp.setAttribute(
            "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
            ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/jakarta.servlet.jsp.jstl-[^/]*\\.jar$|.*taglibs.*\\.jar$|.*jstl.*\\.jar$"
        );

        webapp.setThrowUnavailableOnStartupException(true);
        server.setHandler(webapp);

        System.out.println("==========================================================================");
        System.out.println("   Student Record Management System (SRMS) - Server Starting...");
        System.out.println("   Access URL: http://localhost:8080/srms/");
        System.out.println("   Default Admin Demo: admin / admin123");
        System.out.println("   Default Faculty Demo: faculty1 / faculty123");
        System.out.println("   Default Student Demo: student1 / student123");
        System.out.println("==========================================================================");

        try {
            server.start();
            if (webapp.isFailed() || webapp.getUnavailableException() != null) {
                System.err.println("CRITICAL: Webapp failed to initialize: " + webapp.getUnavailableException());
                if (webapp.getUnavailableException() != null) {
                    webapp.getUnavailableException().printStackTrace();
                }
            }
            server.join();
        } catch (Exception e) {
            System.err.println("CRITICAL SERVER START ERROR: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
