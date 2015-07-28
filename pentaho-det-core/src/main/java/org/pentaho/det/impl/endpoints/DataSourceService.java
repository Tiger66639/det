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
import org.pentaho.det.impl.services.IDataSourceService;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Path( "det" )
public class DataSourceService implements IDataSourceService {

  // region Properties
  public IDataSourceProvider getDataSourceProvider() {
    return this.dataSourceProvider;
  }
  public void setDataSourceProvider( IDataSourceProvider dataSourceProvider ) {
    this.dataSourceProvider = dataSourceProvider;
  }
  private IDataSourceProvider dataSourceProvider;

  // endregion

  // region Constructors
  public DataSourceService() {

  }

  // endregion

  // region Endpoints
  @GET
  @Path( "/hello" )
  @Produces( MediaType.TEXT_PLAIN )
  public String hello() {
    return "Hello from Data Explorer Tool";
  }

  public Collection<DataSourceDTO> getDataSources() {
    Collection<DataSourceDTO> dataSourceDTOs = new ArrayList<>();
    for ( IDataSource dataSource : this.getDataSourceProvider().getDataSources().values() ) {
      DataSourceDTO dataSourceDTO = new DataSourceDTO( dataSource );
      dataSourceDTOs.add( dataSourceDTO );
    }
    return dataSourceDTOs;
  }

  public DataSourceDTO getDataSource( String dataSourceUuidOrName,
                                      final HttpServletResponse response ) {

    IDataSource dataSource = findDataSourceByIdOrName( this.getDataSourceProvider().getDataSources(), dataSourceUuidOrName );
    if ( dataSource == null ) {
      response.setStatus( Response.Status.NOT_FOUND.getStatusCode() );
      return null;
    }

    DataSourceDTO dataSourceDTO = new DataSourceDTO( dataSource );
    return dataSourceDTO;
  }

  public DataTableDTO getDataSourceData( String dataSourceUuidOrName,
                                         final HttpServletResponse response ) {

    IDataSource dataSource = findDataSourceByIdOrName( this.getDataSourceProvider().getDataSources(),
        dataSourceUuidOrName );
    if ( dataSource == null ) {
      response.setStatus( Response.Status.NOT_FOUND.getStatusCode() );
      return null;
    }

    IDataTable dataTable = dataSource.getData();
    DataTableDTO dataTableDTO = new DataTableDTO( dataTable );
    return dataTableDTO;
  }

  // endregion

  // region auxiliary methods
  /**
   * Searches for a {@link IDataSource} with the given {@code name}.
   * @param dataSources The data sources where to search for the data source.
   * @param name The name to match.
   * @return The first {@link IDataSource} found with the given {@code name} or {@code null} if no data source is found.
   */
  private IDataSource findDataSourceByName( Iterable<? extends IDataSource> dataSources, String name ) {
    if ( name == null || name.isEmpty() ) {
      return null;
    }

    for ( IDataSource dataSource : dataSources ) {
      if ( name.equals( dataSource.getName() ) ) {
        return dataSource;
      }
    }
    return null;
  }

  private IDataSource findDataSourceByIdOrName( Map<UUID, ? extends IDataSource> dataSources, String dataSourceUuidOrName ) {
    IDataSource dataSource;
    try {
      UUID dataSourceUUID = UUID.fromString( dataSourceUuidOrName );
      dataSource = dataSources.get( dataSourceUUID );
    } catch ( IllegalArgumentException e ) {
      // invalid UUID, check if there is a datasource with the given name
      dataSource = findDataSourceByName( dataSources.values(), dataSourceUuidOrName );
    }
    return dataSource;
  }

  // endregion
}
