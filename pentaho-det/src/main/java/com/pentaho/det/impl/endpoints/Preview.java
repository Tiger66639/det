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

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Path( "preview" )
public class Preview {

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
  public Preview() {

  }

  // endregion

  // region Methods
  @GET
  @Path( "/hello" )
  @Produces( MediaType.TEXT_PLAIN )
  public String hello() {
    return "Hello from Data Explorer Tool";
  }



  @GET
  @Produces( MediaType.APPLICATION_JSON )
  @Path( "/dataSources" )
  public Collection<DataSourceDTO> getDataSources() {
    Collection<DataSourceDTO> dataSourceDTOs = new ArrayList<>();
    for ( Map.Entry<UUID, ? extends IDataSource> entry : this.getDataSourceProvider().getDataSources().entrySet() ) {
      // TODO check converstion datasource => datasource dto
      DataSourceDTO dataSourceDTO = new DataSourceDTO().setUUID( entry.getKey() );
      dataSourceDTOs.add( dataSourceDTO );
    }
    return dataSourceDTOs;
  }

  @GET
  @Produces( MediaType.APPLICATION_JSON )
  @Path( "/dataSources/{dataSourceId}" )
  public DataSourceDTO getDataSource( @PathParam( "dataSourceId" ) UUID dataSourceId,
                                      @Context final HttpServletResponse response ) {

    IDataSource dataSource = this.getDataSourceProvider().getDataSources().get( dataSourceId );
    if ( dataSource == null ) {
      response.setStatus( Response.Status.NOT_FOUND.getStatusCode() );
      return null;
    }

    DataSourceDTO dataSourceDTO = new DataSourceDTO().setUUID( dataSourceId );
    return dataSourceDTO;
  }

  @GET
  @Produces( MediaType.APPLICATION_JSON )
  @Path( "/dataSources/{dataSourceId}/data" )
  public DataTableDTO getDataSourceData( @PathParam( "dataSourceId" ) UUID dataSourceId,
                                         @Context final HttpServletResponse response ) {

    // TODO optimize for DataSourceProviderAggregator?
    IDataSource dataSource = this.getDataSourceProvider().getDataSources().get( dataSourceId );
    if ( dataSource == null ) {
      response.setStatus( Response.Status.NOT_FOUND.getStatusCode() );
      return null;
    }

    IDataTable dataTable = dataSource.getData();
    DataTableDTO dataTableDTO = new DataTableDTO( dataTable );
    return dataTableDTO;
  }

  // endregion

}
