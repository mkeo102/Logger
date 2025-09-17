package dev.mkeo102.logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class CustomLoggerTest {

    @Test
    void testMultipleOutput() throws Exception {

      Logger logger = Logger.getLogger("Test");

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      final String utf8 = StandardCharsets.UTF_8.name();

      PrintStream ps = new PrintStream(baos,true,utf8);

      logger.addOutput(ps);
      logger.info("This is a test");

      assert baos.toString(utf8).contains("This is a test");
    }

    @Test
    void testDebugEffectiveness() throws Exception{
      {
        Logger logger = Logger.getLogger("Test", false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final String utf8 = StandardCharsets.UTF_8.name();

        PrintStream ps = new PrintStream(baos, true, utf8);

        logger.addOutput(ps);
        logger.debug("This is a test");

        assert !baos.toString(utf8).contains("This is a test");
      }

      {
        Logger logger = Logger.getLogger("Test", true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final String utf8 = StandardCharsets.UTF_8.name();

        PrintStream ps = new PrintStream(baos, true, utf8);

        logger.addOutput(ps);
        logger.debug("This is a test");

        assert baos.toString(utf8).contains("This is a test");
      }

    }


    @Test
    public void testNullFormat() throws Throwable {
      {
        Logger logger = Logger.getLogger("Test", true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final String utf8 = StandardCharsets.UTF_8.name();

        PrintStream ps = new PrintStream(baos, true, utf8);

        logger.addOutput(ps);
        logger.info("{}", (Object) null);

        assert baos.toString(utf8).contains("null");
      }


      {
        Logger logger = Logger.getLogger("Test", true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final String utf8 = StandardCharsets.UTF_8.name();

        PrintStream ps = new PrintStream(baos, true, utf8);

        logger.addOutput(ps);
        logger.info("{} {}", "test", null);

        assert baos.toString(utf8).contains("test null");
      }

    }


    @Test
    public void testExceptionPrinter() throws Throwable {
        {
            Logger logger = Logger.getLogger("Test", true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final String utf8 = StandardCharsets.UTF_8.name();

            PrintStream ps = new PrintStream(baos, true, utf8);

            logger.addOutput(ps);
            logger.exception(new IOException("This is a test exception!"));

            assert baos.toString(utf8).contains("This is a test exception!") && baos.toString(utf8).contains("testExceptionPrinter");
        }

    }

}
