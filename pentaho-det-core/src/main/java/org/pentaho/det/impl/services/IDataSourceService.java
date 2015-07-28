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

package org.pentaho.det.impl.services;

import org.pentaho.det.impl.endpoints.dto.DataSourceDTO;
import org.pentaho.det.impl.endpoints.dto.DataTableDTO;

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
