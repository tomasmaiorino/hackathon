package com.skip.hackathon.service;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

import com.skip.hackathon.repository.IBaseRepository;

public interface IBaseService<I, T extends Serializable> {

	IBaseRepository<I, T> getRepository();

	I save(final I model);

	I update(final I origin, final I model);

	I findById(final T id);

	Optional<I> findOptionalById(final T id);

	Set<I> findAll();

}
