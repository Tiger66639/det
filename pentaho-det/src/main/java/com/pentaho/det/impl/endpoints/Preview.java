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

import com.pentaho.det.impl.di.PreviewListener;
import com.pentaho.det.impl.domain.DataTable;
import com.pentaho.det.impl.endpoints.dto.ColumnDefinitionDTO;
import com.pentaho.det.impl.endpoints.dto.DataTableDTO;
import com.pentaho.det.impl.endpoints.dto.RowDTO;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Path( "preview" )
public class Preview {

  // region Properties
  PreviewListener previewListener;
  // endregion

  // region Constructors
  public Preview( PreviewListener previewListener ) {
    this.previewListener = previewListener;
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
  @Path( "/steps/{stepName}" )
  @Produces( MediaType.APPLICATION_JSON )
  public DataTable getStepPreviewDataTest( @PathParam( "stepName" ) String stepName ) {
    return this.previewListener.getPreviewData().get( stepName );
  }

  @GET
  @Path( "/steps" )
  @Produces( MediaType.APPLICATION_JSON )
  public Map<String, DataTable> getPreviewDataTest() {
    return this.previewListener.getPreviewData();
  }

  @GET
  @Path( "/dataTable/{id}" )
  @Produces( MediaType.APPLICATION_JSON )
  public DataTableDTO getDataTable( @PathParam( "id" ) String id ) {
    DataTable dataTable = this.getStepPreviewDataTest( id );
    DataTableDTO dataTableDTO = new DataTableDTO( dataTable );
    return dataTableDTO;
  }


  // endregion

}
