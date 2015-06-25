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

import com.pentaho.det.api.domain.IDataSource;

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
