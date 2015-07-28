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

import org.pentaho.det.api.domain.IDataSource;

import javax.xml.bind.annotation.XmlElement;
import java.util.UUID;

public final class DataSourceDTO {

  // region Properties
  @XmlElement( name = "uuid" )
  public UUID getUUID() {
    return this.uuid;
  }
  public DataSourceDTO setUUID( UUID uuid ) {
    this.uuid = uuid;
    return this;
  }
  private UUID uuid;

  @XmlElement( name = "name" )
  public String getName() {
    return name;
  }
  public DataSourceDTO setName( String name ) {
    this.name = name;
    return this;
  }
  private String name;
  // endregion

  // region Constructors
  public DataSourceDTO() {

  }

  public DataSourceDTO( IDataSource dataSource ) {
    this.setUUID( dataSource.getUUID() );
    this.setName( dataSource.getName() );
  }
  // endregion

}
