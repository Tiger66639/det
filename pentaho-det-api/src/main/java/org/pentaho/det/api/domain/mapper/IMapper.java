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

package org.pentaho.det.api.domain.mapper;

import org.pentaho.det.impl.domain.mapper.UnableToConvertException;

import java.util.Map;
import java.util.Set;


public interface IMapper<OriginalType, TargetType> {

    void putDataType( OriginalType source, TargetType target );
    void removeDataType( OriginalType source );
    void clearDataTypeMapping();
    TargetType getDataType( OriginalType source );
    Set<Map.Entry<OriginalType,TargetType>> getDataTypes();

    void putConverter( OriginalType source, IConverter converter );
    void removeConverter( OriginalType source );
    void clearConverterMapping();
    IConverter getConverter( OriginalType source ) throws UnableToConvertException;
    Set<Map.Entry<OriginalType,IConverter>> getConverters();
}
