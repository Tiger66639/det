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

package org.pentaho.det.impl.domain.mapper;

import org.junit.Test;
import org.pentaho.di.core.row.ValueMetaInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.hamcrest.MatcherAssert.assertThat;

public class DateToCalendarConverterTest {

    //region Tests
    @Test
    public void testConvert() throws Exception {
        DateToCalendarConverter converter = new DateToCalendarConverter();
        TimeZone tz = TimeZone.getTimeZone( "UTC" );
        Date date = this.createDate( "yyyy/MM/dd HH:mm:ss.S", "2000/04/25 13:37:00.666" );
        ValueMetaInterface valueMeta = this.createValueMetaInterface( date, tz );

        Calendar expectedCalendar = this.getCalendarFromDate( date, tz );
        Calendar returnedCalendar = (Calendar) converter.convertObject( date, valueMeta );

        assertThat( returnedCalendar, is( equalTo( expectedCalendar ) ) );
    }
    //endregion

    //region Auxiliary Methods
    private Date createDate( String format, String date ) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat( format );
        return dateFormat.parse(date);
    }

    private Calendar getCalendarFromDate( Date date, TimeZone timezone ) {
        Calendar calendar = new GregorianCalendar();

        calendar.setTimeZone( timezone );
        calendar.setTime( date );
        return calendar;
    }

    private ValueMetaInterface createValueMetaInterface( Object date, TimeZone timezone ) throws Exception {
        ValueMetaInterface vmi = mock( ValueMetaInterface.class );
        when( vmi.convertToNormalStorageType( date ) ).thenReturn( date );
        when( vmi.getDateFormatTimeZone() ).thenReturn( timezone );
        return vmi;
    }
    //endregion
}
