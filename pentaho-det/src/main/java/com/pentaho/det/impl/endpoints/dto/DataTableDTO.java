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

package com.pentaho.det.impl.endpoints.dto;

import com.pentaho.det.impl.domain.DataTable;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public final class DataTableDTO {

  @XmlElement( name = "cols" )
  public List<ColumnDefinitionDTO> columnDefinitions;

  @XmlElement( name = "rows" )
  public List<RowDTO> rows;

  @XmlElement( name = "p" )
  public Object custom;

  public DataTableDTO() {
    this.columnDefinitions = new ArrayList<>();
    this.rows = new ArrayList<>();
  }

  public DataTableDTO( DataTable dataTable ) {
    this();

    if( dataTable != null ) {
      RowMetaInterface rowMeta = dataTable.getRowMetaInterface();
      String[] fieldNames = rowMeta.getFieldNames();
      for ( int iColumn = 0; iColumn < rowMeta.size(); iColumn++ ) {
        String fieldName = fieldNames[iColumn];
        ValueMetaInterface cellValueMeta = rowMeta.getValueMeta( iColumn );
        this.columnDefinitions.add( iColumn, new ColumnDefinitionDTO( cellValueMeta, fieldName ) );
      }

      List<Object[]> rows = dataTable.getRows();
      for ( int iRow = 0; iRow < rows.size(); iRow++ ) {
        RowDTO rowDto = new RowDTO( rows.get( iRow ) );
        this.rows.add( iRow, rowDto );
      }
    }

  }



}
