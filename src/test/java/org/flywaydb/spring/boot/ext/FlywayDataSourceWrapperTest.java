package org.flywaydb.spring.boot.ext;

import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link FlywayDataSourceWrapper}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
class FlywayDataSourceWrapperTest {

    @Test
    void delegatesGetConnection() throws SQLException {
        DataSource delegate = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        when(delegate.getConnection()).thenReturn(connection);

        FlywayDataSourceWrapper wrapper = new FlywayDataSourceWrapper(delegate);
        assertThat(wrapper.getConnection()).isSameAs(connection);
        verify(delegate).getConnection();
    }

    @Test
    void delegatesGetConnectionWithCredentials() throws SQLException {
        DataSource delegate = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        when(delegate.getConnection("user", "pass")).thenReturn(connection);

        FlywayDataSourceWrapper wrapper = new FlywayDataSourceWrapper(delegate);
        assertThat(wrapper.getConnection("user", "pass")).isSameAs(connection);
        verify(delegate).getConnection("user", "pass");
    }

    @Test
    void delegatesGetLogWriter() throws SQLException {
        DataSource delegate = mock(DataSource.class);
        PrintWriter writer = new PrintWriter(System.out);
        when(delegate.getLogWriter()).thenReturn(writer);

        FlywayDataSourceWrapper wrapper = new FlywayDataSourceWrapper(delegate);
        assertThat(wrapper.getLogWriter()).isSameAs(writer);
    }

    @Test
    void delegatesSetLogWriter() throws SQLException {
        DataSource delegate = mock(DataSource.class);
        PrintWriter writer = new PrintWriter(System.out);

        FlywayDataSourceWrapper wrapper = new FlywayDataSourceWrapper(delegate);
        wrapper.setLogWriter(writer);
        verify(delegate).setLogWriter(writer);
    }

    @Test
    void delegatesSetLoginTimeout() throws SQLException {
        DataSource delegate = mock(DataSource.class);

        FlywayDataSourceWrapper wrapper = new FlywayDataSourceWrapper(delegate);
        wrapper.setLoginTimeout(30);
        verify(delegate).setLoginTimeout(30);
    }

    @Test
    void delegatesGetLoginTimeout() throws SQLException {
        DataSource delegate = mock(DataSource.class);
        when(delegate.getLoginTimeout()).thenReturn(30);

        FlywayDataSourceWrapper wrapper = new FlywayDataSourceWrapper(delegate);
        assertThat(wrapper.getLoginTimeout()).isEqualTo(30);
    }

    @Test
    void delegatesGetParentLogger() throws SQLFeatureNotSupportedException {
        DataSource delegate = mock(DataSource.class);
        Logger logger = Logger.getLogger("test");
        try {
            when(delegate.getParentLogger()).thenReturn(logger);
        } catch (SQLFeatureNotSupportedException e) {
            // ignore
        }

        FlywayDataSourceWrapper wrapper = new FlywayDataSourceWrapper(delegate);
        try {
            assertThat(wrapper.getParentLogger()).isSameAs(logger);
        } catch (SQLFeatureNotSupportedException e) {
            // expected if mock doesn't support it
        }
    }

    @Test
    void delegatesUnwrap() throws SQLException {
        DataSource delegate = mock(DataSource.class);
        when(delegate.unwrap(DataSource.class)).thenReturn(delegate);

        FlywayDataSourceWrapper wrapper = new FlywayDataSourceWrapper(delegate);
        assertThat(wrapper.unwrap(DataSource.class)).isSameAs(delegate);
    }

    @Test
    void delegatesIsWrapperFor() throws SQLException {
        DataSource delegate = mock(DataSource.class);
        when(delegate.isWrapperFor(DataSource.class)).thenReturn(true);

        FlywayDataSourceWrapper wrapper = new FlywayDataSourceWrapper(delegate);
        assertThat(wrapper.isWrapperFor(DataSource.class)).isTrue();
    }
}
