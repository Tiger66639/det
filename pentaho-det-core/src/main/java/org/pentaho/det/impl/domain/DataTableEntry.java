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

package org.pentaho.det.impl.domain;

import org.pentaho.det.api.domain.IDataTable;
import org.pentaho.det.api.domain.IDataTableEntry;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.RowMetaInterface;

import java.util.ArrayList;
import java.util.List;

public class DataTableEntry implements IDataTableEntry {

  // region Properties
  @Override public IDataTable getDataTable() {
    return this.dataTable;
  }
  public DataTableEntry setDataTable( IDataTable dataTable ) {
    this.dataTable = dataTable;
    return this;
  }
  private IDataTable dataTable;

  @Override public List<Object> getData() {
    return this.data;
  }
  public DataTableEntry setData( List<Object> data ) {
    this.data = data;
    return this;
  }
  private List<Object> data;
  // endregion

  // region Constructors
  public DataTableEntry() {
    this.setData( new ArrayList<>() );
  }

  public DataTableEntry( List<Object> data ) {
    this.setData( data );
  }
  // endregion
}
