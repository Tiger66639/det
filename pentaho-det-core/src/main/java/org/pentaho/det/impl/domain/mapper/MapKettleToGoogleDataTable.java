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


import org.pentaho.det.api.domain.IField.ColumnType;
import org.pentaho.det.api.domain.mapper.IConverter;
import org.pentaho.det.api.domain.mapper.IMapKettleToGoogleDataType;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.det.impl.domain.mapper.UnableToConvertException;
import org.pentaho.di.core.row.ValueMetaInterface;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * MapKettleToGoogleDataTable is responsible for the mapping between Kettle Data Types and Google DataTable Data Types
 * <p/>
 * Kettle Data Types:
 * +-------------+---------------------------------------------------------------+
 * |             |                                                               |
 * | Kettle Type |                        Description                            |
 * |             |                                                               |
 * +-------------+---------------------------------------------------------------+
 * | String      | A variable (unlimited) length text encoded in UTF-8 (Unicode) |
 * +-------------+---------------------------------------------------------------+
 * | Integer     | An signed long (64-bit) integer                               |
 * +-------------+---------------------------------------------------------------+
 * | Number      | A double precision floating point value                       |
 * +-------------+---------------------------------------------------------------+
 * | BigNumber   | An arbitrary (unlimited) precision number                     |
 * +-------------+---------------------------------------------------------------+
 * | Date        | A date-time value with millisecond precision                  |
 * +-------------+---------------------------------------------------------------+
 * | Timestamp*  | A date-time value with nanosecond precision                   |
 * +-------------+---------------------------------------------------------------+
 * | Boolean     | A boolean value (true or false)                               |
 * +-------------+---------------------------------------------------------------+
 * | Binary      | An array of bytes that contain any type of binary data.       |
 * +-------------+---------------------------------------------------------------+
 * | Internet A. | Internet address                                              |
 * +-------------+---------------------------------------------------------------+
 * <p/>
 * Google DataTable Data Types:
 * +-------------+----------------------------------------------------+
 * |             |                                                    |
 * | Google Type |                     Description                    |
 * |             |                                                    |
 * +-------------+----------------------------------------------------+
 * | string      | JavaScript string value                            |
 * +-------------+----------------------------------------------------+
 * | number      | JavaScript number value                            |
 * +-------------+----------------------------------------------------+
 * | boolean     | JavaScript boolean value                           |
 * +-------------+----------------------------------------------------+
 * | datetime    | JavaScript Date object including the time          |
 * +-------------+----------------------------------------------------+
 * | date        | JavaScript Date object (zero-based month)          |
 * +-------------+----------------------------------------------------+
 * | timeofday   | Array of three numbers and an optional fourth      |
 * |             | hour (0=midnight), minute, second, and millisecond |
 * +-------------+----------------------------------------------------+
 * <p/>
 * Mapping between Kettle and Google DataTable
 * We are following this logic(see bellow) to map data types between Kettle and Google DataTable based on each
 * data type description and what is already being done in CDA, that follows a very similar logic.
 * <p/>
 * If a valid match is not found for a data type, it will be mapped as a string by default.
 * <p/>
 * +-------------+----------------------------+
 * |             |                            |
 * | Google Type |        Kettle Type         |
 * |             |                            |
 * +-------------+----------------------------+
 * | string      | String                     |
 * +-------------+----------------------------+
 * | number      | Integer, Number, BigNumber |
 * +-------------+----------------------------+
 * | boolean     | Boolean                    |
 * +-------------+----------------------------+
 * | datetime    | Date, Timestamp            |
 * +-------------+----------------------------+
 */
public class MapKettleToGoogleDataTable implements IMapKettleToGoogleDataType {

    //region properties
    private Map<Integer, ColumnType> dataTypeMapping;
    private Map<Integer, IConverter> converterMapping;
    private static MapKettleToGoogleDataTable instance = null;
    //endregion

    //region Constructors
    private MapKettleToGoogleDataTable() {
        this.dataTypeMapping = defaultTypeMapConfiguration();
        this.converterMapping = defaultConverterConfiguration();
    }
    //endregion

    public static MapKettleToGoogleDataTable getInstance() {
        if ( instance == null) {
            instance = new MapKettleToGoogleDataTable();
        }
        return instance;
    }

