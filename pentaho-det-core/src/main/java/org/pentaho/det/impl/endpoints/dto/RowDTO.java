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

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public final class RowDTO {

  public static final class CellDTO<T> {

    @XmlElement( name = "v" )
    //public T value;
    public T v;

    @XmlElement( name = "f" )
    //public String formatedValue;
    public String f;

    @XmlElement( name = "p" )
    //public Object custom;
    public Object p;

    public CellDTO() { }

    public CellDTO( T value ) {
      this.v = value;
    }

  }

  @XmlElement( name = "c", required = true )
  //public List<CellDTO> cells;
  public List<CellDTO> c;

  @XmlElement( name = "p" )
  //public Object custom;
  public Object p;


  public RowDTO() {
    this.c = new ArrayList<>();
  }

  public RowDTO( Object[] row ) {
    this();

    for ( int iCell = 0; iCell < row.length; iCell++ ) {
      RowDTO.CellDTO cellDto = new RowDTO.CellDTO<>( row[ iCell ] );
      if ( cellDto.v == null ) {
        cellDto = null;
      }
      this.c.add( iCell, cellDto );
    }
  }

}
