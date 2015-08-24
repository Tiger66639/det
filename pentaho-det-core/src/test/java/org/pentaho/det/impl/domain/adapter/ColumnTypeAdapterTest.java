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


import org.junit.Test;
import org.pentaho.det.api.domain.IField.ColumnType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ColumnTypeAdapterTest {

    @Test
    public void testColumnTypeMarshall() throws Exception {
        ColumnTypeAdapter adapter = new ColumnTypeAdapter();

        ColumnType colType = ColumnType.NUMBER;
        String expectedResult = "number";

        String result = adapter.marshal( colType );
        assertThat(result, is(equalTo(expectedResult)));
    }

    @Test
    public void testColumnTypeUnmarshal() throws Exception {
        ColumnTypeAdapter adapter = new ColumnTypeAdapter();

        String colTypeString = "string";
        ColumnType expectedResult = ColumnType.STRING;

        ColumnType result = adapter.unmarshal(colTypeString);
        assertThat(result, is(equalTo(expectedResult)));
    }
}
