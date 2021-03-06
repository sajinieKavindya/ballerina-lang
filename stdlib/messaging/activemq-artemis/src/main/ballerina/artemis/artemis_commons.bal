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

import ballerina/io;

# Constant for the artemis error code.
public const ARTEMIS_ERROR_CODE = "{ballerina/artemis}ArtemisError";

# The Artemis error record.
#
# + message - the error message.
public type ArtemisError record {|
    string message?;
|};

# The url configuration for `Producer` and `Consumer`.
#
# + host - The host
# + port - The port
# + username - The username
# + password - The password
public type URLConfiguration record {|
    string host;
    int port;
    string username?;
    string password?;
|};

# Determines how messages are sent to the queues associated with an address.
public type RoutingType MULTICAST | ANYCAST;

# If you want your messages routed to every queue within the matching address, in a publish-subscribe manner.
public const MULTICAST = "MULTICAST";
# If you want your messages routed to a single queue within the matching address, in a point-to-point manner.
public const ANYCAST = "ANYCAST";
