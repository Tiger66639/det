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


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.TimeZone;
import java.util.GregorianCalendar;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

@RunWith( Parameterized.class )
public class RowValueAdapterTest {

    private Object valueToMarshal;
    private Object valueToUnmarshal;
    private RowValueAdapter rowValueAdapter;

    @Before
    public void initialize() {
        rowValueAdapter = this.createRowValueAdapter();
    }

    public RowValueAdapterTest( Object valueToMarshal, Object valueToUnmarshal ) {
        this.valueToMarshal = valueToMarshal;
        this.valueToUnmarshal = valueToUnmarshal;
    }

    @Parameters
    public static Collection<Object[]> rowValues() throws Exception {
        return Arrays.asList(new Object[][] {
                {
                        "FooBar", "FooBar"
                },
                {
                        1337, 1337
                },
                {
                        false, false
                },
                {
                        createCalendar("yyyy/MM/dd HH:mm:ss.S", "2000/04/25 13:37:00.666"),
                        "2000-04-25T13:37:00.666Z"
                }
        });
    }

    @Test
    public void testRowValueMarshal() throws Exception {
        Object result = this.rowValueAdapter.marshal( this.valueToMarshal );
        assertThat( result, is( equalTo( this.valueToUnmarshal ) ) );
    }

    @Test
    public void testRowValueUnmarshal() throws Exception {
        Object result = this.rowValueAdapter.unmarshal( this.valueToUnmarshal );
        assertThat( result, is( equalTo( this.valueToMarshal ) ) );
    }

    @Test
    public void testRowValueMarshalAndUnmarshal() throws Exception {
        Object marshalResult = this.rowValueAdapter.marshal( this.valueToMarshal );
        Object unmarsalResult = this.rowValueAdapter.unmarshal( marshalResult );

        assertThat( unmarsalResult, is( equalTo( this.valueToMarshal ) ) );
    }

    @Test
    public void testRowValueUnMarshalAndMarshal() throws Exception {
        Object unmarsalResult = this.rowValueAdapter.unmarshal( this.valueToUnmarshal );
        Object marshalResult = this.rowValueAdapter.marshal( unmarsalResult );

        assertThat( marshalResult, is( equalTo( this.valueToUnmarshal ) ) );
    }


    //region Auxiliary Methods
    private RowValueAdapter createRowValueAdapter() {
        return new RowValueAdapter();
    }

    private static Calendar createCalendar( String format, String date ) throws Exception {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat dateFormat = new SimpleDateFormat( format );
        dateFormat.setTimeZone( tz );

        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone( tz );
        calendar.setTime( dateFormat.parse( date ) );

        return calendar;
    }
    //endregion
}
