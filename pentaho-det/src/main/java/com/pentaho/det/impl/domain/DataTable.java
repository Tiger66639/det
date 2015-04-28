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

package com.pentaho.det.impl.domain;

import org.pentaho.di.core.row.RowMetaInterface;

import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataTable {

  // region Properties
  // TODO move annotation to DTO
  @XmlTransient
  public RowMetaInterface getRowMetaInterface() {
    return rowMetaInterface;
  }
  public void setRowMetaInterface( RowMetaInterface rowMetaInterface ) {
    this.rowMetaInterface = rowMetaInterface;
  }
  private RowMetaInterface rowMetaInterface;

  public List<Object[]> getRows() {
    return rows;
  }
  public void setRows( List<Object[]> rows ) {
    this.rows = rows;
  }
  private List<Object[]> rows;

  public List<String> getFieldNames() {
    return Arrays.asList( this.getRowMetaInterface().getFieldNames() );
  }
  // endregion

  // region Constructors
  public DataTable() {
    this.setRows( new ArrayList() );
  }
  // endregion

}
