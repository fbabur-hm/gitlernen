package edu.hm.hafner.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link FilteredLog}.
 *
 * @author Ullrich Hafner
 */
class FilteredLogTest {
    private static final String TITLE = "Title: ";

    @Test
    void shouldLogNothing() {
        FilteredLog filteredLog = new FilteredLog(TITLE, 5);

        assertThat(filteredLog.getErrorMessages()).isEmpty();
        filteredLog.logSummary();
        assertThat(filteredLog.getErrorMessages()).isEmpty();
    }

    @Test
    void shouldLogAllErrors() {
        FilteredLog filteredLog = new FilteredLog(TITLE, 5);

        filteredLog.logError("1");
        filteredLog.logError("2");
        filteredLog.logError("3");
        filteredLog.logError("4");
        filteredLog.logError("5");

        assertThatExactly5MessagesAreLogged(filteredLog);

        filteredLog.logSummary();

        assertThatExactly5MessagesAreLogged(filteredLog);
        assertThat(filteredLog.size()).isEqualTo(5);
    }

    @Test
    void shouldSkipAdditionalErrors() {
        FilteredLog filteredLog = new FilteredLog(TITLE, 5);

        filteredLog.logError("1");
        filteredLog.logError("2");
        filteredLog.logError("3");
        filteredLog.logError("4");
        filteredLog.logError("5");
        filteredLog.logError("6");
        filteredLog.logError("7");

        assertThatExactly5MessagesAreLogged(filteredLog);

        filteredLog.logSummary();

        assertThat(filteredLog.getErrorMessages()).containsExactly(TITLE, "1", "2", "3", "4", "5",
                "  ... skipped logging of 2 additional errors ...");
        assertThat(filteredLog.size()).isEqualTo(7);
    }

    private void assertThatExactly5MessagesAreLogged(final FilteredLog filteredLog) {
        assertThat(filteredLog.getErrorMessages()).containsExactly(TITLE, "1", "2", "3", "4", "5");
    }

    @Test
    void shouldLogExceptions() {
        FilteredLog filteredLog = new FilteredLog(TITLE, 1);

        filteredLog.logException(new IllegalArgumentException("Cause"), "Message");
        filteredLog.logException(new IllegalArgumentException(""), "Message");

        assertThat(filteredLog.getErrorMessages()).contains(TITLE,
                "Message", "java.lang.IllegalArgumentException: Cause",
                "\tat edu.hm.hafner.util.FilteredLogTest.shouldLogExceptions(FilteredLogTest.java:71)");
    }
}
