/*
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2015 Pentaho Corporation. All rights reserved.
 */

package com.pentaho.det.impl.endpoints;

import com.pentaho.det.api.domain.IDataSource;
import com.pentaho.det.api.domain.IDataTable;
import com.pentaho.det.api.services.IDataSourceProvider;
import com.pentaho.det.impl.endpoints.dto.DataSourceDTO;
import com.pentaho.det.impl.endpoints.dto.DataTableDTO;
import org.junit.Test;
import org.junit.Ignore;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import static junit.framework.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class DataSourceServiceTest {

  // region Tests
  @Test
  public void testGetDataSources() {
    UUID expectedDataSourceUUID = UUID.randomUUID();
    String expectedDataSourceName = "myAmazingDataSource";
    IDataSource dataSourceMock = this.createDataSourceMock( expectedDataSourceUUID, expectedDataSourceName );
    IDataSourceProvider dataSourceProviderMock = this.createMockDataSourceProvider( dataSourceMock );

    DataSourceService dataSourceService = this.createDataSourceService();
    dataSourceService.setDataSourceProvider( dataSourceProviderMock );

    // act
    Collection<DataSourceDTO> actualDataSourceDTOs = dataSourceService.getDataSources();

    // test
    assertThat( actualDataSourceDTOs, hasSize( 1 ) );
    DataSourceDTO actualDataSourceDTO = actualDataSourceDTOs.iterator().next();
    assertThat( actualDataSourceDTO.getUUID(), equalTo( expectedDataSourceUUID ) );
    assertThat( actualDataSourceDTO.getName(), equalTo( expectedDataSourceName ) );
  }

  @Test
  public void testGetDataSourceByUUID() {
    UUID expectedDataSourceUUID = UUID.randomUUID();
    String expectedDataSourceName = "myAmazingDataSource";
    IDataSource dataSourceMock = this.createDataSourceMock( expectedDataSourceUUID, expectedDataSourceName );
    IDataSourceProvider dataSourceProviderMock = this.createMockDataSourceProvider( dataSourceMock );

    HttpServletResponse responseMock = mock( HttpServletResponse.class );

    DataSourceService dataSourceService = this.createDataSourceService();
    dataSourceService.setDataSourceProvider( dataSourceProviderMock );

    // act
    DataSourceDTO actualDataSourceDTO = dataSourceService.getDataSource( expectedDataSourceUUID.toString(), responseMock );
    assertThat( actualDataSourceDTO.getUUID(), equalTo( expectedDataSourceUUID ) );
    assertThat( actualDataSourceDTO.getName(), equalTo( expectedDataSourceName ) );

  }

  @Test
  public void testGetDataSourceByName() {
    UUID expectedDataSourceUUID = UUID.randomUUID();
    String expectedDataSourceName = "myAmazingDataSource";
    IDataSource dataSourceMock = this.createDataSourceMock( expectedDataSourceUUID, expectedDataSourceName );
    IDataSourceProvider dataSourceProviderMock = this.createMockDataSourceProvider( dataSourceMock );

    HttpServletResponse responseMock = mock( HttpServletResponse.class );

    DataSourceService dataSourceService = this.createDataSourceService();
    dataSourceService.setDataSourceProvider( dataSourceProviderMock );

    // act
    DataSourceDTO actualDataSourceDTO = dataSourceService.getDataSource( expectedDataSourceName, responseMock );
    assertThat( actualDataSourceDTO.getUUID(), equalTo( expectedDataSourceUUID ) );
    assertThat( actualDataSourceDTO.getName(), equalTo( expectedDataSourceName ) );
  }

  @Test
  public void testGetDataSourceByUUIDNotFoundStatus () {
    UUID nonexistingDataSourceUUID = UUID.randomUUID();
    IDataSourceProvider dataSourceProviderMock = this.createMockDataSourceProvider();
    HttpServletResponse responseMock = mock( HttpServletResponse.class );

    DataSourceService dataSourceService = this.createDataSourceService();
    dataSourceService.setDataSourceProvider( dataSourceProviderMock );

    // act
    DataSourceDTO actualDataSourceDTO = dataSourceService.getDataSource( nonexistingDataSourceUUID.toString(), responseMock );
    assertThat( actualDataSourceDTO, nullValue() );
    verify( responseMock ).setStatus( Response.Status.NOT_FOUND.getStatusCode() );
  }

  @Test
  public void testGetDataSourceByNameNotFoundStatus () {
    String nonexistingDataSourceName = "myNonExistingDataSource";
    IDataSourceProvider dataSourceProviderMock = this.createMockDataSourceProvider();
    HttpServletResponse responseMock = mock( HttpServletResponse.class );

    DataSourceService dataSourceService = this.createDataSourceService();
    dataSourceService.setDataSourceProvider( dataSourceProviderMock );

    // act
    DataSourceDTO actualDataSourceDTO = dataSourceService.getDataSource( nonexistingDataSourceName, responseMock );
    assertThat( actualDataSourceDTO, nullValue() );
    verify( responseMock ).setStatus( Response.Status.NOT_FOUND.getStatusCode() );
  }

  @Test
  @Ignore("Test not implemented")
  public void testGetDataSourceData() {
    DataTableDTO expectedDataTableDTO = new DataTableDTO(  );
    IDataTable dataTableMock = mock( IDataTable.class );

    UUID expectedDataSourceUUID = UUID.randomUUID();
    String expectedDataSourceName = "myAmazingDataSource";
    IDataSource dataSourceMock = this.createDataSourceMock( expectedDataSourceUUID, expectedDataSourceName );
    when( dataSourceMock.getData() ).thenReturn( dataTableMock );
    IDataSourceProvider dataSourceProviderMock = this.createMockDataSourceProvider( dataSourceMock );

    DataSourceService dataSourceService = this.createDataSourceService();
    dataSourceService.setDataSourceProvider( dataSourceProviderMock );

    fail( "Test not implemented" );

  }
  // endregion

  // region auxiliary methods

  private DataSourceService createDataSourceService() {
    return new DataSourceService();
  }

  private IDataSource createDataSourceMock( UUID uuid, String name ) {
    IDataSource dataSourceMock = mock( IDataSource.class );
    when( dataSourceMock.getUUID() ).thenReturn( uuid );
    when( dataSourceMock.getName() ).thenReturn( name );

    return dataSourceMock;
  }

  private IDataSourceProvider createMockDataSourceProvider( IDataSource ... dataSources ) {
    final Map<UUID, IDataSource> dataSourceMap = new Hashtable<>();
    for( IDataSource dataSource : dataSources ) {
      dataSourceMap.put( dataSource.getUUID(), dataSource );
    }

    IDataSourceProvider dataSourceProviderMock = mock( IDataSourceProvider.class );
    when( dataSourceProviderMock.getDataSources() ).thenAnswer( new Answer<Map<UUID, IDataSource>>() {
      @Override public Map<UUID, IDataSource> answer( InvocationOnMock invocationOnMock ) throws Throwable {
        return dataSourceMap;
      }
    } );

    return  dataSourceProviderMock;
  }

  /*
  private IDataTable createDataTableMock( List<IField> fields, List<IDataTableEntry> entries ) {
    IDataTable dataTableMock = mock( IDataTable.class );
    when( dataTableMock.getFields() ).thenReturn( fields );
    when( dataTableMock.getEntries() ).thenReturn( entries );

    return dataTableMock;
  }
  */

  // endregion
}