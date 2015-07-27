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
