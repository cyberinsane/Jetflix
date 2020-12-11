package com.cyberinsane.jetflix.domain.usecase

import com.cyberinsane.jetflix.domain.model.Resource

/**
 * BaseAsyncUseCase
 *
 * Base use case to define input and output as wild cards and invoke operator for execution
 * @param Input - variable to use case
 * @return Output
 */
abstract class BaseUseCase<Input, Output : Any> {

    abstract suspend fun createSuspend(data: Input): Resource<Output>

    suspend operator fun invoke(withData: Input) = createSuspend(withData)

}
