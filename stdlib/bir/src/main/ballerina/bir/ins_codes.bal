// Copyright (c) 2019 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

public const int INS_GOTO = 1;
public const int INS_CALL = 2;
public const int INS_BRANCH = 3;
public const int INS_RETURN = 4;
public const int INS_ASYNC_CALL = 5;
public const int INS_WAIT = 6;
public const int INS_FP_CALL = 7;

// Non-terminating instructions
public const int INS_MOVE = 20;
public const int INS_CONST_LOAD = 21;
public const int INS_NEW_MAP = 22;
public const int INS_MAP_STORE = 23;
public const int INS_MAP_LOAD = 24;
public const int INS_NEW_ARRAY = 25;
public const int INS_ARRAY_STORE = 26;
public const int INS_ARRAY_LOAD = 27;
public const int INS_NEW_ERROR = 28;
public const int INS_TYPE_CAST = 29;
public const int INS_IS_LIKE = 30;
public const int INS_TYPE_TEST = 31;
public const int INS_NEW_INST = 32;
public const int INS_OBJECT_STORE = 33;
public const int INS_OBJECT_LOAD = 34;
public const int INS_PANIC = 35;
public const int INS_FP_LOAD = 36;

public const int INS_NEW_XML_ELEMENT = 37;
public const int INS_NEW_XML_TEXT = 38;
public const int INS_NEW_XML_COMMENT = 39;
public const int INS_NEW_XML_PI = 40;
public const int INS_NEW_XML_SEQ = 41;
public const int INS_NEW_XML_QNAME = 42;
public const int INS_NEW_STRING_XML_QNAME = 43;
public const int INS_XML_SEQ_STORE = 44;
public const int INS_XML_SEQ_LOAD = 45;
public const int INS_XML_LOAD = 46;
public const int INS_XML_LOAD_ALL = 47;
public const int INS_XML_ATTRIBUTE_LOAD = 48;
public const int INS_XML_ATTRIBUTE_STORE = 49;
public const int INS_NEW_TABLE = 50;


// Binary expression related instructions.
public const int INS_ADD = 51;
public const int INS_SUB = 52;
public const int INS_MUL = 53;
public const int INS_DIV = 54;
public const int INS_MOD = 55;
public const int INS_EQUAL = 56;
public const int INS_NOT_EQUAL = 57;
public const int INS_GREATER_THAN = 58;
public const int INS_GREATER_EQUAL = 59;
public const int INS_LESS_THAN = 60;
public const int INS_LESS_EQUAL = 61;
public const int INS_AND = 62;
public const int INS_OR = 63;

// Unary expression related instructions.
public const int INS_TYPEOF = 70;
public const int INS_NOT = 71;
