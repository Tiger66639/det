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

import org.pentaho.det.api.domain.IField;
import org.pentaho.det.impl.domain.mapper.MapKettleToGoogleDataTable;
import org.pentaho.di.core.row.ValueMetaInterface;

public final class Field implements IField {

  // region Properties
  @Override public String getName() {
    return this.name;
  }
  public Field setName( String name ) {
    if ( name == null ) {
      throw new IllegalArgumentException( this.getClass().getName() + " name can not be null." );
    }

    this.name = name;
    return this;
  }
  private String name;


  @Override public ColumnType getType() {
    return this.type;
  }
  public Field setType( ColumnType type ) {
    this.type = type;
    return this;
  }
  private ColumnType type;

  public MapKettleToGoogleDataTable getMapping() {
    return this.mapping;
  }
  public void setMapping( MapKettleToGoogleDataTable map ) {
    this.mapping = map;
  }
  private MapKettleToGoogleDataTable mapping;

  // endregion

  // region Constructors
  public Field() {
    this.mapping = new MapKettleToGoogleDataTable();
  }

  public Field( ValueMetaInterface valueMeta ) {
    this();
    this.setName( valueMeta.getName() )
        .setType( mapping.getDataType( valueMeta ) );  // TODO Convert type properly
  }

  @Override public boolean equals( Object o ) {
    if ( this == o ) {
      return true;
    }
    if ( o == null || getClass() != o.getClass() ) {
      return false;
    }

    Field field = (Field) o;

    return getName().equals( field.getName() );

  }

  @Override public int hashCode() {
    return getName().hashCode();
  }
  // endregion
}
