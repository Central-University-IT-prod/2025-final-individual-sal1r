# Название проекта

## Оглавление

- [Реализованный проект](#реализованный-проект)
- [Документация](#документация)
- [ADR – Обоснование принятых решений](#adr--обоснование-принятых-решений)

## Реализованный проект
**Жирным** выделены доп.фичи

#### Главный экран
На главном экране реализован **обновляемый** с переодичностью в 5 секунд или по кнопке список тикеров, во время загрузки используюется **скелет с шиммерами**. Список тикеров хранится в **удалённом хранилище**, но на случай
неполадок с ним список так же хранится локально, чтобы всегда можно было что-то показать. Реализован список новостей, во время загрузки так же использованы **скелеты с шиммерами**. Новости кешируются и пока не загрузятся новые, отображаются из кеша. **Кеш действителен 6 часов**. Реализован **PullToRefresh** для обновления новостей.

<img src="/resources/Screenshot_2025-02-22-06-07-18-618_com.salir.superFinancer.jpg" width="300">

#### Поиск
На главном экране присутсвует панель поиска. Поиск реализован как встроенный на главный экран элемент(написан самостоятельно, SearchBar из material3 не подошёл). При нажатии на поиск с красивой **анимацией** разворачивается окно поиска, разделённое на 2 секции: под тикеры и под новости. Во время загрузки использованы **скелеты и шиммеры**. Для реализации списка новостей применена **пагинация**.

<img src="/resources/Screenshot_2025-02-22-06-07-24-331_com.salir.superFinancer.jpg" width="300">

<img src="/resources/Screenshot_2025-02-22-06-07-57-314_com.salir.superFinancer.jpg" width="300">

#### Экран новости
По нажатию на карточку новости в поиске или на главной странице пользователь переходит на экран чтения новости с WebView. Пока пользователь находится на этом экране, приложение переходит в **полноэкранный режим** для удобства чтения. На экране находится также панель, на которой расположены кнопка создания поста, при нажатии на которую пользователь переходит на экран создания поста и **кнопка "поделиться"**, по нажатию на которую пользователю будет предложено выбрать, каким способом поделиться новостью(она будет оформлена как заголовок и ссылка).

<img src="/resources/Screenshot_2025_02_22_08_39_43_738_com_salir_superFinancer_debug.jpg" width="300">

#### Экран финансов
Экран финансовм содержит секцию общей статистики с общей суммой накомлений и процентом достижения целей, который расчитывается как _(сумма накоплений) / (сумма целей)_. Сумма накоплений считается из суммы накоплений каждой цели, ограниченной размером цели. **Цвет** зависит от процента. Далее расположена секци целей. Каждая цель имеет название и может иметь дату. Цели имеют ProgressBar'ы, с **градиентом** от красного цвета(0%) до зелёного цвета(100%). По нажатию на плюсик открывается BottomSheet с формой для создания цели. Далее расположена секция операций. Каждая операция имеет сумму(положительную или отрицательную) и цель, к которой она относится. Цель может иметь комментарий. Если комментарий больше двух строк, то по его нижней части отображается **градиент**, при нажатии на такой комментарий, он **раскроется** с **анимацией** до полного при повторном нажати он с анимацией закроется. Операции отсортированы и **разделены** по дате совершения. При нажатии на плюсик открывается BottomSheet с формой создания операции. При удалении цели создаётся операция с суммой равной накопленной сумме в цели со знаком минус(если не накоплено ничего, то операция не появится).

<img src="/resources/Screenshot_2025-02-22-06-10-21-789_com.salir.superFinancer.jpg" width="300">

<img src="/resources/Screenshot_2025-02-22-06-08-42-854_com.salir.superFinancer.jpg" width="300">

<img src="/resources/Screenshot_2025-02-22-06-09-20-468_com.salir.superFinancer.jpg" width="300">

#### Лента(соц.сеть)
Главной фишкой приложения является **мини-соц.сеть** для инвесторов.

#### Экран ленты
Сверху экрана расположена падель, на которой, если пользователь не авторизован, будут кнопки **входа** и **регистрации**, а если авторизован, то - его логин, кнопка выхода из профиля и кнопка создния поста. Остальную часть экрана занимает список всех постов с **пагинацией**. Каждый пост содержит **логин опубликовавшего его пользователя**, **скрываемый** с **анимацией** текст поста с **градиентом**, список тегов и **сетку изображений**. Пока изображение загружается, используется **шиммер**. В конце списка постов отображается индикатор загрузки, если пагинация грузит данные. При нажатии на изображение откроется **полноэкранный диалог**, на котором можно двумя пальцами **увеличивать/уменьшать** и **перемещать** изображение для удобства просмотра. Все посты хранятся в **удалённом хранилище**. Реализован **PullToRefresh** для обновления списка постов. При нажатии на кнопку создания поста открывается экран создания поста, где можно ввести текст поста, теги через запятую и прикрепить изображения.

<img src="/resources/Screenshot_2025-02-22-06-11-06-493_com.salir.superFinancer.jpg" width="300">

<img src="/resources/Screenshot_2025-02-22-06-13-54-191_com.salir.superFinancer.jpg" width="300">

<img src="/resources/Screenshot_2025-02-22-06-13-22-828_com.salir.superFinancer.jpg" width="300">

<img src="/resources/Screenshot_2025-02-22-06-12-41-102_com.salir.superFinancer.jpg" width="300">

#### Экран входа/регистрации
Этот экран представляет из себя **ViewPager** и **вкладки** сверху для перехода между авторизацией и входом.

<img src="/resources/Screenshot_2025_02_22_07_48_10_375_com_salir_superFinancer_debug.jpg" width="300">

<img src="/resources/Screenshot_2025_02_22_07_48_15_149_com_salir_superFinancer_debug.jpg" width="300">

#### Прочее
Приложение выполнно в Material 3 дизайне с собственной темой. Местами добавлены анимации, в частности для иконок на BottomBar'е. Переходы для некоторых экранов. Кроме того, приложение поддерживает русский и английский язык. Положено начало покрытию приложения **тестами** (`NewsRepositoryImplTest`).

## Документация

#### Сборка
  1. Сoздайте файл local.properties, содержащий следующие поля(прикладываю с ключами):
  ```
  FINHUB_API_KEY=REDACTED
  NYTIMES_API_KEY=REDACTED
  SUPABASE_URL=https://xknabzcisyovosxfbrdc.supabase.co
  SUPABASE_KEY=REDACTED
  ```
  2. Запустите `./gradlew assembleProdRelease` в терминале, чтобы собрать проект
---

#### Архитектура
Проект разделён на следующие модули:
- `app` - входня точка в наше приложение, содержит навигацию, DI и тему
- `core` - модули - общий для других модулей data-слой и domain-слой, общие ресурсы и UI элементы, util модуль
- `feature` - фича-модули. Груба говоря - одна фича = один экран с его содержимым или логический блок
- `shared` - сожержат обособленную и логику или код, которые могут переиспользоваться в несколькольких других фичах(пагинация, например)
- `buildSrc` - служебный модуль, в котором объявлены константы сборки и вспомогательные функции для быстрой конфигурации сборки
<img src="/resources/Arch.svg" width="400">

## ADR – Обоснование принятых решений

#### Архитектура
Так как наше приложение планирует развиваться, а штат работающих над ним программистов - расти, была выбрана многомодульная архитектура.

**Плюсы:**
- разделение ответственности
- упрощение параллельной разработки
- переиспользуемость модулей
- упрощение тестирования
- масштабируемость

**Минусы:**
- усложняется управление зависимостями и сборкой
- сложнее для понимания

---

#### Выбор библиотек и технологий
Для создания UI был выбран Jetpack Compose с Kotlin как рекомендованный гуглом инструмент для создания современных мобильных приложений.

По той же причине были выбраны корутины и Flow, но помимо этого, у них есть конкретные преимущества. Корутины легковесны и удобны, а Flow, работая с корутирнами, предоставляет очень мощный функционал "потоков" через простой синтаксис а так же является удобным способом хранениея состояния UI.

В качестве DI был выбран Koin - современный и удобный фреймворк, поддержващий как dsl, так и аннотации + простой и не запутанный. Так же кроссплатформенный, что будет плюсом, если будет решено добавить поддержку других платформ.

В качестве http-клиента выбран Ktor Client, так как он интегрирован с корутинами и kotlinx serialization и использует dsl для формирования запросов. Также он кроссплатформенный.

Загрузка изображениц происходит через Coil 3. Соверменная кроссплатформенная библиотека, интегрированная с Jetpack Compose.

Для remote хранилища использован сервис Supabase. Это простое и в то же время мощное бесплатное решение, которое заменяет Firebase, зависящий от Google. Имеется удобная кроссплатформенная Kotlin библиотека.

Для локальной базы данных использован Room. Классическое решение, мощный функционал, интеграция с корутинами и Flow, рекомендован гуглом. Недавно обзавёлся кроссплатформенностью.

Для навигации была использована библиотека androidx navigation. Она стала класическим решением, рекомендуется гуглом и давно лишена старых проблем.

На замену Shared Preferences был взят DataStore, как современная и безопасная альтернатива с поддержкой flow и корутин.

Пагинация была написана самостоятельно.

Для тестирования взяты JUnit 5 и Mockito.
