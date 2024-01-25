package com.gobinda.currency.converter.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.gobinda.currency.converter.data.model.ExchangeRateInfo
import com.gobinda.currency.converter.data.source.OpenExchangeApi
import com.gobindacurrency.converter.countryimages.CountryImageProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AppRepositoryImplTest {

    companion object {
        private const val TEST_APP_ID = "test_app_id_does_not_matter"

        private const val CURRENCY_BDT = "BDT"
        private const val CURRENCY_CAD = "CAD"
        private const val CURRENCY_EUR = "EUR"
        private const val CURRENCY_USD = "USD"
        private const val CURRENCY_YEN = "YEN"

        private val validExchangeRateInfo = ExchangeRateInfo(
            disclaimer = "test_disclaimer",
            license = "test_license",
            timestamp = System.currentTimeMillis(),
            base = "BDT",
            rates = mapOf(
                CURRENCY_BDT to 1.90,
                CURRENCY_USD to 89.0
            )
        )

        private val validCurrencyMap = mapOf(
            CURRENCY_BDT to "Bangladesh",
            CURRENCY_CAD to "Canada",
            CURRENCY_EUR to "Europe",
            CURRENCY_USD to "United State",
            CURRENCY_YEN to "Japan"
        )
    }

    private lateinit var context: Context
    private lateinit var openExchangeApi: OpenExchangeApi
    private lateinit var appRepositoryImpl: AppRepositoryImpl
    private lateinit var mockedBitmap: Bitmap

    @Before
    fun setUp() {
        mockedBitmap = mockk()

        mockkObject(CountryImageProvider)
        every { CountryImageProvider.getBitmap(any(), any()) } returns mockedBitmap

        context = mockk()
        openExchangeApi = mockk()
        appRepositoryImpl = AppRepositoryImpl(context, openExchangeApi)

        every { context.getString(any()) } returns TEST_APP_ID
    }

    @After
    fun tearDown() {
        unmockkObject(CountryImageProvider)
    }

    @Test
    fun `both getCurrency & getExchangeRate requests are successful`() {
        coEvery { openExchangeApi.getExchangeRate(any()) } answers {
            Response.success(validExchangeRateInfo)
        }
        coEvery { openExchangeApi.getCurrencies(any()) } answers {
            Response.success(validCurrencyMap)
        }

        runBlocking {
            appRepositoryImpl.requestForLatestData()

            val expectedRequestStatus = RequestStatus.Successful
            val foundRequestStatus = appRepositoryImpl.requestStatus.value
            TestCase.assertEquals(expectedRequestStatus, foundRequestStatus)

            val currencyInfo = appRepositoryImpl.currencyInfo.value ?: emptyMap()
            TestCase.assertEquals(2, currencyInfo.size)
        }
    }

    @Test
    fun `both getCurrency & getExchangeRate requests are failed`() {
        coEvery { openExchangeApi.getExchangeRate(any()) } answers {
            Response.error(404, "Not Found".toResponseBody())
        }
        coEvery { openExchangeApi.getCurrencies(any()) } answers {
            Response.error(404, "Not Found".toResponseBody())
        }

        runBlocking {
            appRepositoryImpl.requestForLatestData()

            val expectedRequestStatus = RequestStatus.Failed
            val foundRequestStatus = appRepositoryImpl.requestStatus.value
            TestCase.assertEquals(expectedRequestStatus, foundRequestStatus)

            val currencyInfo = appRepositoryImpl.currencyInfo.value ?: emptyMap()
            TestCase.assertEquals(0, currencyInfo.size)
        }
    }

    @Test
    fun `only getCurrency request is successful but getExchangeRate gets failed`() {
        coEvery { openExchangeApi.getExchangeRate(any()) } answers {
            Response.error(404, "Not Found".toResponseBody())
        }
        coEvery { openExchangeApi.getCurrencies(any()) } answers {
            Response.success(validCurrencyMap)
        }

        runBlocking {
            appRepositoryImpl.requestForLatestData()

            val expectedRequestStatus = RequestStatus.Failed
            val foundRequestStatus = appRepositoryImpl.requestStatus.value
            TestCase.assertEquals(expectedRequestStatus, foundRequestStatus)

            val currencyInfo = appRepositoryImpl.currencyInfo.value ?: emptyMap()
            TestCase.assertEquals(0, currencyInfo.size)
        }
    }

    @Test
    fun `only getExchangeRate request is successful but getCurrency gets failed`() {
        coEvery { openExchangeApi.getExchangeRate(any()) } answers {
            Response.success(validExchangeRateInfo)
        }
        coEvery { openExchangeApi.getCurrencies(any()) } answers {
            Response.error(404, "Not Found".toResponseBody())
        }

        runBlocking {
            appRepositoryImpl.requestForLatestData()

            val expectedRequestStatus = RequestStatus.Failed
            val foundRequestStatus = appRepositoryImpl.requestStatus.value
            TestCase.assertEquals(expectedRequestStatus, foundRequestStatus)

            val currencyInfo = appRepositoryImpl.currencyInfo.value ?: emptyMap()
            TestCase.assertEquals(0, currencyInfo.size)
        }
    }
}