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

import org.pentaho.det.api.domain.IField.ColumnType;
import org.pentaho.det.impl.domain.adapter.ColumnTypeAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public final class ColumnDefinitionDTO {

  @XmlElement( required = true )
  @XmlJavaTypeAdapter( ColumnTypeAdapter.class )
  public ColumnType type;

  @XmlElement( name = "id" )
  public String id;

  @XmlElement( name = "label" )
  public String label;

  @XmlElement( name = "pattern" )
  public String pattern;

  public ColumnDefinitionDTO( ColumnType type, String label ) {
    this.label = label;
    this.type = type;
  }

}
