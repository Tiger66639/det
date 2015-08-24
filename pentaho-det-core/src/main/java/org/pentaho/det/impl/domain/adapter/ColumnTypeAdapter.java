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

package org.pentaho.det.impl.domain.adapter;


import org.pentaho.det.api.domain.IField.ColumnType;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ColumnTypeAdapter extends XmlAdapter<String, ColumnType> {

    /**
     *
     * @param stringColType string value of a ColumnType
     * @return ColumnType object relative to the string value
     * @throws Exception
     */
    @Override
    public ColumnType unmarshal( String stringColType ) throws Exception {
        return ColumnType.valueOf( stringColType.toUpperCase() );
    }

    /**
     *
     * @param columnType ColumnType object
     * @return lower case string value of columnType
     * @throws Exception
     */
    @Override
    public String marshal( ColumnType columnType ) throws Exception {
        return columnType.toString().toLowerCase();
    }
}
