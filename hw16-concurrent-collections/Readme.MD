## Queues

### Цель:
На практике освоить применение очередей для взаимодействия между потоками

### Описание:
Реализовать `SensorDataProcessorBuffered`, представляющий собой класс,
который накапливает внутри себя данные, в порядке времени измерения, а при достижении заданного размера буфера
выполняет запись этого буфера с помощью метода `flush` и зависимости `SensorDataBufferedWriter`. <br />
Тесты внутри `SensorDataProcessorBufferedTest` должны проходить
