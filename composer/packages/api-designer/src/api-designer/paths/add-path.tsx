/**
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

import * as Swagger from "openapi3-ts";
import * as React from "react";
import { Button, Form, Input, Label, Transition } from "semantic-ui-react";

export interface AddOpenApiPathProps {
    onAddOpenApiPath: (path: Swagger.PathItemObject, onAdd: (state: boolean) => void) => void;
}

export interface AddOpenApiPathState {
    openApiResourceObj: OpenApiResource;
    operationMethods: OpenApiOperationMethod[];
    showState: ShowState;
}

export interface OpenApiResource {
    name: string;
    methods: string[];
}

export interface OpenApiOperationMethod {
    text: string;
    value: string;
}

export interface ShowState {
    show: boolean;
    state: string;
}

class AddOpenApiPath extends React.Component<AddOpenApiPathProps, AddOpenApiPathState> {

    constructor(props: AddOpenApiPathProps) {
        super(props);

        this.state = {
            openApiResourceObj: {
                methods: [],
                name: ""
            },
            operationMethods: [],
            showState: {
                show: false,
                state: ""
            }
        };

        this.clearFields = this.clearFields.bind(this);
        this.onAddPath = this.onAddPath.bind(this);
        this.onShowMessage = this.onShowMessage.bind(this);

    }

    public componentDidMount() {
        this.populateOperationMethods();
    }

    public populateOperationMethods() {
        const methodOpts: OpenApiOperationMethod[] = [];

        const availableMethods = ["GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS"];

        availableMethods.forEach((method) => {
            methodOpts.push({
                text: method,
                value: method.toLowerCase(),
            });
        });

        this.setState({
            operationMethods: methodOpts,
        });
    }

    public render() {
        const { operationMethods, showState } = this.state;
        const { onAddOpenApiPath } = this.props;

        return (
            <Form size="mini" className="add-resource">
                <Form.Field>
                    <label>Resource Name</label>
                    <Input label="/"
                        placeholder="Example: users/{userId}"
                    value={this.state.openApiResourceObj.name}
                        onChange={(e) => {
                            this.setState({
                                openApiResourceObj: {
                                    ...this.state.openApiResourceObj,
                                    name: e.target.value.replace(/^\//, "").trim()
                                }
                            });
                        }}
                    />
                </Form.Field>
                <Form.Group inline>
                    <label>Methods</label>
                    {operationMethods.map((method) => {
                        return (
                            <Form.Checkbox
                                size="mini"
                                label={method.text}
                                value={method.value}
                                onChange={(e: React.SyntheticEvent, data: any) => {
                                    if (data.checked) {
                                        this.setState({
                                            openApiResourceObj: {
                                                ...this.state.openApiResourceObj,
                                                methods: [...this.state.openApiResourceObj.methods, data.label],
                                            }
                                        });
                                    }
                                }}
                            />
                        );
                    })}
                </Form.Group>
                <Button size="mini" onClick={() => {
                    onAddOpenApiPath(this.state.openApiResourceObj, this.onAddPath);
                    this.clearFields();
                }}>Add Resource</Button>
                <Transition visible={showState.show && showState.state === "successful"}
                    onComplete={this.onShowMessage} animation="scale" duration={500}>
                    <Label color="green">
                        successfully added the path.
                    </Label>
                </Transition>
                <Transition visible={showState.show && showState.state === "error"}
                    onComplete={this.onShowMessage} animation="scale" duration={500}>
                    <Label color="red">
                        Error occured while adding path.
                    </Label>
                </Transition>
            </Form>
        );
    }

    private onAddPath(state: boolean) {
        this.setState({
            showState: {
                show: true,
                state: state ? "successful" : "error"
            }
        });
    }

    private onShowMessage() {
        setTimeout(() => {
            this.setState({
                showState: {
                    show: false,
                    state: ""
                }
            });
        } , 1500);
    }

    private clearFields() {
        this.setState({
            openApiResourceObj: {
                methods: [],
                name: ""
            },
            operationMethods: []
        }, () => {
            this.populateOperationMethods();
        });
    }
}

export default AddOpenApiPath;