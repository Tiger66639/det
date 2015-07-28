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
