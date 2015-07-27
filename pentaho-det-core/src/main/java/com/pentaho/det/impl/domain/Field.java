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

import com.pentaho.det.api.domain.IField;
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


  @Override public String getType() {
    return this.type;
  }
  public Field setType( String type ) {
    this.type = type;
    return this;
  }
  private String type;

  // endregion

  // region Constructors
  public Field() {

  }

  public Field( ValueMetaInterface valueMeta ) {
    this.setName( valueMeta.getName() )
        .setType( valueMeta.getTypeDesc() );  // TODO Convert type properly
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
