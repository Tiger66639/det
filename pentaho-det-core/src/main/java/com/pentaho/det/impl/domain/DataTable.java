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

import com.pentaho.det.api.domain.IDataTable;
import com.pentaho.det.api.domain.IDataTableEntry;
import com.pentaho.det.api.domain.IField;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DataTable implements IDataTable {

  // region Properties
  @Override public List<IField> getFields() {
    return this.fields;
  }
  private List<IField> fields;

  @Override public List<IDataTableEntry> getEntries() {
    return this.entries;
  }
  private List<IDataTableEntry> entries;
  // endregion

  // region Constructors
  public DataTable() {
    // TODO: Depency injection?
    this.fields = new ArrayList<>();
    this.entries = new ArrayList<>();
  }
  // endregion
}
