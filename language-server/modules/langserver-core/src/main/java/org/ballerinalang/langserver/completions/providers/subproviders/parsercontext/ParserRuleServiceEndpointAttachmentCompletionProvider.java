/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.ballerinalang.langserver.completions.providers.subproviders.parsercontext;

import org.ballerinalang.langserver.compiler.LSContext;
import org.ballerinalang.langserver.completions.CompletionKeys;
import org.ballerinalang.langserver.completions.SymbolInfo;
import org.ballerinalang.langserver.completions.providers.subproviders.AbstractSubCompletionProvider;
import org.eclipse.lsp4j.CompletionItem;
import org.wso2.ballerinalang.compiler.semantics.model.symbols.BEndpointVarSymbol;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Completion item provider for the service endpoint attachment context.
 */
public class ParserRuleServiceEndpointAttachmentCompletionProvider extends AbstractSubCompletionProvider {
    @Override
    public List<CompletionItem> resolveItems(LSContext context) {
        List<SymbolInfo> endpointSymbols = (context.get(CompletionKeys.VISIBLE_SYMBOLS_KEY)).stream()
                .filter(symbolInfo -> symbolInfo.getScopeEntry().symbol instanceof BEndpointVarSymbol)
                .collect(Collectors.toList());
        
        return this.getCompletionItemList(endpointSymbols, context);
    }
}
