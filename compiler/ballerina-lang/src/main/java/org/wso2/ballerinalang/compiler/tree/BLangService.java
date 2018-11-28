/*
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.wso2.ballerinalang.compiler.tree;

import org.ballerinalang.model.elements.Flag;
import org.ballerinalang.model.tree.AnnotationAttachmentNode;
import org.ballerinalang.model.tree.DeprecatedNode;
import org.ballerinalang.model.tree.IdentifierNode;
import org.ballerinalang.model.tree.MarkdownDocumentationNode;
import org.ballerinalang.model.tree.NodeKind;
import org.ballerinalang.model.tree.ServiceNode;
import org.ballerinalang.model.tree.expressions.SimpleVariableReferenceNode;
import org.wso2.ballerinalang.compiler.semantics.model.symbols.BSymbol;
import org.wso2.ballerinalang.compiler.semantics.model.types.BType;
import org.wso2.ballerinalang.compiler.tree.expressions.BLangExpression;
import org.wso2.ballerinalang.compiler.tree.expressions.BLangRecordLiteral;
import org.wso2.ballerinalang.compiler.tree.statements.BLangSimpleVariableDef;
import org.wso2.ballerinalang.compiler.tree.types.BLangUserDefinedType;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * @since 0.94
 *
 *  TODO : Fix me.
 */
public class BLangService extends BLangNode implements ServiceNode {

    public Set<Flag> flagSet;
    public List<BLangAnnotationAttachment> annAttachments;
    public BLangMarkdownDocumentation markdownDocumentationAttachment;
    public List<BLangDeprecatedNode> deprecatedAttachments;

    public BSymbol symbol;
    public BLangIdentifier name;
    public BLangUserDefinedType serviceUDT;
    public BLangTypeDefinition serviceTypeDefinition;
    public BLangExpression attachExpr;
    public boolean isAnonymousServiceValue;

    // Cached values.
    public String listenerName;
    public BType listerType;
    public List<BLangFunction> resourceFunctions;

    // Old values. TODO : Remove this.
    @Deprecated
    public List<BLangResource> resources = new ArrayList<>();
    @Deprecated
    public List<BLangSimpleVariableDef> vars = new ArrayList<>();
    @Deprecated
    public List<BLangEndpoint> endpoints = new ArrayList<>();
    @Deprecated
    public BLangRecordLiteral anonymousEndpointBind = null;
    @Deprecated
    public List<? extends SimpleVariableReferenceNode> boundEndpoints = new ArrayList<>();

    public BLangService() {
        this.flagSet = EnumSet.noneOf(Flag.class);
        this.annAttachments = new ArrayList<>();
        this.deprecatedAttachments = new ArrayList<>();
        this.resourceFunctions = new ArrayList<>();
    }

    @Override
    public BLangIdentifier getName() {
        return name;
    }

    @Override
    public void setName(IdentifierNode name) {
        this.name = (BLangIdentifier) name;
    }

    @Override
    public boolean isAnonymousService() {
        return this.isAnonymousServiceValue;
    }

    public List<BLangFunction> getResources() {
        return resourceFunctions;
    }

    public BLangExpression getAttachExpr() {
        return this.attachExpr;
    }

    @Override
    public BLangUserDefinedType getUserDefinedTypeNode() {
        return this.serviceUDT;
    }

    @Override
    public BLangTypeDefinition getTypeDefinition() {
        return this.serviceTypeDefinition;
    }

    @Override
    public Set<Flag> getFlags() {
        return null;
    }

    @Override
    public void addFlag(Flag flag) {
        this.getFlags().add(flag);
    }

    @Override
    public List<BLangAnnotationAttachment> getAnnotationAttachments() {
        return annAttachments;
    }

    @Override
    public void addAnnotationAttachment(AnnotationAttachmentNode annAttachment) {
        this.getAnnotationAttachments().add((BLangAnnotationAttachment) annAttachment);
    }

    @Override
    public BLangMarkdownDocumentation getMarkdownDocumentationAttachment() {
        return markdownDocumentationAttachment;
    }

    @Override
    public void setMarkdownDocumentationAttachment(MarkdownDocumentationNode documentationNode) {
        this.markdownDocumentationAttachment = (BLangMarkdownDocumentation) documentationNode;
    }

    @Override
    public List<BLangDeprecatedNode> getDeprecatedAttachments() {
        return deprecatedAttachments;
    }

    @Override
    public void addDeprecatedAttachment(DeprecatedNode deprecatedNode) {
        this.deprecatedAttachments.add((BLangDeprecatedNode) deprecatedNode);
    }

    @Override
    public void accept(BLangNodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public NodeKind getKind() {
        return NodeKind.SERVICE;
    }

    @Override
    public String toString() {
        return "BLangService: " + flagSet + " " + annAttachments + " " + getName();
    }
}
