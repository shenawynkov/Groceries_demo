package com.shenawynkov.groceriesdemo.data.repository

import com.shenawynkov.groceriesdemo.data.local.GroceryDao
import com.shenawynkov.groceriesdemo.data.mappers.toDomain
import com.shenawynkov.groceriesdemo.data.mappers.toEntity
import com.shenawynkov.groceriesdemo.domain.repository.GroceryRepository
import com.shenawynkov.groceriesdemo.domain.model.GroceryItem
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroceryRepositoryImpl @Inject constructor(private val dao: GroceryDao) : GroceryRepository {

    override fun getAll(): Flow<Result<List<GroceryItem>>> = dao.getAll()
        .map { entities -> Result.success(entities.map { it.toDomain() }) }
        .catch { e ->
            if (e is CancellationException) throw e
            else emit(Result.failure(e))
        }
        .flowOn(Dispatchers.IO)

    override suspend fun add(item: GroceryItem): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            dao.insert(item.toEntity())
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun update(item: GroceryItem): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            dao.update(item.toEntity())
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun delete(item: GroceryItem): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            dao.delete(item.toEntity())
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
