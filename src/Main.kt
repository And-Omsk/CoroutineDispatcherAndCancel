import kotlinx.coroutines.*
import kotlin.random.Random

data class Person(val name: String, val salary: Int)

val personList = mutableListOf<Person>()
val resultList = mutableMapOf<Person, Int>()

class PersonManager {
    fun addPerson(person: Person) {
        personList.add(person)
    }
}

suspend fun addPassword(person: Person) {
    val password = Random.nextInt(100000, 999999)
    runBlocking {
        delay(500L)
        resultList[person] = password
    }
}

suspend fun readDataPersonList() {
    runBlocking {
        delay(1000L)
        for (entry in resultList) {
            println("Сотрудник: ${entry.key}; пароль: ${entry.value}")
        }
    }
}

suspend fun main() = coroutineScope{
    println("Программа работы с базой данных сотрудников")
    runBlocking {
        delay(1000L)
        var continueAdding = true
        val personManager = PersonManager()

        while (continueAdding) {
            println("Добавить сотрудника: 1-Да   2-Нет Прочитать базу данных")
            val choice = readLine()

            when (choice) {
                "1" -> {
                    println("Введите имя сотрудника:")
                    val name = readLine() ?: ""
                    println("Введите зарплату сотрудника:")
                    val salary = readLine()?.toInt() ?: 0

                    val newPerson = Person(name, salary)
                    personManager.addPerson(newPerson)
                    addPassword(newPerson)

                    println("Сотрудник добавлен.")
                }
                "2" -> {
                    println("Чтение базы данных:")
                    readDataPersonList()
                    continueAdding = false
                }
                else -> {
                    println("Некорректный ввод. Повторите попытку.")
                }
            }
        }
    }

    val job = GlobalScope.launch {
        while (true) {
            println("Завершение программы - 0")
            if (readLine() == "0") {
                println("Завершение полной работы")
                break
            }
            else println("Некорректный ввод.")
        }
    }

    runBlocking {
        job.join()
    }
}
