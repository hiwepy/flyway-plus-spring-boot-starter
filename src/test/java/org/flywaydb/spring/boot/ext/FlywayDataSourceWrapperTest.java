package org.flywaydb.spring.boot.ext;

import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FlywayDataSourceWrapperTest {

    @Test
    void delegatesTheCompleteDataSourceContract() throws Exception {
        DataSource delegate = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        Connection credentialConnection = mock(Connection.class);
        PrintWriter writer = new PrintWriter(System.out);
        Logger logger = Logger.getLogger("flyway-test");
        when(delegate.getConnection()).thenReturn(connection);
        when(delegate.getConnection("user", "secret")).thenReturn(credentialConnection);
        when(delegate.getLogWriter()).thenReturn(writer);
        when(delegate.getLoginTimeout()).thenReturn(7);
        when(delegate.getParentLogger()).thenReturn(logger);
        when(delegate.unwrap(DataSource.class)).thenReturn(delegate);
        when(delegate.isWrapperFor(DataSource.class)).thenReturn(true);

        FlywayDataSourceWrapper wrapper = new FlywayDataSourceWrapper(delegate);
        assertThat(wrapper.getConnection()).isSameAs(connection);
        assertThat(wrapper.getConnection("user", "secret")).isSameAs(credentialConnection);
        assertThat(wrapper.getLogWriter()).isSameAs(writer);
        assertThat(wrapper.getLoginTimeout()).isEqualTo(7);
        assertThat(wrapper.getParentLogger()).isSameAs(logger);
        assertThat(wrapper.unwrap(DataSource.class)).isSameAs(delegate);
        assertThat(wrapper.isWrapperFor(DataSource.class)).isTrue();

        wrapper.setLogWriter(writer);
        wrapper.setLoginTimeout(9);
        verify(delegate).setLogWriter(writer);
        verify(delegate).setLoginTimeout(9);
    }
}
