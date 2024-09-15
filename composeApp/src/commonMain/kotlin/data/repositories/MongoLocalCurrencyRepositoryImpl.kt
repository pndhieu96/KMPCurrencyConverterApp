package data.repositories

import data.local.realm_object.CurrencyRO
import data.mappers.DataMapper
import domain.models.Currency
import domain.models.RequestState
import domain.repository_Interfaces.LocalCurrencyRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.delete
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MongoLocalCurrencyRepositoryImpl(
    private val mapper: DataMapper
)
    : LocalCurrencyRepository {

    private var realm: Realm? = null

    init {
        configureTheRealm()
    }

    private fun configureTheRealm() {
        if(realm == null || realm!!.isClosed()) {
            val config = RealmConfiguration.Builder(
                schema = setOf(CurrencyRO::class)
            )
                .compactOnLaunch()
                .build()
            realm = Realm.open(config)
        }
    }

    override suspend fun insertCurrencyData(currency: Currency) {
        realm?.write { copyToRealm(mapper.mapCurrencyToCurrencyRO(currency)) }
    }

    override fun readCurrencyData(): Flow<RequestState<List<Currency>>> {
        return realm?.query<CurrencyRO>()
            ?.asFlow()
            ?.map { result ->
                RequestState.Success(result.list.map { mapper.mapCurrencyROToCurrency(it) })
            }
            ?: flow{ RequestState.Error(message = "Realm not configured.") }
    }

    override suspend fun cleanUp() {
        realm?.write {
            val currencyCollection = this.query<CurrencyRO>()
            delete(currencyCollection)
        }
    }
}