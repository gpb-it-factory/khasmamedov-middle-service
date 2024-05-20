![img.png](images/img.png)

<h1 align="center"> khasmamedov-middle-service </h1>

### Это - вторая часть, а именно сервис, или прослойка (движок), написанная на java, выполняет бизнес-логику и выступает прокладкой между фронтом и бэкендом

![Static Badge](https://img.shields.io/badge/Java%20ver.=17-green)
![Static Badge](https://img.shields.io/badge/Spring-blue)
![Static Badge](https://img.shields.io/badge/Spring%20Boot-darkgreen)
![Static Badge](https://img.shields.io/badge/Tests:Junit%20%2B%20Mockito-red)
![Static Badge](https://img.shields.io/badge/Git-green)

### Базовое верхнеуровневое представление:
![Overall.png](images/Overall.png)
<br/><br/>

### Про проект в-общем:
[ну же, нажми меня!](https://github.com/gpb-it-factory/khasmamedov-telergam-bot/blob/trunk/README.md)  

  <br/><br/>
  <b><a href="https://gpb.fut.ru/itfactory/backend?utm_source=gpb&utm_medium=expert&utm_campaign=recommend&utm_content=all">GBP IT-factory</a></b> | <b><a href="">Документация(_пока пуста_)</a></b> | <b><a href="">Демо(_пока пуста_)</a></b> | <b><a href="https://github.com/gpb-it-factory/khasmamedov-middle-service">GitHub (middle)</a></b> <br>  
  <b><a href="#За_что_отвечает_middle-service">За_что_отвечает_middle-service</a></b> <br>
  <br/><br/>
  <a target="_blank" href="https://github.com/gpb-it-factory/khasmamedov-telergam-bot"><img src="https://img.shields.io/github/last-commit/gpb-it-factory/khasmamedov-telergam-bot?logo=github&color=609966&logoColor=fff" alt="Last commit"/></a>
  <br/><br/>

### Картинка всего проекта в качестве UML-диаграммы: (для общего представления)
```plantuml
@startUML
actor Клиент
participant Telegram
participant Service
participant Backend

Клиент -> Telegram: Запрос на действие со счетом:\n проверить баланс,\n пополнить,\n снять деньги,\n перевести
activate Telegram
Telegram -> Service: Валидация запроса
deactivate Telegram
activate Service
alt Вернуть клиенту запрос на доработку 
    Service -> Telegram: Клиентский запрос содержит ошибки
    deactivate Service
    activate Telegram
    Telegram -> Клиент: Клиент перепроверяет запрос;\n завершает работу, либо шлет запрос заново
    deactivate Telegram
else Запрос уже прошел цепочку клиент-телеграм-сервис + в сервисе успешно прошел проверку
    activate Service
    Service -> Service: Бизнес-логика
    Service -> Backend: Обработка данных
    deactivate Service
    activate Backend 
    Backend -> Service: Возврат успешности операции и\или \n данных, если они были запрошены
    deactivate Backend 
    activate Service
    Service -> Telegram: Дополнительная обработка данных (если требуется)
    deactivate Service
    activate Telegram
end    
Telegram -> Клиент: возврат данных
deactivate Telegram
@endUML
```

### Как_запустить_и_начать_работу _  - здесь будет по идее докер компоуз после всех сборок_

Для локальной установки приложения вам понадобятся [Git](https://git-scm.com/), [Java 21](https://axiomjdk.ru/pages/downloads/), [Gradle](https://gradle.org/), [IDEA](https://www.jetbrains.com/idea/download/)
0. Сперва, вам нужен токен (действует как пароль), если его еще нет - озаботьтесь его получением:    
   см. [документацию](https://core.telegram.org/bots/tutorial#obtain-your-bot-token)

Способы запуска приложения - standAlone (1 и 2) или как докер-приложение (3)

1. [x] Как получите токен, нужен сам проект:
   * Скачать проект с репозитория выше целиком [перейдя по ссылке на гитхаб](https://github.com/gpb-it-factory/khasmamedov-middle-service)    
     ````code -> download zip````    
     Распаковать архив, добавить в среду разработки как новый проект
   * Либо, **что обычно проще**, склонировать его    
     ````git@github.com:gpb-it-factory/khasmamedov-middle-service.git````  
     _в обоих случаях среда разработки сама подтянет gradle и зависимости_
2. [x] Запустить проект:
   * Собрать проект в готовый к исполнению файл   
     ````gradle build````  (если есть установленный грэдл)  
     + ИЛИ же ````gradlew build````  (если такового нет на Windows)  
     + ИЛИ же ````chmod +x gradlew```` ````./gradlew build```` (если такового нет на Linux)
   * Запустить его:  
       ````java -jar ./build/libs/khasmamedov-middle-service-0.0.1-SNAPSHOT.jar````    
       , где после команды -jar идет путь (полный или относительный) до сборки
       обычно это build/libs/
   * Еще, **проще** - такая команда не требует собирать проект самостоятельно и все сделает за вас:  
     ````.\gradlew.bat bootRun```` (Windows)  
     ````./gradlew.bat bootRun```` (Linux)
     _ctrl+c, чтобы выйти из приложения в общем виде_  
     в последнем случае (Б), на вопрос:  
     ````Завершить выполнение пакетного файла [Y(да)/N(нет)]?```` -> y
3. [x] Запустить проект с помощью докера.
   * Если у вас еще нет докера, его нужно скачать под вашу систему, иначе способ 3 не заработает:  
     [тык](https://docs.docker.com/get-docker/)
   * Такая команда создаст в докере image (посмотреть docker -> images в системе)  
     `docker build -t middle-service .`
   * Далее проще всего запустить его так - эта команда запустит его в фоновом режиме (т.е. вы не будете видеть в консоли идеи ничего кроме строки с номером приложения, пугаться не стоит):  
     `docker run --name middle-service -d middle-service`  
     _чтобы выйти в этом случае - набрать в терминале: `docker stop telegram-bot`_
   * Или, если вы хотите видеть результаты работы программы, включая логи и возможные ошибки:
     `docker run --name middle-service -it middle-service`  
     _ctrl+c, чтобы выйти из приложения в данном случае_
   * примечание. Если вы видите ошибку типа `docker: Error response from daemon: Conflict. The container name "/такой-то" is already in use by container "23a960d080bd5798917cb70c5a33992c3ae2a715a9cd0187822cab80f632973e". You have to remove (or rename) that container to be able to reuse that name.`,  
     вам потребуется остановить это контейнер перед тем как запускать программу:  
     `docker stop 23a960d080bd5798917cb70c5a33992c3ae2a715a9cd0187822cab80f632973e`
     `docker rm 23a960d080bd5798917cb70c5a33992c3ae2a715a9cd0187822cab80f632973e`

### За_что_отвечает_middle-service
Пока - функция одна:  
![gandalf_yapfiles.ru.gif](images%2Fgandalf_yapfiles.ru.gif)

