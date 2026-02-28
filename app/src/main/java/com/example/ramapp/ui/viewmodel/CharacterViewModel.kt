package com.example.ramapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ramapp.domain.model.CharacterFilters
import com.example.ramapp.domain.model.PaginationInfo
import com.example.ramapp.domain.usecase.GetCharactersUseCase
import com.example.ramapp.domain.usecase.GetCharacterDetailUseCase
import com.example.ramapp.ui.state.DetailUiState
import com.example.ramapp.ui.state.ListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val getCharacterDetailUseCase: GetCharacterDetailUseCase
) : ViewModel() {
    private val _listState = MutableStateFlow<ListUiState>(ListUiState.Loading)
    val listState: StateFlow<ListUiState> = _listState.asStateFlow()

    private val _detailState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailState: StateFlow<DetailUiState> = _detailState.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    private val _selectedFilters = MutableStateFlow(CharacterFilters())
    val selectedFilters: StateFlow<CharacterFilters> = _selectedFilters.asStateFlow()

    init {
        loadCharacters()
    }

    fun loadCharacters(page: Int = 1) {
        _currentPage.value = page
        _listState.value = ListUiState.Loading
        viewModelScope.launch {
            try {
                val (items, pagination) = getCharactersUseCase(page, _selectedFilters.value)
                _listState.value = ListUiState.Content(items, pagination)
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    // Нет результатов по фильтрам - показываем пустой список
                    _listState.value = ListUiState.Content(
                        items = emptyList(),
                        pagination = PaginationInfo(
                            currentPage = page,
                            totalPages = 1,
                            hasNext = false,
                            hasPrev = false
                        )
                    )
                } else {
                    _listState.value = ListUiState.Error(
                        "Не удалось загрузить список. Попробуйте еще раз."
                    )
                }
            } catch (e: Exception) {
                _listState.value = ListUiState.Error(
                    "Не удалось загрузить список. Попробуйте еще раз."
                )
            }
        }
    }

    fun applyFilters(filters: CharacterFilters) {
        _selectedFilters.value = filters
        loadCharacters(1)
    }

    fun nextPage() {
        loadCharacters(_currentPage.value + 1)
    }

    fun prevPage() {
        if (_currentPage.value > 1) {
            loadCharacters(_currentPage.value - 1)
        }
    }

    fun loadCharacter(id: Int) {
        _detailState.value = DetailUiState.Loading
        viewModelScope.launch {
            try {
                val character = getCharacterDetailUseCase(id)
                _detailState.value = DetailUiState.Content(character)
            } catch (e: Exception) {
                _detailState.value = DetailUiState.Error(
                    "Не удалось загрузить детали. Попробуйте еще раз."
                )
            }
        }
    }
}
