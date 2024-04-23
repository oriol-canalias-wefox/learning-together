# Kotlin workshop

## Step 1: Hello world
create a project form the scratch, with gradle. 
- create package
- Add a Main class with main method, and print a `helloWorld`
    - explain `fun`
    - explain no need `;`
- Remove the class, explain top level 
- Add var/val to contain the string
    - `val text: String = "hello world"`
    - `val text = "hello world"`
    - `text = "helloworld"` <- immutable
    - `var text = "hello world"`


## Step 2: classes
- Create a class `CalculationService`
- Create a method `fun calculation(numerator: Int, denominator: Int): Int`
    - No primitive
    - parameters name: type
    - return type at the end
    - no return type, nothing at the end
    - access modifiers, public by default
- Add the call in the main
    - no `new` word
    - named parameters
- add to println and show the String template
- Add new parameter with default value
- inline return
    - with and without type
- add a field to the class to show the constructor

## Step 3: Dataclass
- Create a Data class
    - Like record, but better
    ```kotlin
    data class Product(
    val id: String,
    val amount: Int,
    val currency: String = "EUR")
    ```
    - Create one in main
    - println 
    - access to property
    - copy
    - compare with `equals`
    - compare with `==`

## Step 4: Null-safe
- Add color in product
- validate in main
    - println("length of color: ${product1.color.length}")
    ```kotlin
    if (product1.color != null) {
        println("length of color: ${product1.color.length}")
    } 
    ```
- `println("length of color: ${product1.color?.length}")`
    - Explain the meaning of ?.
    - Explain the meaning of !!

## Step 5: Inheritance
- Create Animal interface
```kotlin
interface Animal {
    fun run()
    fun eat(amountInGr: Int)
}
```
- Create Cat class and implement `Animal`
```kotlin
    override fun eat(amountInGr: Int) {
        if (amountInGr < 100) {
            throw Exception("not enough meaauuu")
        }
    }
```

- Create a hunter class:
```kotlin
class Hunter(private val animals: List<String>) {
    fun canHunt(animal: String): Boolean = animals.contains(animal)
}
```
- Remove the first inheritance
- try to inheritance it:
    - explain `final` by default
    - add `open`
    - explain `listOf` and the amount of methods similar to that
    - try to override `canHunt`
- add both inheritance

## Step 6: Extension functions
- Create extension function for toString with currency
```kotlin
fun Int.toStringWithCurrency(currency: String = "EUR"): String {
    return "$this $currency"
}
```
- move to another file
- move to another package, show the import


```kotlin
listOf(product1, product2).stream().findFirst().orElse(null)
// Create extension function??
listOf(product1, product2).firstOrNull()
```

```kotlin
    val listOfProducts = listOf(product1, product2)
    if (!listOfProducts.isEmpty()) {
        val product = listOf(product1, product2).firstOrNull()
        println(product)
    }
```
- Change for `isNotNull`

- do stream to map or print
    ```kotlin
        val listOfProducts = listOf(product1, product2)
        val pricelist = listOfProducts.stream().map { it -> it.amount.toStringWithCurrency(it.currency) }.toList()
        println("pricelist: $pricelist")
    ```
- explain the `it`


## Step 7: Scope functions
- Log the calculation result before return
```kotlin
    fun calculation(a: Int, b: Int, c: Int = 2): Int {
      val result = (a + b) * c
      println("CalculationService#calculation $result")
      return result
    }
```    
```kotlin
    fun calculation(a: Int, b: Int, c: Int = 2) =
       ((a + b) * c).also {
          println("CalculationService#calculation $it")
        }
```

- let for not null
```kotlin
    val product3 = Product(id = "123", amount = 10, color = "red")
    if (product3.color != null) {
        println(product3.color)
    }
```

```kotlin
    val product3 = Product(id = "123", amount = 10, color = "red")
    product3.color?.let { 
        println(it)
    }
```
- also explain the `?:`
https://kotlinlang.org/docs/scope-functions.html#function-selection

## Step 8: Passing trailing lambdas
Imagine this:
```kotlin
fun process(block: Runnable) {
    block.run()
}
process(Runnable() {println("Run inside")})

process({println("Run inside")})

process() { println("run inside") }
process { println("run inside") }
```
- Just for the last parameter
```kotlin
fun process(message: String, block: Runnable) {
    println(message)
    block.run()
}
process(Runnable() {println("Run inside")})

process({println("Run inside")})

process() { println("run inside") }
process { println("run inside") }
```


## Step 9: Run catching
- Cat try catch
```kotlin
    val myCat = Cat()
    try {
        myCat.eat(50)
        myCat.run()
    } catch(e: Exception) {
        println("Error ${e.message}")
        myCat.eat(200)
    } 
```
- Cat run catching
```kotlin
val myCat = Cat()
    runCatching {
        myCat.eat(50)
    }.onSuccess {
        myCat.run()
    }.onFailure {
        println("Error ${it.message}")
        myCat.eat(200)
    }
```
