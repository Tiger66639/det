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

package org.pentaho.det.di;

public abstract class EventHandler<T> implements  IEventHandler<T> {

  public EventHandler( Class<T> typeParameter ) {
    if ( typeParameter == null ) {
      throw new IllegalArgumentException();
    }

    this.typeParameter = typeParameter;
  }

  // region Methods
  @Override
  public abstract void handle( T arg );

  @Override
  public Class<T> getTypeParameter() {
    return this.typeParameter;
  }
  private Class<T> typeParameter;
  // endregion

}
