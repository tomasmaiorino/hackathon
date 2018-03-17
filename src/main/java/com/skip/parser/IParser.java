package com.skip.parser;

import java.util.Set;

public interface IParser<R, M> {

	M toModel(final R resource);

	R toResource(final M model);

	Set<R> toResources(final Set<M> models);
}
