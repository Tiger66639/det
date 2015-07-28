/*
 * Copyright 2002 - 2015 Webdetails, a Pentaho company. All rights reserved.
 *
 * This software was developed by Webdetails and is provided under the terms
 * of the Mozilla Public License, Version 2.0, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 */

package org.pentaho.det.impl.endpoints;

import org.pentaho.det.api.domain.IDataSource;
import org.pentaho.det.api.domain.IDataTable;
import org.pentaho.det.api.services.IDataSourceProvider;
import org.pentaho.det.impl.endpoints.dto.DataSourceDTO;
import org.pentaho.det.impl.endpoints.dto.DataTableDTO;
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