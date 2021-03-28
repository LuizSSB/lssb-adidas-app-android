package com.luizssb.adidas.confirmed.service.product.impl

import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.service.PaginationResult
import com.luizssb.adidas.confirmed.service.product.ProductService
import com.luizssb.adidas.confirmed.service.retrofit.RetrofitProductRESTAPI
import com.luizssb.adidas.confirmed.utils.PageRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.await
import java.util.*
import kotlin.math.min

class ProductServiceImpl(private val api: RetrofitProductRESTAPI) : ProductService {
    override suspend fun getProducts(searchQuery: String?, pageRef: PageRef): PaginationResult<Product> =
            withContext(Dispatchers.IO) {
                val products = api.getProducts().await()
                        .filter { it.isValid }
                        // luizssb: since we are faking filtering, may as well fake pagination.
                        .run { subList(pageRef.skip, min(pageRef.upperBound, size)) }
                        .map { it.fakeData()}

                // luizssb: simple check to avoid having to filter when there's no search query.
                val filtered = searchQuery?.takeIf { it.isNotBlank() }
                        ?.run { toLowerCase(Locale.getDefault()) }
                        ?.let { query ->
                            products.filter {
                                // luizssb: filters on the ids, as all the products in the sample
                                // database have the same name.
                                it.id.toLowerCase(Locale.getDefault()).contains(query)
                            }
                } ?:
                    products

                PaginationResult(filtered, filtered.size >= pageRef.limit)
            }

    override suspend fun getProduct(productId: String): Product? = withContext(Dispatchers.IO) {
        try {
            api.getProduct(productId)
                    .await()
                    .takeIf { it.isValid }
                    ?.run { fakeData() }
        } catch (ex: HttpException) {
            if (ex.code() == 500) null
                else throw ex
        }
    }

    companion object {
        // luizssb: also faking prices and currency, which come nil
        private fun Product.fakeData() = copy(
                price = (id.hashCode() / 1e6).toFloat(),
                currency = "EUR"
        )

        // luizssb: there is an is an entry in the sample database that, for some
        // reason, has all null data. I'm assumming this is not on purpose, therefore
        // to avoid having to implement some extra structures to handle this single
        // problematic case, I'm just cutting it off right here.
        @Suppress("SENSELESS_COMPARISON")
        private val Product.isValid get() = id != null
    }
}
