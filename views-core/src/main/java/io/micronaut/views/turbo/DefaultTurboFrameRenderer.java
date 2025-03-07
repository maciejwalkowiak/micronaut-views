/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.views.turbo;

import io.micronaut.http.MediaType;
import io.micronaut.views.ViewsRendererLocator;
import jakarta.inject.Singleton;

/**
 * {@link io.micronaut.context.annotation.DefaultImplementation} of {@link TurboFrameRenderer}.
 * @author Sergio del Amo
 * @since 3.4.0
 */
@Singleton
public class DefaultTurboFrameRenderer extends AbstractTurboRenderer<TurboFrame.Builder> implements TurboFrameRenderer {
    /**
     * Constructor.
     * @param viewsRendererLocator Views Renderer Locator.
     */
    public DefaultTurboFrameRenderer(ViewsRendererLocator viewsRendererLocator) {
        super(viewsRendererLocator, MediaType.TEXT_HTML_TYPE);
    }
}
