package dev.mkeo102.logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import dev.mkeo102.logger.loggingStrategy.impl.StreamLoggingStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomLoggerTest {

    @Test
    void testDebugEffectiveness() throws Exception {

        LoggerFactory factory = new LoggerFactory();

        {
            factory.setDebugEnabled(false);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final String utf8 = StandardCharsets.UTF_8.name();
            PrintStream ps = new PrintStream(baos, true, utf8);
            factory.setFallbackProvider(new StreamLoggingStrategy(ps));

            Logger logger = factory.getLogger("Test");


            logger.debug("This is a test");

            assert !baos.toString(utf8).contains("This is a test");
            ps.close();
        }

        {
            factory.setDebugEnabled(true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final String utf8 = StandardCharsets.UTF_8.name();
            PrintStream ps = new PrintStream(baos, true, utf8);
            factory.setFallbackProvider(new StreamLoggingStrategy(ps));

            Logger logger = factory.getLogger("Test1");

            logger.debug("This is a test");

            assert baos.toString(utf8).contains("This is a test");
        }

    }

    @Test
    public void testExceptionPrinter() throws Throwable {
        LoggerFactory factory = new LoggerFactory();

        factory.setDebugEnabled(false);

        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final String utf8 = StandardCharsets.UTF_8.name();
            PrintStream ps = new PrintStream(baos, true, utf8);
            factory.setFallbackProvider(new StreamLoggingStrategy(ps));

            Logger logger = factory.getLogger("Test");

            logger.exception(new IOException("This is a test exception!"));

            assert baos.toString(utf8).contains("This is a test exception!") && baos.toString(utf8).contains("testExceptionPrinter");
        }

    }

    @Test
    public void testFormatter() {
        {
            String correct = String.format("This is a test of the formatter with a String : %s", "Hello World!");
            String custom = Logger.format("This is a test of the formatter with a String : {}", "Hello World!");

            assertEquals(correct, custom, "Failed to format with a String");
        }

        {
            String correct = String.format("This is a test of the formatter with null : %s", (Object) null);
            String custom = Logger.format("This is a test of the formatter with null : {}", (Object) null);

            assertEquals(correct, custom, "Failed to format with null");
        }

        {
            String correct = String.format("This is a test of the formatter with an int : %s", 5);
            String custom = Logger.format("This is a test of the formatter with an int : {}", 5);

            assertEquals(correct, custom, "Failed to format with an int");
        }

        {
            String correct = String.format("This is a test of the formatter with a long : %s", 5L);
            String custom = Logger.format("This is a test of the formatter with a long : {}", 5L);

            assertEquals(correct, custom, "Failed to format with a long");
        }

        {
            String correct = String.format("This is a test of the formatter with a float : %s", 5.5f);
            String custom = Logger.format("This is a test of the formatter with a float : {}", 5.5f);

            assertEquals(correct, custom, "Failed to format with a float");
        }

        {
            String correct = String.format("This is a test of the formatter with a double : %s", 5.5D);
            String custom = Logger.format("This is a test of the formatter with a double : {}", 5.5D);

            assertEquals(correct, custom, "Failed to format with a double");
        }

        {
            String correct = String.format("This is a test of the formatter with a boolean : %s", true);
            String custom = Logger.format("This is a test of the formatter with a boolean : {}", true);

            assertEquals(correct, custom, "Failed to format with a boolean");
        }

        {
            String correct = String.format("This is a test of the formatter's escaping : {} %s", true);
            String custom = Logger.format("This is a test of the formatter's escaping : \\{} {}", true);

            assertEquals(correct, custom, "Failed to format with an escape");
        }

        {
            String correct = String.format("This is a test of the formatter's escaping format strings : %s", "{working}");
            String custom = Logger.format("This is a test of the formatter's escaping format strings : {}", "{working}");

            assertEquals(correct, custom, "Failed to format with an arg that contains a format identifier");
        }
    }

    @Test
    public void testMuting() throws UnsupportedEncodingException {
        LoggerFactory factory = new LoggerFactory();
        factory.setLoggersMuted(true);
        {
            Logger logger = factory.getLogger("Test");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final String utf8 = StandardCharsets.UTF_8.name();
            PrintStream ps = new PrintStream(baos, true, utf8);
            factory.setFallbackProvider(new StreamLoggingStrategy(ps));

            logger.info("This is a test");

            assert baos.toString(utf8).isEmpty();
        }

        factory.setLoggersMuted(false);


        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final String utf8 = StandardCharsets.UTF_8.name();
            PrintStream ps = new PrintStream(baos, true, utf8);
            factory.setFallbackProvider(new StreamLoggingStrategy(ps));

            Logger logger = factory.getLogger("Test1");


            logger.info("This is a test");

            assert baos.toString(utf8).contains("This is a test");
        }

        factory.setLoggersMuted(true);

        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final String utf8 = StandardCharsets.UTF_8.name();
            PrintStream ps = new PrintStream(baos, true, utf8);

            factory.setFallbackProvider(new StreamLoggingStrategy(ps));

            Logger logger = factory.getLogger("Test2");

            logger.info("This is a test");

            assert baos.toString(utf8).isEmpty();
        }


    }

}
