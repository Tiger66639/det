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

package org.pentaho.det.impl.endpoints.dto;

import org.pentaho.det.api.domain.IDataTable;
import org.pentaho.det.api.domain.IDataTableEntry;
import org.pentaho.det.api.domain.IField;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public final class DataTableDTO {

  @XmlElement( name = "cols" )
  //public List<ColumnDefinitionDTO> cols;
  public List<ColumnDefinitionDTO> cols;

  @XmlElement( name = "rows" )
  public List<RowDTO> rows;

  @XmlElement( name = "p" )
  //public Object p;
  public Object p;

  public DataTableDTO() {
    this.cols = new ArrayList<>();
    this.rows = new ArrayList<>();
  }

  /*
  public DataTableDTO( DataTableOld dataTable ) {
    this();

    if( dataTable != null ) {
      RowMetaInterface rowMeta = dataTable.getRowMetaInterface();
      String[] fieldNames = rowMeta.getFieldNames();
      for ( int iColumn = 0; iColumn < rowMeta.size(); iColumn++ ) {
        String fieldName = fieldNames[iColumn];
        ValueMetaInterface cellValueMeta = rowMeta.getValueMeta( iColumn );
        this.cols.add( iColumn, new ColumnDefinitionDTO( cellValueMeta, fieldName ) );
      }

      List<Object[]> rows = dataTable.getRows();
      for ( int iRow = 0; iRow < rows.size(); iRow++ ) {
        RowDTO rowDto = new RowDTO( rows.get( iRow ) );
        this.rows.add( iRow, rowDto );
      }
    }

  }
*/
  public DataTableDTO( IDataTable dataTable ) {
    this();

    if ( dataTable != null ) {
      for ( IField field : dataTable.getFields() ) {
        ColumnDefinitionDTO columnDefinitionDTO = new ColumnDefinitionDTO( field.getType(), field.getName() );
        this.cols.add( columnDefinitionDTO );
      }

      for ( IDataTableEntry dataTableEntry : dataTable.getEntries() ) {
        RowDTO rowDto = new RowDTO( dataTableEntry.getData().toArray() );
        this.rows.add( rowDto );
      }
    }

  }



}
