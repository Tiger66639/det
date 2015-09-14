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

package org.pentaho.det.di;

import org.pentaho.det.api.domain.IDataSource;
import org.pentaho.det.api.domain.IDataTable;
import org.pentaho.det.api.domain.IDataTableEntry;
import org.pentaho.det.api.domain.IField;
import org.pentaho.det.api.domain.mapper.IConverter;
import org.pentaho.det.impl.domain.DataTable;
import org.pentaho.det.impl.domain.DataTableEntry;
import org.pentaho.det.impl.domain.Field;
import org.pentaho.det.impl.domain.mapper.MapKettleToGoogleDataTable;
import org.pentaho.det.impl.domain.mapper.UnableToConvertException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.step.RowAdapter;
import org.pentaho.di.trans.step.RowListener;
import org.pentaho.di.trans.step.StepInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class StepPreviewDataSource implements IDataSource {

  // region Inner Definitions
  private static final class CacheStepWrittenRowsRowListener extends RowAdapter {

    private final IDataTable dataTable;
    private final MapKettleToGoogleDataTable mapping;

    public CacheStepWrittenRowsRowListener( IDataTable dataTable, MapKettleToGoogleDataTable map ) {
      this.dataTable = dataTable;
      this.mapping = map;
    }

    @Override public void rowWrittenEvent( RowMetaInterface rowMeta, Object[] rowData )
      throws KettleStepException {

      List<Object> rowDataList = this.handleRowDataValues( rowMeta, rowData );

      IDataTableEntry entry = new DataTableEntry( rowDataList );
      this.dataTable.getEntries().add( entry );

      this.initFields( rowMeta );
    }

    private List<Object> handleRowDataValues( RowMetaInterface rowMeta, Object[] rowData )
            throws KettleStepException {

      Object[] rowDataTrimmed = Arrays.copyOf( rowData, rowMeta.size() );

      List<Object> dataList = new ArrayList<Object>();
      for ( int i = 0; i < rowMeta.size(); i++ ) {
        try {
          ValueMetaInterface valueMeta = rowMeta.getValueMeta(i);
          IConverter<ValueMetaInterface> converter = mapping.getConverter( valueMeta.getType() );

          Object value = null;
          if( rowDataTrimmed[i] != null ) {
            value = converter.convertObject( rowDataTrimmed[i], valueMeta );
          }

          dataList.add( value );

        } catch ( UnableToConvertException utce ) {
          throw new KettleStepException( utce.getMessage() );
        }
      }
      return dataList;
    }

    private void initFields( RowMetaInterface rowMeta ) {
      List<IField> fields = this.dataTable.getFields();
      // check if fields have been set
      if ( fields.size() == 0 ) { //TODO: better way to check if fields have been initialized
        for ( ValueMetaInterface valueMeta : rowMeta.getValueMetaList() ) {
          Field field = new Field( valueMeta );
          field.setType( mapping.getDataType( valueMeta.getType() ) );
          fields.add( field );
        }
      }
    }
  }
  // endregion

  // region Properties
  @Override public UUID getUUID() {
    return this.uuid;
  }
  private UUID uuid;

  @Override public String getName() {
    if ( this.getStep() == null || this.getStep().getStepMeta() == null ) {
      return null;
    }

    return this.getStep().getStepMeta().getName();
  }

  @Override public IDataTable getData() {
    return this.dataTable;
  }
  private IDataTable dataTable;

  private MapKettleToGoogleDataTable mapping;

  public StepInterface getStep() {
    return this.step;
  }
  public void setStep( StepInterface step ) {
    if ( this.step != null ) {
      step.removeRowListener( this.rowListener );
      this.clear();
    }

    this.step = step;
    this.rowListener = new CacheStepWrittenRowsRowListener( this.dataTable, this.mapping );
    this.step.addRowListener( this.rowListener );
  }
  private StepInterface step;

  private RowListener rowListener;
  // endregion

  // region Constructors
  public StepPreviewDataSource( StepInterface step ) {
    this.uuid = UUID.randomUUID();
    this.dataTable = new DataTable();
    this.mapping = MapKettleToGoogleDataTable.getInstance();
    this.setStep( step );
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
