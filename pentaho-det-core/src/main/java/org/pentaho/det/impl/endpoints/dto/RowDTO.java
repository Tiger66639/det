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
