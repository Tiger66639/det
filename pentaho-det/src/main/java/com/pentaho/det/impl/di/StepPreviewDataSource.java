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

package com.pentaho.det.impl.di;

import com.pentaho.det.api.domain.IDataSource;
import com.pentaho.det.api.domain.IDataTable;
import com.pentaho.det.api.domain.IDataTableEntry;
import com.pentaho.det.api.domain.IField;
import com.pentaho.det.impl.domain.DataTable;
import com.pentaho.det.impl.domain.DataTableEntry;
import com.pentaho.det.impl.domain.Field;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.step.RowAdapter;
import org.pentaho.di.trans.step.StepInterface;

import java.util.Arrays;
import java.util.List;

public class StepPreviewDataSource implements IDataSource {

  // region Inner Definitions
  private static final class CacheStepWrittenRowsRowListener extends RowAdapter {

    private final IDataTable dataTable;

    public CacheStepWrittenRowsRowListener( IDataTable dataTable ) {
      this.dataTable = dataTable;
    }

    @Override public void rowWrittenEvent( RowMetaInterface rowMeta, Object[] rowData )
      throws KettleStepException {

      Object[] rowDataTrimmed = Arrays.copyOf( rowData, rowMeta.size() );
      IDataTableEntry entry = new DataTableEntry( Arrays.asList( rowDataTrimmed ) );
      this.dataTable.getEntries().add( entry );
      List<IField> fields = this.dataTable.getFields();
      // check if fields have been set
      if ( fields.size() == 0 ) { // TODO: better way to check if fields have been initialized
        for ( ValueMetaInterface valueMeta : rowMeta.getValueMetaList() ) {
          Field field = new Field( valueMeta );
          fields.add( field );
        }
      }
    }
  }
  // endregion

  // region Properties
  @Override public IDataTable getData() {
    return this.dataTable;
  }
  private IDataTable dataTable;
  // endregion

  // region Constructors
  public StepPreviewDataSource( StepInterface step ) {
    this.dataTable = new DataTable();
    step.addRowListener( new CacheStepWrittenRowsRowListener( this.dataTable ) );
  }
  // endregion

  // region Methods
  public void clear() {
    IDataTable dataTable = this.getData();
    dataTable.getEntries().clear();
    dataTable.getFields().clear();
  }
  // endregion
}
