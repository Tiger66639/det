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
import com.pentaho.det.impl.services.IDataSourceService;

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
