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

import org.pentaho.di.core.row.ValueMetaInterface;

import javax.xml.bind.annotation.XmlElement;

public final class ColumnDefinitionDTO {

  // TODO: these are the possible google data types
  public enum ColumnType { BOOLEAN, NUMBER, STRING, DATE, DATETIME, TIMEOFDAY }

  @XmlElement( required = true )
  public final ColumnType type;

  @XmlElement( name = "id" )
  public String id;

  @XmlElement( name = "label" )
  public String label;

  @XmlElement( name = "pattern" )
  public String pattern;

  public ColumnDefinitionDTO( ColumnType type, String label ) {
    if ( type == null ) {
      throw new IllegalArgumentException( "columnType can not be null." );
    }
    this.type = type;
    this.label = label;
  }

  public ColumnDefinitionDTO( ValueMetaInterface valueMetaInterface, String label ) {
    this( getColumnType( valueMetaInterface ), label );
  }

  public ColumnDefinitionDTO( String type, String label ) {
    this( getColumnType( type ), label );
  }


  private static ColumnType getColumnType( ValueMetaInterface valueMetaInterface ) {
    int valueType = valueMetaInterface.getType();
    switch ( valueType ) {
      case ValueMetaInterface.TYPE_BIGNUMBER:
      case ValueMetaInterface.TYPE_INTEGER:
      case ValueMetaInterface.TYPE_NUMBER:
        return ColumnType.NUMBER;
      case ValueMetaInterface.TYPE_BOOLEAN:
        return ColumnType.BOOLEAN;
      case ValueMetaInterface.TYPE_STRING:
        return ColumnType.STRING;
      case ValueMetaInterface.TYPE_DATE:
        return ColumnType.DATE;
      default:
        throw new IllegalArgumentException( "Unknown v meta interface type: " + valueType );
    }
  }

  // TODO converter
  private static ColumnType getColumnType( String type ) {
    if ( type == null ) {
      throw new IllegalArgumentException( "Invalid null type." );
    }

    switch ( type.toUpperCase() ) {
      case "BIGNUMBER":
      case "INTEGER":
      case "NUMBER":
        return ColumnType.NUMBER;
      case "BOOLEAN":
        return ColumnType.BOOLEAN;
      case "STRING":
        return ColumnType.STRING;
      case "DATE":
        return ColumnType.DATE;
      default:
        throw new IllegalArgumentException( "Unknown field type: " + type );
    }
  }

}
