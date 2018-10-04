/*
 *  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.ballerinalang.model.values;

import org.ballerinalang.model.types.BType;
import org.ballerinalang.model.types.BTypes;
import org.wso2.ballerinalang.compiler.semantics.model.types.util.Decimal128;

/**
 * The {@code BDecimal} represents a decimal value in Ballerina.
 *
 * @since 0.981.2
 */
public final class BDecimal extends BValueType implements BRefType<Decimal128> {

    private Decimal128 value;

    public BDecimal(Decimal128 value) {
        this.value = value;
    }

    @Override
    public Decimal128 decimalValue() {
        return this.value;
    }

    @Override
    public long intValue() {
        return value.longValue();
    }

    @Override
    public byte byteValue() {
        return value.byteValue();
    }

    @Override
    public double floatValue() {
        return value.doubleValue();
    }

    @Override
    public boolean booleanValue() {
        return false;
    }

    @Override
    public String stringValue() {
        return value.toString();
    }

    @Override
    public Decimal128 value() {
        return this.value;
    }

    @Override
    public BType getType() {
        return BTypes.typeDecimal;
    }

    @Override
    public BValue copy() {
        return new BDecimal(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        BDecimal bDecimal = (BDecimal) obj;
        return (value.compareTo(bDecimal.value) == 0);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
