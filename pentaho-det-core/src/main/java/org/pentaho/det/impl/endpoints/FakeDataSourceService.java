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

import org.pentaho.det.impl.endpoints.dto.ColumnDefinitionDTO;
import org.pentaho.det.impl.endpoints.dto.DataSourceDTO;
import org.pentaho.det.impl.endpoints.dto.DataTableDTO;
import org.pentaho.det.impl.endpoints.dto.RowDTO;
import org.pentaho.det.impl.services.IDataSourceService;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Path( "det" )
public class FakeDataSourceService implements IDataSourceService {

  private DataSourceDTO dataSource;
  private DataTableDTO dataTableDTO;

  public FakeDataSourceService() {
    this.dataSource = new DataSourceDTO().setUUID( UUID.randomUUID() ).setName( "fakeDataSource" );

    ColumnDefinitionDTO.ColumnType[] columnTypes = new ColumnDefinitionDTO.ColumnType[] {
        ColumnDefinitionDTO.ColumnType.STRING,
        ColumnDefinitionDTO.ColumnType.NUMBER
    };
    this.dataTableDTO = this.createDataTableDTO( Arrays.asList( columnTypes ), 100 );
    //this.dataTableDTO = this.createDataTableDTO( 2, 100 );
  }

  @Override public Collection<DataSourceDTO> getDataSources() {
    Collection<DataSourceDTO> dataSources = new ArrayList<>( );
    dataSources.add( this.dataSource );
    return dataSources;
  }

  @Override public DataSourceDTO getDataSource( String dataSourceUuidOrName, HttpServletResponse response ) {
    return this.dataSource;
  }

  @Override
  public DataTableDTO getDataSourceData( String dataSourceUuidOrName, HttpServletResponse response ) {
    return this.dataTableDTO;
  }

  private DataTableDTO createDataTableDTO( int numberOfColumns, int numberOfRows ) {
    DataTableDTO dataTable = new DataTableDTO( );
    for ( int iColumnDefinition = 0; iColumnDefinition < numberOfColumns; iColumnDefinition++ ) {
      ColumnDefinitionDTO columnDefinition = new ColumnDefinitionDTO( ColumnDefinitionDTO.ColumnType.STRING, "column" + iColumnDefinition );
      dataTable.cols.add( columnDefinition );
    }

    for ( int iRow = 0; iRow < numberOfRows; iRow++ ) {
      String[] rowData = new String[numberOfColumns];
      for ( int iColumn = 0; iColumn < numberOfColumns; iColumn++ ) {
        rowData[iColumn] = UUID.randomUUID().toString();
      }
      RowDTO row = new RowDTO( rowData );
      dataTable.rows.add( row );
    }

    return dataTable;

  }

  private DataTableDTO createDataTableDTO( Iterable<ColumnDefinitionDTO.ColumnType> columnTypes, int numberOfRows ) {
    DataTableDTO dataTable = new DataTableDTO( );
    int columLabelIdx = 0;
    for ( ColumnDefinitionDTO.ColumnType columnType : columnTypes ) {
      ColumnDefinitionDTO columnDefinition = new ColumnDefinitionDTO( columnType, "column" + columLabelIdx++ );
      dataTable.cols.add( columnDefinition );
    }

    for ( int iRow = 0; iRow < numberOfRows; iRow++ ) {
      RowDTO row = this.createRow( columnTypes );
      dataTable.rows.add( row );
    }

    return dataTable;
  }

  private RowDTO createRow( Iterable<ColumnDefinitionDTO.ColumnType> columnTypes ) {
    List<Object> rowData = new ArrayList<>(  );
    for ( ColumnDefinitionDTO.ColumnType columnType : columnTypes ) {
      Object value = this.createRandomValue( columnType );
      rowData.add( value );
    }
    RowDTO row = new RowDTO( rowData.toArray() );
    return row;
  }

  private Object createRandomValue( ColumnDefinitionDTO.ColumnType valueType ) {
    switch ( valueType ) {
      case STRING:
        return UUID.randomUUID().toString();
      case NUMBER:
        return Math.random() * 100;
      default:
        return UUID.randomUUID().toString();
    }
  }

}