    /**
     * Adds a new mapping entry between a kettle data type and a Google DataTable data type
     *
     * @param kettleType Kettle data type
     * @param columnType Google DataTable data type
     */
    @Override
    public void putDataType( Integer kettleType, ColumnType columnType ) {
        this.dataTypeMapping.put( kettleType, columnType );
    }

    /**
     * Removes an existing entry of the mapping between a kettle data type and a Google DataTable data type
     *
     * @param kettleType Kettle data type
     */
    @Override
    public void removeDataType( Integer kettleType ) {
        this.dataTypeMapping.remove( kettleType );
    }


    /**
     * Removes all existing entry of the mapping between a kettle data type and a Google DataTable data type
     *
     */
    @Override
    public void clearDataTypeMapping() {
        this.dataTypeMapping.clear();
    }

    /**
     * Gets the corresponding Google DataTable data type for the given kettle data type. If
     * the given kettle Data Type isn't mapped, it will fallback to the default, ColumnType.STRING
     *
     * @param kettleType kettle data type
     *
     * @return Google DataTable data type
     */
    @Override
    public ColumnType getDataType( Integer kettleType ) {
        ColumnType colType = this.dataTypeMapping.get( kettleType );

        if ( colType == null ) {
            colType = ColumnType.STRING;
        }

        return colType;
    }

    @Override
    public Set<Map.Entry<Integer, ColumnType>> getDataTypes() {
        return this.dataTypeMapping.entrySet();
    }

    /**
     * Adds a new mapping entry between a kettle data type and a object type converter
     *
     * @param kettleType Kettle data type
     * @param converter Object Type Converter
     */
    @Override
    public void putConverter( Integer kettleType, IConverter converter ) {
        this.converterMapping.put( kettleType, converter );
    }

    /**
     * Removes an existing entry of the mapping between a kettle data type and a object type converter
     *
     * @param kettleType Kettle data type
     */
    @Override
    public void removeConverter( Integer kettleType ) {
       this.converterMapping.remove( kettleType );
    }

    /**
     * Removes all existing entry of the mapping between a kettle data type and a object type converter
     *
     */
    @Override
    public void clearConverterMapping() {
        this.converterMapping.clear();
    }

    @Override
    public IConverter getConverter( Integer kettleType ) {
        IConverter converter = this.converterMapping.get( kettleType );

        if ( converter == null) {
            converter = new IConverter<ValueMetaInterface>() {
                @Override
                public Object convertObject( Object originalValue, ValueMetaInterface valueMeta ) throws UnableToConvertException {
                    try {
                        return valueMeta.convertToNormalStorageType( originalValue );
                    } catch ( KettleValueException kve ) {
                        throw new UnableToConvertException( originalValue, kve.getCause() );
                    }
                }
            };
        }

        return converter;
    }

    @Override
    public Set<Map.Entry<Integer, IConverter>> getConverters() {
        return this.converterMapping.entrySet();
    }

    //region aux methods
    protected Map<Integer, ColumnType> defaultTypeMapConfiguration() {
        Map<Integer, ColumnType> defaultConfig = new HashMap<Integer, ColumnType>();

        defaultConfig.put( ValueMetaInterface.TYPE_INTEGER,   ColumnType.NUMBER );
        defaultConfig.put( ValueMetaInterface.TYPE_NUMBER,    ColumnType.NUMBER );
        defaultConfig.put( ValueMetaInterface.TYPE_BIGNUMBER, ColumnType.NUMBER );
        defaultConfig.put( ValueMetaInterface.TYPE_BOOLEAN,   ColumnType.BOOLEAN );
        defaultConfig.put( ValueMetaInterface.TYPE_DATE,      ColumnType.DATETIME );
        defaultConfig.put( ValueMetaInterface.TYPE_TIMESTAMP, ColumnType.DATETIME );
        defaultConfig.put( ValueMetaInterface.TYPE_STRING,    ColumnType.STRING );

        return defaultConfig;
    }

    protected Map<Integer, IConverter> defaultConverterConfiguration() {
        Map<Integer, IConverter> defaultConfig = new HashMap<Integer, IConverter>();

        IConverter dateConvert = new DateToCalendarConverter();
        defaultConfig.put( ValueMetaInterface.TYPE_DATE,      dateConvert );
        defaultConfig.put( ValueMetaInterface.TYPE_TIMESTAMP, dateConvert );

        return defaultConfig;
    }
    //endregion
}
