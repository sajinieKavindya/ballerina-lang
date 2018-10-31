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
package org.ballerinalang.net.grpc.builder.components;

/**
 * Bean object class of attribute object.
 */
public class Attribute {
    private String name;
    private String type;
    private String label;
    private String tag;
    private String method;
    
    public Attribute(String name, String type, String label, String tag, String method) {
        this.name = name;
        this.type = type;
        this.label = label;
        this.tag = tag;
        this.method = method;
    }

    public String getTag() {
        return tag;
    }
  
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getLabel() {
        return label;
    }

    public String getMethod() {
        return method;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
