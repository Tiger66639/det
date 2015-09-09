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


import org.pentaho.det.api.domain.mapper.IConverter;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.det.impl.domain.mapper.UnableToConvertException;
import org.pentaho.di.core.row.ValueMetaInterface;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateToCalendarConverter implements IConverter<ValueMetaInterface> {

    /**
     * Converts {@code originalValue} to a calendar, using {@code valueMeta}
     * to set its timezone and to get the date object from {@code originalValue}
     * if it isn't already, before converting it to a calendar.
     *
     * @param originalValue object containing a date
     * @param valueMeta meta information to help convert the {@code originalValue} to a calendar
     * @return Calendar representation of the {@code originalValue}
     * @throws KettleValueException
     */
    @Override
    public Object convertObject( Object originalValue, ValueMetaInterface valueMeta ) throws UnableToConvertException {

        try {
            Date normalStorageValue = (Date) valueMeta.convertToNormalStorageType( originalValue );
            TimeZone timezone = valueMeta.getDateFormatTimeZone();

            Calendar convertedValue = new GregorianCalendar();
            convertedValue.setTime( normalStorageValue );
            convertedValue.setTimeZone( timezone );

            return convertedValue;
        } catch ( KettleValueException kve ) {
            throw new UnableToConvertException( originalValue, kve.getCause() );
        }
    }

    @Override
    public boolean equals( Object o ) {
        return this == o || !( o == null || getClass() != o.getClass() );
    }
}
