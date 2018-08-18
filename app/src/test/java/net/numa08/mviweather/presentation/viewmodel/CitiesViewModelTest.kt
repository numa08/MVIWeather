@file:Suppress("RemoveRedundantBackticks")

package net.numa08.mviweather.presentation.viewmodel

import assertk.assert
import assertk.assertions.isEqualTo
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.Single
import net.numa08.mviweather.data.City
import net.numa08.mviweather.data.source.CitiesDataSource
import net.numa08.mviweather.presentation.intent.CitiesViewIntent
import net.numa08.mviweather.presentation.result.CitiesViewResult
import net.numa08.mviweather.presentation.state.CitiesViewState
import net.numa08.mviweather.utils.TestSchedulerProvider
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class CitiesViewModelTest {


    @Mock
    private val dataSource: CitiesDataSource = mock()

    private lateinit var viewModel: CitiesViewModel

    @Before
    fun initViewModel() {
        viewModel = CitiesViewModel(dataSource, TestSchedulerProvider())
    }

    @Test
    fun `読み込み中にエラーが発生した場合`() {
        val error = RuntimeException("error")
        val response = Single.error<List<City>>(error)
        whenever(dataSource.getCities()).thenReturn(response)

        val subscriber = viewModel.states().test()
        viewModel.processIntents(Observable.just(CitiesViewIntent.InitialIntent))

        subscriber.assertValues(
                CitiesViewState(isLoading = true, cities = emptyList(), error = null),
                CitiesViewState(isLoading = false, cities = emptyList(), error = error)
        )
    }

    @Test
    fun  `読み込みに成功した場合`() {
        val cities = listOf(City("東京", "tokyo"))
        val response = Single.just(cities)
        whenever(dataSource.getCities()).thenReturn(response)

        val subscriber = viewModel.states().test()
        viewModel.processIntents(Observable.just(CitiesViewIntent.InitialIntent))

        subscriber.assertValues(
                CitiesViewState(isLoading = true, cities = emptyList(), error = null),
                CitiesViewState(isLoading = false, cities = cities)
        )
    }

    @Test
    fun `初期状態の state をテスト`() {
        val expected = CitiesViewState(isLoading = true, cities = emptyList())

        val subscriber = viewModel.states().test()
        viewModel.processIntents(Observable.just(CitiesViewIntent.InitialIntent))
        subscriber.assertValue(expected)
    }

    @Test
    fun `読み込み中のreducer`() {
        val result = CitiesViewResult.LoadCitiesResult.InFlight
        val actual = CitiesViewModel.reducer.apply(CitiesViewState.idle() ,result).isLoading
        assert(actual).isEqualTo(true)
    }

    @Test
    fun `読み込み成功のreducer`() {
        val result = CitiesViewResult.LoadCitiesResult.Success(listOf(City("東京", "tokyo")))
        val actual = CitiesViewModel.reducer.apply(CitiesViewState.idle(), result)
        val expected = CitiesViewState(false, listOf(City("東京", "tokyo")))
        assert(actual).isEqualTo(expected)
    }

    @Test
    fun `読み込み失敗のreducer`() {
        val result = CitiesViewResult.LoadCitiesResult.Failure(Exception(""))
        val actual = CitiesViewModel.reducer.apply(CitiesViewState.idle(), result).isLoading
        assert(actual).isEqualTo(false)
    }

}