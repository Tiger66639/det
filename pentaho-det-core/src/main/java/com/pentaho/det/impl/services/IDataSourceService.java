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

package com.pentaho.det.impl.services;

import com.pentaho.det.impl.endpoints.dto.DataSourceDTO;
import com.pentaho.det.impl.endpoints.dto.DataTableDTO;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

public interface IDataSourceService {

  @GET
  @Produces( MediaType.APPLICATION_JSON )
  @Path( "/dataSources" )
  Collection<DataSourceDTO> getDataSources();

  @GET
  @Produces( MediaType.APPLICATION_JSON )
  @Path( "/dataSources/{dataSourceUuidOrName}" )
  DataSourceDTO getDataSource(
    @PathParam( "dataSourceUuidOrName" ) String dataSourceUuidOrName,
    @Context HttpServletResponse response );

  @GET
  @Produces( MediaType.APPLICATION_JSON )
  @Path( "/dataSources/{dataSourceUuidOrName}/data" )
  DataTableDTO getDataSourceData(
    @PathParam( "dataSourceUuidOrName" ) String dataSourceUuidOrName,
    @Context HttpServletResponse response );
}