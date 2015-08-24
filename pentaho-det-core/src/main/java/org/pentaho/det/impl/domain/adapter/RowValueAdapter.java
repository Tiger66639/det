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


import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RowValueAdapter extends XmlAdapter<Object, Object> {


    @Override
    public Object unmarshal( Object s ) throws Exception {
        return null;
    }

    /**
     *
     * @param rowValue row value
     * @return a string if rowValue is a Date, otherwise returns rowValue unchanged
     * @throws Exception
     */
    @Override
    public Object marshal( Object rowValue ) throws Exception {
        if( rowValue instanceof Date ) { //ugly
            return this.marshal( (Date) rowValue );
        }

        return rowValue;
    }

    /**
     * Function that will format the Date value and return a string representation of it,
     * according to google DataTable specifications
     *
     * @param rowValue date to be formatted
     * @return string representation of the date value
     * @throws Exception
     */
    public Object marshal( Date rowValue ) throws  Exception {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime( rowValue );

        return "Date(" +
                cal.get( Calendar.YEAR ) + ", " +
                cal.get( Calendar.MONTH ) + ", " +
                cal.get( Calendar.DAY_OF_MONTH ) + ", " +
                cal.get( Calendar.HOUR_OF_DAY ) + ", " +
                cal.get( Calendar.MINUTE ) + ", " +
                cal.get( Calendar.SECOND ) + ", " +
                cal.get( Calendar.MILLISECOND ) +  ")";
    }
}
