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

package org.pentaho.det.impl.domain;

import org.pentaho.det.api.domain.IDataTable;
import org.pentaho.det.api.domain.IDataTableEntry;
import org.pentaho.det.api.domain.IField;

import java.util.ArrayList;
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
