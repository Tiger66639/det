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

import com.pentaho.det.impl.endpoints.dto.ColumnDefinitionDTO;
import com.pentaho.det.impl.endpoints.dto.DataSourceDTO;
import com.pentaho.det.impl.endpoints.dto.DataTableDTO;
import com.pentaho.det.impl.endpoints.dto.RowDTO;
import com.pentaho.det.impl.services.IDataSourceService;

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
