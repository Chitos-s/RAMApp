RAMApp

Работу выполнила студентка Б9123-09.03.03 пикд Бородина Полина Андреевна

VariantCode: RAM-CHAR-MOD_A2_GRID

Модификатор A2:
Grid вместо списка (LazyVerticalGrid) + detail экран по клику

Что сделано в модификаторе:
1. LazyVerticalGrid с GridCells.Adaptive (минимальная ширина 150dp) вместо обычного LazyColumn списка
2. Карточки персонажей адаптивно размещаются в сетке в зависимости от ширины экрана
3. При клике на карточку происходит навигация на detail экран
4. Detail экран делает отдельный запрос GET /character/{id} для получения полной информации о персонаже
5. Навигация реализована через Navigation Compose с передачей id персонажа в route

Использованные endpoints:
1. GET /character?page={page}&name={name}&status={status}&species={species}&type={type}&gender={gender}
2. GET /character/{id}

Скриншоты:
1. Screenshots/DetailScreen.jpg - Экран, отображающий Detail персонажа.
2. Screenshots\Filters.jpg - Видно протыканные фильтры и что они работают.
3. Screenshots\GeneralScreen.jpg - Общее представление приложения (таким экраном оно встречает нас при запуске)
4. Screenshots\LoadingError.jpg - Ошибка загрузки данных (неважно каких) (например инет рубанули или сервак с данными умер у апишки)
5. Screenshots\NullListExceptionAndFilters.jpg - Показаны расширенные фильтры и обработка случая когда апи нам возвращает пустой список.
 